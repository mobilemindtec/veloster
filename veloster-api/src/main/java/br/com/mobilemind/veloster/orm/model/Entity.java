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

import java.io.Serializable;

/**
 *
 *
 * @author Ricardo Bocchi
 */
public interface Entity<T> extends Serializable {

    Long getId();

    void setId(Long id);

    boolean isLoaded();

    boolean isPersisted();

    void setLoaded(boolean loaded);

     <E extends Entity> E lazy(String fieldName);

    T load();
}
