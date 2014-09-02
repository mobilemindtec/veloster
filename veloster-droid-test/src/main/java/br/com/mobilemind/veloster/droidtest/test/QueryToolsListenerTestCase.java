package br.com.mobilemind.veloster.droidtest.test;

import br.com.mobilemind.api.droidunit.Assert;
import br.com.mobilemind.veloster.droidtest.pack.NullebleEntity;
import br.com.mobilemind.veloster.droidtest.pack.PersonGroup;
import br.com.mobilemind.veloster.event.InsertListener;
import br.com.mobilemind.veloster.event.InsertListenerAdapter;
import br.com.mobilemind.veloster.event.DeleteListener;
import br.com.mobilemind.veloster.event.UpdateListener;
import br.com.mobilemind.veloster.event.UpdateListenerAdapter;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.tools.VelosterRepository;

/**
 *
 * @author Ricardo Bocchi
 */
public class QueryToolsListenerTestCase extends BaseTestCase {

    private boolean preInsertCalled;
    private boolean preUpdateCalled;
    private boolean preDeleteCalled;
    private boolean posInsertCalled;
    private boolean posUpdateCalled;
    private boolean posDeleteCalled;
    InsertListener insertAllListener = new InsertListener() {
        @Override
        public boolean preInsert(Entity entity) {
            preInsertCalled = true;
            return true;
        }

        @Override
        public void posInsert(Entity entity) {
            posInsertCalled = true;
        }

        @Override
        public Class<? extends Entity> getEntityToListen() {
            return null;
        }
    };
    DeleteListener deleteAllListener = new DeleteListener() {
        @Override
        public boolean preDelete(Entity entity) {
            preDeleteCalled = true;
            return true;
        }

        @Override
        public void posDelete(Entity entity) {
            posDeleteCalled = true;
        }

        @Override
        public Class<? extends Entity> getEntityToListen() {
            return null;
        }
    };
    UpdateListener updateAllListener = new UpdateListener() {
        @Override
        public boolean preUpdate(Entity entity) {
            preUpdateCalled = true;
            return true;
        }

        @Override
        public void posUpdate(Entity entity) {
            posUpdateCalled = true;
        }

        @Override
        public Class<? extends Entity> getEntityToListen() {
            return null;
        }
    };
    InsertListener insertGroupOnlyListener = new InsertListener() {
        @Override
        public boolean preInsert(Entity entity) {
            preInsertCalled = true;
            return true;
        }

        @Override
        public void posInsert(Entity entity) {
            posInsertCalled = true;
        }

        @Override
        public Class<? extends Entity> getEntityToListen() {
            return PersonGroup.class;
        }
    };
    DeleteListener deleteGroupOnlyListener = new DeleteListener() {
        @Override
        public boolean preDelete(Entity entity) {
            preDeleteCalled = true;
            return true;
        }

        @Override
        public void posDelete(Entity entity) {
            posDeleteCalled = true;
        }

        @Override
        public Class<? extends Entity> getEntityToListen() {
            return PersonGroup.class;
        }
    };
    UpdateListener updateGroupOnlyListener = new UpdateListener() {
        @Override
        public boolean preUpdate(Entity entity) {
            preUpdateCalled = true;
            return true;
        }

        @Override
        public void posUpdate(Entity entity) {
            posUpdateCalled = true;
        }

        @Override
        public Class<? extends Entity> getEntityToListen() {
            return PersonGroup.class;
        }
    };

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        this.preInsertCalled = false;
        this.preUpdateCalled = false;
        this.preDeleteCalled = false;
        this.posInsertCalled = false;
        this.posUpdateCalled = false;
        this.posDeleteCalled = false;
    }

    public void testInsertListener() {

        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        manager.addInsertListener(insertAllListener);

        PersonGroup entity = new PersonGroup();
        manager.save(entity);
        manager.update(entity);

        Assert.isTrue(preInsertCalled);
        Assert.isTrue(posInsertCalled);

        Assert.isFalse(preUpdateCalled);
        Assert.isFalse(posUpdateCalled);

        Assert.isFalse(preDeleteCalled);
        Assert.isFalse(posDeleteCalled);

    }

    public void testUpdateListener() {

        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        manager.addUpdateListener(updateAllListener);

        PersonGroup entity = new PersonGroup();
        manager.save(entity);
        manager.update(entity);


        Assert.isFalse(preInsertCalled);
        Assert.isFalse(posInsertCalled);

        Assert.isTrue(preUpdateCalled);
        Assert.isTrue(posUpdateCalled);

        Assert.isFalse(preDeleteCalled);
        Assert.isFalse(posDeleteCalled);
    }

    public void testDeleteListener() {

        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        manager.addDeleteListener(deleteAllListener);

        PersonGroup entity = new PersonGroup();
        manager.save(entity);
        manager.delete(entity);

        Assert.isFalse(preInsertCalled);
        Assert.isFalse(posInsertCalled);

        Assert.isFalse(preUpdateCalled);
        Assert.isFalse(posUpdateCalled);

        Assert.isTrue(preDeleteCalled);
        Assert.isTrue(posDeleteCalled);
    }

    public void testAllListenerOnGroupOnly() {

        Veloster<PersonGroup> managerG = VelosterRepository.getVeloster(PersonGroup.class);
        Veloster<NullebleEntity> managerN = VelosterRepository.getVeloster(NullebleEntity.class);

        managerG.addDeleteListener(deleteGroupOnlyListener);
        managerG.addInsertListener(insertGroupOnlyListener);
        managerG.addUpdateListener(updateGroupOnlyListener);

        managerN.addDeleteListener(deleteGroupOnlyListener);
        managerN.addInsertListener(insertGroupOnlyListener);
        managerN.addUpdateListener(updateGroupOnlyListener);


        NullebleEntity e = new NullebleEntity();
        e.setDescription("aaaa");

        managerN.save(e);
        managerN.update(e);
        managerN.delete(e);


        Assert.isFalse(preInsertCalled);
        Assert.isFalse(posInsertCalled);

        Assert.isFalse(preUpdateCalled);
        Assert.isFalse(posUpdateCalled);

        Assert.isFalse(preDeleteCalled);
        Assert.isFalse(posDeleteCalled);

        PersonGroup group = new PersonGroup();


        managerG.save(group);
        managerG.update(group);
        managerG.delete(group);

        Assert.isTrue(preInsertCalled);
        Assert.isTrue(posInsertCalled);

        Assert.isTrue(preUpdateCalled);
        Assert.isTrue(posUpdateCalled);

        Assert.isTrue(preDeleteCalled);
        Assert.isTrue(posDeleteCalled);
    }

    public void testListenerCancelInsert() {

        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        manager.addInsertListener(new InsertListenerAdapter() {
            @Override
            public boolean preInsert(Entity entity) {
                return false;
            }
        });

        PersonGroup entity = new PersonGroup();
        manager.save(entity);

        Assert.isNull(entity.getId());
        Assert.isTrue(manager.list().isEmpty());
    }

    public void testListenerCancelUpdate() {

        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        manager.addUpdateListener(new UpdateListenerAdapter() {
            @Override
            public boolean preUpdate(Entity entity) {
                return false;
            }
        });


        PersonGroup entity = new PersonGroup();
        manager.save(entity);

        PersonGroup other = manager.load(entity.getId());

        other.setName("other");

        manager.update(other);

        other = manager.load(entity.getId());

        Assert.areEqual(entity.getName(), other.getName());
    }
}
