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
public interface Statement{

    static final Object SYNC = new Object();
    
    Statement setQuery(String query);

    boolean executeNonQuery() throws SQLException;

    void executeUpdate() throws SQLException;
    
    int executeInsert(boolean getRowId) throws SQLException;

    ResultSet executeQuery() throws SQLException;

    Statement setInteger(int pos, Integer value) throws SQLException;

    Statement setString(int pos, String value) throws SQLException;

    Statement setDouble(int pos, Double value) throws SQLException;

    Statement setLong(int pos, Long value) throws SQLException;

    Statement setBoolean(int pos, Boolean value) throws SQLException;

    Statement setDate(int pos, Date date) throws SQLException;
 
    String getQuery();
    
    void setDataFormat(SimpleDateFormat format);
}
