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
import br.com.mobilemind.api.droidutil.logs.AppLogger;
import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.exceptions.VelosterException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.text.SimpleDateFormat;

/**
 *
 * @author Ricardo Bocchi
 */
public class ConnectionImplDroid implements Connection {

    private DataHelper helper;
    private boolean trans;
    private List<String> pragmas;
    private SQLiteDatabase data;
    private int pool;
    static long POOL_TIME = 0;
    static final Object SYNC = new Object();
    static Timer TIMER;
    private SimpleDateFormat format;

    public ConnectionImplDroid(DataHelper helper) {
        this(helper, null);
    }

    public ConnectionImplDroid(DataHelper helper, SimpleDateFormat format) {
        this.helper = helper;
        this.pragmas = new LinkedList<String>();
        this.format = format;
    }

    private void timePool() {
        POOL_TIME = System.currentTimeMillis();


        if (TIMER == null) {
            TIMER = new Timer(true);
            TIMER.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (SYNC) {
                        long time = System.currentTimeMillis() - POOL_TIME;
                        AppLogger.info(getClass(), "###### timer pool called");
                        if (time > 120000) {
                            try {
                                AppLogger.info(getClass(), "###### close connetion");
                                helper.getSQLiteDatabase().close();
                            } catch (Exception e) {
                                AppLogger.error(getClass(), e);
                            }
                        } else {
                            AppLogger.info(getClass(), "###### open connetion");
                        }
                    }
                }
            }, 30000);
            AppLogger.info(getClass(), "###### timer running");
        }
    }

    @Override
    public void commit() throws SQLException {
        synchronized (SYNC) {
            if (!isActive()) {
                throw new SQLException("connection is closed");
            }

            if (!trans) {
                throw new VelosterException("commig can't be used in non transactional operations. use setCommitTrans to true");
            }

            if (pool == 0) {
                throw new VelosterException("connection pool can't be zero on commit. you called begin in start operation?");
            }

            pool--;

            MMLogger.log(Level.INFO, getClass(), "commit say: connection pool is [" + pool + "]");

            if (pool == 0) {
                SQLiteDatabase sqlite = this.helper.getSQLiteDatabase();
                sqlite.setTransactionSuccessful();
                sqlite.endTransaction();
            }
            timePool();
        }
    }

    @Override
    public void rollback() throws SQLException {
        synchronized (SYNC) {
            pool = 0;

            if (isActive()) {
                this.helper.getSQLiteDatabase().endTransaction();
            }
            timePool();
        }
    }

    @Override
    public void open() throws SQLException {
        synchronized (SYNC) {
            timePool();
        }
    }

    @Override
    public void close() throws SQLException {
        synchronized (SYNC) {
//            if (isActive()) {
//                if (trans) {
//                    if (pool == 0) {
//                        this.helper.getSQLiteDatabase().close();
//                    }
//                } else {
//                    this.helper.getSQLiteDatabase().close();
//                }
//            }
//            MMLogger.log(Level.INFO, getClass(), "close say: connection pool is [" + pool + "]");

            if (trans && pool == 0) {
                trans = false;
            }
            timePool();
        }
    }

    @Override
    public void begin() throws SQLException {
        synchronized (SYNC) {
            if (!isActive()) {
                throw new SQLException("connection is closed");
            }

            if (pool == 0) {
                this.helper.getSQLiteDatabase().beginTransaction();
            }

            if (trans) {
                pool++;
            }
            timePool();
        }
    }

    @Override
    public boolean isActive() throws SQLException {
        synchronized (SYNC) {
            SQLiteDatabase sqlite = this.helper.getSQLiteDatabase();
            timePool();
            return sqlite != null && sqlite.isOpen();
        }
    }

    @Override
    public boolean isClosed() {
        synchronized (SYNC) {
            SQLiteDatabase sqlite = this.helper.getSQLiteDatabase();
            timePool();
            return sqlite == null || !sqlite.isOpen();
        }
    }

    @Override
    public void setCommitTrans(boolean bln) {
        this.trans = bln;
    }

    @Override
    public void addAfterConnection(String string) {
        this.pragmas.add(string);
    }

    @Override
    public Statement prepare(String string, boolean isInsert) throws SQLException {
        return new StatementImplDroid(helper, string, this.format);
    }

    public void setDataFormat(SimpleDateFormat format) {
        this.format = format;
    }     
}
