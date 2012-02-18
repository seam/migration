/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.open18.model.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Base DAO class.
 *
 * @param <E>  Entity type.
 * @param <PK> Primary key type.
 */
public abstract class BaseDao<E, PK extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1845865757364398127L;

    protected Class<E> entityType;
    protected Class<PK> idType;

    /**
     * Persist (new entity) or merge the given entity.
     *
     * @param entity Entity to save.
     * @return Returns the modified entity.
     */
    public E save(E entity) {
        checkProperInit();
        if (isPkSet(entity)) {
            return getEntityManager().merge(entity);
        } else {
            getEntityManager().persist(entity);
            return entity;
        }
    }

    /**
     * {@link #save(Object)}s the given entity and flushes the persistence context afterwards.
     *
     * @param entity Entity to save.
     * @return Returns the modified entity.
     */
    public E saveAndFlush(E entity) {
        checkProperInit();
        final E savedEntity = this.save(entity);
        this.flush();

        return savedEntity;
    }

    /**
     * Convenience access to {@link javax.persistence.EntityManager#remove(Object)}.
     *
     * @param entity Entity to remove.
     */
    public void remove(E entity) {
        checkProperInit();
        getEntityManager().remove(entity);
    }

    /**
     * Convenience access to {@link javax.persistence.EntityManager#refresh(Object)}.
     *
     * @param entity Entity to refresh.
     */
    public void refresh(E entity) {
        checkProperInit();
        getEntityManager().refresh(entity);
    }

    /**
     * Convenience access to {@link javax.persistence.EntityManager#flush()}.
     */
    public void flush() {
        checkProperInit();
        getEntityManager().flush();
    }

    /**
     * Entity lookup by primary key. Convenicence method around {@link javax.persistence.EntityManager#find(Class, Object)}.
     *
     * @param primaryKey DB primary key.
     * @return Entity identified by primary or null if it does not exist.
     */
    public E findBy(PK primaryKey) {
        checkProperInit();
        return getEntityManager().find(this.entityType, primaryKey);
    }

    /**
     * Lookup all existing entities of entity class {@code <E>}.
     *
     * @return List of entities, empty if none found.
     */
    public List<E> findAll() {
        checkProperInit();
        final String entityName = getEntityManager().getMetamodel().entity(this.entityType).getName();
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

        final CriteriaQuery<E> query = cb.createQuery(this.entityType);
        final TypedQuery<E> typedQuery = getEntityManager().createQuery(query.select(query.from(this.entityType)));
        return typedQuery.getResultList();
    }

    /**
     * Query by example - for a given object and a specific set of properties.
     *
     * @param example    Sample entity. Query all like.
     * @param attributes Which attributes to consider for the query.
     * @return List of entities matching the example, or empty if none found.
     */
    public List<E> findBy(E example, SingularAttribute<E, ?>... attributes) {
        checkProperInit();
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<E> query = cb.createQuery(this.entityType);
        final Root<E> root = query.from(this.entityType);

        query.select(root).where(cb.and(getAndPredicates(cb, root, example, attributes)));
        final TypedQuery<E> typedQuery = getEntityManager().createQuery(query);
        return typedQuery.getResultList();
    }

    /**
     * Count all existing entities of entity class {@code <E>}.
     *
     * @return Counter.
     */
    public Long count() {
        checkProperInit();
        final CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        final CriteriaQuery<Long> query = cb.createQuery(Long.class);
        query.select(cb.count(query.from(this.entityType)));

        return getEntityManager().createQuery(query).getSingleResult();
    }

    public boolean isManaged(E entity) {
        return getEntityManager().contains(entity);
    }

    protected abstract EntityManager getEntityManager();

    public abstract void setEntityManager(EntityManager em);

    private boolean isPkSet(E entity) {
        checkProperInit();
        final EntityType<?> entityType = getEntityManager().getMetamodel().entity(this.entityType);
        // Cheating as we know there are no entities in this example with @IdClass
        // also cheating here as for this example we know these will all be Methods
        final Method idMember = (Method) entityType.getDeclaredId(this.idType).getJavaMember();
        Object idValue = null;

        try {
            idValue = idMember.invoke(entity);
        } catch (IllegalAccessException e) {
            // eat it
        } catch (InvocationTargetException e) {
            // eat it
        }

        return idValue == null;
    }

    private void checkProperInit() {
        if (this.idType == null || this.entityType == null) {
            throw new IllegalStateException("Dao not properly initialized.");
        }
    }

    private Predicate[] getAndPredicates(CriteriaBuilder cb, Root<E> root, E example, SingularAttribute<E, ?>... attributes) {
        final Predicate[] allPredicates = new Predicate[attributes.length];

        for (int i = 0; i < attributes.length; i++) {
            allPredicates[i] = cb.equal(root.get(attributes[i]), getActualValueOfAttribute(example, attributes[i]));
        }

        return allPredicates;
    }

    private Object getActualValueOfAttribute(E example, SingularAttribute<E, ?> attribute) {
        // Cheating here as we know this will be a method
        final Method attribMethod = (Method) attribute.getJavaMember();

        try {
            return attribMethod.invoke(example);
        } catch (IllegalAccessException e) {
            // Eat it
        } catch (InvocationTargetException e) {
            // Eat it
        }

        return null;
    }
}
