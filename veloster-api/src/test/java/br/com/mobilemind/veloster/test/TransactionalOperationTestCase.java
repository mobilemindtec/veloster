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

import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.orm.TransactionalOperation;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import br.com.mobilemind.veloster.teste.pack.Person;
import br.com.mobilemind.veloster.teste.pack.PersonGroup;
import br.com.mobilemind.veloster.teste.pack.PersonType;
import java.util.Date;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Ricardo Bocchi
 */
public class TransactionalOperationTestCase extends BaseTestCase {

    @Test
    public void testSalvarDependenciasNaMesmaTransacao() {
        final Veloster<Person> managerP = VelosterRepository.getVeloster(Person.class);
        final Veloster<PersonGroup> managerG = VelosterRepository.getVeloster(PersonGroup.class);

        if (!managerG.tableExists()) {
            managerG.tableCreate();
        }

        if (!managerP.tableExists()) {
            managerP.tableCreate();
        }


        final PersonGroup personGroup = new PersonGroup();
        final Person person = new Person();
        person.setAge(10);
        person.setBirthDay(new Date());
        person.setDoubleValue(10);
        person.setPersonType(PersonType.PERSON_1);
        person.setName("transactionaltest");
        person.setGroup(personGroup);

        managerG.runTrans(new TransactionalOperation() {
            @Override
            public void execute() {
                managerG.save(personGroup);
                managerP.save(person);
            }
        });


        Assert.assertNotNull(person.getId());
        Assert.assertNotNull(personGroup.getId());

        Assert.assertTrue(person.getId() > 0);
        Assert.assertTrue(personGroup.getId() > 0);

        Assert.assertNotNull(managerG.load(personGroup.getId()));
        Assert.assertNotNull(managerP.load(person.getId()));
    }

    @Test
    public void testNaoSalvarDependenciasNaMesmaTransacao() {
        final Veloster<Person> managerP = VelosterRepository.getVeloster(Person.class);
        final Veloster<PersonGroup> managerG = VelosterRepository.getVeloster(PersonGroup.class);

        if (!managerG.tableExists()) {
            managerG.tableCreate();
        }

        if (!managerP.tableExists()) {
            managerP.tableCreate();
        }


        final PersonGroup personGroup = new PersonGroup();
        final Person person = new Person();
        person.setAge(10);
        person.setBirthDay(new Date());
        person.setDoubleValue(10);
        person.setPersonType(PersonType.PERSON_1);
        person.setName("transactional test");
        person.setGroup(personGroup);

        try {
            managerG.runTrans(new TransactionalOperation() {
                @Override
                public void execute() {
                    managerG.save(personGroup);
                    managerP.save(person);
                }
            });
        } catch (Exception e) {
        }

        Assert.assertTrue(managerP.list().isEmpty());
        Assert.assertTrue(managerG.list().isEmpty());        
    }
}
