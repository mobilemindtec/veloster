package br.com.mobilemind.veloster.sql.type;

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


import br.com.mobilemind.veloster.orm.model.ColumnWrapper;
import java.util.Set;

/**
 *.eq.               igual a (equal) 
.ne.               diferente de (not equal)
.lt.                 menor que (less than) 
.le.                menor ou igual (less or equal)
.gt.                maior que (greater than)      
.ge.               maior ou igual (greater or equal)
 * @author Ricardo Bocchi
 */
public interface Expression {
    
    Set<Object> getValues(); 
    
    ColumnWrapper getField();
    
    void setField(ColumnWrapper field);
    
    String getKeyWork();
}
