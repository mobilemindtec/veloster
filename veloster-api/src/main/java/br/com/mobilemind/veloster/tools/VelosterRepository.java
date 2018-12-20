package br.com.mobilemind.veloster.tools;

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

import br.com.mobilemind.veloster.orm.QueryExecutor;
import br.com.mobilemind.veloster.orm.QueryFormatter;
import br.com.mobilemind.veloster.orm.QueryStatementBuilder;
import br.com.mobilemind.veloster.orm.EntityValidator;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.orm.QueryBuilder;
import br.com.mobilemind.veloster.exceptions.VelosterException;
import br.com.mobilemind.veloster.extra.DatabaseBackupHelper;
import br.com.mobilemind.veloster.orm.core.AnnotationsManager;
import br.com.mobilemind.veloster.orm.core.DynamicFinderProcessor;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.orm.core.DDLProcessor;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * QueryTools repository
 *
 * @author Ricardo Bocchi
 */
public class VelosterRepository {

    private static Map<Class, VelosterBox> VELOSTER_CACHE = new HashMap<Class, VelosterBox>();
    private static Map<Class, Object> FINDERS_CACHE = new HashMap<Class, Object>();
    private static List<VelosterCreatorListener> CREATE_LISTENERS = new LinkedList<VelosterCreatorListener>();

    /**
     * create global listener
     *
     * @param listener
     */
    public static void addORMManagerCreateListener(VelosterCreatorListener listener) {
        CREATE_LISTENERS.add(listener);
    }

    /**
     * remove global listener
     *
     * @param listener
     */
    public static void removeORMManagerCreateListener(VelosterCreatorListener listener) {
        CREATE_LISTENERS.remove(listener);
    }

    /**
     * get orm control to entity class
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T extends Entity> VelosterBox<T> getVelosterControll(Class<T> clazz) {
        if (!VELOSTER_CACHE.containsKey(clazz)) {
            VELOSTER_CACHE.put(clazz, load(clazz));
        }
        return VELOSTER_CACHE.get(clazz);
    }

    /**
     * get data base backup tool
     *
     * @return
     */
    public static DatabaseBackupHelper getDatabaseBackupHelper() {
        return VelosterConfig.getConf().getDatabaseBackupHelper();
    }

    /**
     * return new instance of Veloster<T>
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T extends Entity> Veloster<T> getVeloster(Class<T> clazz) {
        return load(clazz).getManager();
    }

    /**
     * return new instance of Veloster<T>
     *
     * @param clazz
     * @return
     *
     */
    public static Veloster getVeloster0(Class clazz) {
        return load(clazz).getManager();
    }

    /**
     * return shared (cached control) instance of Veloster<T>. Not use with
     * threads
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T extends Entity> Veloster<T> getSharedVeloster(Class<T> clazz) {
        return getVelosterControll(clazz).getManager();
    }

    /**
     * get dynamic finder
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T> T getDynamicFinder(Class<T> proxyInterface, Class<? extends Entity> entityClass) {

        if (!FINDERS_CACHE.containsKey(proxyInterface)) {
            DynamicFinderProcessor handler = new DynamicFinderProcessor(entityClass);
            Class[] interfacesArray = new Class[]{proxyInterface};
            T finder = (T) Proxy.newProxyInstance(proxyInterface.getClassLoader(), interfacesArray, handler);
            FINDERS_CACHE.put(proxyInterface, finder);
        }

        return (T) FINDERS_CACHE.get(proxyInterface);
    }

    /**
     * create orm control to entity
     *
     * @param <T>
     * @param clazz
     * @return
     */
    private static <T extends Entity> VelosterBox<T> load(Class<T> clazz) {
        VelosterBox<T> controll = new VelosterBox<T>();
        AnnotationsManager annotationsManager = null;
        EntityValidator validator = null;
        QueryFormatter formatter = null;
        QueryBuilder builder = null;
        QueryStatementBuilder statementBuilder = null;
        QueryExecutor executor = null;
        Veloster veloster = null;

        try {
            annotationsManager = new AnnotationsManager(clazz);
            controll.setAnnotationsManager(annotationsManager);
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, VelosterRepository.class, e);
            throw new VelosterException("error create AnnotationsManager. Message: " + e.getMessage());
        }

        try {
            validator = VelosterConfig.getConf().getEntityValidator().newInstance();
            validator.build(clazz);
            controll.setEntityValidator(validator);
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, VelosterRepository.class, e);
            throw new VelosterException("error create EntityValidator. Message: " + e.getMessage());
        }

        try {
            formatter = VelosterConfig.getConf().getFormatter().newInstance();
            controll.setFormatter(formatter);
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, VelosterRepository.class, e);
            throw new VelosterException("error create QueryFormatter. Message: " + e.getMessage());
        }

        try {
            builder = VelosterConfig.getConf().getQueryBuilder().newInstance();
            builder.build(clazz, annotationsManager, formatter, VelosterConfig.getConf().getDialect());
            controll.setQueryBuilder(builder);
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, VelosterRepository.class, e);
            throw new VelosterException("error create QueryStatementBuilder. Message: " + e.getMessage());
        }

        try {
            statementBuilder = VelosterConfig.getConf().getQueryStatementBuilder().newInstance();
            statementBuilder.build(clazz, builder, annotationsManager);
            controll.setQueryStatementBuilder(statementBuilder);
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, VelosterRepository.class, e);
            throw new VelosterException("error create QueryBuilder. Message: " + e.getMessage());
        }

        try {
            executor = VelosterConfig.getConf().getQueryExecutor().newInstance();
            executor.build(clazz, builder, executor, statementBuilder, validator);
            controll.setQueryExecutor(executor);
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, VelosterRepository.class, e);
            throw new VelosterException("error create QueryExecutor. Message: " + e.getMessage());
        }

        try {
            veloster = VelosterConfig.getConf().getManager().newInstance();
            veloster.build(clazz, builder, executor, statementBuilder, validator);
            controll.setManager(veloster);
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, VelosterRepository.class, e);
            throw new VelosterException("error create Veloster. Message: " + e.getMessage());
        }

        for (VelosterCreatorListener event : CREATE_LISTENERS) {
            event.created(controll.getManager());
        }

        return controll;
    }

    /**
     * clear list of orm control
     *
     */
    public static void reset() {
        VELOSTER_CACHE.clear();
        FINDERS_CACHE.clear();
        DDLProcessor.clear();
        CREATE_LISTENERS.clear();
    }
}
