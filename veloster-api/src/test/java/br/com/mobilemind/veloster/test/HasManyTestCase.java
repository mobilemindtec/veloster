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
import br.com.mobilemind.veloster.teste.pack.LazyListEntity;
import br.com.mobilemind.veloster.teste.pack.LazyListReference;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import java.util.LinkedList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Ricardo Bocchi
 */
public class HasManyTestCase extends BaseTestCase {

    @Test
    public void testLoadHasMany() {

        Veloster<LazyListEntity> velosterEntity = VelosterRepository.getVeloster0(LazyListEntity.class);
        Veloster<LazyListReference> velosterReference = VelosterRepository.getVeloster0(LazyListReference.class);

        LazyListEntity entity = new LazyListEntity("value abc");
        velosterEntity.save(entity);

        List<LazyListReference> list = new LinkedList<LazyListReference>();

        for (int i = 0; i < 10; i++) {
            list.add(new LazyListReference("value " + i, entity));
        }

        for (LazyListReference it : list) {
            velosterReference.save(it);
        }

        LazyListEntity e = velosterEntity.load(entity.getId());

        List<LazyListReference> items = e.getList();
        Assert.assertTrue(items.size() == 10);
        for (int i = 0; i < 10; i++) {
            Assert.assertTrue(items.get(i).getValue().equals("value " + i));
        }

    }
}
