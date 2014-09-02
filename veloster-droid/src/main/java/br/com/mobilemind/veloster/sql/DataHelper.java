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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.tools.VelosterConfig;
import br.com.mobilemind.veloster.tools.VelosterResource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author Ricardo Bocchi
 */
public class DataHelper {

    private int dataBaseVerion;
    private Context context;
    private SQLiteDatabase db;
    private OpenHelper openHelper;
    private String dbName;
    private String dbTestName;
    private SQLiteDatabase data;
    private static List<DatabaseHelperListener> LISTENERS = new ArrayList<DatabaseHelperListener>();

    public DataHelper(Context context) {
        this.context = context;
        this.dbName = VelosterResource.getProperty("br.com.mobilemind.db.name");
        this.dbTestName = VelosterResource.getProperty("br.com.mobilemind.db.testName");
        this.dataBaseVerion = Integer.parseInt(VelosterResource.getProperty("br.com.mobilemind.android.dataBaseVersion"));

        MMLogger.log(Level.INFO, getClass(), "database name [" + dbName + "]");
        MMLogger.log(Level.INFO, getClass(), "database test name [" + dbTestName + "]");
    }

    public void addDatabaseHelperListener(DatabaseHelperListener event) {
        LISTENERS.add(event);
    }

    public void removeDatabaseHelperListener(DatabaseHelperListener event) {
        LISTENERS.remove(event);
    }

    public SQLiteDatabase getSQLiteDatabase() {
        if (data == null || !data.isOpen()) {
            String db = null;
            if (VelosterConfig.getConf().isTestMode()) {
                db = dbTestName;
                MMLogger.log(Level.INFO, getClass(), "##############################");
                MMLogger.log(Level.INFO, getClass(), "Application in test mode");
                MMLogger.log(Level.INFO, getClass(), "##############################");
            } else {
                db = dbName;
            }
            this.openHelper = new OpenHelper(context, db, this.dataBaseVerion);
            data = this.openHelper.getWritableDatabase();
        }
        return data;
    }

    public String getDbName() {
        return dbName;
    }

    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context, String dataBaseName, int dataBaseVersion) {
            super(context, dataBaseName, null, dataBaseVersion);

            for (DatabaseHelperListener event : LISTENERS) {
                event.onInstanceCreate(context, dataBaseName, dataBaseVersion);
            }
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for (DatabaseHelperListener event : LISTENERS) {
                event.onCreate(db);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            for (DatabaseHelperListener event : LISTENERS) {
                event.onUpgrade(db, oldVersion, newVersion);
            }
        }
    }
}