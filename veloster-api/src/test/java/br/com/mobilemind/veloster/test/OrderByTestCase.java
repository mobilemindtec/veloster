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
import br.com.mobilemind.veloster.sql.type.Criteria;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import br.com.mobilemind.veloster.teste.pack.PersonGroup;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ricardo BOcchi
 */
public class OrderByTestCase extends BaseTestCase {

  
    @Test
    public void testOrderByName() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup p1 = new PersonGroup();
        p1.setName("joselito");

        PersonGroup p2 = new PersonGroup();
        p2.setName("bruno");

        PersonGroup p3 = new PersonGroup();
        p3.setName("ataides");

        manager.save(p1);
        manager.save(p2);
        manager.save(p3);

        Criteria<PersonGroup> criteria = manager.createCriteria();

        List<PersonGroup> items = criteria.orderBy("name").list();

        Assert.assertTrue(items.size() == 3);

        Assert.assertEquals("ataides", items.get(0).getName());
        Assert.assertEquals("bruno", items.get(1).getName());
        Assert.assertEquals("joselito", items.get(2).getName());
    }

    @Test
    public void testOrderByNameAndId() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup p1 = new PersonGroup();
        p1.setName("joselito");

        PersonGroup p2 = new PersonGroup();
        p2.setName("bruno");

        PersonGroup p3 = new PersonGroup();
        p3.setName("ataides");

        manager.save(p1);
        manager.save(p2);
        manager.save(p3);

        Criteria<PersonGroup> criteria = manager.createCriteria();

        List<PersonGroup> items = criteria.orderBy("name", "id").list();

        Assert.assertTrue(items.size() == 3);

        Assert.assertEquals("ataides", items.get(0).getName());
        Assert.assertEquals("bruno", items.get(1).getName());
        Assert.assertEquals("joselito", items.get(2).getName());
    }

    @Test
    public void testOrderByNameDesc() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup p1 = new PersonGroup();
        p1.setName("ataides");

        PersonGroup p2 = new PersonGroup();
        p2.setName("joselito");

        PersonGroup p3 = new PersonGroup();
        p3.setName("bruno");

        manager.save(p1);
        manager.save(p2);
        manager.save(p3);

        Criteria<PersonGroup> criteria = manager.createCriteria();

        List<PersonGroup> items = criteria.orderByDesc("name").list();

        Assert.assertTrue(items.size() == 3);

        Assert.assertEquals("joselito", items.get(0).getName());
        Assert.assertEquals("bruno", items.get(1).getName());
        Assert.assertEquals("ataides", items.get(2).getName());
    }

    @Test
    public void testOrderByNameAndIdDesc() {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup p1 = new PersonGroup();
        p1.setName("ataides");

        PersonGroup p2 = new PersonGroup();
        p2.setName("joselito");

        PersonGroup p3 = new PersonGroup();
        p3.setName("bruno");

        manager.save(p1);
        manager.save(p2);
        manager.save(p3);

        Criteria<PersonGroup> criteria = manager.createCriteria();

        List<PersonGroup> items = criteria.orderByDesc("name", "id").list();

        Assert.assertTrue(items.size() == 3);

        Assert.assertEquals("joselito", items.get(0).getName());
        Assert.assertEquals("bruno", items.get(1).getName());
        Assert.assertEquals("ataides", items.get(2).getName());
    }
}
