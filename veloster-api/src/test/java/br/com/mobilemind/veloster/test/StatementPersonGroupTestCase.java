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
import br.com.mobilemind.veloster.teste.pack.NullebleEntity;
import br.com.mobilemind.veloster.teste.pack.Person;
import br.com.mobilemind.veloster.teste.pack.PersonGroup;
import br.com.mobilemind.veloster.teste.pack.PersonType;
import java.util.Date;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Ricardo Bocchi
 */
public class StatementPersonGroupTestCase extends BaseTestCase {

    @Test
    public void testCreateAndDropTablePersonGroup() throws Exception {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        Assert.assertTrue(manager.tableExists());

        manager.tableUpdate();

        manager.tableDelete();
        Assert.assertFalse(manager.tableExists());
    }

    @Test
    public void testSavePersonGroup() throws Exception {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();

        Assert.assertTrue(manager.tableExists());

        manager.save(group);

        Assert.assertTrue(group.getId() != null && group.getId() > 0);

        PersonGroup p = manager.load(group.getId());

        Assert.assertNotNull(p);
        Assert.assertEquals(p, group);
        Assert.assertEquals(p.getName(), group.getName());

        manager.delete(p);

        p = manager.load(group.getId());

        Assert.assertNull(p);

        manager.tableDelete();
        Assert.assertFalse(manager.tableExists());
    }

    @Test
    public void testUpdatePersonGroup() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();

        Assert.assertTrue(manager.tableExists());

        manager.save(group);

        Assert.assertTrue(group.getId() != null && group.getId() > 0);

        PersonGroup p = manager.load(group.getId());

        Assert.assertNotNull(p);
        Assert.assertEquals(p, group);
        Assert.assertEquals(p.getName(), group.getName());

        p.setName("frederico");
        manager.update(p);


        p = manager.load(p);

        Assert.assertNotNull(p);
        Assert.assertEquals(p.getName(), "frederico");


        manager.delete(p);

        p = manager.load(group.getId());

        Assert.assertNull(p);

        manager.tableDelete();
        Assert.assertFalse(manager.tableExists());
    }

    @Test
    public void testUpdatePersonGroupWithWhereName() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();

        Assert.assertTrue(manager.tableExists());

        manager.save(group);

        Assert.assertTrue(group.getId() != null && group.getId() > 0);

        PersonGroup p = manager.load(group.getId());

        Assert.assertNotNull(p);
        Assert.assertEquals(p, group);
        Assert.assertEquals(p.getName(), group.getName());

        p.setName("frederico");
        manager.update(p);

        manager.createCriteria().add("name", new Like(group.getName(), Match.EXACT));
        p = manager.load(p);

        Assert.assertNotNull(p);
        Assert.assertEquals(p.getName(), "frederico");


        manager.delete(p);

        p = manager.load(group.getId());

        Assert.assertNull(p);

        manager.tableDelete();
        Assert.assertFalse(manager.tableExists());
    }

    @Test
    public void testSelectPersonGroupByNameNotLake() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();
        group.setName("querytools");
        manager.save(group);

        Criteria criteria = manager.createCriteria().add("name", new Like("query", Match.STARTWITH));
        Assert.assertFalse(manager.listByCriteria(criteria).isEmpty());

        criteria = manager.createCriteria().add("name", new Like("query", Match.STARTWITH, false, true));
        Assert.assertTrue(manager.listByCriteria(criteria).isEmpty());
    }

    @Test
    public void testUpdatePersonGroupWithWhereNameAndId() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();

        Assert.assertTrue(manager.tableExists());

        manager.save(group);

        Assert.assertTrue(group.getId() != null && group.getId() > 0);

        PersonGroup p = manager.load(group.getId());

        Assert.assertNotNull(p);
        Assert.assertEquals(p, group);
        Assert.assertEquals(p.getName(), group.getName());

        manager.createCriteria().add("name", new Like(group.getName(), Match.EXACT)).add("id", new Eq(group.getId()));
        p.setName("frederico");
        manager.update(p);

        manager.createCriteria().add("name", new Like(p.getName(), Match.EXACT));
        p = manager.load(p);

        Assert.assertNotNull(p);
        Assert.assertEquals(p.getName(), "frederico");


        manager.delete(p);

        p = manager.load(group.getId());

        Assert.assertNull(p);

        manager.tableDelete();
        Assert.assertFalse(manager.tableExists());
    }

    @Test
    public void testDeletePersonGroupWithWhereNameAndId() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();

        Assert.assertTrue(manager.tableExists());

        manager.save(group);

        Assert.assertTrue(group.getId() != null && group.getId() > 0);

        PersonGroup p = manager.load(group.getId());

        Assert.assertNotNull(p);
        Assert.assertEquals(p, group);
        Assert.assertEquals(p.getName(), group.getName());

        manager.createCriteria().add("id", new Eq(group.getId())).add("name", new Eq(p.getName()));

        manager.delete(p);

        p = manager.load(group.getId());

        Assert.assertNull(p);

        manager.tableDelete();
        Assert.assertFalse(manager.tableExists());
    }

    @Test
    public void testSelectAllPersonGroup() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();
        PersonGroup group2 = new PersonGroup("Group Two");

        Assert.assertTrue(manager.tableExists());

        manager.save(group);
        Assert.assertTrue(group.getId() != null && group.getId() > 0);

        manager.save(group2);
        Assert.assertTrue(group2.getId() != null && group2.getId() > 0);
        Assert.assertTrue(group.getId() != group2.getId() && !group.getName().equals(group2.getName()));

        List<PersonGroup> result = manager.list();

        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 2);

        for (PersonGroup p : result) {
            manager.delete(p);
        }

        result = manager.list();
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 0);

        manager.tableDelete();
        Assert.assertFalse(manager.tableExists());
    }

    @Test
    public void testJoin() throws Exception {
        Veloster<PersonGroup> managerGroup = VelosterRepository.getVeloster(PersonGroup.class);
        Veloster<Person> managerPerson = VelosterRepository.getVeloster(Person.class);

        if (!managerGroup.tableExists()) {
            managerGroup.tableCreate();
        }

        Assert.assertTrue(managerGroup.tableExists());

        if (!managerPerson.tableExists()) {
            managerPerson.tableCreate();
        }

        Assert.assertTrue(managerPerson.tableExists());

        PersonGroup group = new PersonGroup();
        group.setName("group test");
        managerGroup.save(group);


        Person person = new Person();
        person.setAge(10);
        person.setBirthDay(new Date());
        person.setDoubleValue(10);
        person.setPersonType(PersonType.PERSON_1);
        person.setLongValue(10);
        person.setName("test");
        person.setGroup(group);

        managerPerson.save(person);

        Criteria<Person> criteria = managerPerson.createCriteria();
        criteria.add("group.name", new Like(group.getName(), Match.EXACT));
        List<Person> list = managerPerson.listByCriteria(criteria);


        Assert.assertTrue(list.size() == 1);

        criteria = managerPerson.createCriteria();
        criteria.add("group.name", new Like(group.getName(), Match.STARTWITH));
        criteria.add("group.id", new Eq(group.getId()));
        criteria.add("name", new Like(person.getName(), Match.EXACT));
        list = managerPerson.listByCriteria(criteria);

        managerPerson.tableDelete();
        managerGroup.tableDelete();

        Assert.assertFalse(managerPerson.tableExists());
        Assert.assertFalse(managerGroup.tableExists());
    }

    @Test
    public void testCriteriaIsNotNull() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<NullebleEntity> manager = VelosterRepository.getVeloster(NullebleEntity.class);


        Assert.assertTrue(manager.tableExists());

        NullebleEntity e = new NullebleEntity();

        manager.save(e);

        Criteria<NullebleEntity> criteria = manager.createCriteria();

        criteria.add("description", new IsNull());

        NullebleEntity other = manager.loadByCriteria(criteria);

        Assert.assertNotNull(other);

        manager.tableDelete();
        Assert.assertFalse(manager.tableExists());
    }

    @Test
    public void testCriteriaIsNull() throws VelosterException, EntityValidatorException, FailProcessExcetion {
        Veloster<NullebleEntity> manager = VelosterRepository.getVeloster(NullebleEntity.class);


        Assert.assertTrue(manager.tableExists());

        NullebleEntity e = new NullebleEntity();
        e.setDescription("null test");
        manager.save(e);

        Criteria<NullebleEntity> criteria = manager.createCriteria();

        criteria.add("description", new IsNull());

        NullebleEntity other = manager.loadByCriteria(criteria);


        Assert.assertNull(other);

        criteria = manager.createCriteria();

        criteria.add("description", new NotIsNull());

        other = manager.loadByCriteria(criteria);

        Assert.assertNotNull(other);

        manager.tableDelete();
        Assert.assertFalse(manager.tableExists());
    }

    @Test
    public void testPagedListFormPersonGroup() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        if (!manager.tableExists()) {
            manager.tableCreate();
        }

        for (int i = 0; i < 50; i++) {
            manager.save(new PersonGroup("ID='" + i + "'"));
        }

        Assert.assertEquals(50, manager.list().size());

        int offset = 0;
        int limit = 9;
        int assertCount = 0;

        for (int i = 0; i < 6; i++) {
            Criteria criteria = manager.createCriteria().setLimit(limit)
                    .setOffset(offset);
            offset += limit;

            List<PersonGroup> items = manager.listByCriteria(criteria);

            if (i == 5) {
                Assert.assertEquals(5, items.size());
            } else {
                Assert.assertEquals(9, items.size());
            }

            for (PersonGroup it : items) {
                Assert.assertTrue(it.getName().contains("ID='" + (assertCount++) + "'"));
            }
        }

        Assert.assertEquals(50, assertCount);
    }

    @Test
    public void testPagedListFormPersonGroupWithAutoIncrement() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        if (!manager.tableExists()) {
            manager.tableCreate();
        }

        for (int i = 0; i < 50; i++) {
            manager.save(new PersonGroup("ID='" + i + "'"));
        }

        Assert.assertEquals(50, manager.list().size());

        int offset = 0;
        int limit = 9;
        int assertCount = 0;

        Criteria criteria = manager.createCriteria().setLimit(limit)
                .setOffset(offset).setAutoIncrementOffset(true);

        for (int i = 0; i < 100; i++) {

            List<PersonGroup> items = manager.listByCriteria(criteria);

            if (i == 5) {
                Assert.assertEquals(5, items.size());
            } else {
                Assert.assertEquals(9, items.size());
            }

            for (PersonGroup it : items) {
                Assert.assertEquals("ID='" + (assertCount++) + "'", it.getName());
            }

            if (items.size() < limit) {
                break;
            }
        }

        Assert.assertEquals(50, assertCount);
    }
}
