package br.com.mobilemind.veloster.orm.validator;

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


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Ricardo Bocchi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NumberValidation {
    
    long maxLong() default Long.MAX_VALUE;
    
    long minLong() default Long.MIN_VALUE;

    int maxInt() default Integer.MAX_VALUE;
    
    int minInt() default Integer.MIN_VALUE;
    
    double maxDouble() default Double.MAX_VALUE;
    
    double minDouble() default Double.MIN_VALUE;
    
    String messageKey() default "br.com.mobilemind.number";
    
    String message() default "";
}
