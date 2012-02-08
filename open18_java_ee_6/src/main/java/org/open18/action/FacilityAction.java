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

package org.open18.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.metamodel.SingularAttribute;

import org.open18.model.Facility;
import org.open18.model.Facility_;
import org.open18.model.dao.FacilityDao;

/**
 *
 */
@Stateful
@ConversationScoped
@Named
public class FacilityAction implements Serializable {

    private static final long serialVersionUID = 6201511634440442162L;

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Inject
    private transient FacilityDao dao;

    private Facility facility;

    private String from;

    private List<Facility> resultList;

    @Inject
    private Conversation conversation;

    @Inject
    private void init() {
        facility = new Facility();
        this.loadResults();
    }

    public void loadResults() {
        final SingularAttribute<Facility, ?>[] attribs = findFilledAttributes();

        if (attribs.length == 0) {
            resultList = dao.findAll();
        } else {
            resultList = dao.findBy(facility, attribs);
        }
    }

    public void search() {
        if (conversation.isTransient()) {
            conversation.begin();
        }

        loadResults();
    }

    @SuppressWarnings("unchecked")
    private SingularAttribute<Facility, ?>[] findFilledAttributes() {
        final List<SingularAttribute<Facility, ?>> filledAttribs = new ArrayList<SingularAttribute<Facility, ?>>(13);

        if (facility.getAddress() != null && !facility.getAddress().isEmpty()) {
            filledAttribs.add(Facility_.address);
        }

        if (facility.getType() != null && !facility.getType().isEmpty()) {
            filledAttribs.add(Facility_.type);
        }

        if (facility.getCity() != null && !facility.getCity().isEmpty()) {
            filledAttribs.add(Facility_.city);
        }

        if (facility.getCountry() != null && !facility.getCountry().isEmpty()) {
            filledAttribs.add(Facility_.country);
        }

        if (facility.getCounty() != null && !facility.getCounty().isEmpty()) {
            filledAttribs.add(Facility_.county);
        }

        if (facility.getDescription() != null && !facility.getDescription().isEmpty()) {
            filledAttribs.add(Facility_.description);
        }

        if (facility.getName() != null && !facility.getName().isEmpty()) {
            filledAttribs.add(Facility_.name);
        }

        if (facility.getPhone() != null && !facility.getPhone().isEmpty()) {
            filledAttribs.add(Facility_.phone);
        }

        if (facility.getState() != null && !facility.getState().isEmpty()) {
            filledAttribs.add(Facility_.state);
        }

        if (facility.getZip() != null && !facility.getZip().isEmpty()) {
            filledAttribs.add(Facility_.zip);
        }

        if (facility.getUri() != null && !facility.getUri().isEmpty()) {
            filledAttribs.add(Facility_.uri);
        }

        if (filledAttribs.size() == 0) {
            return new SingularAttribute[0];
        }

        return filledAttribs.toArray(new SingularAttribute[0]);
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<Facility> getResultList() {
        return resultList;
    }

    public void setResultList(List<Facility> resultList) {
        this.resultList = resultList;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}
