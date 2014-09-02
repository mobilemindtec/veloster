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

import br.com.mobilemind.veloster.orm.model.ColumnWrapper;
import java.util.Set;

/**
 *
 * @author Ricardo Bocchi
 */
public class OrderBy implements Expression {

    private Object orderBy;
    private ColumnWrapper field;
    private boolean desc;

    public OrderBy() {
    }

    public OrderBy(Object orderBy) {
        this.orderBy = orderBy;
    }

    public OrderBy(Object orderBy, boolean desc) {
        this.orderBy = orderBy;
        this.desc = desc;
    }

    @Override
    public Set<Object> getValues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ColumnWrapper getField() {
        return this.field;
    }

    @Override
    public void setField(ColumnWrapper field) {
        this.field = field;
    }

    public Object getOrderBy() {
        return orderBy;
    }

    public boolean isDesc() {
        return desc;
    }

    @Override
    public String getKeyWork() {
        return "Order By";
    }
}
