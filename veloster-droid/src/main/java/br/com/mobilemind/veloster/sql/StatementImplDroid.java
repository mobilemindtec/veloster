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
import android.database.sqlite.SQLiteDatabase;
import br.com.mobilemind.api.utils.ClassUtil;
import br.com.mobilemind.veloster.exceptions.VelosterException;
import br.com.mobilemind.veloster.tools.VelosterResource;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Ricardo Bocchi
 */
public class StatementImplDroid implements Statement {

    private SimpleDateFormat format;
    private ArrayList values;
    private String query;
    private DataHelper helper;

    private SQLiteDatabase getSQLIteDataBase() {
        return this.helper.getSQLiteDatabase();
    }

    public StatementImplDroid(DataHelper helper) {
        this(helper, null);
    }

    public StatementImplDroid(DataHelper helper, String query) {
        this.query = query;
        this.helper = helper;
        this.values = new ArrayList();                
    }

    @Override
    public Statement setQuery(String string) {
        this.query = string;
        return this;
    }

    @Override
    public String getQuery() {
        return this.query;
    }

    @Override
    public boolean executeNonQuery() throws SQLException {
        int dbVersion = Integer.parseInt(VelosterResource.getProperty("br.com.mobilemind.android.dataBaseVersion"));
        this.getSQLIteDataBase().setVersion(dbVersion);
        this.getSQLIteDataBase().setLocale(Locale.getDefault());
        this.getSQLIteDataBase().setLockingEnabled(true);
        this.getSQLIteDataBase().execSQL(query);
        this.getSQLIteDataBase().setLockingEnabled(false);
        return true;
    }

    @Override
    public void executeUpdate() throws SQLException {
        this.getSQLIteDataBase().execSQL(query, values.toArray());
    }

    @Override
    public int executeInsert(boolean getRowId) throws SQLException {
        SQLiteDatabase sqlite = this.getSQLIteDataBase();
        sqlite.execSQL(query, values.toArray());

        if (getRowId) {
            android.database.Cursor cursor = sqlite.rawQuery("SELECT last_insert_rowid()", new String[]{});

            if (cursor.moveToNext()) {
                return cursor.getInt(0);
            }
            throw new VelosterException("error get last insert rowid");
        }
        return 0;
    }

 

    @Override
    public ResultSet executeQuery() throws SQLException {
        String[] selectArgs = new String[this.values.size()];
        int i = 0;
        for (Object object : values) {
            if (ClassUtil.isBoolean(object)) {
                selectArgs[i++] = Boolean.valueOf(object.toString()) ? "1" : "0";
            } else {
                selectArgs[i++] = object == null ? null : object.toString();
            }
        }
        return new ResultSetImplDroid(this.getSQLIteDataBase().rawQuery(query, selectArgs));
    }

    @Override
    public void setDataFormat(SimpleDateFormat sdf) {
        this.format = sdf;
    }

    @Override
    public Statement setInteger(int pos, Integer value) throws SQLException {
        this.values.add(value);
        return this;
    }

    @Override
    public Statement setString(int pos, String value) throws SQLException {
        this.values.add(value);
        return this;
    }

    @Override
    public Statement setDouble(int pos, Double value) throws SQLException {
        this.values.add(value);
        return this;
    }

    @Override
    public Statement setLong(int pos, Long value) throws SQLException {
        this.values.add(value);
        return this;
    }

    @Override
    public Statement setBoolean(int pos, Boolean value) throws SQLException {
        this.values.add(value);
        return this;
    }

    @Override
    public Statement setDate(int pos, Date date) throws SQLException {
        this.values.add(this.format.format(date));
        return this;
    }
}
