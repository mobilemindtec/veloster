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
import br.com.mobilemind.veloster.teste.pack.HasManyOwner;
import br.com.mobilemind.veloster.teste.pack.HasManyReference;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Ricardo Bocchi
 */
public class HasManyCascadeTestCase extends BaseTestCase {

    @Test
    public void testSaveListOnInsert() {

        HasManyOwner ownerInsert = new HasManyOwner("ownerInsert");
        ownerInsert.getList().add(new HasManyReference("ReferenceInsert 1"));
        ownerInsert.getList().add(new HasManyReference("ReferenceInsert 2"));
        ownerInsert.getList().add(new HasManyReference("ReferenceInsert 3"));
        ownerInsert.getList().add(new HasManyReference("ReferenceInsert 4"));

        Veloster<HasManyOwner> veloster = VelosterRepository.getVeloster(HasManyOwner.class);
        veloster.save(ownerInsert);

        ownerInsert = veloster.load(ownerInsert.getId());

        Assert.assertNotNull(ownerInsert);
        int size = ownerInsert.getList().size();
        Assert.assertTrue(size == 4);

        veloster.delete(ownerInsert);

        ownerInsert = veloster.load(ownerInsert.getId());
        Assert.assertNull(ownerInsert);

        Veloster<HasManyReference> velosterReference = VelosterRepository.getVeloster(HasManyReference.class);
        Assert.assertTrue(velosterReference.list().isEmpty());

    }
}
