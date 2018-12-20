package br.com.mobilemind.veloster.extra;

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

import java.util.List;

/**
 *
 * @author Ricardo Bocchi
 */
public interface DatabaseBackupHelper {

    /**
     * do database backup. configure options in resources.properties
     * 
     */
    void doBackup();

    /**
     * do restore of dababase backup. configure options in resources.properties
     * 
     * @param info backup info to restore
     */
    void doRestore(BackupInfo info);

    /**
     * remove database
     * 
     */
    void deleteDatabase();
    
    /**
     * remove database test
     * 
     */
    void deleteTestDatabase();
    
    /**
     * list old backups
     * 
     * @param ignoreInvalidsFormats. if invalids formats in backup folder should be ignored.
     * @return list of old backups
     */
    List<BackupInfo> listOldBackups(boolean ignoreInvalidsFormats);
}
