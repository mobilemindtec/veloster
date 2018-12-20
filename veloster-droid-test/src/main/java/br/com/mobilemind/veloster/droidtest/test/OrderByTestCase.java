package br.com.mobilemind.veloster.droidtest.test;

import br.com.mobilemind.api.droidunit.Assert;
import br.com.mobilemind.veloster.droidtest.pack.PersonGroup;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.sql.type.Criteria;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import java.util.List;

/**
 *
 * @author Ricardo BOcchi
 */
public class OrderByTestCase extends BaseTestCase {

    
    public void testOrderByName() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup p1 = new PersonGroup();
        p1.setName("joselito");

        PersonGroup p2 = new PersonGroup();
        p2.setName("bruno");

        PersonGroup p3 = new PersonGroup();
        p3.setName("ataides");

        manager.save(p1);
        manager.save(p2);
        manager.save(p3);

        Criteria<PersonGroup> criteria = manager.createCriteria();

        List<PersonGroup> items = criteria.orderBy("name").list();

        Assert.isTrue(items.size() == 3);

        Assert.areEqual("ataides", items.get(0).getName());
        Assert.areEqual("bruno", items.get(1).getName());
        Assert.areEqual("joselito", items.get(2).getName());
    }

    
    public void testOrderByNameAndId() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup p1 = new PersonGroup();
        p1.setName("joselito");

        PersonGroup p2 = new PersonGroup();
        p2.setName("bruno");

        PersonGroup p3 = new PersonGroup();
        p3.setName("ataides");

        manager.save(p1);
        manager.save(p2);
        manager.save(p3);

        Criteria<PersonGroup> criteria = manager.createCriteria();

        List<PersonGroup> items = criteria.orderBy("name", "id").list();

        Assert.isTrue(items.size() == 3);

        Assert.areEqual("ataides", items.get(0).getName());
        Assert.areEqual("bruno", items.get(1).getName());
        Assert.areEqual("joselito", items.get(2).getName());
    }

    
    public void testOrderByNameDesc() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup p1 = new PersonGroup();
        p1.setName("ataides");

        PersonGroup p2 = new PersonGroup();
        p2.setName("joselito");

        PersonGroup p3 = new PersonGroup();
        p3.setName("bruno");

        manager.save(p1);
        manager.save(p2);
        manager.save(p3);

        Criteria<PersonGroup> criteria = manager.createCriteria();

        List<PersonGroup> items = criteria.orderByDesc("name").list();

        Assert.isTrue(items.size() == 3);

        Assert.areEqual("joselito", items.get(0).getName());
        Assert.areEqual("bruno", items.get(1).getName());
        Assert.areEqual("ataides", items.get(2).getName());
    }

    
    public void testOrderByNameAndIdDesc() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup p1 = new PersonGroup();
        p1.setName("ataides");

        PersonGroup p2 = new PersonGroup();
        p2.setName("joselito");

        PersonGroup p3 = new PersonGroup();
        p3.setName("bruno");

        manager.save(p1);
        manager.save(p2);
        manager.save(p3);

        Criteria<PersonGroup> criteria = manager.createCriteria();

        List<PersonGroup> items = criteria.orderByDesc("name", "id").list();

        Assert.isTrue(items.size() == 3);

        Assert.areEqual("joselito", items.get(0).getName());
        Assert.areEqual("bruno", items.get(1).getName());
        Assert.areEqual("ataides", items.get(2).getName());
    }
}
