package br.com.mobilemind.veloster.droidtest.test;

import br.com.mobilemind.api.droidunit.Assert;
import br.com.mobilemind.veloster.droidtest.pack.EntityInsertCascade;
import br.com.mobilemind.veloster.droidtest.pack.EntityUpdateCascade;
import br.com.mobilemind.veloster.droidtest.pack.PersonGroup;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.tools.VelosterRepository;

public class ReferenceCascadeTestCase extends BaseTestCase {

    public void testSaveReference() {
        Veloster<EntityInsertCascade> managere = VelosterRepository.getVeloster(EntityInsertCascade.class);
        Veloster<PersonGroup> managerg = VelosterRepository.getVeloster(PersonGroup.class);

        Assert.isTrue(managerg.list().isEmpty());

        EntityInsertCascade e = new EntityInsertCascade();
        e.setGrupo(new PersonGroup());

        String name = e.getGrupo().getName();

        managere.save(e);

        Assert.isNotNull(managere.load(e.getId()));

        Assert.isFalse(managerg.list().isEmpty());

        PersonGroup other = managerg.load(e.getGrupo().getId());

        Assert.isNotNull(other);
        Assert.areEqual(name, other.getName());

        e.getGrupo().setName("name two");

        managere.update(e);

        other = managerg.load(e.getGrupo().getId());

        Assert.isNotNull(other);
        Assert.areEqual(name, other.getName());
    }

    public void testUpdateReference() {
        Veloster<EntityUpdateCascade> managere = VelosterRepository.getVeloster(EntityUpdateCascade.class);
        Veloster<PersonGroup> managerg = VelosterRepository.getVeloster(PersonGroup.class);

        Assert.isTrue(managerg.list().isEmpty());

        EntityUpdateCascade e = new EntityUpdateCascade();
        e.setGrupo(new PersonGroup());

        String name = e.getGrupo().getName();

        managere.save(e);

        Assert.isNotNull(managere.load(e.getId()));

        Assert.isFalse(managerg.list().isEmpty());

        PersonGroup other = managerg.load(e.getGrupo().getId());

        Assert.isNotNull(other);
        Assert.areEqual(name, other.getName());

        e.getGrupo().setName("name two");

        managere.update(e);

        other = managerg.load(e.getGrupo().getId());

        Assert.isNotNull(other);
        Assert.areEqual("name two", other.getName());
    }
}
