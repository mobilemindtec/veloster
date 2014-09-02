package br.com.mobilemind.veloster.sql;

/*
 * #%L
 * Veloster Framework
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

import android.content.Context;
import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.orm.core.QueryToolUtil;
import br.com.mobilemind.veloster.tools.VelosterConfig;
import java.util.logging.Level;

/**
 *
 * @author Ricardo Bocchi
 */
public class VelosterDroid {

    public static void build(Context context) {
        VelosterConfig conf = new VelosterConfig();
        conf.setDatabaseBackupHelper(new DatabaseBackupHelperDroid());
        conf.setConnectionFactory(new ConnectionFactoryImplDroid(context));
        conf.buildMe();
        QueryToolUtil.setConverterDataBaseErroCodeListener(new ConverterDataBaseErroCodeListenerImpl());

        try {
            DriverManager.getConnection().open();
            DriverManager.getConnection().isActive();
            DriverManager.getConnection().close();
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, VelosterDroid.class, e);
        }
    }

    public static void buildTestMode(Context context) {
        VelosterConfig conf = new VelosterConfig();
        conf.setTestMode(true);
        conf.setDatabaseBackupHelper(new DatabaseBackupHelperDroid());
        conf.setConnectionFactory(new ConnectionFactoryImplDroid(context));
        conf.buildMe();
        QueryToolUtil.setConverterDataBaseErroCodeListener(new ConverterDataBaseErroCodeListenerImpl());

        try {
            DriverManager.getConnection().open();
            DriverManager.getConnection().isActive();
            DriverManager.getConnection().close();
        } catch (Exception e) {
            MMLogger.log(Level.SEVERE, VelosterDroid.class, e);
        }
    }
}
