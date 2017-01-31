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
import br.com.mobilemind.veloster.event.MetadataUpdateListener;
import br.com.mobilemind.veloster.orm.QueryExecutor;
import br.com.mobilemind.veloster.orm.QueryStatementBuilder;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.orm.QueryBuilder;
import br.com.mobilemind.veloster.orm.TransactionalOperation;
import br.com.mobilemind.veloster.event.DeleteListener;
import br.com.mobilemind.veloster.event.InsertListener;
import br.com.mobilemind.veloster.event.UpdateListener;
import br.com.mobilemind.veloster.orm.EntityValidator;
import br.com.mobilemind.veloster.sql.type.Criteria;
import br.com.mobilemind.veloster.exceptions.VelosterException;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.sql.Statement;
import java.util.List;

/**
 *
 * @author Ricardo Bocchi
 */
public class VelosterImpl<T extends Entity> implements Veloster<T> {

    private QueryBuilder<T> queryBuilder;
    private QueryExecutor<T> queryExecutor;
    private EntityValidator validator;

    @Override
    public void build(Class<T> clazz, QueryBuilder<T> queryBuilder, QueryExecutor<T> queryExecutor, QueryStatementBuilder<T> queryStatementBuilder, EntityValidator validator) {
        this.queryBuilder = queryBuilder;
        this.queryExecutor = queryExecutor;
        this.validator = validator;

        new DDLProcessor().process(this, clazz);
    }

    @Override
    public void deleteByCriteria(Criteria<T> criteria) throws VelosterException {
        synchronized (Statement.SYNC) {
            this.queryExecutor.deleteByCriteria(criteria);
        }
    }

    @Override
    public List<T> list() throws VelosterException {
        synchronized (Statement.SYNC) {
            return this.queryExecutor.list();
        }
    }

    @Override
    public List<T> list(int limit, int offset) {
        return this.queryExecutor.list(limit, offset);
    }

    @Override
    public List<T> listByCriteria(Criteria<T> criteria, int limit, int offset) {
        return this.queryExecutor.listByCriteria(criteria, limit, offset);
    }

    @Override
    public List<T> listByCriteria(Criteria<T> criteria) throws VelosterException {
        synchronized (Statement.SYNC) {
            return this.queryExecutor.listByCriteria(criteria);
        }
    }

    @Override
    public int count() throws VelosterException {
        synchronized (Statement.SYNC) {
            return this.queryExecutor.count();
        }
    }

    @Override
    public int countByCriteria(Criteria<T> criteria) throws VelosterException {
        synchronized (Statement.SYNC) {
            return this.queryExecutor.countByCriteria(criteria);
        }
    }

    @Override
    public T loadByCriteria(Criteria<T> criteria) throws VelosterException {
        synchronized (Statement.SYNC) {
            return this.queryExecutor.loadByCriteria(criteria);
        }
    }

    @Override
    public T load(Long id) throws VelosterException {
        synchronized (Statement.SYNC) {
            return this.queryExecutor.load(id);
        }
    }

    @Override
    public T load(T entity) throws VelosterException {
        synchronized (Statement.SYNC) {
            return this.queryExecutor.load(entity);
        }
    }

    @Override
    public void executeNativeQuery(String query, Object... params) throws VelosterException {
        synchronized (Statement.SYNC) {
            this.queryExecutor.executeNativeQuery(query, params);
        }
    }

    @Override
    public void tableCreate() throws VelosterException {
        synchronized (Statement.SYNC) {
            this.queryExecutor.tableCreate();
        }
    }

    @Override
    public void tableDelete() throws VelosterException {
        synchronized (Statement.SYNC) {
            this.queryExecutor.tableDelete();
        }
    }

    @Override
    public void tableUpdate() throws VelosterException {
        synchronized (Statement.SYNC) {
            this.queryExecutor.tableUpdate();
        }
    }

    @Override
    public boolean tableExists() throws VelosterException {
        synchronized (Statement.SYNC) {
            return this.queryExecutor.tableExists();
        }
    }

    @Override
    public boolean tableExists(String tableName) throws VelosterException {
        synchronized (Statement.SYNC) {
            return this.queryExecutor.tableExists(tableName);
        }
    }

    @Override
    public Criteria<T> createCriteria() {
        return this.queryBuilder.createCriteria();
    }

    @Override
    public EntityValidator getValidator() {
        return this.validator;
    }

    @Override
    public void delete(T entity) {
        synchronized (Statement.SYNC) {            
            this.queryExecutor.delete(entity);            
        }
    }

    @Override
    public void update(T entity) {
        synchronized (Statement.SYNC) {
            this.validator.validate(entity);
            this.queryExecutor.update(entity);
        }
    }

    @Override
    public void updateWithoutValidation(T entity) {
        synchronized (Statement.SYNC) {
            this.queryExecutor.updateWithoutValidation(entity);
        }
    }

    @Override
    public T save(T entity) {
        synchronized (Statement.SYNC) {
            this.validator.validate(entity);
            return this.queryExecutor.save(entity);
        }
    }

    @Override
    public T saveWithoutValidation(T entity) {
        return this.queryExecutor.saveWithoutValidation(entity);
    }

    @Override
    public List<Object[]> executeNativeSelect(String query, Object... params) {
        synchronized (Statement.SYNC) {
            return this.queryExecutor.executeNativeSelect(query, params);
        }
    }

    @Override
    public <E> E executeNativeSingleTransformer(String query, Class<E> resultTransformer, Object... params) {
        synchronized (Statement.SYNC) {
            return this.queryExecutor.executeNativeSingleTransformer(query, resultTransformer, params);
        }
    }

    @Override
    public <E> List<E> executeNativeTransformer(String query, Class<E> resultTransformer, Object... params) {
        synchronized (Statement.SYNC) {
            return this.queryExecutor.executeNativeTransformer(query, resultTransformer, params);
        }
    }

    @Override
    public void runTrans(TransactionalOperation operation) {
        synchronized (Statement.SYNC) {
            this.queryExecutor.runTrans(operation);
        }
    }

    @Override
    public void addInsertListener(InsertListener listener) {
        this.queryExecutor.addInsertListener(listener);
    }

    @Override
    public void removeInsertListener(InsertListener listener) {
        this.queryExecutor.removeInsertListener(listener);
    }

    @Override
    public void addUpdateListener(UpdateListener listener) {
        this.queryExecutor.addUpdateListener(listener);
    }

    @Override
    public void removeUpdateListener(UpdateListener listener) {
        this.queryExecutor.removeUpdateListener(listener);
    }

    @Override
    public void addDeleteListener(DeleteListener listener) {
        this.queryExecutor.addDeleteListener(listener);
    }

    @Override
    public void removeDeleteListener(DeleteListener listener) {
        this.queryExecutor.removeDeleteListener(listener);
    }

    @Override
    public void addMetadataUpdateListener(MetadataUpdateListener listener) {
        this.queryExecutor.addMetadataUpdateListener(listener);
    }

    @Override
    public void removeMetadataUpdateListener(MetadataUpdateListener listener) {
        this.queryExecutor.removeMetadataUpdateListener(listener);
    }

    @Override
    public String tableName() {
        return this.queryExecutor.tableName();
    }
}
