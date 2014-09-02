package br.com.mobilemind.veloster.sql.impl;

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

import br.com.mobilemind.veloster.orm.QueryFormatter;
import br.com.mobilemind.veloster.orm.model.TableImpl;
import br.com.mobilemind.veloster.sql.type.Between;
import br.com.mobilemind.veloster.sql.type.Expression;
import br.com.mobilemind.veloster.sql.type.IsNull;
import br.com.mobilemind.veloster.sql.type.Like;
import br.com.mobilemind.veloster.sql.type.Match;
import br.com.mobilemind.veloster.sql.type.NotIsNull;
import br.com.mobilemind.veloster.sql.type.OrderBy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



/**
 *
 * @author Ricardo Bocchi
 */
public class QueryFormatterImpl implements QueryFormatter {

    private List<Expression> where;
    private List<String> fields;
    private List<OrderBy> ordeBy;
    private String table;
    private StringBuilder query;
    private int limit;
    private int offset;

    public QueryFormatterImpl() {
        reset();
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<String> getFields() {
        return fields;
    }

    private void reset() {
        this.query = new StringBuilder();
        this.fields = new ArrayList<String>();
        this.where = new ArrayList<Expression>();
        this.ordeBy = new ArrayList<OrderBy>();
        this.table = "";
        this.query = new StringBuilder("");
    }

    @Override
    public String compileInsert() {
        return getInsert();
    }

    @Override
    public String compileInsert(String tabela, String... values) {
        this.table = tabela;
        for (String s : values) {
            this.fields.add(s);
        }
        return getInsert();
    }

    private String getInsert() {
        query.append("Insert Into ").append(table).append(" (");
        int j = fields.size();
        for (int i = 0; i < j; i++) {
            query.append(fields.get(i));
            if (i < j - 1) {
                query.append(", ");
            }
        }

        query.append(") Values (");

        for (int i = 0; i < j; i++) {
            query.append("?");
            if (i < j - 1) {
                query.append(", ");
            }
        }
        query.append(")");
        try {
            return query.toString();
        } finally {
            this.reset();
        }
    }

    @Override
    public String compileSelect(String table, String... values) {
        this.table = table;
        for (String s : values) {
            this.fields.add(s);
        }
        return getSelect();
    }

    @Override
    public String compileCount(String table) {
        this.table = table;
        return this.getCount();
    }

    @Override
    public String compileSelect() {
        return getSelect();
    }

    private String getCount() {
        query.append("Select Count(id) From ").append(this.table);

        if (where.size() > 0) {
            process(query, where.get(0), false, false);
            if (where.size() > 1) {
                for (int i = 1; i < where.size(); i++) {
                    process(query, where.get(i), true, false);
                }
            }
        }
        try {
            return query.toString();
        } finally {
            this.reset();
        }

    }

    private String getSelect() {
        boolean joineable = false;
        for (Expression e : where) {
            if (e.getField().isJoin()) {
                joineable = true;
            }

            if (e instanceof OrderBy) {
                this.ordeBy.add(((OrderBy) e));
            }
        }

        String prefix = "";

        if (joineable) {
            prefix = this.table + ".";
        }

        query.append("Select ");
        int j = this.fields.size();
        for (int i = 0; i < j; i++) {
            query.append(prefix).append(this.fields.get(i));
            if (i < j - 1) {
                query.append(", ");
            }
        }
        query.append(" From ").append(this.table);

        if (joineable) {
            List<String> tables = new LinkedList<String>();
            for (Expression e : where) {
                if (e.getField().isJoin()) {
                    if (tables.contains(table)) {
                        continue;
                    }
                    String tbName = new TableImpl(e.getField().getType()).name();
                    query.append(" join ").append(tbName);
                    query.append(" on ").append(prefix).append(e.getField().getName());
                    query.append(" = ").append(tbName).append(".id");
                    tables.add(table);
                }
            }
        }

        if (where.size() > 0) {
            process(query, where.get(0), false, joineable);
            if (where.size() > 1) {
                for (int i = 1; i < where.size(); i++) {
                    process(query, where.get(i), true, joineable);
                }
            }
        }

        if (ordeBy.size() > 0) {
            OrderBy order = ordeBy.get(0);
            query.append(" Order By ").append(order.getField().getName());
            if (order.isDesc()) {
                query.append(" desc");
            }
            if (ordeBy.size() > 1) {
                for (int i = 1; i < ordeBy.size(); i++) {
                    order = ordeBy.get(i);
                    query.append(", ").append(order.getField().getName());
                    if (order.isDesc()) {
                        query.append(" desc");
                    }
                }
            }
        }

        if (limit > 0) {
            query.append(" limit ").append(limit)
                    .append(" offset ").append(offset);
        }

        try {
            return query.toString();
        } finally {
            this.reset();
        }
    }

    @Override
    public String compileUpdate(String tabela, String... values) {
        this.table = tabela;
        for (String s : values) {
            this.fields.add(s);
        }
        return getUpdate();
    }

    @Override
    public String compileUpdate() {
        return getUpdate();
    }

    private String getUpdate() {
        query.append("Update ").append(this.table).append(" Set ");
        int j = this.fields.size();
        for (int i = 0; i < j; i++) {
            query.append(this.fields.get(i)).append(" = ?");
            if (i < j - 1) {
                query.append(", ");
            }
        }
        if (where.size() > 0) {
            process(query, where.get(0), false, false);
            if (where.size() > 1) {
                for (int i = 1; i < where.size(); i++) {
                    process(query, where.get(i), true, false);
                }
            }
        }
        try {
            return query.toString();
        } finally {
            this.reset();
        }
    }

    @Override
    public String compileDelete(String table, Expression... expressions) {
        this.table = table;
        for (Expression e : expressions) {
            this.where.add(e);
        }
        return getDelete();
    }

    @Override
    public String compileDelete() {
        return getDelete();
    }

    private String getDelete() {
        query.append("Delete From ").append(this.table);

        if (where.size() > 0) {
            process(query, where.get(0), false, false);
            if (where.size() > 1) {
                for (int i = 1; i < where.size(); i++) {
                    process(query, where.get(i), true, false);
                }
            }
        }
        try {
            return query.toString();
        } finally {
            this.reset();
        }
    }

    @Override
    public QueryFormatterImpl setWhere(Expression... expressions) {
        for (Expression s : expressions) {
            this.where.add(s);
        }
        return this;
    }

    @Override
    public QueryFormatterImpl setWhere(List<Expression> expressions) {
        for (Expression s : expressions) {
            this.where.add(s);
        }
        return this;
    }

    @Override
    public QueryFormatterImpl setFields(String... fields) {
        for (String s : fields) {
            this.fields.add(s);
        }

        return this;
    }

    @Override
    public QueryFormatterImpl setTable(String table) {
        this.table = table;
        return this;
    }

    @Deprecated
    @Override
    public QueryFormatterImpl setOrdeBy(String... ordeBy) {
        throw new UnsupportedOperationException();
    }

    private void process(StringBuilder query, Expression e, boolean whereExistes, boolean joinExists) {

        if (e instanceof OrderBy) {
            return;
        }

        String and = " And (";
        String or = " Or ";
        String where = " Where (";
        boolean andAndWhereUsed = false;
        Object key = null;
        Iterator it;
        String word = whereExistes ? and : where;
        boolean joineable = e.getField().isJoin();
        String joinTable = "";
        String fieldName;

        if (joineable) {
            joinTable = new TableImpl(e.getField().getType()).name() + ".";
            fieldName = e.getField().getJoinField().getName();
        } else {
            if (joinExists) {
                joinTable = this.table + ".";
            }
            fieldName = e.getField().getName();
        }

        if (e instanceof Like) {
            Like like = (Like) e;
            Map<Object, Match> map = like.getMap();
            it = map.keySet().iterator();
            while (it.hasNext()) {
                key = it.next();
                if (andAndWhereUsed) {
                    word = or;
                }
                andAndWhereUsed = true;

                query.append(word);
                if (like.isIgnoreCase()) {
                    query.append("Upper(").append(joinTable).append(fieldName).append(")");
                } else {
                    query.append(joinTable).append(fieldName);
                }
                query.append(" ").append(like.isNot() ? "Not " : "").append(e.getKeyWork()).append(" ").append("?");
            }
        } else if (e instanceof Between) {
            int len = ((Between) e).getMap().size();
            for (int i = 0; i < len; i++) {
                if (andAndWhereUsed) {
                    word = or;
                }
                andAndWhereUsed = true;
                query.append(word).append(joinTable).append(fieldName).append(" ").append(e.getKeyWork()).
                        append(" ? ").append("And").append(" ?");
            }
        } else if (e instanceof IsNull || e instanceof NotIsNull) {
            if (andAndWhereUsed) {
                word = or;
            }
            andAndWhereUsed = true;
            query.append(word).append(joinTable).append(fieldName).append(" ").append(e.getKeyWork());
        } else {
            int len = e.getValues().size();
            for (int i = 0; i < len; i++) {
                if (andAndWhereUsed) {
                    word = or;
                }
                andAndWhereUsed = true;
                query.append(word).append(joinTable).append(fieldName).append(" ").append(e.getKeyWork()).
                        append(" ?");
            }
        }
        if (andAndWhereUsed) {
            query.append(")");
        }
    }

    @Override
    public QueryFormatter setPagination(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
        return this;
    }
}
