package br.com.mobilemind.veloster.test;

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

import br.com.mobilemind.veloster.sql.Connection;
import br.com.mobilemind.veloster.sql.DriverManager;
import br.com.mobilemind.veloster.sql.impl.ConnectionFactoryImpl;
import java.io.IOException;
import java.sql.SQLException;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Ricardo Biocchi
 */
public class ConnectionTestCase extends BaseTestCase {

    @Test
    public void testConnection() throws SQLException, IOException {
        ConnectionFactoryImpl factory = new ConnectionFactoryImpl();
        DriverManager.setConnectionFactory(factory);
        Connection connection = DriverManager.getConnection();

        connection.open();

        Assert.assertTrue(connection.isActive());
        
        connection.close();
        
    }

   
}
