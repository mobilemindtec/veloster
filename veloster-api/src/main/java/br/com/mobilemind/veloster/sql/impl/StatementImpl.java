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
import br.com.mobilemind.veloster.sql.Statement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Ricardo Bocchi
 */
public class StatementImpl implements Statement {

    private java.sql.PreparedStatement stmt;
    private String query;
    private SimpleDateFormat format;
    private boolean generateKey;

    protected StatementImpl(String query) {
        this(null, query, false);
    }

    public StatementImpl(java.sql.PreparedStatement stmt, String query, boolean generateKey) {
        this.stmt = stmt;
        this.setQuery(query);
        this.generateKey = generateKey;
    }

    @Override
    public boolean executeNonQuery() throws SQLException {
        return this.stmt.execute();
    }

    @Override
    public void executeUpdate() throws SQLException {
        this.stmt.executeUpdate();
    }

    @Override
    public int executeInsert(boolean getRowId) throws SQLException {
        int val = this.stmt.executeUpdate();

        if (generateKey && getRowId) {
            java.sql.ResultSet rs = stmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                return rs.getInt(1);
            }
        }

        return val;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return (ResultSet) new ResultSetImpl(this.stmt.executeQuery());
    }

    @Override
    public Statement setInteger(int pos, Integer value) throws SQLException {
        this.stmt.setInt(pos, value);
        return this;
    }

    @Override
    public Statement setString(int pos, String value) throws SQLException {
        this.stmt.setString(pos, value);
        return this;
    }

    @Override
    public Statement setDouble(int pos, Double value) throws SQLException {
        this.stmt.setDouble(pos, value);
        return this;

    }

    @Override
    public Statement setLong(int pos, Long value) throws SQLException {
        this.stmt.setLong(pos, value);
        return this;
    }

    @Override
    public Statement setBoolean(int pos, Boolean value) throws SQLException {
        this.stmt.setBoolean(pos, value);
        return this;
    }

    @Override
    public Statement setDate(int pos, Date date) throws SQLException {
        String value = null;
        if (date != null) {
            value = format.format(date);
        }
        this.stmt.setString(pos, value);
        return this;
    }

    @Override
    public String getQuery() {
        return this.query;
    }

    @Override
    public void setDataFormat(SimpleDateFormat format) {
        this.format = format;
    }

    @Override
    public Statement setQuery(String query) {
        this.query = query;
        return this;
    }
}
