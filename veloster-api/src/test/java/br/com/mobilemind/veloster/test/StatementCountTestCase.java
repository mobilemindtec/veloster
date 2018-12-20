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
import br.com.mobilemind.veloster.sql.type.Eq;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import br.com.mobilemind.veloster.teste.pack.PersonGroup;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ricardo Bocchi
 */
public class StatementCountTestCase extends BaseTestCase {

    @Test
    public void testCountPersonGroup() throws Exception {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();
        PersonGroup group2 = new PersonGroup();


        manager.save(group);

        manager.save(group2);


        int count = manager.count();

        Assert.assertTrue("count expected > 0 but is " + count, count == 2);

        List<PersonGroup> items = manager.list();

        Assert.assertTrue("list size expected " + count + " but is " + items.size(), items.size() == 2);

        for (PersonGroup p : items) {
            manager.delete(p);
        }

        items = manager.list();

        Assert.assertTrue("list size expected 0 but is " + items.size(), items.size() == 0);
    }

    @Test
    public void testCountByCriteriaPersonGroup() throws Exception {
        Veloster<PersonGroup> manager = VelosterRepository.getVeloster(PersonGroup.class);
        PersonGroup group = new PersonGroup();
        PersonGroup group2 = new PersonGroup();

        manager.save(group);

        manager.save(group2);

        Criteria criteria = manager.createCriteria();
        criteria.add("id", new Eq(group.getId()));

        int count = criteria.count();

        Assert.assertTrue("count expected > 0 but is " + count, count == 1);

        List<PersonGroup> items = manager.list();

        Assert.assertTrue("list size expected " + 2 + " but is " + items.size(), items.size() == 2);

        for (PersonGroup p : items) {
            manager.delete(p);
        }

        items = manager.list();

        Assert.assertTrue("list size expected 0 but is " + items.size(), items.size() == 0);
    }
}
