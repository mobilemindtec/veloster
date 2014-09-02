package br.com.mobilemind.veloster.droidtest.test;

import br.com.mobilemind.api.droidunit.Assert;
import br.com.mobilemind.veloster.droidtest.pack.Person;
import br.com.mobilemind.veloster.droidtest.pack.PersonGroup;
import br.com.mobilemind.veloster.droidtest.pack.PersonType;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.orm.TransactionalOperation;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import java.util.Date;

/**
 *
 * @author Ricardo Bocchi
 */
public class TransactionalOperationTestCase extends BaseTestCase {

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

        managerG.setEntity(personGroup);
        managerP.setEntity(person);

        managerG.runTrans(new TransactionalOperation() {
            @Override
            public void execute() {
                managerG.save();
                managerP.save();
            }
        });


        Assert.isNotNull(person.getId());
        Assert.isNotNull(personGroup.getId());

        Assert.isTrue(person.getId() > 0);
        Assert.isTrue(personGroup.getId() > 0);

        Assert.isNotNull(managerG.load(personGroup.getId()));
        Assert.isNotNull(managerP.load(person.getId()));
    }

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

        managerG.setEntity(personGroup);
        managerP.setEntity(person);

        try {
            managerG.runTrans(new TransactionalOperation() {
                @Override
                public void execute() {
                    managerG.save();
                    managerP.save();
                }
            });
        } catch (Exception e) {
        }

        Assert.isTrue(managerP.list().isEmpty());
        Assert.isTrue(managerG.list().isEmpty());
    }
}
