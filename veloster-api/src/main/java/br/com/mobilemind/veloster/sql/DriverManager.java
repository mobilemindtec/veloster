package br.com.mobilemind.veloster.sql;

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


import java.security.InvalidParameterException;

/**
 *
 * @author Ricardo Bocchi
 */
public class DriverManager {
    
    private static ConnectionFactory factory;
    
    public static void setConnectionFactory(ConnectionFactory factory){
        DriverManager.factory = factory;
    }
    
    public static ConnectionFactory getConnectionFactory(){
        return DriverManager.factory;
    }
    
    public static Connection getConnection(){
        
        if(DriverManager.factory == null){
            throw new InvalidParameterException("factory parameter can't be null");
        }
        
        return DriverManager.factory.getConnection();
    }
}
