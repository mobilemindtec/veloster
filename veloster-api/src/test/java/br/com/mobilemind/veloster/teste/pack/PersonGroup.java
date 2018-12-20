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
import br.com.mobilemind.veloster.orm.validator.Length;
import br.com.mobilemind.veloster.orm.validator.NotNull;
import br.com.mobilemind.veloster.orm.annotations.Table;

/**
 *
 * @author Ricardo Bocchi
 */
@Table(name = "PersonsGroups")
public class PersonGroup extends EntityImpl {

    @Column(length = 100)
    @NotNull
    @Length(max = 10, min = 5)
    private String name;

    public PersonGroup() {
        this("Group One");
    }

    public PersonGroup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getId() + " - " + this.getName();
    }
}
