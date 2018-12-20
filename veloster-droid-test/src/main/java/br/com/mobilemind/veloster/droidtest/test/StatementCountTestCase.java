package br.com.mobilemind.veloster.droidtest.test;

import br.com.mobilemind.api.droidunit.Assert;
import br.com.mobilemind.veloster.droidtest.pack.PersonGroup;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.sql.type.Criteria;
import br.com.mobilemind.veloster.sql.type.Eq;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import java.util.List;

/**
 *
 * @author Ricardo Bocchi
 */
public class StatementCountTestCase extends BaseTestCase {

    public void testCountPersonGroup() throws Exception {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();
        PersonGroup group2 = new PersonGroup();

        Assert.isTrue(manager.tableExists());

        manager.setEntity(group);
        manager.save();

        manager.setEntity(group2);
        manager.save();


        int count = manager.count();

        Assert.isTrue(count == 2, "count expected > 0 but is " + count);

        List<PersonGroup> items = manager.list();

        Assert.isTrue(items.size() == 2, "list size expected " + count + " but is " + items.size());

        for (PersonGroup p : items) {
            manager.setEntity(p);
            manager.delete();
        }

        items = manager.list();

        Assert.isTrue(items.size() == 0, "list size expected 0 but is " + items.size());

        manager.tableDelete();
        Assert.isFalse(manager.tableExists());
    }

    public void testCountByCriteriaPersonGroup() throws Exception {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();
        PersonGroup group2 = new PersonGroup();

        Assert.isTrue(manager.tableExists());

        manager.setEntity(group);
        manager.save();

        manager.setEntity(group2);
        manager.save();

        Criteria criteria = manager.createCriteria();
        criteria.add("id", new Eq(group.getId()));

        int count = manager.countByCriteria();

        Assert.isTrue(count == 1, "count expected > 0 but is " + count);

        List<PersonGroup> items = manager.list();

        Assert.isTrue(items.size() == 2, "list size expected " + 2 + " but is " + items.size());

        for (PersonGroup p : items) {
            manager.setEntity(p);
            manager.delete();
        }

        items = manager.list();

        Assert.isTrue(items.size() == 0, "list size expected 0 but is " + items.size());

        manager.tableDelete();
        Assert.isFalse(manager.tableExists());
    }
}
