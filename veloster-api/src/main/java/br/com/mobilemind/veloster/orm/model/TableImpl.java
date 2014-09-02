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




import br.com.mobilemind.veloster.orm.annotations.Table;
import java.lang.annotation.Annotation;
import java.security.InvalidParameterException;

/**
 *
 * @author Ricardo Bocchi
 */
public class TableImpl<T extends Entity> implements Table {

    private Table table;
    private Class<T> clazz;
    private String name;

    public TableImpl(Class<T> clazz) {
        this.table = clazz.getAnnotation(Table.class);
        this.clazz = clazz;

        if (table == null) {
            throw new InvalidParameterException("table can't be null. Annotation @Table not found in " + clazz.getSimpleName());
        }

        init();
    }

    @Override
    public String name() {
        return this.name;
    }
    
    private void init() {
        if ("".equals(this.table.name())) {
            this.name = this.clazz.getSimpleName();
        } else {
            this.name = this.table.name();
        }
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return this.table.annotationType();
    }
    
    public Class getTableClass(){
        return this.clazz;
    }
}
