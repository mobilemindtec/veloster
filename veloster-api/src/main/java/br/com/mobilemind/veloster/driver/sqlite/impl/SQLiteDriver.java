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
import br.com.mobilemind.veloster.sql.DataType;
import br.com.mobilemind.veloster.sql.Driver;
import br.com.mobilemind.veloster.sql.TableMetadataResolver;

/**
 *
 * @author Ricardo Bocchi
 */
public class SQLiteDriver implements Driver {

    private DataType dataType = new SQLiteDataType();
    private TableMetadataResolver tableMetadataResolver = new SQLiteTableMetadataResolver();
    private boolean pragmaExecute = false;
    private final String[] pragma = new String[]{"PRAGMA foreign_keys = ON"};
    private final String getRowId = "SELECT last_insert_rowid();";
    private final String autoIncrementoKey = "Autoincrement";
    private final String dataBaseExtension = "sqlite";
    private final String urlPrefix = "jdbc:sqlite";

    public SQLiteDriver() {
        super();
    }

    @Override
    public String getShowTableMetadataStatement(String tableName) {
        return "PRAGMA table_info('" + tableName + "')";
    }

    @Override
    public String[] getPragmas() {
        return pragma;
    }

    @Override
    public String getShowTableStatement(String tableName) {
        return "Select name From sqlite_master Where type='table' And name = '" + tableName + "'";
    }

    @Override
    public String getRowIdQuery(String tableName) {
        return getRowId;
    }

    @Override
    public String getDatabaseExtension() {
        return dataBaseExtension;
    }

    @Override
    public String getUrlPrefix() {
        return urlPrefix;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    @Override
    public TableMetadataResolver getTableMetadataResolver() {
        return this.tableMetadataResolver;
    }

    @Override
    public String getAutoincrementKey() {
        return autoIncrementoKey;
    }

    @Override
    public boolean isPragmaExecute() {
        return this.pragmaExecute;
    }

    @Override
    public void setPragmaExecute(boolean execute) {
        this.pragmaExecute = execute;
    }
}
