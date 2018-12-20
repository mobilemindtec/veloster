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


import java.security.InvalidParameterException;

/**
 *
 * @author Ricardo Bocchi
 */
public enum PersonType {
    PERSON_1 (1, "Person One"),
    PERSON_2 (2, "Person Two");
    
    private int id;
    private String name;
    
    private PersonType(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }    
    
    @Override
    public String toString() {
        return this.name;        
    }
    
    public static PersonType parse(int i){
        PersonType[] values = values();
        for (PersonType t : values) {
            if(t.getId() == i){
                return t;
            }
        }
        throw new InvalidParameterException("enum number " + i + " not found");
    }    
}
