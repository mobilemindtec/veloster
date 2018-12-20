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
import br.com.mobilemind.veloster.teste.pack.Unique;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Ricardo Bocchi
 */
public class UniqueIndexTestCase extends BaseTestCase {

    @Test
    public void testCreateDuplicateValue() {

        Veloster<Unique> veloster = VelosterRepository.getVeloster(Unique.class);

        Unique unique = new Unique("unique");
        Unique unique2 = new Unique("unique");

        veloster.save(unique);
        veloster.save(unique2);

        
        Assert.assertTrue(veloster.count() == 1);

    }
}
