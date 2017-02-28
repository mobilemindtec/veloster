package br.com.mobilemind.veloster.sql;

/*
 * #%L
 * Veloster Framework
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

import android.database.Cursor;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Ricardo Bocchi
 */
public class ResultSetImplDroid implements ResultSet {

    private Cursor cursor;
    private SimpleDateFormat format;
    private boolean begin;

    public ResultSetImplDroid(Cursor cursor) {
        this.cursor = cursor;
        this.begin = true;
    }

    @Override
    public boolean next() throws SQLException {
        if (this.begin) {
            this.begin = false;
            return this.cursor.moveToFirst();
        }
        return this.cursor.moveToNext();
    }

    @Override
    public void close() throws SQLException {
        this.cursor.close();
    }

    @Override
    public String getString(int i) throws SQLException {
        return this.cursor.getString(i - 1);
    }

    @Override
    public Integer getInteger(int i) throws SQLException {
        return this.cursor.getInt(i - 1);
    }

    @Override
    public Double getDouble(int i) throws SQLException {
        return this.cursor.getDouble(i - 1);
    }

    @Override
    public Boolean getBoolean(int i) throws SQLException {
        return this.cursor.getInt(i - 1) == 1;
    }

    @Override
    public Long getLong(int i) throws SQLException {
        return this.cursor.getLong(i - 1);
    }

    @Override
    public Date getDate(int i) throws SQLException {
        try {
            
            String dateSrt = this.cursor.getString(i - 1);
            
            if(dateSrt == null || dateSrt.trim().length() == 0)
                return null;

            return format.parse(dateSrt);
        } catch (ParseException ex) {
            throw new SQLException("can't format date " + this.cursor.getString(i - 1));
        }
    }

    @Override
    public String getString(String string) throws SQLException {
        return this.getString(this.cursor.getColumnIndex(string) + 1);
    }

    @Override
    public Integer getInteger(String string) throws SQLException {
        return this.getInteger(this.cursor.getColumnIndex(string) + 1);
    }

    @Override
    public Double getDouble(String string) throws SQLException {
        return this.getDouble(this.cursor.getColumnIndex(string) + 1);
    }

    @Override
    public Boolean getBoolean(String string) throws SQLException {
        return this.getBoolean(this.cursor.getColumnIndex(string) + 1);
    }

    @Override
    public Long getLong(String string) throws SQLException {
        return this.getLong(this.cursor.getColumnIndex(string) + 1);
    }

    @Override
    public Date getDate(String string) throws SQLException {
        return this.getDate(this.cursor.getColumnIndex(string) + 1);
    }

    @Override
    public void setDataFormat(SimpleDateFormat sdf) {
        this.format = sdf;
    }

    @Override
    public Object getObject(int pos) throws SQLException {
        return this.getString(pos);
    }

    @Override
    public Object getObject(String columnName) throws SQLException {
        return this.getString(columnName);
    }

    @Override
    public int getColumnCount() throws SQLException {
        return this.cursor.getColumnCount();
    }

    @Override
    public String[] getColumnNames() throws SQLException {
        return this.cursor.getColumnNames();
    }
}
