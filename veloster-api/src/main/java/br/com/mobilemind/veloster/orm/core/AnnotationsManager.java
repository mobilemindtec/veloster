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


import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.orm.annotations.Column;
import br.com.mobilemind.veloster.orm.model.ColumnWrapper;
import br.com.mobilemind.veloster.orm.model.TableImpl;
import br.com.mobilemind.api.utils.ClassUtil;
import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.exceptions.VelosterException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author Ricardo Bocchi
 */
public class AnnotationsManager<T extends Entity> {

    private final static Map<String, Object[]> CACHE = new HashMap<String, Object[]>();
    private Class<T> clazz;
    private final String key;

    public AnnotationsManager(Class<T> clazz) {
        this.clazz = clazz;
        this.key = this.clazz.getName();
        this.initFieldsAnnotations(this.clazz);
    }

    public TableImpl<T> getTable() {
        return (TableImpl<T>) CACHE.get(key)[0];
    }

    public List<ColumnWrapper> getFields() {
        return (List<ColumnWrapper>) CACHE.get(key)[1];
    }

    private void initFieldsAnnotations(Class clazz) {

        if (!CACHE.containsKey(key)) {
            List<ColumnWrapper> columns = new LinkedList<ColumnWrapper>();
            List<Field> fs = ClassUtil.getAnnotatedsFields(clazz, Column.class);
            TableImpl<T> table = new TableImpl<T>(clazz);
            
            MMLogger.log(Level.INFO, getClass(), "### class " + clazz.getName() + " - " + table);
                
            
            for (Field field : fs) {
                columns.add(new ColumnWrapper(field, table));
            }

            if (columns.isEmpty()) {
                throw new VelosterException("not found annotated field in " + table.getTableClass().getSimpleName());
            }
            CACHE.put(key, new Object[]{table, columns});
        }
    }
}
