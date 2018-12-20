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
import br.com.mobilemind.api.utils.ClassUtil;
import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.orm.annotations.JoinColumn;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import br.com.mobilemind.veloster.tools.VelosterResource;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 * @author Ricardo Bocchi
 */
public class DDLProcessor {

    private static final Set<Class> DDL_PROCESSED = new HashSet<Class>();
    private static String DDL_STRATEGY; //create | update | none
    private static boolean UNDEFINED;

    public <T extends Entity> void process(Veloster<T> veloster, Class<T> entityClazz) {
        if (!UNDEFINED && !DDL_PROCESSED.contains(entityClazz)) {

            DDL_PROCESSED.add(entityClazz);

            if (DDL_STRATEGY == null) {
                DDL_STRATEGY = VelosterResource.getProperty("br.com.mobilemind.db.ddl");
            }

            if (DDL_STRATEGY == null) {
                MMLogger.log(Level.INFO, getClass(), "property br.com.mobilemind.db.ddl undefined");
                UNDEFINED = true;
            }

            if (UNDEFINED) {
                return;
            }
            MMLogger.log(Level.INFO, getClass(), "property br.com.mobilemind.db.ddl defined as " + DDL_STRATEGY);

            if ("create".equals(DDL_STRATEGY)) {
                if (!veloster.tableExists()) {
                    resolveDependency(veloster, entityClazz);
                    veloster.tableCreate();
                }
            } else if ("update".equals(DDL_STRATEGY)) {
                resolveDependency(veloster, entityClazz);
                if (!veloster.tableExists()) {
                    veloster.tableCreate();
                } else {
                    veloster.tableUpdate();
                }
            }
        }
    }

    public static void clear() {
        DDL_PROCESSED.clear();
        DDL_STRATEGY = null;
        UNDEFINED = false;
    }

    /**
     * resolv table dependency
     *
     * @param <T>
     * @param veloster
     * @param entityClazz
     */
    private <T extends Entity> void resolveDependency(Veloster<T> veloster, Class<T> entityClazz) {
        List<Field> items = ClassUtil.getAnnotatedsFields(entityClazz, JoinColumn.class);

        if (!items.isEmpty()) {
            for (Field it : items) {
                if (ClassUtil.isAssignableFrom(it.getType(), Entity.class)) {
                    MMLogger.log(Level.INFO, getClass(), "dependency " + it.getName() + " to " + entityClazz.getName());
                    VelosterRepository.getVeloster0(it.getType());
                }
            }
        }
    }
}
