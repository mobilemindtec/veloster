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

import br.com.mobilemind.veloster.event.InsertListener;
import br.com.mobilemind.veloster.event.InsertListenerAdapter;
import br.com.mobilemind.veloster.event.DeleteListener;
import br.com.mobilemind.veloster.event.UpdateListener;
import br.com.mobilemind.veloster.event.UpdateListenerAdapter;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import br.com.mobilemind.veloster.teste.pack.NullebleEntity;
import br.com.mobilemind.veloster.teste.pack.PersonGroup;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

    @Before
    public void initUp() throws SQLException {
        super.setUp();
        this.preInsertCalled = false;
        this.preUpdateCalled = false;
        this.preDeleteCalled = false;
        this.posInsertCalled = false;
        this.posUpdateCalled = false;
        this.posDeleteCalled = false;
    }

    @Test
    public void testInsertListener() {

        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        manager.addInsertListener(insertAllListener);

        PersonGroup entity = new PersonGroup();
        manager.save(entity);
        manager.update(entity);

        Assert.assertTrue(preInsertCalled);
        Assert.assertTrue(posInsertCalled);

        Assert.assertFalse(preUpdateCalled);
        Assert.assertFalse(posUpdateCalled);

        Assert.assertFalse(preDeleteCalled);
        Assert.assertFalse(posDeleteCalled);

    }

    @Test
    public void testUpdateListener() {

        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        manager.addUpdateListener(updateAllListener);

        PersonGroup entity = new PersonGroup();
        manager.save(entity);
        manager.update(entity);


        Assert.assertFalse(preInsertCalled);
        Assert.assertFalse(posInsertCalled);

        Assert.assertTrue(preUpdateCalled);
        Assert.assertTrue(posUpdateCalled);

        Assert.assertFalse(preDeleteCalled);
        Assert.assertFalse(posDeleteCalled);
    }

    @Test
    public void testDeleteListener() {

        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        manager.addDeleteListener(deleteAllListener);

        PersonGroup entity = new PersonGroup();
        manager.save(entity);
        manager.delete(entity);

        Assert.assertFalse(preInsertCalled);
        Assert.assertFalse(posInsertCalled);

        Assert.assertFalse(preUpdateCalled);
        Assert.assertFalse(posUpdateCalled);

        Assert.assertTrue(preDeleteCalled);
        Assert.assertTrue(posDeleteCalled);
    }

    @Test
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


        Assert.assertFalse(preInsertCalled);
        Assert.assertFalse(posInsertCalled);

        Assert.assertFalse(preUpdateCalled);
        Assert.assertFalse(posUpdateCalled);

        Assert.assertFalse(preDeleteCalled);
        Assert.assertFalse(posDeleteCalled);

        PersonGroup group = new PersonGroup();


        managerG.save(group);
        managerG.update(group);
        managerG.delete(group);

        Assert.assertTrue(preInsertCalled);
        Assert.assertTrue(posInsertCalled);

        Assert.assertTrue(preUpdateCalled);
        Assert.assertTrue(posUpdateCalled);

        Assert.assertTrue(preDeleteCalled);
        Assert.assertTrue(posDeleteCalled);
    }

    @Test
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

        Assert.assertNull(entity.getId());
        Assert.assertTrue(manager.list().isEmpty());
    }

    @Test
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

        Assert.assertEquals(entity.getName(), other.getName());
    }
}
