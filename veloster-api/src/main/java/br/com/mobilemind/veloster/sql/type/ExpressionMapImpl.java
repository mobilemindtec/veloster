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
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Ricardo Bocchi
 */
public abstract class ExpressionMapImpl<One, Two> implements Expression {

    private Map<One, Two> values;
    private ColumnWrapper field;

    protected ExpressionMapImpl(One valueOne, Two valueTwo) {
        this.values = new HashMap<One, Two>();
        this.setValues(valueOne, valueTwo);
    }

    protected void setValues(One valueOne, Two valueTwo) {
        if (valueOne == null || valueTwo == null) {
            throw new InvalidParameterException("values can't be null");
        }
        this.values.put(valueOne, valueTwo);
    }

    @Override
    public Set<Object> getValues() {
        return (Set<Object>) this.values.keySet();
    }

    public Map<One, Two> getMap() {
        return this.values;
    }

    @Override
    public ColumnWrapper getField() {
        return this.field;
    }

    @Override
    public void setField(ColumnWrapper field) {
        this.field = field;
    }
}
