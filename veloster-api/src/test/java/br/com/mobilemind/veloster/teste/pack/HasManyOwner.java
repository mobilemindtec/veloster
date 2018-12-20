package br.com.mobilemind.veloster.teste.pack;

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

import br.com.mobilemind.veloster.orm.annotations.Column;
import br.com.mobilemind.veloster.orm.annotations.HasMany;
import br.com.mobilemind.veloster.orm.annotations.Table;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ricardo Bocchi
 */
@Table
public class HasManyOwner extends EntityImpl {

    @Column
    private String name;
    @Column
    @HasMany(reference = "hasManyOwner", cascadeAll = true)
    private List<HasManyReference> list = new LinkedList<HasManyReference>();

    public HasManyOwner() {
    }

    public HasManyOwner(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HasManyReference> getList() {
        return list;
    }

    public void setList(List<HasManyReference> list) {
        this.list = list;
    }
}
