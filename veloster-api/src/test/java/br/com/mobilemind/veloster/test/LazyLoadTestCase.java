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
import br.com.mobilemind.veloster.teste.pack.EntityInsertCascadeLazy;
import br.com.mobilemind.veloster.teste.pack.PersonGroup;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ricardo Bocchi
 */
public class LazyLoadTestCase extends BaseTestCase {   

    @Test
    public void testLoadEntityWithoutLazy() {
        Veloster<EntityInsertCascade> manager = VelosterRepository.getVeloster(EntityInsertCascade.class);

        EntityInsertCascade e = new EntityInsertCascade();
        e.setGrupo(new PersonGroup("aaaaa"));
        manager.save(e);


        EntityInsertCascade other = manager.load(e.getId());

        Assert.assertNotNull(other);

        Assert.assertFalse(other.getGrupo().getName().equals("aaaaa"));

        other.getGrupo().load();

        Assert.assertEquals("aaaaa", other.getGrupo().getName());
    }

    @Test
    public void testInternalLoadEntityLazy() {
        Veloster<EntityInsertCascadeLazy> manager = VelosterRepository.getVeloster(EntityInsertCascadeLazy.class);

        EntityInsertCascadeLazy e = new EntityInsertCascadeLazy();
        e.setGrupo(new PersonGroup("aaaaa"));
        manager.save(e);


        EntityInsertCascadeLazy other = manager.load(e.getId());

        Assert.assertNotNull(other);

        Assert.assertTrue(other.getGrupo().getName().equals("aaaaa"));
    }
}
