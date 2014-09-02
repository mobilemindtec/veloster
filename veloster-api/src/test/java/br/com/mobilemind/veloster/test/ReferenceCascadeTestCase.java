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
import br.com.mobilemind.veloster.tools.VelosterRepository;
import br.com.mobilemind.veloster.teste.pack.EntityInsertCascade;
import br.com.mobilemind.veloster.teste.pack.EntityUpdateCascade;
import br.com.mobilemind.veloster.teste.pack.PersonGroup;
import junit.framework.Assert;
import org.junit.Test;

public class ReferenceCascadeTestCase extends BaseTestCase {

    @Test
    public void testSaveReference() {
        Veloster<EntityInsertCascade> managere = VelosterRepository.getVeloster(EntityInsertCascade.class);
        Veloster<PersonGroup> managerg = VelosterRepository.getVeloster(PersonGroup.class);

        Assert.assertTrue(managerg.list().isEmpty());

        EntityInsertCascade e = new EntityInsertCascade();
        e.setGrupo(new PersonGroup());

        String name = e.getGrupo().getName();

        managere.save(e);

        Assert.assertNotNull(managere.load(e.getId()));

        Assert.assertFalse(managerg.list().isEmpty());

        PersonGroup other = managerg.load(e.getGrupo().getId());

        Assert.assertNotNull(other);
        Assert.assertEquals(name, other.getName());

        e.getGrupo().setName("name two");

        managere.update(e);

        other = managerg.load(e.getGrupo().getId());

        Assert.assertNotNull(other);
        Assert.assertEquals(name, other.getName());
    }
    
    @Test
    public void testUpdateReference() {
        Veloster<EntityUpdateCascade> managere = VelosterRepository.getVeloster(EntityUpdateCascade.class);
        Veloster<PersonGroup> managerg = VelosterRepository.getVeloster(PersonGroup.class);

        Assert.assertTrue(managerg.list().isEmpty());

        EntityUpdateCascade e = new EntityUpdateCascade();
        e.setGrupo(new PersonGroup());

        String name = e.getGrupo().getName();

        managere.save(e);

        Assert.assertNotNull(managere.load(e.getId()));

        Assert.assertFalse(managerg.list().isEmpty());

        PersonGroup other = managerg.load(e.getGrupo().getId());

        Assert.assertNotNull(other);
        Assert.assertEquals(name, other.getName());

        e.getGrupo().setName("name two");

        managere.update(e);

        other = managerg.load(e.getGrupo().getId());

        Assert.assertNotNull(other);
        Assert.assertEquals("name two", other.getName());
    }
}
