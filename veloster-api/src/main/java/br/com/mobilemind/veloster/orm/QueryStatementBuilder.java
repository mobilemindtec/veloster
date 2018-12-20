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
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.sql.ResultSet;
import br.com.mobilemind.veloster.sql.Statement;
import br.com.mobilemind.veloster.sql.type.Criteria;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Ricardo Bocchi
 */
public interface QueryStatementBuilder<T extends Entity> {

    void build(Class<T> clazz, QueryBuilder<T> queryBuilder, AnnotationsManager<T> annotationsManager);

    void compileParameters(T entity, Statement stmt, Criteria<T> criteria) throws Exception;

    void compileParameters(Statement stmt, Object[] params) throws Exception;

    List<T> getResult(ResultSet rs) throws Exception;

    <E> List<E> getResultTransformer(ResultSet rs, Class<E> resultTransformer) throws SQLException;

    T getUniqueResult(ResultSet rs) throws Exception;
    
    T getUniqueResult(T entity, ResultSet rs) throws Exception;

    public boolean isGetRowId();
}
