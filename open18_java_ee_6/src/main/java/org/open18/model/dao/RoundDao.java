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

import java.util.Arrays;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.open18.criteria.RoundCriteria;
import org.open18.model.Course;
import org.open18.model.Golfer;
import org.open18.model.Golfer_;
import org.open18.model.Round;
import org.open18.model.Round_;
import org.open18.model.TeeSet;
import org.open18.model.TeeSet_;

/**
 *
 */
public class RoundDao extends BaseDao<Round, Long> {
    private static final long serialVersionUID = -7350946401944999968L;

    public RoundDao() {
        this.entityType = Round.class;
        this.idType = Long.class;
    }

    /*
    Creating an override so we can deal with other attributes such as teeSet color, Golder, etc.
     */
    public List<Round> findBy(RoundCriteria criteria) {
        CriteriaBuilder builder = this.em.getCriteriaBuilder();
        CriteriaQuery<Round> roundCriteria = builder.createQuery(Round.class);
        Root<Round> roundFrom = roundCriteria.from(Round.class);

        CriteriaQuery<Round> select = roundCriteria.select(roundFrom);
        Predicate[] predicates = new Predicate[6];
        int i = 0;

        if (criteria.getTeeSetColor() != null && !criteria.getTeeSetColor().trim().equals("")) {
            Fetch<Round, TeeSet> teeSetJoin = roundFrom.fetch(Round_.teeSet);
            predicates[i++] = builder.equal(roundFrom.get(Round_.teeSet).get(TeeSet_.color), criteria.getTeeSetColor());
        }
        if (criteria.getAfterDate() != null) {
            predicates[i++] = builder.greaterThan(roundFrom.get(Round_.date), criteria.getAfterDate());
        }
        if (criteria.getBeforeDate() != null) {
            predicates[i++] = builder.lessThan(roundFrom.get(Round_.date), criteria.getBeforeDate());
        }
        if ((criteria.getGolferFirstName() != null && !criteria.getGolferFirstName().trim().equals(""))
                || criteria.getGolferLastName() != null && !criteria.getGolferLastName().trim().equals("")) {
            Fetch<Round, Golfer> golferFetch = roundFrom.fetch(Round_.golfer);

            if (criteria.getGolferFirstName() != null && !criteria.getGolferFirstName().trim().equals("")) {
                predicates[i++] = builder.like(builder.lower(roundFrom.get(Round_.golfer).get(Golfer_.firstName)),
                        criteria.getGolferFirstName().toLowerCase() + "%");
            }
            if (criteria.getGolferLastName() != null && !criteria.getGolferLastName().trim().equals("")) {
                predicates[i++] = builder.like(builder.lower(roundFrom.get(Round_.golfer).get(Golfer_.lastName)),
                        criteria.getGolferLastName().toLowerCase() + "%");
            }
        }
        if (criteria.getCourses() != null && !criteria.getCourses().isEmpty()) {
            Fetch<TeeSet, Course> courseFetch = roundFrom.fetch(Round_.teeSet).fetch(TeeSet_.course);
            predicates[i++] = roundFrom.get(Round_.teeSet).get(TeeSet_.course).in(criteria.getCourses());
        }

        TypedQuery<Round> finalQuery = this.em.createQuery(roundCriteria.where(builder.and(Arrays.copyOf(predicates, i)))
                .orderBy(builder.desc(roundFrom.get(Round_.date)))).setMaxResults(5);
        return finalQuery.getResultList();
    }

    public Double averageScore(Golfer golfer) {
        if (golfer != null) {
            final TypedQuery<Double> q = this.em.createQuery("select avg(r.totalScore) from Round r join r.golfer where r.golfer = :golfer", Double.class);
            q.setParameter("golfer", golfer);
            return q.getSingleResult();
        } else {
            final TypedQuery<Double> q = this.em.createQuery("select avg(r.totalScore) from Round r ", Double.class);
            return q.getSingleResult();
        }
    }
}
