package br.com.mobilemind.veloster.driver.mysql.impl;

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
public class MySqlDriver implements Driver {

    private DataType dataType = new MySqlDataType();
    private TableMetadataResolver tableMetadataResolver = new MySqlTableMetadataResolver();

    @Override
    public String getShowTableMetadataStatement(String tableName) {
        return "DESCRIBE " + tableName;
    }

    @Override
    public String[] getPragmas() {
        return new String[]{};
    }

    @Override
    public String getShowTableStatement(String tableName) {
        return "SELECT distinct(table_name) FROM information_schema.tables WHERE table_type = 'BASE TABLE' and table_name = '" + tableName + "'";
    }

    @Override
    public String getRowIdQuery(String tableName) {
        return "";
    }

    @Override
    public String getDatabaseExtension() {
        return "";
    }

    @Override
    public String getUrlPrefix() {
        return "jdbc:mysql";
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
        return "AUTO_INCREMENT";
    }

    @Override
    public boolean isPragmaExecute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setPragmaExecute(boolean execute) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
