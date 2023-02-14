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
import br.com.mobilemind.veloster.orm.DDLDialect;
import br.com.mobilemind.veloster.sql.type.*;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.orm.model.ColumnWrapper;
import br.com.mobilemind.veloster.orm.QueryBuilder;
import br.com.mobilemind.veloster.orm.QueryFormatter;
import br.com.mobilemind.api.utils.log.MMLogger;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * Implementação para o construtor de comandos SQL
 *
 * @author Ricardo Bocchi
 */
public class QueryBuilderImpl<T extends Entity> implements QueryBuilder<T> {

    private int queryType;
    private AnnotationsManager<T> annotationsManager;
    private Class<T> clazz;
    private QueryFormatter formatter;
    private DDLDialect dialect;
    private String tableName;
    private final static Map<Class, List<ColumnWrapper>> CASCADE_FIELDS = new HashMap<Class, List<ColumnWrapper>>();
    private final static Map<Class, List<ColumnWrapper>> HASMANY_FIELDS = new HashMap<Class, List<ColumnWrapper>>();
    private final static Map<Class, List<ColumnWrapper>> INSERT_FIELDS = new HashMap<Class, List<ColumnWrapper>>();
    private final static Map<Class, String[]> ORDERED_FIELDS_FIELDS = new HashMap<Class, String[]>();
    private final static Map<Class, String[]> ORDERED_FIELDS_FIELDS_INSERT = new HashMap<Class, String[]>();
    private final static Map<Class, List<ColumnWrapper>> FIELDS_FOR_PARAMETERS = new HashMap<Class, List<ColumnWrapper>>();
    private final static Map<Class, String> DROP_TABLE = new HashMap<Class, String>();
    private final static Map<Class, String> CREATE_TABLE = new HashMap<Class, String>();
    private final static Map<Class, String> INSERT_QUERY = new HashMap<Class, String>();
    private final static Map<String, String> UPDATE_QUERY = new HashMap<String, String>();

    @Override
    public void build(Class<T> clazz, AnnotationsManager<T> annotationsManager, QueryFormatter formatter, DDLDialect dialect) {
        this.clazz = clazz;
        this.annotationsManager = annotationsManager;
        this.formatter = formatter;
        this.dialect = dialect;
        this.tableName = this.annotationsManager.getTable().name();
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public int getQueryType() {
        return queryType;
    }

    @Override
    public Criteria<T> createCriteria() {
        return new Criteria<T>(this.clazz);
    }

    @Override
    public String getCreateTableQuery() {

        this.queryType = DDL;

        if (CREATE_TABLE.containsKey(clazz)) {
            return CREATE_TABLE.get(clazz);
        }

        String query = this.dialect.complileCreateTable(
                tableName,
                this.annotationsManager.getFields());

        MMLogger.log(Level.INFO, this.getClass(), query);

        CREATE_TABLE.put(clazz, query);

        return query;
    }

    @Override
    public String getDropTableQuery() {

        this.queryType = DDL;

        if (DROP_TABLE.containsKey(clazz)) {
            return DROP_TABLE.get(clazz);
        }

        String query = this.dialect.compileDropTable(
                tableName);

        MMLogger.log(Level.INFO, this.getClass(), query);

        DROP_TABLE.put(clazz, query);

        return query;
    }

    @Override
    public String getInsertQuery() {

        this.queryType = INSERT;

        if (INSERT_QUERY.containsKey(clazz)) {
            return INSERT_QUERY.get(clazz);
        }


        String query = this.formatter.compileInsert(tableName,
                getAllOrderedFieldsForQuery());

        MMLogger.log(Level.INFO, this.getClass(), query);

        INSERT_QUERY.put(clazz, query);

        return query;
    }

    @Override
    public String getUpdateQuery(Criteria<T> criteria) {
        this.queryType = UPDATE;
        final String key = tableName + "#" + criteria.keyHashCode();

        if (UPDATE_QUERY.containsKey(key)) {
            return UPDATE_QUERY.get(key);
        }

        if (criteria != null) {
            this.formatter.setWhere(criteria.getExpressions());
        }

        String query = this.formatter.compileUpdate(tableName,
                getAllOrderedFieldsForQuery());

        MMLogger.log(Level.SEVERE, this.getClass(), query);

        UPDATE_QUERY.put(key, query);

        return query;
    }

    @Override
    public String getDeleteQuery(Criteria<T> criteria) {
        this.queryType = DELETE;

        if (criteria != null) {
            this.formatter.setWhere(criteria.getExpressions());
        }

        String query = this.formatter.compileDelete(tableName);

        MMLogger.log(Level.INFO, this.getClass(), query);
        return query;
    }

    @Override
    public String getSelectQuery(Criteria<T> criteria) {
        this.queryType = SELECT;

        this.formatter.setWhere(criteria.getExpressions())
                .setPagination(criteria.getLimit(), criteria.getOffset());

        if (criteria.isAutoIncrementOffset()) {
            criteria.setOffset(criteria.getOffset() + criteria.getLimit());
        }

        String query = this.formatter.compileSelect(tableName,
                getAllOrderedFieldsForQuery());

        MMLogger.log(Level.INFO, this.getClass(), query);
        return query;
    }

    @Override
    public String getCountQuery(Criteria<T> criteria) {
        this.queryType = COUNT;

        this.formatter.setWhere(criteria.getExpressions());

        String query = this.formatter.compileCount(tableName);
        MMLogger.log(Level.INFO, this.getClass(), query);
        return query;
    }

    @Override
    public String getAddColumnQuery(ColumnWrapper field) {
        this.queryType = DDL;

        String query = this.dialect.compileCreateColumn(field);
        MMLogger.log(Level.INFO, this.getClass(), query);
        return query;
    }

    /**
     * Seleciona os campos dos parametros de um comando SQL
     *
     * @return
     */
    @Override
    public synchronized List<ColumnWrapper> getFieldsForParameters(Criteria<T> criteria) {
        List<ColumnWrapper> fields = new ArrayList<ColumnWrapper>();

        if (this.queryType == UPDATE || this.queryType == INSERT) {
            if (FIELDS_FOR_PARAMETERS.containsKey(clazz)) {
                if (this.queryType == INSERT) {
                    return FIELDS_FOR_PARAMETERS.get(clazz);
                } else {
                    fields.addAll(FIELDS_FOR_PARAMETERS.get(clazz));
                }
            } else {
                for (ColumnWrapper f : this.annotationsManager.getFields()) {

                    if (f.ignoreInsert() && (this.queryType == INSERT || this.queryType == UPDATE)) {
                        continue;
                    }
                    fields.add(f);
                }
            }
            if (this.queryType == INSERT) {
                FIELDS_FOR_PARAMETERS.put(clazz, fields);
                return fields;
            } else {
                List<ColumnWrapper> list = new ArrayList<ColumnWrapper>(fields.size());
                list.addAll(fields);
                FIELDS_FOR_PARAMETERS.put(clazz, list);
            }
        }

        //Adiciona os parametros condicionais
        switch (this.queryType) {
            case SELECT:
            case UPDATE:
            case DELETE:
            case COUNT:
                List<Expression> expressions = criteria.getExpressions();
                ColumnWrapper f = null;
                for (Expression e : expressions) {

                    if (e instanceof OrderBy || e instanceof Exists || e instanceof NotExists) {
                        continue;
                    }

                    ColumnWrapper ff = e.getField();

                    if (e instanceof Between) {
                        Between between = (Between) e;
                        Map map = between.getMap();
                        Set set = map.keySet();
                        for (Object s : set) {
                            f = new ColumnWrapper(ff.getFieldReflect(),
                                    ff.getJoinField(), ff.getTable());
                            f.setCriteria(true);
                            f.setValue(s);
                            fields.add(f);

                            f = new ColumnWrapper(ff.getFieldReflect(),
                                    ff.getJoinField(), ff.getTable());
                            f.setCriteria(true);
                            f.setValue(map.get(s));
                            fields.add(f);
                        }
                    } else {
                        Iterator it = e.getValues().iterator();
                        while (it.hasNext()) {
                            f = new ColumnWrapper(ff.getFieldReflect(),
                                    ff.getJoinField(), ff.getTable());
                            f.setCriteria(true);
                            f.setValue(it.next());
                            fields.add(f);
                        }
                    }
                }
                break;
        }
        return fields;
    }

    /**
     * Retorna todos os campos ornedados para construir uma SQL
     *
     * @return
     */
    private String[] getAllOrderedFieldsForQuery() {


        if (this.queryType == INSERT || this.queryType == UPDATE) {
            if (ORDERED_FIELDS_FIELDS_INSERT.containsKey(clazz)) {
                return ORDERED_FIELDS_FIELDS_INSERT.get(clazz);
            }
        } else {
            if (ORDERED_FIELDS_FIELDS.containsKey(clazz)) {
                return ORDERED_FIELDS_FIELDS.get(clazz);
            }
        }

        List<String> fields = new ArrayList<String>();

        for (ColumnWrapper f : this.annotationsManager.getFields()) {
            if (f.ignoreInsert() && (this.queryType == INSERT || this.queryType == UPDATE)) {
                continue;
            }
            if (f.isHasMany()) {
                continue;
            }
            fields.add(f.getName());
        }

        String items[] = fields.toArray(new String[fields.size()]);

        if ((this.queryType == INSERT || this.queryType == UPDATE)) {
            ORDERED_FIELDS_FIELDS_INSERT.put(clazz, items);
        } else {
            ORDERED_FIELDS_FIELDS.put(clazz, items);
        }

        return items;
    }

    /**
     * Retorna a lista de campos para o insert
     *
     * @return
     */
    @Override
    public List<ColumnWrapper> getFieldForInsert() {

        if (INSERT_FIELDS.containsKey(clazz)) {
            return INSERT_FIELDS.get(clazz);
        }

        List<ColumnWrapper> list = new LinkedList<ColumnWrapper>();

        for (ColumnWrapper f : this.annotationsManager.getFields()) {
            if (f.ignoreInsert() && this.queryType == INSERT) {
                continue;
            }
            if (f.isHasMany()) {
                continue;
            }
            list.add(f);
        }

        INSERT_FIELDS.put(clazz, list);

        return list;

    }

    @Override
    public List<ColumnWrapper> getCascadeFields() {

        if (CASCADE_FIELDS.containsKey(this.clazz)) {
            return CASCADE_FIELDS.get(clazz);
        }
        List<ColumnWrapper> list = new ArrayList<ColumnWrapper>();

        for (ColumnWrapper f : this.annotationsManager.getFields()) {
            if (f.cascadeOnInsert() || f.cascadeOnUpdate()) {
                list.add(f);
            }
        }

        CASCADE_FIELDS.put(clazz, list);

        return list;
    }

    @Override
    public List<ColumnWrapper> getHasManyFields() {

        if (HASMANY_FIELDS.containsKey(clazz)) {
            return HASMANY_FIELDS.get(clazz);
        }

        List<ColumnWrapper> list = new ArrayList<ColumnWrapper>();

        for (ColumnWrapper f : this.annotationsManager.getFields()) {
            if (f.isHasMany()) {
                list.add(f);
            }
        }

        HASMANY_FIELDS.put(clazz, list);
        return list;
    }
}
