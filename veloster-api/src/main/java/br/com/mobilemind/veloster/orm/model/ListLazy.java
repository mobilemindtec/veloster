package br.com.mobilemind.veloster.orm.model;

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
import br.com.mobilemind.veloster.orm.Veloster;
import br.com.mobilemind.veloster.sql.type.Criteria;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Ricardo Bocchi
 */
public class ListLazy<T extends Entity> extends LinkedList<T> {

    private Class<T> clazz;
    private Veloster<T> manager;
    private Criteria<T> criteria;
    private int size;
    private int offset = -1;
    private int pageSize;
    private boolean lazy;
    private List<T> list = new LinkedList<T>();
    private List<T> deletedList = new LinkedList<T>();

    public ListLazy(Criteria<T> criteria, Class<T> clazz, int pageSize, boolean lazy) {
        this.clazz = clazz;
        this.pageSize = pageSize > 0 ? pageSize : 20;
        this.criteria = criteria;
        this.size = criteria.count();
        this.lazy = lazy;
    }

    @Override
    public T get(int index) {

        if (!lazy) {
            if (this.list.size() == size) {
                return this.list.get(index);
            }
            list.addAll(criteria.list());
            return list.get(index);
        }

        if (index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        List<T> items = null;

        if (index > offset) {
            if (index > (this.list.size() - 1)) {
                criteria.setLimit(pageSize)
                        .setOffset(offset)
                        .setAutoIncrementOffset(true);
                items = criteria.list();
                offset = criteria.getOffset();
                this.list.addAll(items);
            }
        }
        return list.get(index);
    }

    @Override
    public T set(int index, T element) {
        return this.list.set(index, element);
    }

    @Override
    public boolean add(T e) {
        if (this.list.add(e)) {
            size++;
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (this.list.addAll(c)) {
            size += c.size();
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {

        for (T it : this) {
            if (!this.deletedList.contains(it)) {
                this.deletedList.add(it);
            }
        }

        this.list.clear();
        this.size = 0;
        this.offset = 0;
    }

    @Override
    public boolean contains(Object o) {
        return this.list.contains(o);
    }

    @Override
    public int indexOf(Object o) {
        return this.list.indexOf(o);
    }

    @Override
    public boolean remove(Object o) {
        if (this.list.remove(o)) {
            size -= 1;
            deletedList.add((T) o);
            return true;
        }

        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (this.list.removeAll(c)) {
            size -= c.size();
            deletedList.addAll((List<T>) c);
            return true;
        }

        return false;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.list.toArray(a);
    }

    @Override
    public Object[] toArray() {
        return this.list.toArray();
    }

    public boolean isLazy() {
        return lazy;
    }

    public List<T> getDeletedList() {
        return deletedList;
    }

    @Override
    public Iterator<T> iterator() {

        return new Iterator<T>() {
            int count = 0;

            @Override
            public boolean hasNext() {
                return size > count;
            }

            @Override
            public T next() {
                return get(count++);
            }

            @Override
            public void remove() {
                ListLazy.this.remove(get(count));
            }
        };
    }
}
