package br.com.mobilemind.veloster.droidtest.test;

import br.com.mobilemind.api.droidunit.Assert;
import br.com.mobilemind.api.droidunit.TestBehaviorImpl;
import br.com.mobilemind.veloster.droidtest.pack.Person;
import br.com.mobilemind.veloster.droidtest.pack.PersonGroup;
import br.com.mobilemind.veloster.extra.BackupInfo;
import br.com.mobilemind.veloster.extra.DatabaseBackupHelper;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.tools.VelosterConfig;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Ricardo Bocchi
 */
public class BackupTestCase extends BaseTestCase {

    @Override
    public void setUp() throws SQLException {
        super.setUp();
        
        DatabaseBackupHelper helper = VelosterConfig.build().getDatabaseBackupHelper();

        for (BackupInfo backupInfo : helper.listOldBackups(true)) {
            backupInfo.getLocation().delete();
        }
    }

    public void testBackup() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        manager.setEntity(new PersonGroup());
        manager.save();

        Assert.areEqual(1, manager.list().size());
        say("insert persongroup");

        DatabaseBackupHelper helper = VelosterConfig.build().getDatabaseBackupHelper();

        helper.doBackup();
        say("do backup");

        List<BackupInfo> list = helper.listOldBackups(true);

        say("listing backups");
        Assert.areEqual(1, list.size());


        say("remove persongroup");
        manager.delete();


        Assert.areEqual(0, manager.list().size());

        say("do restore");
        helper.doRestore(list.get(0));

        say("find restored persongroup");
        Assert.areEqual(1, manager.list().size());
    }
}
