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


import java.util.Set;

/**
 *
 * @author Ricardo Bocchi
 */
public class Like extends ExpressionMapImpl<Object, Match> {

    private boolean ignoreCase;
    private boolean not;

    public Like() {
        this(null, null);
    }

    public Like(Object value, Match match) {
        super(match.getValue().replace("like", value.toString()), match);
    }

    public Like(Object value, Match match, boolean ignoreCase) {
        super((ignoreCase ? match.getValue().replace("like", value.toString()).toUpperCase() : match.getValue().replace("like", value.toString())), match);
        this.ignoreCase = ignoreCase;
    }

    public Like(Object value, Match match, boolean ignoreCase, boolean isNot) {
        super((ignoreCase ? match.getValue().replace("like", value.toString()).toUpperCase() : match.getValue().replace("like", value.toString())), match);
        this.ignoreCase = ignoreCase;
        this.not = isNot;
    }

    public Like like(Object value, Match match) {
        super.setValues(match.getValue().replace("like", value.toString()), match);
        return this;
    }

    public Like like(Object value, Match match, boolean ignoreCase) {
        super.setValues((ignoreCase ? match.getValue().replace("like", value.toString()).toUpperCase() : match.getValue().replace("like", value.toString())), match);
        this.ignoreCase = ignoreCase;
        return this;
    }

    public Like like(Object value, Match match, boolean ignoreCase, boolean isNot) {
        super.setValues((ignoreCase ? match.getValue().replace("like", value.toString()).toUpperCase() : match.getValue().replace("like", value.toString())), match);
        this.ignoreCase = ignoreCase;
        this.not = isNot;
        return this;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public boolean isNot() {
        return not;
    }

    @Override
    public String getKeyWork() {
        return "Like";
    }
}
