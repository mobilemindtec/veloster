package br.com.mobilemind.veloster.driver.sqlite.impl;

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

import br.com.mobilemind.api.utils.ClassUtil;
import br.com.mobilemind.veloster.exceptions.VelosterException;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.sql.DataType;

/**
 *
 * @see {@link http://www.sqlite.org/datatype3.html}
 *
 * @author Ricardo Bocchi
 */
public class SQLiteDataType implements DataType {

    public SQLiteDataType() {
        super();
    }

    @Override
    public String getTypeToInteger() {
        return "Integer";
    }

    @Override
    public String getTypeToDouble() {
        return "Double";
    }

    @Override
    public String getTypeToString() {
        return "Varchar({0})";
    }

    @Override
    public String getTypeToBoolean() {
        return "Boolean";
    }

    @Override
    public String getTypeToDate() {
        return "Date";
    }

    @Override
    public String getTypeToTime() {
        return "Date";
    }

    @Override
    public String getTypeToDateTime() {
        return "Date";
    }

    @Override
    public String getTypeForClass(Class clazz) {

        if (ClassUtil.isString(clazz)) {
            return getTypeToString();
        }
        if (ClassUtil.isInteger(clazz)) {
            return getTypeToInteger();
        }
        if (ClassUtil.isDouble(clazz)) {
            return getTypeToDouble();
        }
        if (ClassUtil.isBoolean(clazz)) {
            return getTypeToBoolean();
        }
        if (ClassUtil.isLong(clazz)) {
            return getTypeToInteger();
        }
        if (ClassUtil.isDate(clazz)) {
            return getTypeToDate();
        }
        if (ClassUtil.isAssignableFrom(clazz, Entity.class)) {
            return getTypeToInteger();
        }

        throw new VelosterException("type " + clazz.getName() + " is invalid type");
    }
}
