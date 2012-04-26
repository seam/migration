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
import java.util.Collections;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.open18.extension.ViewScoped;
import org.open18.model.Facility;
import org.open18.model.dao.FacilityDao;

/**
 *
 */
@ViewScoped
@Named
public class FacilitySearch implements Serializable {
    private static final long serialVersionUID = 3274792096739222125L;

    @Inject
    private FacilityDao dao;

    private List<Facility> resultList;

    private Facility facility;

    @Inject
    private void init() {
        facility = new Facility();
        resultList = Collections.emptyList();
    }

    public void search() {
        resultList = dao.findBy(facility);
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public List<Facility> getResultList() {
        return resultList;
    }

    public void setResultList(List<Facility> resultList) {
        this.resultList = resultList;
    }

    @Produces
    @ViewScoped
    @Named
    public List<Facility> getAllFacilities() {
        return dao.getAllFacilities();
    }
}
