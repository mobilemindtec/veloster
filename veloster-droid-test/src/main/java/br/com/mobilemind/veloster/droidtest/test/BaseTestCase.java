package br.com.mobilemind.veloster.droidtest.test;

import br.com.mobilemind.api.droidunit.TestBehaviorImpl;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.sql.DriverManager;
import br.com.mobilemind.veloster.tools.VelosterConfig;
import java.sql.SQLException;

/**
 *
 * @author Ricardo Bocchi
 */
public abstract class BaseTestCase extends TestBehaviorImpl {

    @Override
    public void setUpClass() {
        MMLogger.setActive(true);
        MMLogger.addLogger(br.com.mobilemind.api.droidutil.logs.AppLogger.getInstance());
    }

    @Override
    public void setUp() throws SQLException {
        VelosterConfig.getConf().getDatabaseBackupHelper().deleteTestDatabase();
        VelosterRepository.reset();
    }

    public void say(Object s) {
        br.com.mobilemind.api.droidutil.logs.AppLogger.info(getClass(), s.toString());
    }
}
