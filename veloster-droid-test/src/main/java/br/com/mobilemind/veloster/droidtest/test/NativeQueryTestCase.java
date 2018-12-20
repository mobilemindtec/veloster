package br.com.mobilemind.veloster.droidtest.test;

import br.com.mobilemind.api.droidunit.Assert;
import br.com.mobilemind.veloster.droidtest.pack.PersonGroup;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import java.util.List;

/**
 *
 * @author Ricardo Bocchi
 */
public class NativeQueryTestCase extends BaseTestCase {

    
    public void testExecuteNativeDelete() throws Exception {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();
        manager.setEntity(group);
        manager.save();

        manager.executeNativeQuery("delete from PersonsGroups where id = " + group.getId());

        group = manager.load(group.getId());

        Assert.isNull(group);

    }

    
    public void testExecuteNativeSelect() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        for (int i = 0; i < 10; i++) {
            manager.save(new PersonGroup("ID='" + i + "'"));
        }

        Assert.areEqual(10, manager.list().size());

        List<Object[]> items = manager.executeNativeSelect("select id, name from PersonsGroups");

        int count = 0;
        for (Object[] it : items) {
            Assert.areEqual(2, it.length);

            int id = Integer.parseInt(it[0].toString());
            String name = it[1].toString();
            Assert.isTrue(id > 0);
            Assert.areEqual("ID='" + (count++) + "'", name);

            System.out.println("id=" + id + "   name=" + name);
        }
    }
}
