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
import br.com.mobilemind.veloster.sql.impl.DataBaseImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author Ricardo Bocchi
 */
public class ConnectionFactoryImplDroid implements ConnectionFactory {

    private DataBase data;
    private static Map<DataBase, Connection> conections;

    static {
        conections = new HashMap<DataBase, Connection>();
    }
    private Context context;

    public ConnectionFactoryImplDroid(Context context) {
        this.data = new DataBaseImpl();
        this.context = context;
    }

    @Override
    public void setDataBase(DataBase db) {
    }

    @Override
    public DataBase getDataBase() {
        return this.data;
    }

    @Override
    public Connection getConnection() {
        if (!conections.containsKey(data)) {
            if (MMLogger.isLogable()) {
                MMLogger.log(Level.INFO, this.getClass(), "create connection");
            }
            conections.put(data, new ConnectionImplDroid(new DataHelper(context)));
        }
        return conections.get(data);
    }
}
