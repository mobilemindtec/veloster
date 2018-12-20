package br.com.mobilemind.veloster.orm.model;

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
import br.com.mobilemind.veloster.orm.annotations.Column;
import br.com.mobilemind.api.utils.ClassUtil;
import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.exceptions.VelosterException;
import br.com.mobilemind.veloster.orm.annotations.DefaultValue;
import br.com.mobilemind.veloster.orm.annotations.EnumType;
import br.com.mobilemind.veloster.orm.annotations.Enumerated;
import br.com.mobilemind.veloster.orm.annotations.Id;
import br.com.mobilemind.veloster.orm.annotations.JoinColumn;
import br.com.mobilemind.veloster.orm.annotations.HasMany;
import br.com.mobilemind.veloster.sql.ColumnDefaultValueGenarator;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author Ricardo Bocchi
 */
public class ColumnWrapper {

    private Column column;
    private TableImpl table;
    private Field field;
    private String name;
    private boolean criteria;
    private Object value;
    private ColumnWrapper joinField;
    private boolean notStmtValue;
    private Id primaryKey;
    private JoinColumn joinColumn;
    private HasMany hasMany;
    private Enumerated enumerated;
    private ColumnDefaultValueGenarator genarator;
    private boolean unique;
    private Class genericType;

    public ColumnWrapper(Field field, TableImpl table) {
        this(field, null, table);
    }

    public ColumnWrapper(Field field, TableImpl table, boolean notStmtValue) {
        this(field, null, table);
        this.notStmtValue = notStmtValue;
    }

    public ColumnWrapper(Field field, ColumnWrapper joinField, TableImpl table) {
        this.field = field;
        this.column = this.field.getAnnotation(Column.class);
        this.table = table;
        this.joinField = joinField;
        
        findAnnotations(field);


        if (column == null) {
            throw new InvalidParameterException("field can't be null");
        }

        init();
    }

    private void init() {
        if ("".equals(this.column.name())) {
            this.name = this.field.getName();
        } else {
            this.name = this.column.name();
        }
    }

    private void findAnnotations(Field field) {
        this.primaryKey = this.field.getAnnotation(Id.class);
        this.joinColumn = this.field.getAnnotation(JoinColumn.class);
        this.enumerated = this.field.getAnnotation(Enumerated.class);
        this.hasMany = this.field.getAnnotation(HasMany.class);
        DefaultValue defaultValue = this.field.getAnnotation(DefaultValue.class);

        if (this.joinColumn != null) {
            if (!ClassUtil.isAssignableFrom(field.getType(), Entity.class)) {
                throw new VelosterException("JoinColumn [" + this.field.getName()
                        + "] from Table [" + table.name() + "] should implement Entity class.");
            }
        }

        if (this.enumerated != null) {
            if (!this.field.getType().isEnum()) {
                throw new VelosterException("Enumerated [" + this.field.getName()
                        + "] from Table [" + table.name() + "] should be enum.");
            }
        }

        if (primaryKey != null) {
            if (!ClassUtil.isInteger(this.field.getType()) && !ClassUtil.isLong(this.field.getType())) {
                throw new VelosterException("Id [" + this.field.getName()
                        + "] from Table [" + table.name() + "] should be int or long.");
            }
        } 

        if (defaultValue != null) {
            try {
                this.genarator = defaultValue.generator().newInstance();
            } catch (Exception e) {
                MMLogger.log(Level.SEVERE, getClass(), e);
                throw new VelosterException("error create default value generator from table ["
                        + table.name() + "] field [" + field.getName() + "]");
            }
        }

        if (hasMany != null) {
            if (!ClassUtil.isAssignableFrom(this.field.getType(), List.class)) {
                throw new VelosterException("lazy load field should be List type from table ["
                        + table.name() + "] field [" + field.getName() + "]");
            }

            ParameterizedType listType = (ParameterizedType) field.getGenericType();
            if (listType.getActualTypeArguments() == null
                    || listType.getActualTypeArguments().length == 0) {
                throw new RuntimeException("list " + field.getName() + " not contains generic type");
            }
            Class type = (Class) listType.getActualTypeArguments()[0];

            if (!ClassUtil.isAssignableFrom(type, Entity.class)) {
                throw new RuntimeException("generic in list " + field.getName() + " not is Entity type");
            }
            this.genericType = type;
        }
    }

    public String getName() {
        return this.name;
    }

    public boolean isPrimaryKey() {
        return this.primaryKey != null;
    }

    public boolean isForeignKey() {
        return this.joinColumn != null;
    }

    public boolean isHasMany() {
        return this.hasMany != null;
    }

    public HasMany getHasMany() {
        return hasMany;
    }

    public Class getGenericType() {
        return genericType;
    }

    public boolean isLazy() {
        return this.hasMany != null && this.hasMany.lazy();
    }

    public int getBatchSize() {
        if (this.isHasMany()) {
            return this.hasMany.batchSize();
        }
        return 0;
    }

    public JoinColumn getJoinColumn() {
        return joinColumn;
    }

    public boolean isNullable() {
        return this.column.nullable();
    }

    public String getDefaultValue() {
        return this.column.defaultValue();
    }

    public int getLength() {
        return this.column.length();
    }

    public String getFieldName() {
        return this.field.getName();
    }

    public Class getType() {
        return this.field.getType();
    }

    public ColumnWrapper getJoinField() {
        return joinField;
    }

    public void setJoinField(ColumnWrapper joinField) {
        this.joinField = joinField;
    }

    public boolean isJoin() {
        return this.joinField != null;
    }

    public boolean isUnique() {
        return this.column.unique();
    }

    public Object getDefault() {
        if (ClassUtil.isString(this.field.getType())) {
            return this.column.defaultValue();
        } else if (ClassUtil.isInteger(this.field.getType())) {
            return Integer.parseInt(this.column.defaultValue());
        } else if (ClassUtil.isDouble(this.field.getType())) {
            return Double.parseDouble(this.column.defaultValue());
        } else if (ClassUtil.isBoolean(this.field.getType())) {
            return Boolean.parseBoolean(this.column.defaultValue());
        } else if (ClassUtil.isLong(this.field.getType())) {
            return Long.parseLong(this.column.defaultValue());
        } else {
            throw new InvalidParameterException("type" + this.field.getType() + " can't be default");
        }
    }

    public TableImpl getTable() {
        return this.table;
    }

    public boolean ignoreInsert() {
        if (this.primaryKey != null) {
            return this.primaryKey.ignoreInsert();
        }
        return this.column.ignoreInsert();
    }

    public boolean ignoreUpdate() {
        return this.column.ignoreUpdate();
    }

    public boolean parseEnumInt() {
        if (this.enumerated == null) {
            return false;
        }
        return this.enumerated.enumType() == EnumType.ORDINAL;
    }

    public boolean parseEnumString() {
        if (this.enumerated == null) {
            return false;
        }
        return this.enumerated.enumType() == EnumType.STRING;
    }

    public boolean cascadeOnInsert() {
        if (this.joinColumn == null) {
            return false;
        }
        return this.joinColumn.cascadeOnInsert();
    }

    public boolean cascadeOnUpdate() {
        if (this.joinColumn == null) {
            return false;
        }
        return this.joinColumn.cascadeOnUpdate();
    }

    public boolean cascadeOnInsertOrUpdate() {
        if (this.joinColumn == null) {
            return false;
        }
        return this.joinColumn.cascadeOnInsertAndUpdate();
    }

    public boolean isCriteria() {
        return criteria;
    }

    public void setCriteria(boolean criteria) {
        this.criteria = criteria;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Field getFieldReflect() {
        return field;
    }

    public boolean isNotStmtValue() {
        return notStmtValue;
    }

    public void setNotStmtValue(boolean notStmtValue) {
        this.notStmtValue = notStmtValue;
    }

    public ColumnDefaultValueGenarator getDefaultValueGenarator() {
        return genarator;
    }

    public boolean isDefaultValueGenerated() {
        return genarator != null;
    }
}
