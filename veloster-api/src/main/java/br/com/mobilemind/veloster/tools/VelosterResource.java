package br.com.mobilemind.veloster.tools;

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
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Resource Util
 *
 * @author Ricardo Bocchi
 */
public class VelosterResource {

    private static Properties PROPERTIES = new Properties();
    private static Properties MESSAGES = new Properties();
    private static Properties PROPERTIES_CUSTON = new Properties();

    static {
        load();
        loadMessagesValidate();
    }

    private static void load() {
        InputStream in = null;
        try {
            in = VelosterResource.class.getClassLoader().getResourceAsStream("resources.properties");
            if (in != null) {
                PROPERTIES.load(in);
                if (MMLogger.isLogable()) {
                    MMLogger.log(Level.INFO, VelosterResource.class, "resources.properties loaded...");
                }
            } else {
                MMLogger.log(Level.WARNING, VelosterResource.class, "resources.properties not fount... loading resources_default.properties.");
            }
        } catch (IOException ex) {
            MMLogger.log(Level.SEVERE, VelosterResource.class, ex.getMessage(), ex);
        }

        if (in == null) {
            try {
                in = VelosterResource.class.getClassLoader().getResourceAsStream("resources_default.properties");
                if (in != null) {
                    PROPERTIES.load(in);
                    if (MMLogger.isLogable()) {
                        MMLogger.log(Level.INFO, VelosterResource.class, "resources_default.properties loaded...");
                    }
                } else {
                    MMLogger.log(Level.SEVERE, VelosterResource.class, "resources_default.properties not found");
                }
            } catch (IOException ex) {
                MMLogger.log(Level.SEVERE, VelosterResource.class, ex);
            }
        }
    }

    /**
     * get string in resources.properties
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        if (PROPERTIES_CUSTON.containsKey(key)) {
            return PROPERTIES_CUSTON.getProperty(key);
        }
        return PROPERTIES.getProperty(key);
    }

    /**
     * get message in message_validates.properties
     *
     */
    public static String getMessage(String messageKey) {
        return MESSAGES.getProperty(messageKey);
    }

    private static void loadMessagesValidate() {
        InputStream in = null;
        try {
            in = VelosterResource.class.getClassLoader().getResourceAsStream("message_validates.properties");
            if (in != null) {
                MESSAGES.load(in);
                if (MMLogger.isLogable()) {
                    MMLogger.log(Level.INFO, VelosterResource.class, "message_validates loaded...");
                }
            } else {
                MMLogger.log(Level.WARNING, VelosterResource.class, "message_validates not fount... loading resources_default.properties.");
            }
        } catch (IOException ex) {
            MMLogger.log(Level.SEVERE, VelosterResource.class, ex.getMessage(), ex);
        }

        if (in == null) {
            try {
                in = VelosterResource.class.getClassLoader().getResourceAsStream("message_validates_default.properties");
                if (in != null) {
                    MESSAGES.load(in);
                    if (MMLogger.isLogable()) {
                        MMLogger.log(Level.INFO, VelosterResource.class, "message_validates_default.properties loaded...");
                    }
                } else {
                    MMLogger.log(Level.SEVERE, VelosterResource.class, "message_validates_default.properties not found");
                }
            } catch (IOException ex) {
                MMLogger.log(Level.SEVERE, VelosterResource.class, ex);
            }
        }
    }

    public static void setProperty(String key, String value) {
        PROPERTIES_CUSTON.put(key, value);
    }
}
