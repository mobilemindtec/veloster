package br.com.mobilemind.veloster.orm.model;

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
import br.com.mobilemind.veloster.orm.core.EntityReferenceLoaderImpl;
import br.com.mobilemind.veloster.tools.VelosterRepository;

/**
 *
 * @author Ricardo Bocchi
 */
public abstract class EntityLazy<T extends Entity> implements Entity<T> {

    private final Class<T> clazz;
    private EntityReferenceLoader entityReferenceLoader = new EntityReferenceLoaderImpl();
    private boolean loaded;

    public EntityLazy() {
        this(null);
    }

    public EntityLazy(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public abstract Long getId();

    @Override
    public abstract void setId(Long id);

    @Override
    public boolean isLoaded() {
        return this.loaded;
    }

    @Override
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    @Override
    public boolean isPersisted() {
        return this.getId() != null && this.getId() > 0;
    }

    @Override
    public <E extends Entity> E lazy(String fieldName) {
        return this.entityReferenceLoader.lazyLoad(this, fieldName);
    }

    @Override
    public T load() {
        Class cls = clazz != null ? clazz : getClass();

        if (!isPersisted()) {
            throw new VelosterException("fail load in entity not persisted. entity [" + cls.getName() + "]");
        }

        Veloster<T> manager = VelosterRepository.getVeloster(cls);

        if (manager == null) {
            throw new VelosterException("ORMManager not fount to  entity [" + cls.getName() + "]");
        }

        if (this.isLoaded()) {
            return (T) this;
        }

        T entity = manager.load((T) this);
        if (entity != null) {
            entity.setLoaded(true);
        }
        return entity;
    }
}
