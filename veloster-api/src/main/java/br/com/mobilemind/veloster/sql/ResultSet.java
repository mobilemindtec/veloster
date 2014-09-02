package br.com.mobilemind.veloster.sql;

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


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Ricardo Bocchi
 */
public interface ResultSet {

    boolean next() throws SQLException;

    void close() throws SQLException;

    String getString(int pos) throws SQLException;

    Integer getInteger(int pos) throws SQLException;

    Double getDouble(int pos) throws SQLException;

    Boolean getBoolean(int pos) throws SQLException;

    Long getLong(int pos) throws SQLException;

    Date getDate(int pos) throws SQLException;

    String getString(String columnName) throws SQLException;

    Integer getInteger(String columnName) throws SQLException;

    Double getDouble(String columnName) throws SQLException;

    Boolean getBoolean(String columnName) throws SQLException;

    Long getLong(String columnName) throws SQLException;

    Date getDate(String columnName) throws SQLException;

    Object getObject(int pos) throws SQLException;

    Object getObject(String columnName) throws SQLException;

    void setDataFormat(SimpleDateFormat format);

    int getColumnCount() throws SQLException;
    
    String[] getColumnNames() throws SQLException;
}
