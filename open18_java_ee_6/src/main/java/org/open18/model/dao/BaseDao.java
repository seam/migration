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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

/**
 * Base DAO class. This replaces the Seam 2 Home / Query classes.
 * Functionality of the searches can be replicated using the criteria API from JPA or a simple
 * query by example
 *
 * @param <E>  Entity type.
 * @param <PK> Primary key type.
 */
public abstract class BaseDao<E, PK extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1845865757364398127L;

    @Inject
    protected transient EntityManager em;

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
            return em.merge(entity);
        } else {
            em.persist(entity);
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
        em.remove(entity);
    }

    /**
     * Convenience access to {@link javax.persistence.EntityManager#refresh(Object)}.
     *
     * @param entity Entity to refresh.
     */
    public void refresh(E entity) {
        checkProperInit();
        em.refresh(entity);
    }

    /**
     * Convenience access to {@link javax.persistence.EntityManager#flush()}.
     */
    public void flush() {
        checkProperInit();
        em.flush();
    }

    /**
     * Entity lookup by primary key. Convenicence method around {@link javax.persistence.EntityManager#find(Class, Object)}.
     *
     * @param primaryKey DB primary key.
     * @return Entity identified by primary or null if it does not exist.
     */
    public E findBy(PK primaryKey) {
        checkProperInit();
        return em.find(this.entityType, primaryKey);
    }

    /**
     * Lookup all existing entities of entity class {@code <E>}.
     *
     * @return List of entities, empty if none found.
     */
    public List<E> findAll() {
        checkProperInit();
        final String entityName = em.getMetamodel().entity(this.entityType).getName();
        final CriteriaBuilder cb = em.getCriteriaBuilder();

        final CriteriaQuery<E> query = cb.createQuery(this.entityType);
        final TypedQuery<E> typedQuery = em.createQuery(query.select(query.from(this.entityType)));
        return typedQuery.getResultList();
    }

    /**
     * Query by example - for a given object and all filled SingularAttribute fields.
     *
     * @param example Sample entity. Query all like, joined by "and".
     * @return List of entities matching the example, or empty if none found.
     */
    public List<E> findBy(E example) {
        return findBy(example, getFilledSingularAttributes(example));
    }

    /**
     * Query by example - for a given object and a specific set of properties.
     *
     * @param example    Sample entity. Query all like, joined by "and".
     * @param attributes Which attributes to consider for the query.
     * @return List of entities matching the example, or empty if none found.
     */
    public List<E> findBy(E example, SingularAttribute<E, ?>... attributes) {
        checkProperInit();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<E> query = cb.createQuery(this.entityType);
        final Root<E> root = query.from(this.entityType);

        query.select(root).where(cb.and(getAndPredicates(cb, root, example, attributes)));
        final TypedQuery<E> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    /**
     * Count all existing entities of entity class {@code <E>}.
     *
     * @return Counter.
     */
    public Long count() {
        checkProperInit();
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Long> query = cb.createQuery(Long.class);
        query.select(cb.count(query.from(this.entityType)));

        return em.createQuery(query).getSingleResult();
    }

    public boolean isManaged(E entity) {
        return isPkSet(entity) && em.contains(entity);
    }

    private boolean isPkSet(E entity) {
        checkProperInit();
        final EntityType<?> entityType = em.getMetamodel().entity(this.entityType);
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

        return idValue != null;
    }

    private void checkProperInit() {
        if (this.idType == null || this.entityType == null) {
            throw new IllegalStateException("Dao not properly initialized.");
        }
    }

    private Predicate[] getAndPredicates(CriteriaBuilder cb, Root<E> root, E example, SingularAttribute<E, ?>... attributes) {
        final Predicate[] allPredicates = new Predicate[attributes.length];

        if (attributes.length > 0) {
            for (int i = 0; i < attributes.length; i++) {
                allPredicates[i] = cb.equal(root.get(attributes[i]), getActualValueOfAttribute(example, attributes[i]));
            }
        }

        return allPredicates;
    }

    @SuppressWarnings("unchecked")
    private SingularAttribute<E, ?>[] getFilledSingularAttributes(E example) {
        final EntityType<E> entity = em.getMetamodel().entity((Class<E>) example.getClass());
        final Set<SingularAttribute<E, ?>> allSingularAttributes = entity.getDeclaredSingularAttributes();
        SingularAttribute<E, ?>[] filledAttributes = new SingularAttribute[allSingularAttributes.size()];

        try {
            final E newInstance = (E) example.getClass().newInstance();
            final Iterator<SingularAttribute<E, ?>> allAttributeIterator = allSingularAttributes.iterator();

            while (allAttributeIterator.hasNext()) {
                final SingularAttribute<E, ?> attribute = allAttributeIterator.next();

                // Cheating as we know for this example they will always be methods
                final Object exampleValue = ((Method) attribute.getJavaMember()).invoke(example);
                final Object defaultValue = ((Method) attribute.getJavaMember()).invoke(newInstance);

                if (exampleValue == null || exampleValue.equals(defaultValue) || (exampleValue instanceof String && "".equals(exampleValue))) {
                    allAttributeIterator.remove();
                }
            }
            filledAttributes = allSingularAttributes.toArray(new SingularAttribute[allSingularAttributes.size()]);
        } catch (InvocationTargetException e) {
            // Eating the exception as it doesn't matter
        } catch (InstantiationException e) {
            // eating the exception as it doesn't matter
        } catch (IllegalAccessException e) {
            // eating the exception as it doesn't matter
        }
        return filledAttributes;
    }

    private boolean isDefault(Class<?> type, Object value) {
        if (value == null) {
            return true;
        } else if (type.equals(String.class) && value instanceof String) {
            return "".equals(((String) value).trim());
        } else if (type.isPrimitive()) {
            // TODO: all primitive values
        } else {
            try {
                final Object newInstance = type.newInstance();
                return newInstance.equals(value);
            } catch (InstantiationException e) {
                throw new RuntimeException("Error trying to search", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error trying to search", e);
            }
        }

        // Can't imagine we'd get here
        return true;
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
