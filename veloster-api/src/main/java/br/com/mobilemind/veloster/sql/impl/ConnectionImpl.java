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


import br.com.mobilemind.veloster.exceptions.VelosterException;
import br.com.mobilemind.veloster.sql.Connection;
import br.com.mobilemind.veloster.sql.Statement;
import br.com.mobilemind.api.utils.log.MMLogger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author Ricardo Bocchi
 */
public class ConnectionImpl implements Connection {

    private java.sql.Connection connection;
    private String urlConnection;
    private List<String> pragmas;
    private boolean trans;
    private boolean closed;
    private int pool;
    private String user;
    private String password;

    public ConnectionImpl(String urlConnection, String user, String password) {
        this.urlConnection = urlConnection;
        this.pragmas = new ArrayList<String>();
        this.user = user;
        this.password = password;
    }

    public java.sql.Connection getConnection() {
        return this.connection;
    }

    @Override
    public void commit() throws SQLException {
        if (!isActive()) {
            throw new SQLException("transactions not's active");
        }

        if (!trans) {
            throw new VelosterException("commig can't be used in non transactional operations. use setCommitTrans to true");
        }

        if (pool == 0) {
            throw new VelosterException("connection pool can't be zero on commit. you called begin in start operation?");
        }

        pool--;

        MMLogger.log(Level.INFO, getClass(), "commit say: connection pool is [" + pool + "]");

        if (pool == 0) {
            this.connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (!isActive()) {
            throw new SQLException("transactions not's active");
        }
        if (trans) {
            pool = 0;
            MMLogger.log(Level.INFO, getClass(), "rollback say: connection pool is [" + pool + "]");
        }

        this.connection.rollback();
    }

    @Override
    public void open() throws SQLException {
        if (!isActive()) {
            if (this.user != null && this.password != null) {
                this.connection = java.sql.DriverManager.getConnection(urlConnection, user, password);
            } else {
                this.connection = java.sql.DriverManager.getConnection(urlConnection);
            }
            this.closed = false;
            this.connection.setAutoCommit(true);
        }
    }

    @Override
    public void close() throws SQLException {
        if (isActive()) {
            if (trans) {
                if (pool == 0) {
                    this.connection.close();
                }
            } else {
                this.connection.close();
            }
        }
        MMLogger.log(Level.INFO, getClass(), "close say: connection pool is [" + pool + "]");
        this.closed = true;

        if (trans && pool == 0) {
            trans = false;
        }
    }

    @Override
    public void begin() throws SQLException {
        if (!isActive()) {
            throw new SQLException("transactions not's active");
        }
        if (trans) {
            pool++;
        }
        MMLogger.log(Level.INFO, getClass(), "begin say: connection pool is [" + pool + "]");
    }

    @Override
    public boolean isActive() throws SQLException {
        return this.connection != null && !this.connection.isClosed();
    }

    @Override
    public void addAfterConnection(String pragmaOrQuery) {
        this.pragmas.add(pragmaOrQuery);
    }

    @Override
    public void setCommitTrans(boolean trans) {
        this.trans = trans;
        try {
            if (isActive()) {
                this.connection.setAutoCommit(trans ? false : true);
            }
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, getClass(), e);
        }
    }

    @Override
    public Statement prepare(String query, boolean isInsert) throws SQLException {
        if (!isActive()) {
            throw new SQLException("transactions not's active");
        }
        if (isInsert) {
            return new StatementImpl(this.connection.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS), query, true);
        } else {
            return new StatementImpl(this.connection.prepareStatement(query), query, false);
        }
    }

    @Override
    public boolean isClosed() {
        return closed;
    }
}
