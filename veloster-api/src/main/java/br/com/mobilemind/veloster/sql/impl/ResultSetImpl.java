package br.com.mobilemind.veloster.sql.impl;

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


import br.com.mobilemind.veloster.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Ricardo Bocchi
 */
public class ResultSetImpl implements ResultSet {

    private SimpleDateFormat format;
    private java.sql.ResultSet rs;

    public ResultSetImpl(java.sql.ResultSet rs) {
        this.rs = rs;
    }

    @Override
    public boolean next() throws SQLException {
        return rs.next();
    }

    @Override
    public void close() throws SQLException {
        rs.close();
    }

    @Override
    public String getString(int pos) throws SQLException {
        return rs.getString(pos);
    }

    @Override
    public Integer getInteger(int pos) throws SQLException {
        return rs.getInt(pos);
    }

    @Override
    public Double getDouble(int pos) throws SQLException {
        return rs.getDouble(pos);
    }

    @Override
    public Boolean getBoolean(int pos) throws SQLException {
        return rs.getBoolean(pos);
    }

    @Override
    public Long getLong(int pos) throws SQLException {
        return rs.getLong(pos);
    }

    @Override
    public Date getDate(int pos) throws SQLException {
        try {
            return format.parse(rs.getString(pos));
        } catch (ParseException ex) {
            throw new SQLException("can't parse date " + rs.getString(pos));
        }
    }

    @Override
    public String getString(String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public Integer getInteger(String columnName) throws SQLException {
        return rs.getInt(columnName);
    }

    @Override
    public Double getDouble(String columnName) throws SQLException {
        return rs.getDouble(columnName);
    }

    @Override
    public Boolean getBoolean(String columnName) throws SQLException {
        return rs.getBoolean(columnName);
    }

    @Override
    public Long getLong(String columnName) throws SQLException {
        return rs.getLong(columnName);
    }

    @Override
    public Date getDate(String columnName) throws SQLException {
        try {
            return format.parse(rs.getString(columnName));
        } catch (ParseException ex) {
            throw new SQLException("can't parse date " + rs.getString(columnName));
        }
    }

    @Override
    public Object getObject(int pos) throws SQLException {
        return rs.getString(pos);
    }

    @Override
    public Object getObject(String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public void setDataFormat(SimpleDateFormat format) {
        this.format = format;
    }

    @Override
    public int getColumnCount() throws SQLException {
        return rs.getMetaData().getColumnCount();
    }

    @Override
    public String[] getColumnNames() throws SQLException {
        int columnCount = rs.getMetaData().getColumnCount();
        String columns[] = new String[columnCount];

        for (int i = 0; i < columnCount; i++) {
            columns[i] = rs.getMetaData().getColumnName(i);
        }

        return columns;
    }
}
