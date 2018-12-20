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

import android.os.Environment;
import br.com.mobilemind.api.droidutil.logs.AppLogger;
import br.com.mobilemind.api.droidutil.tools.SdCardTools;
import br.com.mobilemind.api.utils.MobileMindUtil;
import br.com.mobilemind.veloster.extra.BackupException;
import br.com.mobilemind.veloster.extra.BackupInfo;
import br.com.mobilemind.veloster.extra.DatabaseBackupHelper;
import br.com.mobilemind.veloster.tools.VelosterConfig;
import br.com.mobilemind.veloster.tools.VelosterResource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Ricardo Bocchi
 */
public class DatabaseBackupHelperDroid implements DatabaseBackupHelper {

    private String dbName;
    private String dbTestName;
    private String dbBackupPath;
    private String dbBackupSufixFormat;
    private String applicationPakage;
    private String dbLocation;
    private DateFormat dateFormat;
    private final String sufix = ".sqlite";

    public DatabaseBackupHelperDroid() {
        init();
    }

    private void init() {
        this.dbName = VelosterResource.getProperty("br.com.mobilemind.db.name");
        this.dbTestName = VelosterResource.getProperty("br.com.mobilemind.db.testName");
        this.dbBackupPath = VelosterResource.getProperty("br.com.mobilemind.db.backupPath");
        this.dbBackupSufixFormat = VelosterResource.getProperty("br.com.mobilemind.db.backupSufixFormat");
        this.applicationPakage = VelosterResource.getProperty("br.com.mobilemind.android.applicationPakage");
        this.dbLocation = VelosterResource.getProperty("br.com.mobilemind.android.db.Location");

        if (MobileMindUtil.isNullOrEmpty(dbName)) {
            throw new BackupException("data base name in resources can't be null or empty");
        }
        if (MobileMindUtil.isNullOrEmpty(dbBackupPath)) {
            throw new BackupException("data base backup path in resources can't be null or empty");
        }
        if (MobileMindUtil.isNullOrEmpty(dbBackupSufixFormat)) {
            throw new BackupException("data base backup sufix format in resources can't be null or empty");
        }
        if (MobileMindUtil.isNullOrEmpty(applicationPakage)) {
            throw new BackupException("application pakage in resources can't be null or empty");
        }
        if (MobileMindUtil.isNullOrEmpty(dbLocation)) {
            throw new BackupException("data base location in resources can't be null or empty");
        }

        this.dateFormat = new SimpleDateFormat(dbBackupSufixFormat);
    }

    /**
     * do backup
     *
     */
    @Override
    public void doBackup() {

        AppLogger.info(getClass(), "initializing backup");

        if (!SdCardTools.isAvailable()) {
            throw new BackupException(VelosterResource.getMessage("br.com.mobilemind.sdcardNotAvailable"));
        }
        String databaseName = VelosterConfig.getConf().isTestMode() ? dbTestName : dbName;
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        String backupName = dateFormat.format(new Date());

        if (sd.canWrite()) {
            String currentDBPath = MessageFormat.format(dbLocation, applicationPakage, databaseName);
            String backupFile = dbBackupPath + File.separator + backupName + sufix;

            AppLogger.info(getClass(), "prepare backup: origin {0} destination {1}", currentDBPath, backupFile);

            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, backupFile);

            if (!currentDB.exists()) {
                throw new BackupException("current db not found in [" + currentDB.getAbsolutePath() + "]");
            }

            File backupPathDir = new File(sd, dbBackupPath);
            if (!backupPathDir.exists()) {
                if (!backupPathDir.mkdirs()) {
                    throw new BackupException("error on create backup path in in [" + backupPathDir.getAbsolutePath() + "]");
                }
            }

            FileChannel src = null;
            FileChannel dst = null;

            try {
                src = new FileInputStream(currentDB).getChannel();

                AppLogger.info(getClass(), "opening origin channel");

                dst = new FileOutputStream(backupDB).getChannel();

                AppLogger.info(getClass(), "opening destination channel");

                dst.transferFrom(src, 0, src.size());

                AppLogger.info(getClass(), "backup completed");

            } catch (IOException e) {
                AppLogger.error(getClass(), e);
                throw new BackupException("erro tentando realizar backup", e);
            } finally {
                if (src != null) {
                    try {
                        src.close();
                    } catch (IOException ex) {
                        AppLogger.error(getClass(), ex);
                        throw new BackupException("erro tentando realizar backup", ex);
                    }
                }
                if (dst != null) {
                    try {
                        dst.close();
                    } catch (IOException ex) {
                        AppLogger.error(getClass(), ex);
                        throw new BackupException("erro tentando realizar backup", ex);
                    }
                }
            }

        } else {
            throw new BackupException(VelosterResource.getMessage("br.com.mobilemind.sdcartNotCanWrite"));
        }
    }

    /**
     * do restore
     *
     * @param backupInfo backup to restore
     */
    @Override
    public void doRestore(BackupInfo backupInfo) {
        AppLogger.info(getClass(), "initializing restore");

        if (!SdCardTools.isAvailable()) {
            throw new BackupException(VelosterResource.getMessage("br.com.mobilemind.sdcardNotAvailable"));
        }

        if (backupInfo == null) {
            throw new BackupException("paramater backupInfo can't be null");
        }

        if (backupInfo.getLocation() == null) {
            throw new BackupException("backupInfo location can't be null");
        }

        if (!backupInfo.getLocation().exists()) {
            throw new BackupException("backup [" + backupInfo.getLocation().getAbsolutePath() + "] not exists");
        }


        String databaseName = VelosterConfig.getConf().isTestMode() ? dbTestName : dbName;
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();

        if (sd.canWrite()) {
            String currentDBPath = MessageFormat.format(dbLocation, applicationPakage, databaseName);

            AppLogger.info(getClass(), "prepare restore backup: origin {0} destination {1}", backupInfo.getLocation().getAbsoluteFile(), currentDBPath);

            File currentDB = new File(data, currentDBPath);
            File backupDB = backupInfo.getLocation();

            if (!currentDB.exists()) {
                throw new BackupException("current db not found in [" + currentDBPath + "]");
            }
            FileChannel src = null;
            FileChannel dst = null;

            try {
                src = new FileInputStream(backupDB).getChannel();

                AppLogger.info(getClass(), "opening origin channel");

                dst = new FileOutputStream(currentDB).getChannel();

                AppLogger.info(getClass(), "opening destination channel");

                dst.transferFrom(src, 0, src.size());

                AppLogger.info(getClass(), "restore completed");

            } catch (IOException e) {
                AppLogger.error(getClass(), e);
                throw new BackupException("erro tentando realizar restore", e);
            } finally {
                if (src != null) {
                    try {
                        src.close();
                    } catch (IOException ex) {
                        AppLogger.error(getClass(), ex);
                        throw new BackupException("erro tentando realizar restore", ex);
                    }
                }
                if (dst != null) {
                    try {
                        dst.close();
                    } catch (IOException ex) {
                        AppLogger.error(getClass(), ex);
                        throw new BackupException("erro tentando realizar restore", ex);
                    }
                }
            }

        } else {
            throw new BackupException(VelosterResource.getMessage("br.com.mobilemind.sdcartNotCanWrite"));
        }
    }

    /**
     * list old backups
     *
     * @param ignoreInvalidsFormats if ignore invalids formats
     * @return list de old backups
     */
    @Override
    public List<BackupInfo> listOldBackups(boolean ignoreInvalidsFormats) {

        if (!SdCardTools.isAvailable()) {
            throw new BackupException(VelosterResource.getMessage("br.com.mobilemind.sdcardNotAvailable"));
        }

        File sd = Environment.getExternalStorageDirectory();
        String backupPath = sd + File.separator + dbBackupPath;
        File file = new File(backupPath);
        List<BackupInfo> infos = new ArrayList<BackupInfo>();

        if (!file.exists()) {
            AppLogger.info(getClass(),"backup path not exists. location [" + file.getAbsolutePath() + "]");
            return new ArrayList<BackupInfo>();
        }

        File files[] = file.listFiles();

        if (files == null) {
            return infos;
        }

        for (File f : files) {

            String date = f.getName().replace(sufix, "");

            try {
                infos.add(new BackupInfo(f, dateFormat.parse(date)));
            } catch (ParseException e) {
                AppLogger.error(getClass(), e);
                if (!ignoreInvalidsFormats) {
                    throw new BackupException("erro ao ler backup's antigos", e);
                }
            }
        }

        return infos;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    @Override
    public void deleteDatabase() {
        File data = Environment.getDataDirectory();
        String currentDBPath = MessageFormat.format(dbLocation, applicationPakage, dbName);
        File currentDB = new File(data, currentDBPath);

        AppLogger.info(getClass(), "current data base [" + currentDBPath + "]");

        if (currentDB.exists()) {
            if (!currentDB.delete()) {
                throw new BackupException("cannot remove data base [" + currentDBPath + "]");
            } else {
                AppLogger.info(getClass(), "data base was removed with success in [" + currentDBPath + "]");
            }
        }
    }

    @Override
    public void deleteTestDatabase() {
        File data = Environment.getDataDirectory();
        String currentDBPath = MessageFormat.format(dbLocation, applicationPakage, dbTestName);
        File currentDB = new File(data, currentDBPath);

        AppLogger.info(getClass(), "current test data base [" + currentDBPath + "]");

        if (currentDB.exists()) {
            if (!currentDB.delete()) {
                throw new BackupException("cannot remove test data base [" + currentDBPath + "]");
            } else {
                AppLogger.info(getClass(), "data base test was removed with success in [" + currentDBPath + "]");
            }
        }
    }
}
