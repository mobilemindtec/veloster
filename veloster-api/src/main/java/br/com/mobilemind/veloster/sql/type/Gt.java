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


/**
 *
 * @author Ricardo Bocchi
 */
public class Gt extends ExpressionImpl {

    public Gt() {
        this(null);
    }

    public Gt(Object value) {
        super(value);
    }

    public Gt gt(Object value) {
        super.setValue(value);
        return this;
    }
    
    @Override
    public String getKeyWork() {
        return ">";
    }    
}