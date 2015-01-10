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
import br.com.mobilemind.veloster.orm.QueryBuilder;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.orm.model.ColumnWrapper;
import br.com.mobilemind.veloster.orm.QueryStatementBuilder;
import br.com.mobilemind.veloster.sql.ResultSet;
import br.com.mobilemind.veloster.sql.Statement;
import br.com.mobilemind.veloster.tools.VelosterConfig;
import br.com.mobilemind.api.utils.ClassUtil;
import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.exceptions.VelosterException;
import br.com.mobilemind.veloster.orm.model.ListLazy;
import br.com.mobilemind.veloster.sql.type.Criteria;
import br.com.mobilemind.veloster.sql.type.Eq;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author Ricardo Bocchi
 */
public class QueryStatementBuilderImpl<T extends Entity> implements QueryStatementBuilder<T> {

    private QueryBuilder<T> queryBuilder;
    private Class<T> clazz;
    private SimpleDateFormat dataFormat;
    private AnnotationsManager<T> annotationsManager;
    public final static Map<Class, Boolean> FIELD_ROW_ID = new HashMap<Class, Boolean>();

    @Override
    public void build(Class<T> clazz, QueryBuilder<T> queryBuilder, AnnotationsManager<T> annotationsManager) {
        this.clazz = clazz;
        this.dataFormat = VelosterConfig.getConf().getDateFormat();
        this.annotationsManager = annotationsManager;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public void compileParameters(Statement stmt, Object[] params) throws Exception {
        if (params != null) {
            int i = 1;
            for (Object it : params) {
                if (it instanceof Date) {
                    stmt.setDate(i++, null);
                } else if (ClassUtil.isDouble(it.getClass())) {
                    stmt.setDouble(i++, (Double) it);
                } else if (ClassUtil.isBoolean(it.getClass())) {
                    stmt.setBoolean(i++, (Boolean) it);
                } else {
                    stmt.setString(i++, it.toString());
                }
            }
        }
    }

    @Override
    public void compileParameters(T entity, Statement stmt, Criteria<T> criteria) throws Exception {
        List<ColumnWrapper> fields = this.queryBuilder.getFieldsForParameters(criteria);
        Class type = null;
        Object value = null;
        Field field = null;
        int i = 0;

        for (ColumnWrapper f : fields) {

            if (f.isNotStmtValue() || f.isHasMany()) {
                continue;
            }

            field = f.getFieldReflect();
            type = f.getType();

            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            if (!f.isCriteria()) {
                value = field.get(entity);
            } else {
                value = f.getValue();
                if (f.isJoin()) {
                    type = field.getType();
                }
                
                
                MMLogger.log(Level.INFO, getClass(), "#### " + value + " type " + type);
            }

            if (ClassUtil.isAssignableFrom(type, Entity.class)) {
                if (value instanceof Entity) {
                    value = ((Entity) value).getId();
                    type = Long.class;
                } else if (!f.isNullable()) {
                    throw new VelosterException("field " + f.getName() + " to entity " + clazz.getSimpleName() + " can't be null");
                } else {
                    type = Long.class;
                    value = null;
                }

                if (!f.isNullable() && (value == null || Long.parseLong(value.toString()) <= 0)) {
                    throw new VelosterException("field " + f.getName() + "to entity " + clazz.getSimpleName() + " can't have id 0");
                }
            } else if (type.isEnum()) {
                if (f.parseEnumInt()) {
                    if (value != null) {
                        //throw new VelosterException("value can't be null in enum " + type.getSimpleName() + " in entity " + f.getTable().getTableClass().getName());
                        value = ((Enum) value).ordinal();
                    }

                    type = Integer.class;
                } else {
                    if (f.parseEnumString()) {
                        if (value != null) {
                            //throw new VelosterException("value can't be null in enum " + type.getSimpleName() + " in entity " + f.getTable().getTableClass().getName());
                            value = ((Enum) value).name();
                        }

                        type = String.class;
                    }
                }
            }
            createStatement(stmt, type, value, ++i);
        }
    }

    private void createStatement(Statement stmt, Class type, Object value, int i) throws SQLException {
        if (ClassUtil.isLong(type)) {
            stmt.setLong(i, ((value != null && value instanceof Long) ? (Long) value : null));
        } else if (ClassUtil.isString(type)) {
            stmt.setString(i, (String) value);
        } else if (ClassUtil.isInteger(type)) {
            stmt.setInteger(i, (Integer) value);
        } else if (ClassUtil.isBoolean(type)) {
            if (!(value instanceof Boolean)) {
                value = false;
            }
            stmt.setBoolean(i, Boolean.parseBoolean(value.toString()));
        } else if (ClassUtil.isDouble(type)) {
            stmt.setDouble(i, (Double) value);
        } else if (ClassUtil.isDate(type)) {
            stmt.setString(i, value != null ? dataFormat.format(value) : null);
        }
    }

    @Override
    public T getUniqueResult(ResultSet rs) throws Exception {
        if (rs.next()) {
            T e = this.clazz.newInstance();
            e = getUniqueResult0(e, rs, null);
            return e;
        }
        return null;
    }

    @Override
    public T getUniqueResult(T entity, ResultSet rs) throws Exception {
        if (rs.next()) {
            return getUniqueResult0(entity, rs, null);
        }
        return null;
    }

    private T getUniqueResult0(T entity, ResultSet rs, Map<String, Entity> cache) throws SQLException, Exception {
        List<ColumnWrapper> fields = this.annotationsManager.getFields();
        List<ColumnWrapper> lazyLoadWrapper = new ArrayList<ColumnWrapper>();
        Class type = null;
        Object value = null;
        Field field = null;

        for (ColumnWrapper f : fields) {

            type = f.getType();
            field = f.getFieldReflect();
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            //BatchSize batchSize = f.getFieldReflect().getAnnotation(BatchSize.class);

            if (type.isEnum()) {
                if (f.parseEnumInt()) {
                    Integer val = rs.getInteger(f.getName());
                    if (val != null) {
                        for (Object p : type.getEnumConstants()) {
                            if (val == ((Enum) p).ordinal()) {
                                value = p;
                            }
                        }

                        if (value == null) {
                            throw new Exception("enum type " + type.getName() + " value " + val + " not found");
                        }

                        field.set(entity, value);
                    }

                } else {
                    String val = rs.getString(f.getName());

                    if (val != null) {
                        for (Object p : type.getEnumConstants()) {
                            if (((Enum) p).name().equals(val)) {
                                value = p;
                            }
                        }

                        if (value == null) {
                            throw new Exception("enum type " + type.getName() + " name " + val + " not found");
                        }

                        field.set(entity, value);
                    }
                }
                continue;
            }

            if (ClassUtil.isAssignableFrom(type, Entity.class)) {
                Long id = rs.getLong(f.getName());
                Entity en = null;
                String key = f.getName() + "#" + id;

                // cursor android retorna 0 para null
                if (id == null || id == 0) {
                    MMLogger.log(Level.INFO, this.getClass(), "entity id=[" + id + "] not found");
                    continue;
                }

                MMLogger.log(Level.INFO, this.getClass(), "get entity id=[" + id + "]");

                if (cache != null) { //cache control
                    if (cache.containsKey(key)) {
                        en = cache.get(key);
                        field.set(entity, en);
                        MMLogger.log(Level.INFO, this.getClass(), "get entity from cache id=[" + id + "]");
                    }
                }

                if (en == null) {
                    en = (Entity) f.getType().newInstance();
                    en.setId(id);
                    if (cache != null) {
                        cache.put(key, en);
                    }
                    field.set(entity, en);
                    MMLogger.log(Level.INFO, this.getClass(), "entity load id=[" + id + "]");
                } else {
                    MMLogger.log(Level.INFO, this.getClass(), "entity not load id=[" + id + "]");
                }

                continue;
            }

            if (ClassUtil.isString(type)) {
                field.set(entity, rs.getString(f.getName()));
            } else if (ClassUtil.isLong(type)) {
                field.set(entity, rs.getLong(f.getName()));
            } else if (ClassUtil.isInteger(type)) {
                field.set(entity, rs.getInteger(f.getName()));
            } else if (ClassUtil.isBoolean(type)) {
                field.set(entity, rs.getBoolean(f.getName()));
            } else if (ClassUtil.isDouble(type)) {
                field.set(entity, rs.getDouble(f.getName()));
            } else if (ClassUtil.isDate(type)) {
                String str = rs.getString(f.getName());
                if (str != null) {
                    field.set(entity, this.dataFormat.parse(str));
                }
            } else if (f.isHasMany()) {
                lazyLoadWrapper.add(f);
            } else {
                throw new VelosterException("type " + type.getSimpleName() + " not found");
            }
        }
        ColumnWrapper primaryKey = null;

        for (ColumnWrapper it : fields) {
            if (it.isPrimaryKey()) {
                primaryKey = it;
                break;
            }
        }

        //process lazy load lists
        for (ColumnWrapper wrapper : lazyLoadWrapper) {
            Criteria criteria = VelosterRepository.getVeloster(wrapper.getGenericType()).createCriteria();
            criteria.add(wrapper.getHasMany().reference(), new Eq(entity));
            wrapper.getFieldReflect().set(entity, new ListLazy(criteria, wrapper.getGenericType(), wrapper.getBatchSize(), wrapper.isLazy()));
        }

        return entity;
    }

    @Override
    public List<T> getResult(ResultSet rs) throws Exception {
        List<T> items = new ArrayList<T>();
        Map<String, Entity> cache = new HashMap<String, Entity>();

        while (rs.next()) {
            T item = this.clazz.newInstance();
            this.getUniqueResult0(item, rs, cache);
            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(), "created entity " + item);
            }
            items.add(item);
        }
        if (MMLogger.isLogable()) {
            MMLogger.log(Level.INFO, this.getClass(), "created list size " + items.size());
        }

        return items;
    }

    @Override
    public <E> List<E> getResultTransformer(ResultSet rs, Class<E> resultTransformer) throws SQLException {
        List<E> records = new LinkedList<E>();
        String columnNames[] = rs.getColumnNames();
        E data = null;
        Field field;
        Class fieldType;
        Map<String, Entity> cache = new HashMap<String, Entity>();

        try {
            while (rs.next()) {
                data = resultTransformer.newInstance();

                for (String column : columnNames) {
                    field = ClassUtil.getField(resultTransformer, column);

                    if (field != null) {
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        fieldType = field.getType();

                        if (ClassUtil.isString(fieldType)) {
                            field.set(data, rs.getString(column));
                        } else if (ClassUtil.isBoolean(fieldType)) {
                            field.set(data, rs.getBoolean(column));
                        } else if (ClassUtil.isDate(fieldType)) {
                            field.set(data, rs.getDate(column));
                        } else if (ClassUtil.isDouble(fieldType)) {
                            field.set(data, rs.getDouble(column));
                        } else if (ClassUtil.isInteger(fieldType)) {
                            field.set(data, rs.getInteger(column));
                        } else if (ClassUtil.isLong(fieldType)) {
                            field.set(data, rs.getLong(column));
                        } else if (fieldType.isEnum()) {
                            boolean found = false;
                            try {
                                int val = rs.getInteger(column);
                                for (Object p : fieldType.getEnumConstants()) {
                                    if (((Enum) p).ordinal() == val) {
                                        field.set(data, p);
                                        found = true;
                                        break;
                                    }
                                }
                                if (found) {
                                    continue;
                                }
                            } catch (Exception e) {
                                MMLogger.log(Level.INFO, getClass(), "velue to column [" + column + "] is enum, but not is a integer");
                            }

                            try {
                                String value = rs.getString(column);
                                Enum e = Enum.valueOf(fieldType, value);
                                field.set(data, e);
                                found = true;
                            } catch (Exception e) {
                                MMLogger.log(Level.INFO, getClass(), "velue to column [" + column + "] is enum, but not is a string");
                            }

                            if (!found) {
                                throw new VelosterException("not match value to enum column [" + column
                                        + "]. expected enum constant or ordinal, but found [" + rs.getString(column) + "]");
                            }
                        } else if (ClassUtil.isAssignableFrom(fieldType, Entity.class)) {
                            Long id = rs.getLong(column);

                            // cursor android retorna 0 para null
                            if (id == null || id == 0) {
                                MMLogger.log(Level.INFO, this.getClass(), "entity id=[" + id + "] not found");
                                continue;
                            }

                            MMLogger.log(Level.INFO, getClass(), "getting entity reference id=[" + id + "]");

                            String key = fieldType.getName() + "#" + id;

                            if (cache.containsKey(key)) {
                                MMLogger.log(Level.INFO, getClass(), "getting entity from cache id=[" + id + "]");
                                field.set(data, cache.get(key));
                            } else {
                                Entity e = (Entity) fieldType.newInstance();
                                e.setId(id);
                                field.set(data, e);
                                cache.put(key, e);
                            }
                        }
                    }
                }
                records.add(data);
            }
        } catch (VelosterException e) {
            throw e;
        } catch (Exception e) {
            throw new VelosterException(e.getMessage(), e);
        }

        return records;
    }

    @Override
    public boolean isGetRowId() {
        if (FIELD_ROW_ID.containsKey(clazz)) {
            return FIELD_ROW_ID.get(clazz);
        }

        for (ColumnWrapper wrapper : this.annotationsManager.getFields()) {
            if (wrapper.isPrimaryKey()) {
                FIELD_ROW_ID.put(clazz, wrapper.ignoreInsert());
                return wrapper.ignoreInsert();
            }
        }
        return true;
    }
}
