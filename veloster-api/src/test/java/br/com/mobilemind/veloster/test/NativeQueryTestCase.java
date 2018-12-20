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
import br.com.mobilemind.veloster.teste.pack.PersonGroup;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author Ricardo Bocchi
 */
public class NativeQueryTestCase extends BaseTestCase {  

    @Test
    public void testExecuteNativeDelete() throws Exception {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();

        manager.save(group);

        manager.executeNativeQuery("delete from PersonsGroups where id = " + group.getId());

        group = manager.load(group.getId());

        Assert.assertNull(group);

    }

    @Test
    public void testExecuteNativeSelect() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);

        for (int i = 0; i < 10; i++) {
            manager.save(new PersonGroup("ID='" + i + "'"));
        }

        Assert.assertEquals(10, manager.list().size());

        List<Object[]> items = manager.executeNativeSelect("select id, name from PersonsGroups");

        int count = 0;
        for (Object[] it : items) {
            Assert.assertEquals(2, it.length);

            int id = Integer.parseInt(it[0].toString());
            String name = it[1].toString();
            Assert.assertTrue(id > 0);
            Assert.assertEquals("ID='" + (count++) + "'", name);

            System.out.println("id=" + id + "   name=" + name);
        }
    }

    @Test
    public void testExecuteNativeDeleteWithParams() {
        throw new NotImplementedException();
    }

    @Test
    public void testExecuteNativeSelectWithParams() {
        throw new NotImplementedException();
    }

    @Test
    public void testExecuteNativeSelectWithResultTransformerThatReturnList() {
        throw new NotImplementedException();
    }

    @Test
    public void testExecuteNativeSelectWithResultTransformerThatReturnSingleResult() {
        throw new NotImplementedException();
    }

    @Test
    public void testExecuteNativeSelectWithResultTransformerWithEnum() {
        throw new NotImplementedException();
    }

    @Test
    public void testExecuteNativeSelectWithResultTransformerWithLazy() {
        throw new NotImplementedException();
    }
}
