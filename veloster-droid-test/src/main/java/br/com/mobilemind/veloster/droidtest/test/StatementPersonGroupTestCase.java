package br.com.mobilemind.veloster.droidtest.test;

import br.com.mobilemind.api.droidunit.Assert;
import br.com.mobilemind.api.droidunit.ExceptionDelegate;
import br.com.mobilemind.veloster.droidtest.pack.NullebleEntity;
import br.com.mobilemind.veloster.droidtest.pack.Person;
import br.com.mobilemind.veloster.droidtest.pack.PersonGroup;
import br.com.mobilemind.veloster.droidtest.pack.PersonType;
import br.com.mobilemind.veloster.exceptions.DataBaseException;
import br.com.mobilemind.veloster.exceptions.EntityValidatorException;
import br.com.mobilemind.veloster.exceptions.FailProcessExcetion;
import br.com.mobilemind.veloster.exceptions.VelosterException;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.sql.type.Criteria;
import br.com.mobilemind.veloster.sql.type.Eq;
import br.com.mobilemind.veloster.sql.type.IsNull;
import br.com.mobilemind.veloster.sql.type.Like;
import br.com.mobilemind.veloster.sql.type.Match;
import br.com.mobilemind.veloster.sql.type.NotIsNull;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ricardo Bocchi
 */
public class StatementPersonGroupTestCase extends BaseTestCase {

    public void testCreateAndDropTablePersonGroup() throws Exception {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        Assert.isTrue(manager.tableExists());

        manager.tableUpdate();

        manager.tableDelete();
        Assert.isFalse(manager.tableExists());
    }

    public void testSavePersonGroup() throws Exception {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();

        Assert.isTrue(manager.tableExists());

        manager.setEntity(group);
        manager.save();

        Assert.isTrue(group.getId() != null && group.getId() > 0);

        PersonGroup p = manager.load(group.getId());

        Assert.isNotNull(p);
        Assert.areEqual(p, group);
        Assert.areEqual(p.getName(), group.getName());

        manager.delete();

        p = manager.load(group.getId());

        Assert.isNull(p);

        manager.tableDelete();
        Assert.isFalse(manager.tableExists());
    }

    public void testUpdatePersonGroup() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();

        Assert.isTrue(manager.tableExists());

        manager.setEntity(group);
        manager.save();

        Assert.isTrue(group.getId() != null && group.getId() > 0);

        PersonGroup p = manager.load(group.getId());

        Assert.isNotNull(p);
        Assert.areEqual(p, group);
        Assert.areEqual(p.getName(), group.getName());

        p.setName("frederico");
        manager.update();


        p = manager.load();

        Assert.isNotNull(p);
        Assert.areEqual(p.getName(), "frederico");


        manager.delete();

        p = manager.load(group.getId());

        Assert.isNull(p);

        manager.tableDelete();
        Assert.isFalse(manager.tableExists());
    }

    public void testUpdatePersonGroupWithWhereName() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();

        Assert.isTrue(manager.tableExists());

        manager.setEntity(group);
        manager.save();

        Assert.isTrue(group.getId() != null && group.getId() > 0);

        PersonGroup p = manager.load(group.getId());

        Assert.isNotNull(p);
        Assert.areEqual(p, group);
        Assert.areEqual(p.getName(), group.getName());

        manager.createCriteria().add("name", new Like(group.getName(), Match.EXACT));
        p.setName("frederico");
        manager.updateByCriteria();

        manager.createCriteria().add("name", new Like(group.getName(), Match.EXACT));
        p = manager.load();

        Assert.isNotNull(p);
        Assert.areEqual(p.getName(), "frederico");


        manager.delete();

        p = manager.load(group.getId());

        Assert.isNull(p);

        manager.tableDelete();
        Assert.isFalse(manager.tableExists());
    }

    public void testSelectPersonGroupByNameNotLake() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();
        group.setName("querytools");
        manager.save(group);

        manager.createCriteria().add("name", new Like("query", Match.STARTWITH));
        Assert.isFalse(manager.listByCriteria().isEmpty());

        manager.createCriteria().add("name", new Like("query", Match.STARTWITH, false, true));
        Assert.isTrue(manager.listByCriteria().isEmpty());
    }

    public void testUpdatePersonGroupWithWhereNameAndId() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();

        Assert.isTrue(manager.tableExists());

        manager.setEntity(group);
        manager.save();

        Assert.isTrue(group.getId() != null && group.getId() > 0);

        PersonGroup p = manager.load(group.getId());

        Assert.isNotNull(p);
        Assert.areEqual(p, group);
        Assert.areEqual(p.getName(), group.getName());

        manager.createCriteria().add("name", new Like(group.getName(), Match.EXACT)).add("id", new Eq(group.getId()));
        p.setName("frederico");
        manager.updateByCriteria();

        manager.createCriteria().add("name", new Like(p.getName(), Match.EXACT));
        p = manager.load();

        Assert.isNotNull(p);
        Assert.areEqual(p.getName(), "frederico");


        manager.delete();

        p = manager.load(group.getId());

        Assert.isNull(p);

        manager.tableDelete();
        Assert.isFalse(manager.tableExists());
    }

    public void testDeletePersonGroupWithWhereNameAndId() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();

        Assert.isTrue(manager.tableExists());

        manager.setEntity(group);
        manager.save();

        Assert.isTrue(group.getId() != null && group.getId() > 0);

        PersonGroup p = manager.load(group.getId());

        Assert.isNotNull(p);
        Assert.areEqual(p, group);
        Assert.areEqual(p.getName(), group.getName());

        manager.createCriteria().add("id", new Eq(group.getId())).add("name", new Eq(p.getName()));

        manager.deleteByCriteria();

        p = manager.load(group.getId());

        Assert.isNull(p);

        manager.tableDelete();
        Assert.isFalse(manager.tableExists());
    }

    public void testSelectAllPersonGroup() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();
        PersonGroup group2 = new PersonGroup("Group Two");

        Assert.isTrue(manager.tableExists());

        manager.setEntity(group);
        manager.save();
        Assert.isTrue(group.getId() != null && group.getId() > 0);

        manager.setEntity(group2);
        manager.save();
        Assert.isTrue(group2.getId() != null && group2.getId() > 0);
        Assert.isTrue(group.getId() != group2.getId() && !group.getName().equals(group2.getName()));

        List<PersonGroup> result = manager.list();

        Assert.isNotNull(result);
        Assert.areEqual(result.size(), 2);

        for (PersonGroup p : result) {
            manager.setEntity(p);
            manager.delete();
        }

        result = manager.list();
        Assert.isNotNull(result);
        Assert.areEqual(result.size(), 0);

        manager.tableDelete();
        Assert.isFalse(manager.tableExists());
    }

    public void testViolationForeignKey() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<PersonGroup> managerGroup = VelosterRepository.getVeloster(PersonGroup.class);
        final Veloster<Person> managerPerson = VelosterRepository.getVeloster(Person.class);

        if (!managerGroup.tableExists()) {
            managerGroup.tableCreate();
        }

        Assert.isTrue(managerGroup.tableExists());

        if (!managerPerson.tableExists()) {
            managerPerson.tableCreate();
        }

        Assert.isTrue(managerPerson.tableExists());


        Person person = new Person();
        person.getGroup().setId(50L);

        managerPerson.setEntity(person);

        Assert.shouldThrow(DataBaseException.class, new ExceptionDelegate() {
            @Override
            public void execute() throws Exception {
                managerPerson.save();
            }
        });



        managerPerson.tableDelete();
        managerGroup.tableDelete();

        Assert.isFalse(managerPerson.tableExists());
        Assert.isFalse(managerGroup.tableExists());
    }

    public void testJoin() throws Exception {
        Veloster<PersonGroup> managerGroup = VelosterRepository.getVeloster(PersonGroup.class);
        Veloster<Person> managerPerson = VelosterRepository.getVeloster(Person.class);

        if (!managerGroup.tableExists()) {
            managerGroup.tableCreate();
        }

        Assert.isTrue(managerGroup.tableExists());

        if (!managerPerson.tableExists()) {
            managerPerson.tableCreate();
        }

        Assert.isTrue(managerPerson.tableExists());

        PersonGroup group = new PersonGroup();
        group.setName("group test");
        managerGroup.setEntity(group);
        managerGroup.save();


        Person person = new Person();
        person.setAge(10);
        person.setBirthDay(new Date());
        person.setDoubleValue(10);
        person.setPersonType(PersonType.PERSON_1);
        person.setLongValue(10);
        person.setName("test");
        person.setGroup(group);

        managerPerson.setEntity(person);
        managerPerson.save();

        Criteria<Person> criteria = managerPerson.createCriteria();
        criteria.add("group.name", new Like(group.getName(), Match.EXACT));
        List<Person> list = managerPerson.listByCriteria();


        Assert.isTrue(list.size() == 1);

        criteria = managerPerson.createCriteria();
        criteria.add("group.name", new Like(group.getName(), Match.STARTWITH));
        criteria.add("group.id", new Eq(group.getId()));
        criteria.add("name", new Like(person.getName(), Match.EXACT));
        list = managerPerson.listByCriteria();

        managerPerson.tableDelete();
        managerGroup.tableDelete();

        Assert.isFalse(managerPerson.tableExists());
        Assert.isFalse(managerGroup.tableExists());
    }

    public void testCriteriaIsNotNull() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<NullebleEntity> manager = VelosterRepository.getVeloster(NullebleEntity.class);


        Assert.isTrue(manager.tableExists());

        NullebleEntity e = new NullebleEntity();

        manager.setEntity(e);
        manager.save();

        Criteria<NullebleEntity> criteria = manager.createCriteria();

        criteria.add("description", new IsNull());

        NullebleEntity other = manager.loadByCriteria();

        Assert.isNotNull(other);

        manager.tableDelete();
        Assert.isFalse(manager.tableExists());
    }

    public void testCriteriaIsNull() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<NullebleEntity> manager = VelosterRepository.getVeloster(NullebleEntity.class);


        Assert.isTrue(manager.tableExists());

        NullebleEntity e = new NullebleEntity();
        e.setDescription("null test");
        manager.setEntity(e);
        manager.save();

        Criteria<NullebleEntity> criteria = manager.createCriteria();

        criteria.add("description", new IsNull());

        NullebleEntity other = manager.loadByCriteria();


        Assert.isNull(other);

        criteria = manager.createCriteria();

        criteria.add("description", new NotIsNull());

        other = manager.loadByCriteria();

        Assert.isNotNull(other);

        manager.tableDelete();
        Assert.isFalse(manager.tableExists());
    }

    public void testPagedListFormPersonGroup() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        if (!manager.tableExists()) {
            manager.tableCreate();
        }

        for (int i = 0; i < 50; i++) {
            manager.save(new PersonGroup("ID='" + i + "'"));
        }

        Assert.areEqual(50, manager.list().size());

        int offset = 0;
        int limit = 9;
        int assertCount = 0;

        for (int i = 0; i < 6; i++) {
            manager.createCriteria().setLimit(limit)
                    .setOffset(offset);
            offset += limit;

            List<PersonGroup> items = manager.listByCriteria();

            if (i == 5) {
                Assert.areEqual(5, items.size());
            } else {
                Assert.areEqual(9, items.size());
            }

            for (PersonGroup it : items) {
                Assert.isTrue(it.getName().contains("ID='" + (assertCount++) + "'"));
            }
        }

        Assert.areEqual(50, assertCount);
    }

    public void testPagedListFormPersonGroupWithAutoIncrement() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        if (!manager.tableExists()) {
            manager.tableCreate();
        }

        for (int i = 0; i < 50; i++) {
            manager.save(new PersonGroup("ID='" + i + "'"));
        }

        Assert.areEqual(50, manager.list().size());

        int offset = 0;
        int limit = 9;
        int assertCount = 0;

        manager.createCriteria().setLimit(limit)
                .setOffset(offset).setAutoIncrementOffset(true);

        for (int i = 0; i < 100; i++) {

            List<PersonGroup> items = manager.listByCriteria();

            if (i == 5) {
                Assert.areEqual(5, items.size());
            } else {
                Assert.areEqual(9, items.size());
            }

            for (PersonGroup it : items) {
                Assert.areEqual("ID='" + (assertCount++) + "'", it.getName());
            }

            if (items.size() < limit) {
                break;
            }
        }

        Assert.areEqual(50, assertCount);
    }
}
