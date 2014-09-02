package br.com.mobilemind.veloster.orm;

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

import br.com.mobilemind.veloster.orm.core.AnnotationsManager;
import br.com.mobilemind.veloster.sql.type.Criteria;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.orm.model.ColumnWrapper;
import java.util.List;

/**
 *
 * Construtor de comandos SQL
 *
 * @author Ricardo Bocchi
 */
public interface QueryBuilder<T extends Entity> {

    /**
     * SQL Type insert
     */
    public static final int INSERT = 0;
    /**
     * SQL Type update
     */
    public static final int UPDATE = 1;
    /**
     * SQL Type delete
     */
    public static final int DELETE = 2;
    /**
     * SQL Type select
     */
    public static final int SELECT = 3;
    /**
     * SQL Type DDL
     */
    public static final int DDL = 4;
    /**
     * SQL Type count
     */
    public static final int COUNT = 5;

    void build(Class<T> clazz, AnnotationsManager<T> annotationsManager, QueryFormatter formatter, DDLDialect dialect);

    /**
     * create criteria entity
     *
     * @return
     */
    Criteria<T> createCriteria();

    /**
     * get create table query statement
     *
     * @return
     */
    String getCreateTableQuery();

    /**
     * create add column DDL statement
     *
     * @param field
     * @return
     */
    String getAddColumnQuery(ColumnWrapper field);

    /**
     * get delete query statement
     *
     * @return
     */
    String getDeleteQuery(Criteria<T> criteria);

    /**
     * get drop table query statement
     *
     * @return
     */
    String getDropTableQuery();

    /**
     * get insert query statament
     *
     * @return
     */
    String getInsertQuery();

    /**
     * get query type
     *
     * @return
     */
    int getQueryType();

    /**
     * get count query statement
     *
     * @return
     */
    String getCountQuery(Criteria<T> criteria);

    /**
     * get select query statement
     *
     * @return
     */
    String getSelectQuery(Criteria<T> criteria);

    /**
     * get table name
     *
     * @return
     */
    String getTableName();

    /**
     * get update query statement
     *
     * @return
     */
    String getUpdateQuery(Criteria<T> criteria);

    List<ColumnWrapper> getFieldsForParameters(Criteria<T> criteria);

    List<ColumnWrapper> getFieldForInsert();

    List<ColumnWrapper> getCascadeFields();
    
    List<ColumnWrapper> getHasManyFields();
}
