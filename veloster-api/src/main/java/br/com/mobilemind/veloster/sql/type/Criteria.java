package br.com.mobilemind.veloster.sql.type;

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
import br.com.mobilemind.veloster.exceptions.VelosterException;
import br.com.mobilemind.veloster.orm.QueryExecutor;
import br.com.mobilemind.veloster.orm.model.ColumnWrapper;
import br.com.mobilemind.veloster.orm.annotations.Column;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import br.com.mobilemind.api.utils.ClassUtil;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Criate a Criteria to database search
 *
 * @author Ricardo Bocchi
 */
public class Criteria<T extends Entity> {

    private Entity tag;
    private final Class<T> clazz;
    private List<Expression> expressions;
    private int limit;
    private int offset;
    private boolean autoIncrementOffset;
    private QueryExecutor<T> executor;
    private int keyHashCode;
    private String alias;

    /**
     * Construtor
     *
     * @param clazz Persistent Entity
     */
    public Criteria(Class<T> clazz) {
        this.clazz = clazz;
        this.expressions = new ArrayList<Expression>();
    }

    /**
     * add criteria condition
     *
     * @param field field name
     * @param expression expression
     * @return
     */
    public Criteria add(String field, Expression expression) {
        if (expression == null || field == null || "".equals(field)) {
            throw new InvalidParameterException("field and expresson can't be null");
        }
        checkField(field, expression);
        return this;
    }

    public Criteria add(Expression expression) {
        if (expression == null) {
            throw new InvalidParameterException("expresson can't be null");
        }
        checkField(null, expression);
        return this;
    }


    public Criteria<T> setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    private void checkField(String field, Expression expression) {
        try {

            if(field != null) {
                int idx = field.indexOf(".");
                if (idx > -1) {
                    this.joinExpression(field, expression);
                } else {
                    java.lang.reflect.Field f = ClassUtil.getAnnotatedField(this.clazz, Column.class, field);
                    if (f==null) {
                        throw new InvalidParameterException();
                    }
                    boolean b = expression instanceof IsNull || expression instanceof NotIsNull;
                    expression.setField(new ColumnWrapper(f, null, b));
                }
            }

            this.expressions.add(expression);
        } catch (Exception e) {
            throw new InvalidParameterException("field not fount or @Column annotations not fount in " + this.clazz.getSimpleName() + " for field " + field);
        }
    }

    private void joinExpression(String field, Expression expression) {
        String fields[] = field.split("\\.");
        if (fields.length > 2) {
            throw new InvalidParameterException("invalid join level");
        }
        java.lang.reflect.Field f = ClassUtil.getAnnotatedField(this.clazz, Column.class, fields[0]);
        if (f == null) {
            throw new InvalidParameterException("field " + fields[0] + " not found");
        }

        if (!ClassUtil.isAssignableFrom(f.getType(), Entity.class)) {
            throw new InvalidParameterException("type unexpected for join: " + f.getType().getSimpleName());
        }

        java.lang.reflect.Field join = ClassUtil.getAnnotatedField(f.getType(), Column.class, fields[1]);
        if (join == null) {
            throw new InvalidParameterException("field " + fields[1] + " not found");
        }

        expression.setField(new ColumnWrapper(f, new ColumnWrapper(join, null), null));
    }

    /**
     * get list of expressions
     *
     * @return
     */
    public List<Expression> getExpressions() {
        return this.expressions;
    }

    public int keyHashCode() {

        if (keyHashCode > 0) {
            return keyHashCode;
        }

        Set<String> set = new TreeSet<String>();
        for (Expression it : expressions) {
            set.add(it.getField().getFieldName() + "#" + it.getKeyWork());
        }

        String hash = "";

        for (String it : set) {
            hash += it;
        }

        return (keyHashCode = hash.hashCode());
    }

    /**
     * add field for order by query
     *
     * @param fields
     * @return
     */
    public Criteria<T> orderBy(String... fields) {
        for (String field : fields) {
            checkField(field, new OrderBy(field));
        }
        return this;
    }



    public Criteria<T> orderByDesc(String... fields) {
        for (String field : fields) {
            checkField(field, new OrderBy(field, true));
        }
        return this;
    }

    /**
     * set limit of page. default is 0
     *
     * @param limit
     * @return
     */
    public Criteria<T> setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * set offset of page. default is 0
     *
     * @param offset
     * @return
     */
    public Criteria<T> setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    /**
     * enable automatic incremente to paginator. default is false
     *
     * @param autoIncrementOffset
     * @return
     */
    public Criteria<T> setAutoIncrementOffset(boolean autoIncrementOffset) {
        this.autoIncrementOffset = autoIncrementOffset;
        return this;
    }

    /**
     * get limit of page
     *
     * @return
     */
    public int getLimit() {
        return limit;
    }

    /**
     * get offset of page
     *
     * @return
     */
    public int getOffset() {
        return offset;
    }

    /**
     * if auto increment of paginator is enable
     *
     * @return
     */
    public boolean isAutoIncrementOffset() {
        return autoIncrementOffset;
    }

    /**
     * get single result by criteria
     *
     * @return
     */
    public T load() {

        if (this.executor == null) {
            this.loadQueryExecutor();
        }

        return this.executor.loadByCriteria(this);
    }

    public Entity getTag() {
        return tag;
    }

    public void setTag(Entity tag) {
        this.tag = tag;
    }

    /**
     * list by criteria
     *
     * @return
     */
    public List<T> list() {

        if (this.executor == null) {
            this.loadQueryExecutor();
        }

        return this.executor.listByCriteria(this);
    }

    public int count() {
        if (this.executor == null) {
            this.loadQueryExecutor();
        }

        return this.executor.countByCriteria(this);
    }

    public void delete() {
        if (this.executor == null) {
            this.loadQueryExecutor();
        }

        this.executor.deleteByCriteria(this);
    }

    private void loadQueryExecutor() {

        this.executor = VelosterRepository.getVelosterControll(clazz).getQueryExecutor();

        if (this.executor == null) {
            throw new VelosterException("query executor to entity " + this.clazz.getName() + " not found");
        }
    }
}
