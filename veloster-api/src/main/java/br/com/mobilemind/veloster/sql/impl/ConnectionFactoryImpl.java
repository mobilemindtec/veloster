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


import br.com.mobilemind.api.utils.MobileMindUtil;
import br.com.mobilemind.veloster.sql.Connection;
import br.com.mobilemind.veloster.sql.ConnectionFactory;
import br.com.mobilemind.veloster.sql.DataBase;
import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.exceptions.VelosterException;
import br.com.mobilemind.veloster.sql.Driver;
import br.com.mobilemind.veloster.tools.VelosterConfig;
import br.com.mobilemind.veloster.tools.VelosterResource;
import java.text.SimpleDateFormat;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author Ricardo Boicchi
 */
public class ConnectionFactoryImpl implements ConnectionFactory {

    private static DataBase dataBase;
    private SimpleDateFormat format;
    private static final String DRIVER = "br.com.mobilemind.db.driver";
    private static final String DB_NAME = "br.com.mobilemind.db.name";
    private static final String DB_TEST_NAME = "br.com.mobilemind.db.testName";
    private static final String DB_PATH = "br.com.mobilemind.db.path";
    private static final String DB_HOST = "br.com.mobilemind.db.host";
    private static final String DB_PORT = "br.com.mobilemind.db.port";
    private static final String DB_PATH_ENV = "br.com.mobilemind.db.pathEnv";
    private static final String DB_IS_ANDROID = "br.com.mobilemind.db.android";
    private static final String USER = "br.com.mobilemind.db.user";
    private static final String PASSWORD = "br.com.mobilemind.db.password";
    private static Map<DataBase, Connection> conections;

    static {
        ConnectionFactoryImpl.conections = new HashMap<DataBase, Connection>();
    }

    public ConnectionFactoryImpl (){

    }

    public ConnectionFactoryImpl (SimpleDateFormat format){
        this.format = format;
    }

    @Override
    public void setDataBase(DataBase dataBase) {
        ConnectionFactoryImpl.dataBase = dataBase;
    }

    @Override
    public DataBase getDataBase() {
        return ConnectionFactoryImpl.dataBase;
    }

    @Override
    public Connection getConnection() {
        if (!ConnectionFactoryImpl.conections.containsKey(ConnectionFactoryImpl.dataBase)) {
            create();
            loadDriver();
            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(), "create connection");
            }
            
            ConnectionImpl conn = new ConnectionImpl(getUrlConnection(), ConnectionFactoryImpl.dataBase.getUser(),
                    ConnectionFactoryImpl.dataBase.getPassword(), this.format);
            
            ConnectionFactoryImpl.conections.put(ConnectionFactoryImpl.dataBase, conn);
        }
        return ConnectionFactoryImpl.conections.get(ConnectionFactoryImpl.dataBase);
    }

    protected void create() {

        if (ConnectionFactoryImpl.dataBase == null) {
            ConnectionFactoryImpl.dataBase = new DataBaseImpl();
            ConnectionFactoryImpl.dataBase.setDriver(VelosterResource.getProperty(DRIVER));
            ConnectionFactoryImpl.dataBase.setDbHost(VelosterResource.getProperty(DB_HOST));
            ConnectionFactoryImpl.dataBase.setUser(VelosterResource.getProperty(USER));
            ConnectionFactoryImpl.dataBase.setPassword(VelosterResource.getProperty(PASSWORD));

            String path = VelosterResource.getProperty(DB_PATH);
            String dbPathEnv =VelosterResource.getProperty(DB_PATH_ENV);
            
            if (!MobileMindUtil.isNullOrEmpty(dbPathEnv)) {
                path = System.getProperty(dbPathEnv) + File.separator + path;
                File file = new File(path);
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        throw new RuntimeException("error create db path [" + path + "].");
                    }
                    if (MMLogger.isLogable()) {
                        MMLogger.log(Level.INFO, this.getClass(), "db path not already but was create");
                    }
                } else {
                    if (MMLogger.isLogable()) {
                        MMLogger.log(Level.INFO, this.getClass(), "db path already");
                    }
                }

            }

            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(), "db path: " + path);
            }

            ConnectionFactoryImpl.dataBase.setDbPath(path);

            try {
                ConnectionFactoryImpl.dataBase.setDbPort(Integer.parseInt(VelosterResource.getProperty(DB_PORT)));
            } catch (NumberFormatException e) {
                MMLogger.log(Level.WARNING, getClass(), "db port not found");
            }
            ConnectionFactoryImpl.dataBase.setDbName(VelosterResource.getProperty(DB_NAME));
            ConnectionFactoryImpl.dataBase.setDbTestName(VelosterResource.getProperty(DB_TEST_NAME));
        }
    }

    protected String getUrlConnection() {
        String url = VelosterConfig.getConf().getDriver().getUrlPrefix();


        if (!MobileMindUtil.isNullOrEmpty(ConnectionFactoryImpl.dataBase.getDbHost())) {
            url += "://" + ConnectionFactoryImpl.dataBase.getDbHost();

            if (ConnectionFactoryImpl.dataBase.getDbPort() > 0) {
                url += ":" + ConnectionFactoryImpl.dataBase.getDbPort();
            }
        } else {
            url += ":";
        }


        if (!MobileMindUtil.isNullOrEmpty(this.getDataBase().getDbPath())) {
            url +=  this.getDataBase().getDbPath();
        }

        if (VelosterConfig.getConf().isTestMode()) {
            url += "/" + this.getDataBase().getDbTestName();
            MMLogger.log(Level.INFO, getClass(), "##############################");
            MMLogger.log(Level.INFO, getClass(), "Application in test mode");
            MMLogger.log(Level.INFO, getClass(), "##############################");
        } else {
            url += "/" + this.getDataBase().getDbName();
        }

        if (MMLogger.isLogable()) {
            MMLogger.log(Level.INFO, this.getClass(), "created url: " + url);
        }
        return url;
    }

    public static String getDbPath() {
        if (ConnectionFactoryImpl.dataBase != null) {
            return ConnectionFactoryImpl.dataBase.getDbPath();
        }

        return null;
    }

    protected void loadDriver() {
        try {
            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(), "load driver " + this.getDataBase().getDriver() + "...");
            }
            if (this.getDataBase().getDriver() != null && !"".equals(this.getDataBase().getDriver())) {
                Class.forName(this.getDataBase().getDriver());
                if (MMLogger.isLogable()) {
                    MMLogger.log(Level.INFO, this.getClass(), "driver " + this.getDataBase().getDriver() + " loaded...");
                }
            } else {
                if (MMLogger.isLogable()) {
                    MMLogger.log(Level.INFO, this.getClass(), "driver not found...");
                }
                throw new VelosterException("data base driver undefined");
            }
        } catch (ClassNotFoundException ex) {
            MMLogger.log(Level.SEVERE, getClass(), ex);
            throw new VelosterException("error load data base driver [" + this.getDataBase().getDriver() + "]");
        }
    }

    public void setDataFormat(SimpleDateFormat format) {
        this.format = format;
    }     
}
