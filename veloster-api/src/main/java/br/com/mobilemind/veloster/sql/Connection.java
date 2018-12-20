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


import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Ricardo Bocchi
 */
public interface Connection {

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void open() throws SQLException;

    void close() throws SQLException;

    void begin() throws SQLException;
    
    boolean isActive() throws SQLException;
        
    boolean isClosed();
    
    void setCommitTrans(boolean trans);
    
    void addAfterConnection(String pragmaOrQuery);
       
    Statement prepare(String query, boolean isInsert) throws SQLException;

    void setDataFormat(SimpleDateFormat format);
}
