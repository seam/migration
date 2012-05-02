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

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.open18.extension.ViewScoped;
import org.open18.model.TeeSet;

/**
 *
 */
@ViewScoped
public class TeeSetDao extends BaseDao<TeeSet, Long> {
    public TeeSetDao() {
        this.entityType = TeeSet.class;
        this.idType = Long.class;
    }

    public List<String> getAllColors() {
        return this.em.createQuery("select distinct ts.color from TeeSet ts", String.class).getResultList();
    }

    @Produces
    @ViewScoped
    @Named
    public List<TeeSet> getAllTeeSets() {
        return this.findAll();
    }
}
