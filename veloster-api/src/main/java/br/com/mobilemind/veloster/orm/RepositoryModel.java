package br.com.mobilemind.veloster.orm;

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
import br.com.mobilemind.veloster.orm.model.Entity;
import br.com.mobilemind.veloster.sql.type.Criteria;
import java.util.List;

/**
 *
 * @author Ricardo Bocchi
 */
public interface RepositoryModel<T extends Entity> {

    void persist(T entity);

    void persistWithoutValidation(T entity);

    void mergeWithoutValidation(T entity);

    void merge(T entity);

    void remove(T entity);

    void removeByCriteria(Criteria<T> criteria);

    T load(Long id);

    T loadByCriteria(Criteria<T> criteria);

    int count();

    int countByCriteria(Criteria<T> criteria);

    List<T> list();

    List<T> list(int limit, int offser);

    List<T> listByCriteria(Criteria<T> criteria);

    List<T> listByCriteria(Criteria<T> criteria, int limit, int offset);

    Criteria<T> createCriteria();
    
    Veloster<T> getVeloster();
}
