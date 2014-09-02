package br.com.mobilemind.veloster.orm.core;

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
import br.com.mobilemind.veloster.exceptions.EntityValidatorException;
import br.com.mobilemind.veloster.exceptions.FailProcessExcetion;
import br.com.mobilemind.veloster.orm.EntityValidator;
import br.com.mobilemind.veloster.orm.validator.DateValidation;
import br.com.mobilemind.veloster.orm.validator.Length;
import br.com.mobilemind.veloster.orm.validator.NotNull;
import br.com.mobilemind.veloster.orm.validator.Regex;
import br.com.mobilemind.veloster.orm.validator.NumberValidation;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.api.utils.ClassUtil;
import br.com.mobilemind.api.utils.MobileMindUtil;
import br.com.mobilemind.api.utils.log.MMLogger;
import br.com.mobilemind.veloster.tools.VelosterResource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementação para api de validação
 *
 * @author Ricardo Bocchi
 */
public class EntityValidatorImpl<T extends Entity> implements EntityValidator<T> {

    private Class<T> clazz;
    private static final Map<String, Map<Field, List<Annotation>>> CACHE = new HashMap<String, Map<Field, List<Annotation>>>();
    private String key;

    @Override
    public void build(Class<T> clazz) {
        this.clazz = clazz;
        key = clazz.getName();
        if (!CACHE.containsKey(key)) {
            init();
        }
    }

    private void init() {
        List<Field> fields = ClassUtil.getAllFields(this.clazz);
        List<Annotation> items = null;
        Annotation[] annotations = null;
        boolean isValidate = false;
        Map<Field, List<Annotation>> fieldsValids = new HashMap<Field, List<Annotation>>();

        for (Field f : fields) {
            f.setAccessible(true);
            annotations = f.getAnnotations();
            if (annotations != null) {
                for (Annotation a : annotations) {
                    if (a instanceof NotNull) {
                        isValidate = true;
                    } else if (a instanceof DateValidation) {
                        isValidate = true;
                    } else if (a instanceof Length) {
                        isValidate = true;
                    } else if (a instanceof NumberValidation) {
                        isValidate = true;
                    } else if (a instanceof Regex) {
                        isValidate = true;
                    }
                    if (isValidate) {
                        if (items == null) {
                            items = new LinkedList<Annotation>();
                        }
                        items.add(a);
                        isValidate = false;
                    }
                }
            }
            if (items != null) {
                fieldsValids.put(f, items);
                items = null;
            }
        }

        CACHE.put(key, fieldsValids);
    }

    /**
     * validate a entity
     *
     * @param entity
     */
    @Override
    public void validate(T entity) {
        this.validate0(entity);
    }

    /**
     * validate a internal entity
     *
     * @param entity
     */
    private void validate0(T entity) {
        if (entity == null) {
            throw new FailProcessExcetion("entity can't be null");
        }

        if (MMLogger.isLogable()) {
            MMLogger.log(Level.INFO, this.getClass(), "intializing validation for entity " + this.clazz.getSimpleName());
        }

        Set<Field> set = CACHE.get(key).keySet();
        for (Field f : set) {
            for (Annotation a : CACHE.get(key).get(f)) {
                try {
                    process(entity, f, a);
                } catch (EntityValidatorException e) {
                    MMLogger.log(Level.WARNING, this.getClass(), "values are not valid for entity " + this.clazz.getSimpleName());
                    MMLogger.log(Level.WARNING, this.getClass(), e.getMessage());
                    throw e;
                } catch (FailProcessExcetion e) {
                    MMLogger.log(Level.SEVERE, this.getClass(), "there was an error in the validation: " + e.getMessage());
                    throw e;
                }
            }
        }

        if (MMLogger.isLogable()) {
            MMLogger.log(Level.INFO, this.getClass(), "values are valid for entity " + this.clazz.getSimpleName());
        }
    }

    private void process(T entity, Field f, Annotation a) {
        String message = null;

        if (a instanceof NotNull) {
            message = validNull(entity, (NotNull) a, f);
        } else if (a instanceof DateValidation) {
            message = validDate(entity, (DateValidation) a, f);
        } else if (a instanceof Length) {
            message = validLength(entity, (Length) a, f);
        } else if (a instanceof NumberValidation) {
            message = validNumber(entity, (NumberValidation) a, f);
        } else if (a instanceof Regex) {
            message = validRegex(entity, (Regex) a, f);
        }

        if (message != null) {
            throw new EntityValidatorException(message);
        }
    }

    private String validDate(T entity, DateValidation validDate, Field f) {
        String message = null;
        java.util.Date date = null;
        java.util.Date max = null;
        java.util.Date min = null;

        try {
            date = (java.util.Date) f.get(entity);
        } catch (Exception ex) {
            throw new FailProcessExcetion(ex);
        }

        if (!(date instanceof java.util.Date)) {
            throw new FailProcessExcetion("incompatible validations types for field " + f.getName() + ". Validate Date for field type " + f.getType().getSimpleName());
        }

        try {
            max = new SimpleDateFormat("dd/MM/yyyy").parse(validDate.max());
            min = new SimpleDateFormat("dd/MM/yyyy").parse(validDate.min());
        } catch (Exception e) {
            throw new FailProcessExcetion("date format validation invalid. required dd/MM/yyyy");
        }
        if (date.before(min) || date.after(max)) {
            if (!MobileMindUtil.isNullOrEmpty(validDate.message())) {
                message = validDate.message();
            } else {
                message = VelosterResource.getMessage(validDate.messageKey());
            }
            message = MessageFormat.format(message, validDate.min(), validDate.max(), f.getName());
        }
        return message;
    }

    private String validLength(T entity, Length length, Field f) {
        Object value = null;
        String message = null;

        try {
            value = f.get(entity);
        } catch (Exception ex) {
            throw new FailProcessExcetion(ex);
        }

        if (f.getType() != String.class) {
            throw new FailProcessExcetion("incompatible validations length types for field " + f.getName() + ". expexted String");
        }

        if (!(value instanceof String)) {
            return null;
        }
        
        String word = value.toString().trim();
       
        if (word.length() > length.max() || word.length() < length.min()) {
            if (!MobileMindUtil.isNullOrEmpty(length.message())) {
                message = length.message();
            } else {
                message = VelosterResource.getMessage(length.messageKey());
            }
            message = MessageFormat.format(message, Integer.toString(length.min()), Integer.toString(length.max()), f.getName());
        }

        return message;
    }

    private String validNumber(T entity, NumberValidation number, Field f) throws FailProcessExcetion {
        Object value = null;
        String message = null;

        try {
            value = f.get(entity);
        } catch (Exception ex) {
            throw new FailProcessExcetion(ex);
        }

        if (value == null) {
            throw new FailProcessExcetion("value can't be null for field " + f.getName());
        }

        if (ClassUtil.isDouble(f.getType())) {
            Double d = Double.parseDouble(value.toString());
            if (d > number.maxDouble() || d < number.minDouble()) {
                if (!MobileMindUtil.isNullOrEmpty(number.message())) {
                    message = number.message();
                } else {
                    message = VelosterResource.getMessage(number.messageKey());
                }
                message = MessageFormat.format(message, Double.toString(number.minDouble()), Double.toString(number.maxDouble()), f.getName());
            }
        } else if (ClassUtil.isInteger(f.getType())) {
            Integer d = Integer.parseInt(value.toString());
            if (d > number.maxInt() || d < number.minInt()) {
                if (!MobileMindUtil.isNullOrEmpty(number.message())) {
                    message = number.message();
                } else {
                    message = VelosterResource.getMessage(number.messageKey());
                }
                message = MessageFormat.format(message, Integer.toString(number.minInt()), Integer.toString(number.maxInt()), f.getName());
            }
        } else if (ClassUtil.isLong(f.getType())) {
            Long d = Long.parseLong(value.toString());
            if (d > number.maxLong() || d < number.minLong()) {
                if (!MobileMindUtil.isNullOrEmpty(number.message())) {
                    message = number.message();
                } else {
                    message = VelosterResource.getMessage(number.messageKey());
                }
                message = MessageFormat.format(message, Long.toString(number.minLong()), Long.toString(number.maxLong()), f.getName());
            }
        } else {
            throw new FailProcessExcetion("incompatible validations types for field " + f.getName() + ". expected double, long or integer, but is " + f.getType().getSimpleName() + " or null");
        }

        return message;
    }

    private String validRegex(T entity, Regex regex, Field f) {
        String message = null;
        Object value;
        try {
            value = f.get(entity);
        } catch (Exception ex) {
            throw new FailProcessExcetion(ex);
        }

        if (!(value instanceof String)) {
            throw new FailProcessExcetion("incompatible validations regex types for field " + f.getName() + ". expexted String");
        }

        if (regex.value() == null || "".equals(regex.value())) {
            throw new FailProcessExcetion("regex expression can't be null or empty for field " + f.getName());
        }
        Pattern pattern = Pattern.compile(regex.value());
        Matcher matcher = pattern.matcher(value.toString());

        if (!matcher.find()) {
            if (!MobileMindUtil.isNullOrEmpty(regex.message())) {
                message = regex.message();
            } else {
                message = VelosterResource.getMessage(regex.messageKey());
            }
            message = MessageFormat.format(message, regex.value(), f.getName());
        }
        return message;
    }

    private String validNull(T entity, NotNull notNull, Field f) {
        String message = null;
        Object value;
        try {
            value = f.get(entity);
        } catch (Exception ex) {
            throw new FailProcessExcetion(ex);
        }

        if (value == null) {
            if (!MobileMindUtil.isNullOrEmpty(notNull.message())) {
                message = notNull.message();
            } else {
                message = VelosterResource.getMessage(notNull.messageKey());
            }
            message = MessageFormat.format(message, f.getName());
        }
        return message;
    }
}
