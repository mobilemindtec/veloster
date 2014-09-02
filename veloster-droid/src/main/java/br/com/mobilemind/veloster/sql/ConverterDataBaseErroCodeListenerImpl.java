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

import android.database.sqlite.SQLiteException;
import br.com.mobilemind.veloster.exceptions.DataBaseException;
import br.com.mobilemind.veloster.orm.ConverterDataBaseErroCodeListener;

/**
 *
 * @author Ricardo Bocchi
 */
public class ConverterDataBaseErroCodeListenerImpl implements ConverterDataBaseErroCodeListener{

    @Override
    public void analyze(Exception e) throws DataBaseException {
        if(e instanceof SQLiteException){
            throw new DataBaseException(e);            
        }
    }    
}
