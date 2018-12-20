package br.com.mobilemind.veloster.tools;

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

import br.com.mobilemind.veloster.orm.QueryExecutor;
import br.com.mobilemind.veloster.orm.QueryFormatter;
import br.com.mobilemind.veloster.orm.QueryStatementBuilder;
import br.com.mobilemind.veloster.orm.EntityValidator;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.orm.QueryBuilder;
import br.com.mobilemind.veloster.orm.core.AnnotationsManager;
import br.com.mobilemind.veloster.orm.model.Entity;

/**
 *
 * @author Ricardo Bocchi
 */
public class VelosterBox<T extends Entity> {

    private QueryBuilder<T> queryBuilder;
    private QueryStatementBuilder<T> queryStatementBuilder;
    private QueryExecutor<T> queryExecutor;
    private Veloster<T> manager;
    private AnnotationsManager<T> annotationsManager;
    private QueryFormatter formatter;
    private EntityValidator<T> entityValidator;

    public Veloster<T> getManager() {
        return manager;
    }

    public void setManager(Veloster<T> manager) {
        this.manager = manager;
    }

    public QueryBuilder<T> getQueryBuilder() {
        return queryBuilder;
    }

    public void setQueryBuilder(QueryBuilder<T> queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    public QueryExecutor<T> getQueryExecutor() {
        return queryExecutor;
    }

    public void setQueryExecutor(QueryExecutor<T> queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    public QueryStatementBuilder<T> getQueryStatementBuilder() {
        return queryStatementBuilder;
    }

    public void setQueryStatementBuilder(QueryStatementBuilder<T> queryStatementBuilder) {
        this.queryStatementBuilder = queryStatementBuilder;
    }

    public AnnotationsManager<T> getAnnotationsManager() {
        return annotationsManager;
    }

    public void setAnnotationsManager(AnnotationsManager<T> annotationsManager) {
        this.annotationsManager = annotationsManager;
    }

    public void setFormatter(QueryFormatter formatter) {
        this.formatter = formatter;
    }

    public QueryFormatter getFormatter() {
        return formatter;
    }

    public EntityValidator<T> getEntityValidator() {
        return entityValidator;
    }

    public void setEntityValidator(EntityValidator<T> entityValidator) {
        this.entityValidator = entityValidator;
    }
}
