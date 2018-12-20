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

import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.sql.Connection;
import br.com.mobilemind.veloster.sql.ResultSet;
import br.com.mobilemind.veloster.sql.Statement;
import br.com.mobilemind.veloster.sql.TableMetadataResolver;
import br.com.mobilemind.veloster.sql.impl.Column;
import br.com.mobilemind.veloster.sql.impl.Table;
import br.com.mobilemind.veloster.tools.VelosterConfig;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author Ricardo Bocchi
 */
public class MySqlTableMetadataResolver implements TableMetadataResolver {

    @Override
    public Table getTableMetadata(String tableName, Connection connection) throws SQLException {
        ResultSet rs = null;
        Statement stmt;
        String query = VelosterConfig.getConf().getDriver().getShowTableMetadataStatement(tableName);
        Table table = new Table();
        Column column = null;

        if (MMLogger.isLogable()) {
            MMLogger.log(Level.INFO, this.getClass(), "calling table metadata to table " + tableName);
            MMLogger.log(Level.INFO, this.getClass(), "query " + query);
        }

        stmt = connection.prepare(query, false);
        rs = stmt.executeQuery();
        table.setName(tableName);
        int pos = 0;
        while (rs.next()) {
            column = new Column();
            column.setPosition(pos++);
            column.setName(rs.getString(1));
            column.setType(rs.getString(2));
            column.setNullable("YES".equals(rs.getString(3)));
            column.setPrimaryKey("PRI".equals(rs.getString(4)));
            column.setDefaultValue(rs.getString(5));
            table.getColumns().add(column);

            MMLogger.log(Level.INFO, getClass(), "column to table [" + tableName + "]: " + column);
        }

        return table;
    }
}
