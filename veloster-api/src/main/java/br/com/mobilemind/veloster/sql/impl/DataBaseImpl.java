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


import br.com.mobilemind.veloster.sql.DataBase;
import java.io.File;

/**
 *
 * @author Ricardo Bocchi
 */
public class DataBaseImpl implements DataBase {

    private String dbName;
    private String dbTestName;
    private String dbPath;
    private String dbHost;
    private String user;
    private String password;
    private int dbPort;
    private String driver;

    @Override
    public DataBase setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    @Override
    public DataBase setDbTestName(String dbTestName) {
        this.dbTestName = dbTestName;
        return this;
    }

    @Override
    public DataBase setDbPath(String dbPath) {
        this.dbPath = dbPath;
        return this;
    }

    @Override
    public DataBase setDbHost(String dbHost) {
        this.dbHost = dbHost;
        return this;
    }

    @Override
    public DataBase setDbPort(int port) {
        this.dbPort = port;
        return this;
    }

    @Override
    public DataBase setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    @Override
    public String getDbName() {
        return dbName;
    }

    @Override
    public String getDbTestName() {
        return this.dbTestName;
    }

    @Override
    public String getDbPath() {
        return dbPath;
    }

    @Override
    public String getDbHost() {
        return dbHost;
    }

    @Override
    public int getDbPort() {
        return dbPort;
    }

    @Override
    public String getDriver() {
        return driver;
    }

    @Override
    public String toString() {
        return this.driver + ":" + this.dbPath + File.separator + this.dbName;
    }

    @Override
    public DataBase setUser(String user) {
        this.user = user;
        return this;
    }

    @Override
    public DataBase setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
