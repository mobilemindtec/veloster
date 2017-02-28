package br.com.mobilemind.veloster.tools;

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

import br.com.mobilemind.veloster.orm.core.VelosterImpl;
import br.com.mobilemind.veloster.orm.core.QueryBuilderImpl;
import br.com.mobilemind.veloster.orm.core.QueryExecutorImpl;
import br.com.mobilemind.veloster.orm.core.QueryStatementBuilderImpl;
import br.com.mobilemind.veloster.orm.core.EntityValidatorImpl;
import br.com.mobilemind.veloster.orm.QueryExecutor;
import br.com.mobilemind.veloster.orm.DDLDialect;
import br.com.mobilemind.veloster.orm.QueryFormatter;
import br.com.mobilemind.veloster.orm.QueryStatementBuilder;
import br.com.mobilemind.veloster.orm.EntityValidator;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.orm.QueryBuilder;
import br.com.mobilemind.veloster.exceptions.VelosterException;
import br.com.mobilemind.veloster.extra.DatabaseBackupHelper;
import br.com.mobilemind.veloster.sql.ConnectionFactory;
import br.com.mobilemind.veloster.sql.DriverManager;
import br.com.mobilemind.veloster.sql.impl.ConnectionFactoryImpl;
import br.com.mobilemind.veloster.sql.impl.DDLDialectImpl;
import br.com.mobilemind.veloster.sql.impl.QueryFormatterImpl;
import br.com.mobilemind.api.utils.MobileMindUtil;
import br.com.mobilemind.veloster.driver.sqlite.impl.SQLiteDriver;
import br.com.mobilemind.veloster.sql.Driver;
import java.text.SimpleDateFormat;

/**
 *
 * @author Ricardo Bocchi
 */
public class VelosterConfig {

    private static VelosterConfig CONFIGURATION;
    private DDLDialect dialect;
    private Class formatter;
    private Class queryBuilder;
    private Class queryStatementBuilder;
    private Class queryExecutor;
    private Class manager;
    private Class entityValidator;
    private Driver driver;
    private SimpleDateFormat dateFormat;
    private DatabaseBackupHelper databaseBackupHelper;
    private ConnectionFactory connectionFactory;
    private boolean testMode;

    public VelosterConfig() {
    }

    public VelosterConfig setDialect(DDLDialect dialect) {
        this.dialect = dialect;
        return this;
    }

    public DDLDialect getDialect() {
        return dialect;
    }

    public <T extends QueryFormatter> VelosterConfig setFormatter(Class<T> formatter) {
        this.formatter = formatter;
        return this;
    }

    public <T extends QueryFormatter> Class<T> getFormatter() {
        return formatter;
    }

    public VelosterConfig setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public VelosterConfig setDriver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public Driver getDriver() {
        return driver;
    }

    public <T extends EntityValidator> Class<T> getEntityValidator() {
        return entityValidator;
    }

    public <T extends EntityValidator> VelosterConfig setEntityValidator(Class<T> entityValidator) {
        this.entityValidator = entityValidator;
        return this;
    }

    public <T extends Veloster> Class<T> getManager() {
        return manager;
    }

    public <T extends Veloster> VelosterConfig setManager(Class<T> manager) {
        this.manager = manager;
        return this;
    }

    public <T extends QueryBuilder> Class<T> getQueryBuilder() {
        return queryBuilder;
    }

    public <T extends QueryBuilder> VelosterConfig setQueryBuilder(Class<T> queryBuilder) {
        this.queryBuilder = queryBuilder;
        return this;
    }

    public <T extends QueryExecutor> Class<T> getQueryExecutor() {
        return queryExecutor;
    }

    public <T extends QueryExecutor> VelosterConfig setQueryExecutor(Class<T> queryExecutor) {
        this.queryExecutor = queryExecutor;
        return this;
    }

    public <T extends QueryStatementBuilder> Class<T> getQueryStatementBuilder() {
        return queryStatementBuilder;
    }

    public <T extends QueryStatementBuilder> VelosterConfig setQueryStatementBuilder(Class<T> queryStatementBuilder) {
        this.queryStatementBuilder = queryStatementBuilder;
        return this;
    }

    public VelosterConfig setDatabaseBackupHelper(DatabaseBackupHelper databaseBackupHelper) {
        this.databaseBackupHelper = databaseBackupHelper;
        return this;
    }

    public DatabaseBackupHelper getDatabaseBackupHelper() {
        return databaseBackupHelper;
    }

    public VelosterConfig setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        return this;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public String getProperty(String key) {
        return VelosterResource.getProperty(key);
    }

    public void setPropertye(String key, String value) {
        VelosterResource.setProperty(key, value);
    }

    public static VelosterConfig build() {
        if (CONFIGURATION == null) {
            CONFIGURATION = new VelosterConfig();
        }

        loadDefaultValues();

        return CONFIGURATION;
    }

    public VelosterConfig buildMe() {
        CONFIGURATION = this;
        build();
        return this;
    }

    public static VelosterConfig getConf() {

        if (CONFIGURATION == null) {
            throw new VelosterException("configuration can't be null. you called QueryToolsConf.build()?");
        }

        return CONFIGURATION;
    }

    public static VelosterConfig setConf(VelosterConfig conf) {
        CONFIGURATION = conf;
        return CONFIGURATION;
    }

    private static void loadDefaultValues() {

        if (CONFIGURATION.driver == null) {
            CONFIGURATION.driver = new SQLiteDriver();
            CONFIGURATION.driver.setPragmaExecute(true);
        }


        if (CONFIGURATION.dateFormat == null) {
            String formato = VelosterResource.getProperty("br.com.mobilemind.defaultDateFormat");

            if (MobileMindUtil.isNullOrEmpty(formato)) {
                formato = "yyyy-MM-dd hh:mm:ss";
            }
            CONFIGURATION.dateFormat = new SimpleDateFormat(formato);
        }

        if (CONFIGURATION.connectionFactory != null) {
            CONFIGURATION.connectionFactory.setDataFormat(CONFIGURATION.dateFormat);
            DriverManager.setConnectionFactory(CONFIGURATION.connectionFactory);
        } else {
            DriverManager.setConnectionFactory(new ConnectionFactoryImpl(CONFIGURATION.dateFormat));
        }

        if (CONFIGURATION.dialect == null) {
            CONFIGURATION.dialect = new DDLDialectImpl();
        }

        if (CONFIGURATION.formatter == null) {
            CONFIGURATION.formatter = QueryFormatterImpl.class;
        }

        if (CONFIGURATION.entityValidator == null) {
            CONFIGURATION.entityValidator = EntityValidatorImpl.class;
        }

        if (CONFIGURATION.manager == null) {
            CONFIGURATION.manager = VelosterImpl.class;
        }

        if (CONFIGURATION.queryBuilder == null) {
            CONFIGURATION.queryBuilder = QueryBuilderImpl.class;
        }

        if (CONFIGURATION.queryExecutor == null) {
            CONFIGURATION.queryExecutor = QueryExecutorImpl.class;
        }

        if (CONFIGURATION.queryStatementBuilder == null) {
            CONFIGURATION.queryStatementBuilder = QueryStatementBuilderImpl.class;
        }
    }
}
