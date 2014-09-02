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
import br.com.mobilemind.veloster.exceptions.VelosterException;
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.sql.type.Between;
import br.com.mobilemind.veloster.sql.type.Criteria;
import br.com.mobilemind.veloster.sql.type.Eq;
import br.com.mobilemind.veloster.sql.type.Expression;
import br.com.mobilemind.veloster.sql.type.IsNull;
import br.com.mobilemind.veloster.sql.type.Like;
import br.com.mobilemind.veloster.sql.type.Match;
import br.com.mobilemind.veloster.sql.type.Ne;
import br.com.mobilemind.veloster.sql.type.NotIsNull;
import br.com.mobilemind.veloster.tools.VelosterRepository;
import br.com.mobilemind.api.utils.ClassUtil;
import br.com.mobilemind.veloster.orm.annotations.Column;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ricardo Bocchi
 */
public class DynamicFinderProcessor<T extends Entity> implements InvocationHandler {

    private static final Map<String, List<Finder>> CACHE = new HashMap<String, List<Finder>>();
    private final Class<T> clazz;

    public DynamicFinderProcessor(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();
        List<Finder> finders = null;
        final String key = proxy.getClass() + "#" + methodName;

        try {
            return processMethod(methodName, method, args);
        } catch (MethodNotMetchException e) {
        }

        if (CACHE.containsKey(key)) {
            finders = CACHE.get(key);
        } else {
            finders = format(methodName);
            CACHE.put(key, finders);
        }

        Criteria criteria = generateCriteria(finders, args);

        if (methodName.startsWith("findAll")) {
            return criteria.list();
        } else if (methodName.startsWith("findBy")) {
            return criteria.load();
        } else if (methodName.startsWith("countBy")) {
            return criteria.count();
        } else {
            throw new VelosterException("the method name [" + methodName + "] does not match finders dynamics. method should start with findBy or findAllBy");
        }
    }

    private List<Finder> format(String methodName) {
        List<Finder> finders = new LinkedList<Finder>();
        //65 - 90
        char c = 0;
        String word = "";
        String key = "";
        Finder finder = null;
        boolean isReserved = false;
        for (int i = 0; i <= methodName.length(); i++) {

            if (i < methodName.length()) {
                c = methodName.charAt(i);
            }

            if (!isReserved(key) || isNotIsNull(key, i, methodName)) {
                key += c + "";
            } else {

                if (isIgnoreCase(key)) {
                    if (finder == null) {
                        throw new VelosterException("parameter [IgnoreCase] is not in the right place");
                    }
                    if (!ClassUtil.isString(finder.field.getType())) {
                        throw new VelosterException("parameter [IgnoreCase] should used with String type");
                    }

                    finder.isIgnoreCase = true;
                    key = c + "";
                    continue;
                }

                if (isStarstWith(key)) {
                    finder.isStartsWith = true;
                    key = c + "";
                    continue;
                }

                if (isEndsWith(key)) {
                    finder.isEndsWith = true;
                    key = c + "";
                    continue;
                }

                if (isAnyWhere(key)) {
                    finder.isAnyWhere = true;
                    key = c + "";
                    continue;
                }
                if (isIsNull(key)) {
                    finder.isNull = true;
                    key = c + "";
                    continue;
                }

                if (isIsNotNull(key)) {
                    finder.isNotNull = true;
                    key = c + "";
                    continue;
                }

                if (isNot(key)) {
                    finder.isNot = true;
                    key = c + "";
                    continue;
                }

                if (isOrderByDesc(key, i, methodName)) {
                    finder.isOrderByDesc = true;
                    key = c + "";
                    continue;
                }

                if (isBetween(key)) {
                    finder.isBetween = true;
                    key = c + "";
                    continue;
                }

                if ("".equals(word)) {
                    c = (char) (c + 32);
                }
                word += c;
                Field f = ClassUtil.getField(clazz, word);
                if (f != null) {
                    if (f.getAnnotation(Column.class) == null) {
                        continue;
                    }

                    finder = new Finder();
                    finder.field = f;
                    finder.fieldName = word;
                    finder.key = key;
                    finders.add(finder);
                    key = word = "";
                }

            }
        }

        if (finders.isEmpty()) {
            throw new VelosterException("no dynamic finder was built");
        }

        return finders;
    }

    private Criteria generateCriteria(List<Finder> finders, Object... args) {

        final Veloster<T> veloster = VelosterRepository.getVeloster(clazz);

        if (veloster == null) {
            throw new VelosterException("ORMManager not found to entity class [" + clazz.getName() + "]");
        }

        Criteria criteria = veloster.createCriteria();
        int i = 0;

        for (Finder finder : finders) {
            Expression e = null;

            if (isWhere(finder.key) || "And".equals(finder.key)) {

                Match match = null;

                if (finder.isAnyWhere) {
                    match = Match.ANYWHERE;
                } else if (finder.isStartsWith) {
                    match = Match.STARTWITH;
                } else if (finder.isEndsWith) {
                    match = Match.ENDWITH;
                }

                if (finder.isBetween) {
                    Object args1 = args[i];
                    Object args2 = args[++i];
                    if (args2 == null) {
                        throw new VelosterException("second parameter to between [" + finder.field.getName() + "] can1t be null");
                    }
                    e = new Between(args1, args2);
                } else if (match != null || finder.isIgnoreCase) {
                    e = new Like(args[i], match != null ? match : Match.EXACT, finder.isIgnoreCase, finder.isNot);
                } else {
                    if (finder.isNot) {
                        e = new Ne(args[i]);
                    } else {
                        if (finder.isNull) {
                            criteria.add(finder.fieldName, new IsNull());
                            continue;
                        } else if (finder.isNotNull) {
                            criteria.add(finder.fieldName, new NotIsNull());
                            continue;
                        } else {
                            e = new Eq(args[i]);
                        }
                    }
                }
            } else if ("OrderBy".equals(finder.key) || "findAllOrderBy".equals(finder.key)) {
                if (finder.isOrderByDesc) {
                    criteria.orderByDesc(finder.fieldName);
                } else {
                    criteria.orderBy(finder.fieldName);
                }
                continue;
            }

            criteria.add(finder.fieldName, e);
            i++;
        }

        return criteria;
    }

    private boolean isReserved(String key) {
        return "findBy".equals(key)
                || "findAllBy".equals(key)
                || "countBy".equals(key)
                || "And".equals(key)
                || "Between".equals(key)
                || "StartsWith".equals(key)
                || "EndsWith".equals(key)
                || "OrderBy".equals(key)
                || "OrderDesc".equals(key)
                || "IgnoreCase".equals(key)
                || "AnyWhere".equals(key)
                || "Not".equals(key)
                || "findAllOrderBy".equals(key)
                || "IsNull".equals(key)
                || "NotIsNull".equals(key)
                || "findAllIsNull".equals(key)
                || "findAllNotIsNull".equals(key)
                || "findByIsNull".equals(key)
                || "findByNotIsNull".equals(key);
    }

    private boolean isOrderByDesc(String key, int pos, String methodName) {
        return "OrderDesc".equals(key);
    }

    private boolean isNotIsNull(String key, int pos, String methodName) {
        if ("Not".equals(key)) {
            if ((pos + 6) <= methodName.length()) {
                if ("IsNull".equals(methodName.substring(pos, pos + 6))) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isIsNull(String key) {
        return "IsNull".equals(key);

    }

    public boolean isIsNotNull(String key) {
        return "NotIsNull".equals(key);
    }

    private boolean isIgnoreCase(String key) {
        return "IgnoreCase".equals(key);
    }

    private boolean isWhere(String key) {
        return "findBy".equals(key)
                || "findAllBy".equals(key)
                || "listBy".equals(key)
                || "listAllBy".equals(key)
                || "countBy".equals(key);
    }

    private boolean isStarstWith(String key) {
        return "StartsWith".equals(key);
    }

    private boolean isEndsWith(String key) {
        return "EndsWith".equals(key);
    }

    private boolean isBetween(String key) {
        return "Between".equals(key);
    }

    private boolean isAnyWhere(String key) {
        return "AnyWhere".equals(key);
    }

    private boolean isNot(String key) {
        return "Not".equals(key);
    }

    private Object processMethod(String methodName, Method method, Object[] args) {

        final Veloster<T> veloster = VelosterRepository.getVeloster(clazz);

        if ("persist".equals(methodName)) {

            if (args == null || args.length != 1 || args[0].getClass() != clazz) {
                throw new VelosterException("persist method should a paremeter of type [" + clazz.getName() + "]");
            }

            return veloster.save((T) args[0]);

        } else if ("persistWithoutValidation".equals(methodName)) {

            if (args == null || args.length != 1 || args[0].getClass() != clazz) {
                throw new VelosterException("persistWithoutValidation method should a paremeter of type [" + clazz.getName() + "]");
            }

            return veloster.saveWithoutValidation((T) args[0]);

        } else if ("merge".equals(methodName)) {

            if (args == null || args.length != 1 || args[0].getClass() != clazz) {
                throw new VelosterException("merge method should a paremeter of type [" + clazz.getName() + "]");
            }

            veloster.update((T) args[0]);

        } else if ("mergeWithoutValidation".equals(methodName)) {

            if (args == null || args.length != 1 || args[0].getClass() != clazz) {
                throw new VelosterException("mergeWithoutValidation method should a paremeter of type [" + clazz.getName() + "]");
            }

            veloster.updateWithoutValidation((T) args[0]);

        } else if ("remove".equals(methodName)) {

            if (args == null || args.length != 1) {
                throw new VelosterException("remove method should be one parameter");
            }

            veloster.delete((T) args[0]);

        } else if ("removeByCriteria".equals(methodName)) {

            if (args == null && args.length != 1 && !(args[0] instanceof Criteria)) {
                throw new VelosterException("remove method should not any parameters");
            }
            veloster.deleteByCriteria((Criteria) args[0]);

        } else if ("load".equals(methodName)) {

            if (args != null && args.length != 1 && !ClassUtil.isLong(args[0])) {
                throw new VelosterException("load method should a paremeter of type [Long]");
            }
            return veloster.load(new Long(args[0].toString()));

        } else if ("loadByCriteria".equals(methodName)) {

            if (args == null || args.length != 1 || !(args[0] instanceof Criteria)) {
                throw new VelosterException("parameter to loadByCriteria should be Criteria");
            }
            return veloster.loadByCriteria((Criteria) args[0]);

        } else if ("count".equals(methodName)) {

            if (args != null && args.length != 0) {
                throw new VelosterException("count method should not any parameters");
            }
            return veloster.count();

        } else if ("countByCriteria".equals(methodName)) {

            if (args == null || args.length != 1 || !(args[0] instanceof Criteria)) {
                throw new VelosterException("parameter to countByCriteria should be Criteria");
            }
            return veloster.countByCriteria((Criteria) args[0]);

        } else if ("list".equals(methodName)) {


            if (args != null && args.length >= 2) {
                throw new VelosterException("list method not metch");
            } else if (args != null && args.length == 2) {
                if (!ClassUtil.isInteger(args[0]) || !ClassUtil.isInteger(args[1])) {
                    throw new VelosterException("list method not metch");
                }

                return veloster.list(new Integer(args[0].toString()), new Integer(args[1].toString()));
            }

            return veloster.list();

        } else if ("listByCriteria".equals(methodName)) {

            if (args != null && (args.length == 1 || args.length == 3)) {

                if (!(args[0] instanceof Criteria)) {
                    throw new VelosterException("listByCriteria method not metch");
                }

                if (args.length == 3) {
                    if (!ClassUtil.isInteger(args[1]) || !ClassUtil.isInteger(args[2])) {
                        throw new VelosterException("listByCriteria method not metch");
                    }
                    return veloster.listByCriteria((Criteria) args[0], new Integer(args[1].toString()), new Integer(args[2].toString()));
                } else {
                    return veloster.listByCriteria((Criteria) args[0]);
                }
            } else {
                throw new VelosterException("listByCriteria method not metch");
            }
        } else if ("createCriteria".equals(methodName)) {

            if (args != null && args.length != 0) {
                throw new VelosterException("createCriteria method should not any parameters");
            }
            return veloster.createCriteria();

        } else if ("getVeloster".equals(methodName)) {
            return veloster;
        } else {
            throw new MethodNotMetchException();
        }

        return null;
    }

    static class Finder {

        String key;
        String fieldName;
        Field field;
        boolean isIgnoreCase;
        boolean isOrderBy;
        boolean isOrderByDesc;
        boolean isStartsWith;
        boolean isEndsWith;
        boolean isAnyWhere;
        boolean isNot;
        boolean isNull;
        boolean isNotNull;
        boolean isBetween;
    }
}

class MethodNotMetchException extends VelosterException {
}