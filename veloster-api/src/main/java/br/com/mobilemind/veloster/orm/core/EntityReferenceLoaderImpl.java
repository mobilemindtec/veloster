package br.com.mobilemind.veloster.orm.core;

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

import br.com.mobilemind.veloster.exceptions.VelosterException;
import br.com.mobilemind.veloster.orm.EntityReferenceLoader;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.orm.annotations.BatchSize;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import br.com.mobilemind.api.utils.ClassUtil;
import br.com.mobilemind.api.utils.log.MMLogger;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.logging.Level;

/**
 *
 * @author Ricardo Bocchi
 */
public class EntityReferenceLoaderImpl implements EntityReferenceLoader {

    @Override
    public <T extends Entity> T lazyLoad(Entity source, String fieldName) {
        Class clazz = source.getClass();
        Veloster orm = VelosterRepository.getVeloster(clazz);

        if (orm == null) {
            throw new VelosterException("ORMManager not fount to entity [" + clazz.getName() + "]");
        }

        Field field = ClassUtil.getField(clazz, fieldName);

        if (field == null) {
            throw new VelosterException("Field [" + fieldName + "] not fount in entity [" + clazz.getName() + "]");
        }

        field.setAccessible(true);
        Object value = null;
        
        try {
            value = field.get(source);
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, getClass(), e);
            throw new VelosterException("error extract value in field [" + fieldName + "] to entity [" + clazz.getName() + "]");
        }
        
        if(value == null){
            return null;
        }
        
        if (!(value instanceof Entity)) {
            throw new VelosterException("value to field [" + fieldName + "] in entity [" + clazz.getName() + "] must extends Entity");
        }

        Entity entity = (Entity) value;

        if (!entity.isPersisted()) {
            return null;
        }

        if (entity.isLoaded()) {
            return (T) entity;
        }

        T item = null;

        if (ClassUtil.isAssignableFrom(field.getType(), Collection.class)) {
            BatchSize batchSize = field.getAnnotation(BatchSize.class);
            if (batchSize != null) {
            }
        } else {
            item = (T) entity.load();
        }
        item.setLoaded(true);
        return item;
    }
}
