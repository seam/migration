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

import org.open18.extension.ViewScoped;
import org.open18.model.TeeSet;
import org.open18.model.dao.TeeSetDao;

/**
 *
 */
@ViewScoped
@Named
public class TeeSetSearch implements Serializable {
    private static final long serialVersionUID = 3274792096739222125L;

    @Inject
    private TeeSetDao dao;

    private List<TeeSet> resultList;

    private TeeSet teeSet;

    @Inject
    private void init() {
        teeSet = new TeeSet();
        resultList = Collections.emptyList();
    }

    public void search() {
        resultList = dao.findBy(teeSet);
    }

    public TeeSet getTeeSet() {
        return teeSet;
    }

    public void setTeeSet(TeeSet teeSet) {
        this.teeSet = teeSet;
    }

    public List<TeeSet> getResultList() {
        return resultList;
    }

    public void setResultList(List<TeeSet> resultList) {
        this.resultList = resultList;
    }
}
