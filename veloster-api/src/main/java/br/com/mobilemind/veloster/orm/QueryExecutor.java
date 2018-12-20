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

import br.com.mobilemind.veloster.event.DeleteListener;
import br.com.mobilemind.veloster.event.InsertListener;
import br.com.mobilemind.veloster.event.MetadataUpdateListener;
import br.com.mobilemind.veloster.event.UpdateListener;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.sql.type.Criteria;
import java.util.List;

/**
 *
 * @author Ricardo Bocchi
 */
public interface QueryExecutor<T extends Entity> {

    void build(Class<T> clazz, QueryBuilder<T> queryBuilder, QueryExecutor<T> queryExecutor, QueryStatementBuilder<T> queryStatementBuilder, EntityValidator entityValidator);

    /**
     * delete entity and put entity like internal entity
     *
     */
    void delete(T entity);

    /**
     * delete internal entity by criteria
     *
     */
    void deleteByCriteria(Criteria<T> criteria);

    /**
     * update entity and put entity like internal entity
     *
     */
    void update(T entity);
    
    void updateWithoutValidation(T entity);

    /**
     * save entity and put entity like internal entity
     *
     * @return
     */
    T save(T entity);
    
    T saveWithoutValidation(T entity);

    /**
     * load entity by id and put like internal entity
     *
     * @param id
     * @return
     */
    T load(Long id);

    /**
     * reload entity by id and put entity like internal entity
     *
     * @param entity
     * @return
     */
    T load(T entity);

    /**
     * load entity by id and put entity like internal entity
     *
     * @return
     */
    T loadByCriteria(Criteria<T> critaria);

    /**
     * execute select count(*) in data base
     *
     * @return
     */
    int count();

    /**
     * execute select count(*) by criteria in data base
     *
     * @return
     */
    int countByCriteria(Criteria<T> critaria);

    /**
     * list all entities
     *
     * @return
     */
    List<T> list();

    /**
     * list all entities paged
     *
     * @param max max of results
     * @param first first result
     * @return
     */
    List<T> list(int limit, int offset);

    /**
     * list all entities by criteria
     *
     * @return
     */
    List<T> listByCriteria(Criteria<T> critaria);

    /**
     * list all entities paged by criteria
     *
     * @param max max of results
     * @param first first result
     * @return
     */
    List<T> listByCriteria(Criteria<T> critaria, int limit, int offset);

    /**
     * Execute native query
     *
     * @param query
     * @param query parameters
     */
    void executeNativeQuery(String query, Object... params);

    /**
     * Execute native query
     *
     * @param query
     * @param query parameters
     */
    List<Object[]> executeNativeSelect(String query, Object... args);

    /**
     * Execute native query and transform the result at
     * <code>classToTransformer</code>
     *
     * @param query
     * @param classToTransformer class to transform result
     * @param query parameters
     */
    <E> List<E> executeNativeTransformer(String query, Class<E> resultTransformer, Object... params);

    /**
     * Execute native query and transform the result at
     * <code>classToTransformer</code>
     *
     * @param query
     * @param classToTransformer class to transform result
     * @param query parameters
     */
    <E> E executeNativeSingleTransformer(String query, Class<E> resultTransformer, Object... params);

    /**
     * executa DDL create table
     *
     */
    void tableCreate();

    /**
     * execute DDL update table
     *
     */
    void tableUpdate();

    /**
     * executa DDL drop table
     *
     */
    void tableDelete();

    /**
     * verify if table exists
     *
     * @return
     */
    boolean tableExists();
    
    /**
     * verify if table exists
     *
     * @return
     */
    boolean tableExists(String tableName);

    /**
     * name of table
     *
     * @return
     */
    String tableName();

    /**
     * run transactional operation
     *
     */
    void runTrans(TransactionalOperation operation);

    /**
     * add insert listener
     *
     * @param listener
     */
    void addInsertListener(InsertListener listener);

    /**
     * remove insert listener
     *
     * @param listener
     */
    void removeInsertListener(InsertListener listener);

    /**
     * add update listener
     *
     * @param listener
     */
    void addUpdateListener(UpdateListener listener);

    /**
     * remove update listener
     *
     * @param listener
     */
    void removeUpdateListener(UpdateListener listener);

    /**
     * add delete listener
     *
     * @param listener
     */
    void addDeleteListener(DeleteListener listener);

    /**
     * remove delete listener
     *
     * @param listener
     */
    void removeDeleteListener(DeleteListener listener);

    /**
     * add meta data change listener
     *
     * @param listener
     */
    void addMetadataUpdateListener(MetadataUpdateListener listener);

    /**
     * remove meta data change listener
     *
     * @param listener
     */
    void removeMetadataUpdateListener(MetadataUpdateListener listener);
}
