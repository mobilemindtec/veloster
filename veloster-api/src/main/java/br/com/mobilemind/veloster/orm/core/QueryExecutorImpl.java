package br.com.mobilemind.veloster.orm.core;

/*
 * #%L
 * Mobile Mind - Veloster API
 * %%
 * Copyright (C) 2012 Mobile Mind Empresa de Tecnologia
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */
import br.com.mobilemind.api.utils.ClassUtil;
import br.com.mobilemind.veloster.orm.QueryExecutor;
import br.com.mobilemind.veloster.orm.QueryStatementBuilder;
import br.com.mobilemind.veloster.orm.EntityValidator;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.orm.QueryBuilder;
import br.com.mobilemind.veloster.orm.TransactionalOperation;
import br.com.mobilemind.veloster.event.DeleteListener;
import br.com.mobilemind.veloster.event.InsertListener;
import br.com.mobilemind.veloster.event.UpdateListener;
import br.com.mobilemind.veloster.exceptions.DataBaseException;
import br.com.mobilemind.veloster.exceptions.EntityValidatorException;
import br.com.mobilemind.veloster.exceptions.FailProcessExcetion;
import br.com.mobilemind.veloster.exceptions.VelosterException;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.orm.model.ColumnWrapper;
import br.com.mobilemind.veloster.sql.Connection;
import br.com.mobilemind.veloster.sql.DriverManager;
import br.com.mobilemind.veloster.sql.ResultSet;
import br.com.mobilemind.veloster.sql.Statement;
import br.com.mobilemind.veloster.sql.impl.Column;
import br.com.mobilemind.veloster.sql.impl.Table;
import br.com.mobilemind.veloster.sql.type.Criteria;
import br.com.mobilemind.veloster.sql.type.Eq;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.event.MetadataUpdateListener;
import br.com.mobilemind.veloster.orm.model.ListLazy;
import br.com.mobilemind.veloster.sql.Driver;
import br.com.mobilemind.veloster.tools.VelosterConfig;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author Ricardo Bocchi
 */
public class QueryExecutorImpl<T extends Entity> implements QueryExecutor<T> {

    private static final int INSERT_LISTENER = 0;
    private static final int UPDATE_LISTENER = 1;
    private static final int DELETE_LISTENER = 2;
    private QueryBuilder<T> queryBuilder;
    private QueryStatementBuilder<T> statementBulder;
    private Class<T> clazz;
    private Driver driver;
    private List<InsertListener> insertListeners = new LinkedList<InsertListener>();
    private List<UpdateListener> updateListeners = new LinkedList<UpdateListener>();
    private List<DeleteListener> deleteListeners = new LinkedList<DeleteListener>();
    private List<MetadataUpdateListener> metadataUpdateListeners = new LinkedList<MetadataUpdateListener>();

    private static enum OperationType {

        INSERT,
        UPDATE,
        DELETE
    }

    @Override
    public void build(Class<T> clazz, QueryBuilder<T> queryBuilder, QueryExecutor<T> queryExecutor, QueryStatementBuilder<T> queryStatementBuilder, EntityValidator entityValidator) {
        this.clazz = clazz;
        this.queryBuilder = queryBuilder;
        this.statementBulder = queryStatementBuilder;
        this.driver = VelosterConfig.getConf().getDriver();
    }

    @Override
    public synchronized T load(Long id) throws VelosterException {
        if (id == null) {
            throw new VelosterException("id can't be null");
        }
        Criteria<T> criteria = this.queryBuilder.createCriteria();
        criteria.add("id", new Eq(id));
        return this.loadByCriteria(criteria);
    }

    @Override
    public synchronized T load(T entity) throws VelosterException {
        Long id = entity.getId();
        if (id == null) {
            throw new VelosterException("id can't be null");
        }
        Criteria<T> criteria = this.queryBuilder.createCriteria();
        criteria.add("id", new Eq(id));
        return this.loadByCriteria(criteria, entity);
    }

    @Override
    public int count() throws DataBaseException, VelosterException {
        Criteria<T> criteria = this.queryBuilder.createCriteria();
        return this.countByCriteria(criteria);
    }

    @Override
    public int countByCriteria(Criteria<T> criteria) throws DataBaseException, VelosterException {
        int count = 0;
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt;

        try {
            String query = this.queryBuilder.getCountQuery(criteria);
            conn = openConnection();
            stmt = conn.prepare(query, false);
            this.statementBulder.compileParameters(null, stmt, criteria);
            rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInteger(1);
            }

            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(),
                        "count for " + this.clazz.getSimpleName() + " is " + count);
            }
            return count;
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, this.getClass(), e.getMessage(), e);
            if (QueryToolUtil.getConverterDataBaseErroCodeListener() != null) {
                QueryToolUtil.getConverterDataBaseErroCodeListener().analyze(e);
            }
            throw new VelosterException(e);
        } finally {
            close(conn, rs);
        }
    }

    private synchronized T loadByCriteria(Criteria<T> criteria, T entity) {
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt;

        try {
            String query = this.queryBuilder.getSelectQuery(criteria);
            conn = openConnection();
            stmt = conn.prepare(query, false);
            this.statementBulder.compileParameters(null, stmt, criteria);
            rs = stmt.executeQuery();

            if (entity != null) {
                entity = this.statementBulder.getUniqueResult(entity, rs);
            } else {
                entity = this.statementBulder.getUniqueResult(rs);
            }

            return entity;
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, this.getClass(), e.getMessage(), e);
            if (QueryToolUtil.getConverterDataBaseErroCodeListener() != null) {
                QueryToolUtil.getConverterDataBaseErroCodeListener().analyze(e);
            }
            throw new VelosterException(e);
        } finally {
            close(conn, rs);
        }
    }

    @Override
    public synchronized T loadByCriteria(Criteria<T> criteria) throws VelosterException {
        return loadByCriteria(criteria, null);
    }

    @Override
    public synchronized List<T> list() throws VelosterException {
        Criteria<T> criteria = this.queryBuilder.createCriteria();
        return this.listByCriteria(criteria);
    }

    @Override
    public List<T> list(int limit, int offset) {
        Criteria<T> criteria = this.queryBuilder.createCriteria();
        criteria.setLimit(limit);
        criteria.setOffset(offset);
        return listByCriteria(criteria);
    }

    @Override
    public synchronized List<T> listByCriteria(Criteria<T> criteria) throws VelosterException {
        List<T> entities = null;
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt;

        if (MMLogger.isLogable()) {
            MMLogger.log(Level.INFO, this.getClass(), "listing entity " + this.clazz.getSimpleName());
        }
        try {
            String query = this.queryBuilder.getSelectQuery(criteria);
            conn = openConnection();
            stmt = conn.prepare(query, false);
            this.statementBulder.compileParameters(null, stmt, criteria);
            rs = stmt.executeQuery();
            entities = this.statementBulder.getResult(rs);
            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(),
                        "listed " + entities.size() + " results for " + this.clazz.getSimpleName());
            }
            return entities;
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, this.getClass(), e.getMessage(), e);
            if (QueryToolUtil.getConverterDataBaseErroCodeListener() != null) {
                QueryToolUtil.getConverterDataBaseErroCodeListener().analyze(e);
            }
            throw new VelosterException(e);
        } finally {
            close(conn, rs);
        }
    }

    @Override
    public List<T> listByCriteria(Criteria<T> criteria, int limit, int offset) {
        criteria.setLimit(limit);
        criteria.setOffset(offset);
        return listByCriteria(criteria);
    }

    @Override
    public synchronized void deleteByCriteria(Criteria<T> criteria) throws VelosterException {
        Connection conn = null;
        Statement stmt;

        if (!firePreEvents(null, DELETE_LISTENER)) {
            MMLogger.log(Level.INFO, getClass(), "event delete processing stopped");
            return;
        }

        try {
            String query = this.queryBuilder.getDeleteQuery(criteria);
            conn = openConnection();

            if (criteria.getExpressions().size() == 1) {
                if (criteria.getExpressions().get(0).getField().isPrimaryKey()) {
                    Long id = (Long) criteria.getExpressions().get(0).getValues().toArray()[0];
                    T remove = clazz.newInstance();
                    remove.setId(id);
                    processListCascade(remove, true, OperationType.DELETE);
                }
            }

            stmt = conn.prepare(query, false);
            this.statementBulder.compileParameters(null, stmt, criteria);
            stmt.executeUpdate();

            firePosEnents(null, DELETE_LISTENER);

        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, this.getClass(), e.getMessage(), e);
            if (QueryToolUtil.getConverterDataBaseErroCodeListener() != null) {
                QueryToolUtil.getConverterDataBaseErroCodeListener().analyze(e);
            }
            throw new VelosterException(e);
        } finally {
            close(conn, null);
        }
    }

    @Override
    public void executeNativeQuery(String query, Object... params) throws VelosterException {
        Connection conn = null;
        Statement stmt;
        try {
            conn = openConnection();
            stmt = conn.prepare(query, false);
            this.statementBulder.compileParameters(stmt, params);
            stmt.executeUpdate();
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, this.getClass(), e.getMessage(), e);
            if (QueryToolUtil.getConverterDataBaseErroCodeListener() != null) {
                QueryToolUtil.getConverterDataBaseErroCodeListener().analyze(e);
            }
            throw new VelosterException(e);
        } finally {
            close(conn, null);
        }
    }

    @Override
    public List<Object[]> executeNativeSelect(String query, Object... params) {
        List<Object[]> records = new LinkedList<Object[]>();
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt;

        try {
            conn = openConnection();
            stmt = conn.prepare(query, false);
            this.statementBulder.compileParameters(stmt, params);
            rs = stmt.executeQuery();
            int columnCount = rs.getColumnCount();
            Object data[] = null;

            while (rs.next()) {
                data = new Object[columnCount];
                for (int i = 0, j = 1; i < columnCount; i++, j++) {
                    data[i] = rs.getObject(j);
                }
                records.add(data);
            }

            return records;
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, this.getClass(), e.getMessage(), e);
            if (QueryToolUtil.getConverterDataBaseErroCodeListener() != null) {
                QueryToolUtil.getConverterDataBaseErroCodeListener().analyze(e);
            }
            throw new VelosterException(e);
        } finally {
            close(conn, rs);
        }
    }

    @Override
    public <E> List<E> executeNativeTransformer(String query, Class<E> resultTransformer, Object... params) {
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt;

        try {
            conn = openConnection();
            stmt = conn.prepare(query, false);
            this.statementBulder.compileParameters(stmt, params);
            rs = stmt.executeQuery();
            return this.statementBulder.getResultTransformer(rs, resultTransformer);
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, this.getClass(), e.getMessage(), e);
            if (QueryToolUtil.getConverterDataBaseErroCodeListener() != null) {
                QueryToolUtil.getConverterDataBaseErroCodeListener().analyze(e);
            }
            throw new VelosterException(e);
        } finally {
            close(conn, rs);
        }
    }

    @Override
    public <E> E executeNativeSingleTransformer(String query, Class<E> resultTransformer, Object... params) {
        List<E> items = executeNativeTransformer(query, resultTransformer, params);

        if (items.isEmpty()) {
            return null;
        }

        return items.get(0);
    }

    public synchronized void updateByCriteria(T entity, boolean useValidation, Criteria<T> criteria) throws VelosterException {
        Connection conn = null;
        Statement stmt;

        if (!firePreEvents(entity, UPDATE_LISTENER)) {
            MMLogger.log(Level.INFO, getClass(), "event update processing stopped");
            return;
        }

        try {
            conn = openConnection();
            processCascade(entity, useValidation);

            String query = this.queryBuilder.getUpdateQuery(criteria);

            MMLogger.log(Level.INFO, getClass(), "update called :" + query);

            stmt = conn.prepare(query, false);

            this.statementBulder.compileParameters(entity, stmt, criteria);

            stmt.executeUpdate();

            processListCascade(entity, useValidation, OperationType.UPDATE);

            firePosEnents(entity, UPDATE_LISTENER);

        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, this.getClass(), e.getMessage(), e);
            if (QueryToolUtil.getConverterDataBaseErroCodeListener() != null) {
                QueryToolUtil.getConverterDataBaseErroCodeListener().analyze(e);
            }
            throw new VelosterException(e);
        } finally {
            close(conn, null);
        }
    }

    private synchronized void close(Connection conn, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                if (MMLogger.isLogable()) {
                    MMLogger.log(Level.INFO, this.getClass(), "recordset closed...");
                }
            } catch (SQLException ex) {
                MMLogger.log(Level.SEVERE, this.getClass(), ex.getMessage(), ex);
            }
        }
        if (conn != null) {
            try {
                conn.close();
                if (MMLogger.isLogable()) {
                    MMLogger.log(Level.INFO, this.getClass(), "connection closed...");
                }
            } catch (SQLException ex) {
                MMLogger.log(Level.SEVERE, this.getClass(), ex.getMessage(), ex);
            }
        }
    }

    private synchronized Connection openConnection() throws SQLException {
        Connection conn = DriverManager.getConnection();
        conn.open();
        if (this.driver.isPragmaExecute()) {
            executePragmas(conn);
        }
        return conn;
    }

    private synchronized void executePragmas(Connection conn) throws SQLException {
        for (String pragma : this.driver.getPragmas()) {
            conn.prepare(pragma, false).executeNonQuery();
            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(), "executed pragma " + pragma);
            }
        }
    }

    @Override
    public boolean tableExists() throws VelosterException {
        return tableExists(this.queryBuilder.getTableName());
    }

    @Override
    public boolean tableExists(String name) throws VelosterException {
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt;
        String query = this.driver.getShowTableStatement(name);
        if (MMLogger.isLogable()) {
            MMLogger.log(Level.INFO, this.getClass(), "calling table exists: " + name);
        }
        try {
            conn = openConnection();
            stmt = conn.prepare(query, false);
            rs = stmt.executeQuery();
            boolean exists = rs.next();
            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(), "table " + name + ((exists) ? " found..."
                        : " not found..."));
            }
            return exists;
        } catch (SQLException e) {
            MMLogger.log(Level.SEVERE, this.getClass(), e.getMessage(), e);
            throw new VelosterException(e);
        } finally {
            close(conn, rs);
        }
    }

    @Override
    public void tableCreate() throws VelosterException {
        Connection conn = null;
        Statement stmt;

        if (MMLogger.isLogable()) {
            MMLogger.log(Level.INFO, this.getClass(), "creating table for entity" + this.clazz.getSimpleName());
        }
        try {
            String query = this.queryBuilder.getCreateTableQuery();
            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(), "created query for entity " + this.clazz.getSimpleName());
            }

            for (MetadataUpdateListener event : metadataUpdateListeners) {
                event.beforeCreateTable(this.clazz, this.queryBuilder.getTableName());
            }

            conn = openConnection();
            stmt = conn.prepare(query, false);
            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(), "created statement for entity " + this.clazz.getSimpleName());
            }
            stmt.executeNonQuery();
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, this.getClass(), e.getMessage(), e);
            throw new VelosterException(e);
        } finally {
            close(conn, null);
        }

        if (!tableExists()) {
            String error = "could not create table " + this.queryBuilder.getTableName();
            MMLogger.log(Level.INFO, this.getClass(), error);
            throw new VelosterException(error);
        }

        for (MetadataUpdateListener event : metadataUpdateListeners) {
            event.afterCreateTable(this.clazz, this.queryBuilder.getTableName());
        }

        if (MMLogger.isLogable()) {
            MMLogger.log(Level.INFO, this.getClass(), "created table for etity " + this.clazz.getSimpleName());
        }
    }

    @Override
    public void tableDelete() throws VelosterException {
        Connection conn = null;
        Statement stmt;

        if (MMLogger.isLogable()) {
            MMLogger.log(Level.INFO, this.getClass(), "droping table for entity" + this.clazz.getSimpleName());
        }
        try {
            String query = this.queryBuilder.getDropTableQuery();
            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(), "created query for entity " + this.clazz.getSimpleName());
            }

            for (MetadataUpdateListener event : metadataUpdateListeners) {
                event.beforeDropTable(this.clazz, this.queryBuilder.getTableName());
            }

            conn = openConnection();
            stmt = conn.prepare(query, false);
            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(), "created statement for entity " + this.clazz.getSimpleName());
            }
            stmt.executeNonQuery();
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, this.getClass(), e.getMessage(), e);
            throw new VelosterException(e);
        } finally {
            close(conn, null);
        }

        if (tableExists()) {
            String error = "could not delete table " + this.queryBuilder.getTableName();
            MMLogger.log(Level.INFO, this.getClass(), error);
            throw new VelosterException(error);
        }

        for (MetadataUpdateListener event : metadataUpdateListeners) {
            event.afterDropTable(this.clazz, this.queryBuilder.getTableName());
        }

        if (MMLogger.isLogable()) {
            MMLogger.log(Level.INFO, this.getClass(), "droped table for etity " + this.clazz.getSimpleName());
        }
    }

    @Override
    public void tableUpdate() throws VelosterException {
        Table table = null;
        Connection conn = null;

        if (!this.tableExists()) {
            throw new VelosterException("Table " + this.queryBuilder.getTableName() + " not found.");
        }

        if (MMLogger.isLogable()) {
            MMLogger.log(Level.INFO, this.getClass(), "droping table for entity" + this.clazz.getSimpleName());
        }

        try {
            conn = openConnection();
            table = driver.getTableMetadataResolver().getTableMetadata(this.queryBuilder.getTableName(), conn);
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, this.getClass(), e.getMessage(), e);
            throw new VelosterException(e);
        } finally {
            close(conn, null);
        }

        if (table.getColumns().isEmpty()) {
            this.tableDelete();
            this.tableCreate();
            return;
        }

        List<Column> columns = table.getColumns();
        List<ColumnWrapper> fields = this.queryBuilder.getFieldForInsert();
        boolean found = false;
        String query = null;

        for (MetadataUpdateListener event : metadataUpdateListeners) {
            event.beforeUpdateTable(this.clazz, this.queryBuilder.getTableName());
        }

        for (ColumnWrapper field : fields) {
            if (field.isHasMany()) {
                continue;
            }

            for (Column column : columns) {
                if (column.getName().equals(field.getName())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                query = this.queryBuilder.getAddColumnQuery(field);
                this.executeComlumnAddStatement(query, field);
            }
            found = false;
        }

        for (MetadataUpdateListener event : metadataUpdateListeners) {
            event.afterDropTable(this.clazz, this.queryBuilder.getTableName());
        }
    }

    private void executeComlumnAddStatement(String query, ColumnWrapper field) {
        Connection conn = null;
        Statement stmt;
        Table newTable = null;

        if (MMLogger.isLogable()) {
            MMLogger.log(Level.INFO, this.getClass(),
                    "creating column for entity" + this.clazz.getSimpleName());
            MMLogger.log(Level.INFO, this.getClass(), "query " + query);
        }
        try {
            conn = openConnection();
            stmt = conn.prepare(query, false);
            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(), "created statement for entity " + this.clazz.getSimpleName());
            }
            stmt.executeNonQuery();
            newTable = driver.getTableMetadataResolver().getTableMetadata(this.queryBuilder.getTableName(), conn);
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, this.getClass(), e.getMessage(), e);
            throw new VelosterException(e);
        } finally {
            close(conn, null);
        }

        boolean found = false;

        for (Column column : newTable.getColumns()) {
            if (column.getName().equals(field.getName())) {
                found = true;
                break;
            }
        }

        if (!found) {
            String message = "can't create column " + field.getName() + " for entity " + this.clazz.getSimpleName();
            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(), message);
            }
            throw new VelosterException(message);
        } else if (MMLogger.isLogable()) {
            MMLogger.log(Level.INFO, this.getClass(),
                    "created column " + field.getName() + " for etity " + this.clazz.getSimpleName());
        }
    }

    @Override
    public void delete(T entity) {

        Long id = entity.getId();

        if (id == null || id < 0) {
            throw new VelosterException("id can't be null for entity " + this.clazz.getSimpleName());
        }

        Criteria<T> criteria = this.queryBuilder.createCriteria();
        criteria.add("id", new Eq(id));
        this.deleteByCriteria(criteria);
    }

    @Override
    public void update(T entity) {
        Long id = entity.getId();

        if (id == null || id < 0) {
            throw new VelosterException("id can't be null for entity " + this.clazz.getSimpleName());
        }

        Criteria<T> criteria = this.queryBuilder.createCriteria();
        criteria.add("id", new Eq(id));
        this.updateByCriteria(entity, true, criteria);
    }

    @Override
    public void updateWithoutValidation(T entity) {
        Long id = entity.getId();

        if (id == null || id < 0) {
            throw new VelosterException("id can't be null for entity " + this.clazz.getSimpleName());
        }

        Criteria<T> criteria = this.queryBuilder.createCriteria();
        criteria.add("id", new Eq(id));
        this.updateByCriteria(entity, false, criteria);
    }

    @Override
    public T save(T entity) {
        return save(entity, true);
    }

    @Override
    public T saveWithoutValidation(T entity) {
        return save(entity, false);
    }

    public T save(T entity, boolean useValidation) {
        Connection conn = null;
        ResultSet rs = null;
        Statement stmt;

        if (!firePreEvents(entity, INSERT_LISTENER)) {
            return entity;
        }

        try {
            conn = openConnection();

            processCascade(entity, useValidation);

            String query = this.queryBuilder.getInsertQuery();

            stmt = conn.prepare(query, true);

            MMLogger.log(Level.INFO, getClass(), "save called " + query);

            this.statementBulder.compileParameters(entity, stmt, null);

            boolean getRowId = this.statementBulder.isGetRowId();

            long id = stmt.executeInsert(getRowId);

            if (id <= 0 && getRowId) {
                throw new VelosterException("id can't be less or equal than 0");
            }
            Long old = entity.getId();
            if (getRowId) {
                entity.setId(id);
            }

            try {
                processListCascade(entity, useValidation, OperationType.INSERT);
            } catch (Exception e) {
                entity.setId(old);
                throw e;
            }

            firePosEnents(entity, INSERT_LISTENER);

            return entity;
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, this.getClass(), e.getMessage(), e);
            if (QueryToolUtil.getConverterDataBaseErroCodeListener() != null) {
                QueryToolUtil.getConverterDataBaseErroCodeListener().analyze(e);
            }
            throw new VelosterException(e);
        } finally {
            close(conn, rs);
        }
    }

    @Override
    public void runTrans(TransactionalOperation operation) {
        Connection conn = DriverManager.getConnection();

        try {
            conn.open();
            executePragmas(conn);
            conn.setCommitTrans(true);
            conn.begin();
            operation.execute();
            conn.commit();
        } catch (Exception e) {

            try {
                conn.rollback();
            } catch (Exception ee) {
                MMLogger.log(Level.SEVERE, getClass(), e);
            }

            if (e instanceof DataBaseException) {
                throw (DataBaseException) e;
            } else if (e instanceof EntityValidatorException) {
                throw (EntityValidatorException) e;
            } else if (e instanceof FailProcessExcetion) {
                throw (FailProcessExcetion) e;
            } else if (e instanceof VelosterException) {
                throw (VelosterException) e;
            } else {
                throw new VelosterException(e);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    throw new VelosterException(e);
                }
            }
        }
    }

    @Override
    public void addInsertListener(InsertListener listener) {
        this.insertListeners.add(listener);
    }

    @Override
    public void removeInsertListener(InsertListener listener) {
        this.insertListeners.remove(listener);
    }

    @Override
    public void addUpdateListener(UpdateListener listener) {
        this.updateListeners.add(listener);
    }

    @Override
    public void removeUpdateListener(UpdateListener listener) {
        this.updateListeners.remove(listener);
    }

    @Override
    public void addDeleteListener(DeleteListener listener) {
        this.deleteListeners.add(listener);
    }

    @Override
    public void removeDeleteListener(DeleteListener listener) {
        this.deleteListeners.remove(listener);
    }

    @Override
    public void addMetadataUpdateListener(MetadataUpdateListener listener) {
        this.metadataUpdateListeners.add(listener);
    }

    @Override
    public void removeMetadataUpdateListener(MetadataUpdateListener listener) {
        this.metadataUpdateListeners.remove(listener);
    }

    /**
     * fire pre events
     *
     * @param entity
     * @param type
     * @return if execution should proced
     */
    private boolean firePreEvents(T entity, int type) {

        switch (type) {
            case INSERT_LISTENER:
                for (InsertListener it : insertListeners) {

                    if (it.getEntityToListen() != null && it.getEntityToListen() != this.clazz) {
                        break;
                    }

                    if (!it.preInsert(entity)) {
                        return false;
                    }
                }
                break;
            case UPDATE_LISTENER:
                for (UpdateListener it : updateListeners) {
                    if (it.getEntityToListen() != null && it.getEntityToListen() != this.clazz) {
                        break;
                    }

                    if (!it.preUpdate(entity)) {
                        return false;
                    }
                }
                break;
            case DELETE_LISTENER:
                for (DeleteListener it : deleteListeners) {
                    if (it.getEntityToListen() != null && it.getEntityToListen() != this.clazz) {
                        break;
                    }

                    if (!it.preDelete(entity)) {
                        return false;
                    }
                }
                break;
        }

        return true;
    }

    private void firePosEnents(T entity, int type) {
        switch (type) {
            case INSERT_LISTENER:
                for (InsertListener it : insertListeners) {
                    if (it.getEntityToListen() != null && it.getEntityToListen() != this.clazz) {
                        break;
                    }
                    it.posInsert(entity);
                }
                break;
            case UPDATE_LISTENER:
                for (UpdateListener it : updateListeners) {
                    if (it.getEntityToListen() != null && it.getEntityToListen() != this.clazz) {
                        break;
                    }
                    it.posUpdate(entity);
                }
                break;
            case DELETE_LISTENER:
                for (DeleteListener it : deleteListeners) {
                    if (it.getEntityToListen() != null && it.getEntityToListen() != this.clazz) {
                        break;
                    }
                    it.posDelete(entity);
                }
                break;
        }
    }

    private void processCascade(T entity, boolean useValidation) {
        List<ColumnWrapper> items = this.queryBuilder.getCascadeFields();
        Object cascade = null;
        try {
            for (ColumnWrapper it : items) {
                it.getFieldReflect().setAccessible(true);
                cascade = it.getFieldReflect().get(entity);

                if (cascade == null) {
                    continue;
                }

                if (!(cascade instanceof Entity)) {
                    String message = "field [" + clazz.getName() + "." + it.getFieldReflect().getName() + "] is cascade, but your type not implement Entity";
                    MMLogger.log(Level.SEVERE, getClass(), message);
                    throw new VelosterException(message);
                }

                Entity entityCascade = (Entity) cascade;
                Class entityType = it.getFieldReflect().getType();
                Veloster orm = VelosterRepository.getVeloster(entityType);

                if (orm == null) {
                    throw new VelosterException("ORMManager not found to entity [" + entityType.getName() + "]");
                }

                if (it.cascadeOnInsertOrUpdate() || it.cascadeOnInsert()) {
                    if (!entityCascade.isPersisted()) {
                        MMLogger.log(Level.INFO, getClass(), "execute save cascade to [" + clazz.getName() + "." + it.getFieldReflect().getName() + "]");
                        if (useValidation) {
                            orm.save(entityCascade);
                        } else {
                            orm.saveWithoutValidation(entityCascade);
                        }
                    }
                }

                if (it.cascadeOnInsertOrUpdate() || it.cascadeOnUpdate()) {
                    if (entityCascade.isPersisted()) {
                        MMLogger.log(Level.INFO, getClass(), "execute update cascade to [" + clazz.getName() + "." + it.getFieldReflect().getName() + "]");
                        if (useValidation) {
                            orm.update(entityCascade);
                        } else {
                            orm.updateWithoutValidation(entityCascade);
                        }
                    }
                }
            }
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, getClass(), e);
            throw new VelosterException(e);
        }
    }

    private void processListCascade(T entity, boolean useValidation, OperationType type) {
        List<ColumnWrapper> items = this.queryBuilder.getHasManyFields();
        for (ColumnWrapper wrapper : items) {
            MMLogger.log(Level.INFO, getClass(), "process entity cascade to field " + wrapper.getFieldName() + " entity  " + wrapper.getTable().getTableClass().getSimpleName());
            Veloster veloster = VelosterRepository.getVeloster(wrapper.getGenericType());
            List values = null;
            try {
                wrapper.getFieldReflect().setAccessible(true);
                Field field = ClassUtil.getField(wrapper.getGenericType(), wrapper.getHasMany().reference());
                field.setAccessible(true);
                switch (type) {
                    case INSERT:
                        if (wrapper.getHasMany().cascadeAll()
                                || wrapper.getHasMany().cascadeMergeOrInsertOnSave()) {
                            values = (List) wrapper.getFieldReflect().get(entity);
                            if (values != null) {
                                for (Object it : values) {
                                    field.set(it, entity);
                                    if (((Entity) it).isPersisted()) {
                                        if (useValidation) {
                                            veloster.update((Entity) it);
                                        } else {
                                            veloster.updateWithoutValidation((Entity) it);
                                        }
                                    } else {
                                        if (useValidation) {
                                            veloster.save((Entity) it);
                                        } else {
                                            veloster.saveWithoutValidation((Entity) it);
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case UPDATE:
                        if (wrapper.getHasMany().cascadeAll()
                                || wrapper.getHasMany().cascadeMergeOrInsertOnUpdate()) {
                            values = (List) wrapper.getFieldReflect().get(entity);
                            if (values != null) {
                                for (Object it : values) {
                                    field.set(it, entity);
                                    if (((Entity) it).isPersisted()) {
                                        if (useValidation) {
                                            veloster.update((Entity) it);
                                        } else {
                                            veloster.updateWithoutValidation((Entity) it);
                                        }
                                    } else {
                                        if (useValidation) {
                                            veloster.save((Entity) it);
                                        } else {
                                            veloster.saveWithoutValidation((Entity) it);
                                        }
                                    }
                                }
                            }
                        }

                        if (wrapper.getHasMany().cascadeAll() || wrapper.getHasMany().cascadeRemoveOnDelete()) {
                            List list = (List) wrapper.getFieldReflect().get(entity);
                            if (list != null && list instanceof ListLazy) {
                                for (Object it : ((ListLazy) list).getDeletedList()) {
                                    Entity e = (Entity) it;

                                    if (e.isPersisted()) {
                                        veloster.delete(e);
                                    }
                                }

                                ((ListLazy) list).getDeletedList().clear();
                            }
                        }

                        break;
                    case DELETE:
                        if (wrapper.getHasMany().cascadeAll()
                                || wrapper.getHasMany().cascadeRemoveOnDelete()) {
                            List list = (List) wrapper.getFieldReflect().get(entity);
                            if (list != null && list instanceof ListLazy) {
                                for (Object it : ((ListLazy) list).getDeletedList()) {
                                    field.set(it, entity);
                                    if (((Entity) it).isPersisted()) {
                                        veloster.delete((Entity) it);
                                    }
                                }
                                ((ListLazy) list).getDeletedList().clear();
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                throw new VelosterException("erros prccess hasMany references", e);
            }
        }
    }

    @Override
    public String tableName() {
        return this.queryBuilder.getTableName();
    }
}
