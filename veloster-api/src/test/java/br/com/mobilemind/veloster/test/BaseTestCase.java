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

import br.com.mobilemind.veloster.tools.VelosterConfig;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import br.com.mobilemind.api.utils.log.AppLogger;
import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.tools.VelosterResource;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Ricardo Bocchi
 */
public abstract class BaseTestCase {

    @BeforeClass
    public static void setUpClass() throws Exception {
        MMLogger.setActive(true);
        MMLogger.addLogger(new AppLogger() {
            @Override
            public void log(Level level, Class clazz, String message) {
                java.util.logging.Logger.getLogger(clazz.getName()).log(level, message);
            }

            @Override
            public void log(Level level, Class clazz, String message, Exception e) {
                java.util.logging.Logger.getLogger(clazz.getName()).log(level, message, e);
            }

            @Override
            public void log(Level level, Class clazz, Exception e) {
                java.util.logging.Logger.getLogger(clazz.getName()).log(level, e.getMessage(), e);
            }
        });


//        VelosterResource.setProperty("br.com.mobilemind.db.driver", "com.mysql.jdbc.Driver");        
//        VelosterResource.setProperty("br.com.mobilemind.db.password", "d4t4ch4mp$");
//        VelosterResource.setProperty("br.com.mobilemind.db.user", "root");
//        VelosterResource.setProperty("br.com.mobilemind.db.port", "3306");
//        VelosterResource.setProperty("br.com.mobilemind.db.host", "192.168.1.213");
//        VelosterResource.setProperty("br.com.mobilemind.db.name", "test0"); 
//        VelosterResource.setProperty("br.com.mobilemind.defaultDateFormat", "yyyy-MM-dd hh:ss:mm"); 

        //new VelosterConfig().setDriver(new MySqlDriver()).buildMe();

        VelosterConfig.build();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws SQLException {
        delete();
    }

    @After
    public void tearDown() throws SQLException {
        delete();
    }

    public void say(Object s) {
        System.out.println(s);
    }

    private void delete() {
        String path = null;

        if (VelosterResource.getProperty("br.com.mobilemind.db.pathEnv") != null && !"".equals(VelosterResource.getProperty("br.com.mobilemind.db.pathEnv"))) {
            path = System.getProperty(VelosterResource.getProperty("br.com.mobilemind.db.pathEnv")) + File.separator + VelosterResource.getProperty("br.com.mobilemind.db.path");
        } else {
            path = VelosterResource.getProperty("br.com.mobilemind.db.path");
        }

        File file = new File(path + File.separator + VelosterResource.getProperty("br.com.mobilemind.db.name"));

        if (file.exists()) {
            file.delete();
        }
        
        if(file.exists()){
            throw new RuntimeException("file existes????");
        }

        VelosterRepository.reset();
    }
}
