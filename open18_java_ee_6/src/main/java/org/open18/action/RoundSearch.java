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

import javax.inject.Inject;
import javax.inject.Named;

import org.open18.criteria.RoundCriteria;
import org.open18.extension.ViewScoped;
import org.open18.model.Round;
import org.open18.model.dao.RoundDao;
import org.open18.model.dao.TeeSetDao;

/**
 *
 */
@ViewScoped
@Named
public class RoundSearch implements Serializable {
    @Inject
    private RoundDao dao;

    private List<Round> resultList;

    @Inject
    private RoundCriteria roundCriteria;

    @Inject
    private TeeSetDao teeSetDao;

    @Inject
    private void init() {
        resultList = Collections.emptyList();
    }

    public void search() {
        resultList = dao.findBy(roundCriteria);
    }

    public Double getAverageScore() {
        return dao.averageScore(null);
    }

    public List<String> getAllTeeSetColors() {
        return teeSetDao.getAllColors();
    }

    public List<Round> getResultList() {
        return resultList;
    }

    public void setResultList(List<Round> resultList) {
        this.resultList = resultList;
    }
}
