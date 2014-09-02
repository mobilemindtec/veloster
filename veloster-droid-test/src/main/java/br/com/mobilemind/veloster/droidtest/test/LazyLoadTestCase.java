package br.com.mobilemind.veloster.droidtest.test;

import br.com.mobilemind.api.droidunit.Assert;
import br.com.mobilemind.veloster.droidtest.pack.EntityInsertCascade;
import br.com.mobilemind.veloster.droidtest.pack.EntityInsertCascadeLazy;
import br.com.mobilemind.veloster.droidtest.pack.PersonGroup;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.tools.VelosterRepository;

/**
 *
 * @author Ricardo Bocchi
 */
public class LazyLoadTestCase extends BaseTestCase {

    public void testLoadEntityWithoutLazy() {
        Veloster<EntityInsertCascade> manager = VelosterRepository.getVeloster(EntityInsertCascade.class);

        EntityInsertCascade e = new EntityInsertCascade();
        e.setGrupo(new PersonGroup("aaaaa"));
        manager.save(e);


        EntityInsertCascade other = manager.load(e.getId());

        Assert.isNotNull(other);

        Assert.isFalse(other.getGrupo().getName().equals("aaaaa"));

        other.getGrupo().load();

        Assert.areEqual("aaaaa", other.getGrupo().getName());
    }

    public void testInternalLoadEntityLazy() {
        Veloster<EntityInsertCascadeLazy> manager = VelosterRepository.getVeloster(EntityInsertCascadeLazy.class);

        EntityInsertCascadeLazy e = new EntityInsertCascadeLazy();
        e.setGrupo(new PersonGroup("aaaaa"));
        manager.save(e);


        EntityInsertCascadeLazy other = manager.load(e.getId());

        Assert.isNotNull(other);

        Assert.isTrue(other.getGrupo().getName().equals("aaaaa"));
    }
}
