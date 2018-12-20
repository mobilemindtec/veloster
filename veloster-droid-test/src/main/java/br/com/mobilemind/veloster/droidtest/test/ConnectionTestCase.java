package br.com.mobilemind.veloster.droidtest.test;

import br.com.mobilemind.api.droidunit.Assert;
import br.com.mobilemind.veloster.sql.Connection;
import br.com.mobilemind.veloster.sql.ConnectionFactory;
import br.com.mobilemind.veloster.sql.ConnectionFactoryImplDroid;
import br.com.mobilemind.veloster.sql.DriverManager;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Ricardo Biocchi
 */
public class ConnectionTestCase extends BaseTestCase {

    public void testConnection() throws SQLException, IOException {
        ConnectionFactory factory = new ConnectionFactoryImplDroid(getContext());
        DriverManager.setConnectionFactory(factory);
        Connection connection = DriverManager.getConnection();

        connection.open();

        Assert.isTrue(connection.isActive());
    }
}
