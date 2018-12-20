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

import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.orm.DDLDialect;
import br.com.mobilemind.veloster.orm.model.ColumnWrapper;
import br.com.mobilemind.veloster.orm.model.TableImpl;
import br.com.mobilemind.veloster.orm.annotations.Table;
import br.com.mobilemind.veloster.sql.DataType;
import br.com.mobilemind.veloster.sql.Driver;
import br.com.mobilemind.veloster.tools.VelosterConfig;
import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * SQLlite implementation
 *
 * @author Ricardo Bocchi
 */
public class DDLDialectImpl implements DDLDialect {

    private DataType dataType;
    private Driver driver;

    public DDLDialectImpl() {
        this.driver = VelosterConfig.getConf().getDriver();
        this.dataType = driver.getDataType();
    }

    @Override
    public String compileCreateColumn(ColumnWrapper wrapper) {
        //"ALTER TABLE employee ADD new_col CHAR(25) DEFAULT '10' NOT NULL";
        StringBuilder dml = new StringBuilder("Alter Table ");
        String type = null;
        List<ColumnWrapper> foreignKeys = new ArrayList<ColumnWrapper>();
        List<ColumnWrapper> unique = new ArrayList<ColumnWrapper>();

        dml.append(wrapper.getTable().name()).append(" Add ").append(wrapper.getName()).append(" ");

        if (wrapper.getType().isEnum()) {
            type = getDataEnumType(wrapper);
        } else {
            type = this.dataType.getTypeForClass(wrapper.getType());
        }

        if (isStringType(type)) {
            type = MessageFormat.format(type, Integer.toString(wrapper.getLength()));
        }

        dml.append(type).append(" ");

        if (!wrapper.isNullable()) {
            dml.append("Not Null ");
        }

        if (!"".equals(wrapper.getDefaultValue()) && wrapper.getDefaultValue() != null) {
            dml.append("default '").append(wrapper.getDefault()).append("' ");
        }

        if (wrapper.isDefaultValueGenerated()) {
            Object value = wrapper.getDefaultValueGenarator().generate();
            MMLogger.log(Level.INFO, getClass(), "generated custon value " + value);
            dml.append("default '").append(value).append("' ");
        }

        //if (wrapper.isForeignKey()) {
        //    foreignKeys.add(wrapper);
        //}

        //dml.append(compileforeignKeys(foreignKeys, true));



        return dml.toString();
    }

    @Override
    public String complileCreateTable(String table, List<ColumnWrapper> fields) {
        StringBuilder dml = new StringBuilder();
        List<ColumnWrapper> foreignKeys = new ArrayList<ColumnWrapper>();
        List<ColumnWrapper> unique = new ArrayList<ColumnWrapper>();
        String type = null;
        int count = 0;
        int len = fields.size();


        dml.append("Create Table ").append(table);
        dml.append("(");

        for (ColumnWrapper wrapper : fields) {

            if (wrapper.isHasMany()) {
                len--;
                continue;
            }

            if (wrapper.getType().isEnum()) {
                type = getDataEnumType(wrapper);
            } else {
                type = this.dataType.getTypeForClass(wrapper.getType());
            }

            if (isStringType(type)) {
                type = MessageFormat.format(type, Integer.toString(wrapper.getLength()));
            }

            dml.append(wrapper.getName()).append(" ").append(type).append(" ");

            if (wrapper.isPrimaryKey()) {
                dml.append("Primary Key ").append(driver.getAutoincrementKey()).append(" ");
            }

            if (!wrapper.isNullable()) {
                dml.append("Not Null ");
            }

            if (!"".equals(wrapper.getDefaultValue()) && wrapper.getDefaultValue() != null) {
                dml.append("default '").append(wrapper.getDefault()).append("' ");
            }

            if (wrapper.isDefaultValueGenerated()) {
                Object value = wrapper.getDefaultValueGenarator().generate();
                MMLogger.log(Level.INFO, getClass(), "generated custon value " + value);
                dml.append("default '").append(value).append("' ");
            }
            if (wrapper.isUnique()) {
                dml.append(" unique ");
            }

            if (++count < len) {
                dml.append(",");
            }

            if (wrapper.isForeignKey()) {
                foreignKeys.add(wrapper);
            }
        }

        dml.append(compileforeignKeys(foreignKeys, false));

        dml.append(");");

//        for (String index : compileUniqueIndex(unique)) {
//            dml.append(" ").append(index).append("; ");
//        }

        return dml.toString();
    }

    @Override
    public String compileDropTable(String table) {
        StringBuilder dml = new StringBuilder();
        return dml.append("Drop Table ").append(table).append(";").toString();

    }

//    private String[] compileUniqueIndex(List<ColumnWrapper> items) {
//        String uniques[] = new String[items.size()];
//        int i = 0;
//        for (ColumnWrapper wrapper : items) {
//            uniques[i++] = "CREATE UNIQUE INDEX idx_" + wrapper.getTable().name() + "_" + wrapper.getName() + " on " + wrapper.getTable().name() + " (" + wrapper.getName() + ")";
//        }
//        return uniques;
//    }
    private String compileforeignKeys(List<ColumnWrapper> foreinKeys, boolean isAlter) {
        int count = 0;
        int len = foreinKeys.size();
        StringBuilder dml = new StringBuilder();

        if (!foreinKeys.isEmpty()) {
            if (!isAlter) {
                dml.append(" , ");
            } else {
                dml.append(" ADD CONSTRAINT ");
            }
        }

        for (ColumnWrapper f : foreinKeys) {
            //FOREIGN KEY(trackartist) REFERENCES artist(artistid)

            Object table = f.getType().getAnnotation(Table.class);

            if (!(table instanceof Table)) {
                throw new InvalidParameterException("type " + f.getType().getName() + " cant's be foreing key");
            }

            TableImpl impl = new TableImpl(f.getType());

            dml.append("FOREIGN KEY(").append(f.getName()).append(") REFERENCES ").append(impl.name()).append("(id)");

            if (++count < len) {
                dml.append(",");
            }
        }

        return dml.toString();
    }

    private String getDataEnumType(ColumnWrapper wrapper) {
        return wrapper.parseEnumInt() ? this.dataType.getTypeToInteger() : this.dataType.getTypeToString();
    }

    private boolean isStringType(String type) {
        return this.dataType.getTypeToString().equals(type);
    }
}
