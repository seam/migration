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
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.open18.model.Tee;
import org.open18.model.TeeSet;
import org.open18.model.dao.TeeSetDao;

/**
 *
 */
@ConversationScoped
@Named
@Stateful
public class TeeSetAction implements Serializable {

    private static final long serialVersionUID = 2281839629956903065L;

    @Inject
    private TeeSetDao dao;

    @Inject
    private transient Conversation conversation;

    private Long teeSetId;

    private TeeSet teeSet;

    private boolean managed;

    @Inject
    private void init() {
        teeSet = new TeeSet();
    }

    public void loadTeeSet() {
        if (this.teeSetId != null && !FacesContext.getCurrentInstance().isPostback()) {
            this.teeSet = this.dao.findBy(teeSetId);
            this.managed = true;
        }
        beginConversation();
    }

    public void beginConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
        }
    }

    public void endConversation() {
        if (!conversation.isTransient()) {
            conversation.end();
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String save() {
        dao.saveAndFlush(teeSet);
        endConversation();
        return "/TeeSetList.xhtml";
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String update() {
        dao.saveAndFlush(teeSet);
        return "/TeeSet.xhtml?teeSetId=" + teeSet.getId();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String remove() {
        dao.remove(teeSet);
        endConversation();
        return "/TeeSetList.xhtml";
    }

    public Long getTeeSetId() {
        return this.teeSetId;
    }

    public void setTeeSetId(Long newTeeSetId) {
        if (newTeeSetId != null) {
            this.teeSetId = newTeeSetId;
        }
    }

    public TeeSet getTeeSet() {
        return teeSet;
    }

    public void setTeeSet(TeeSet newTee) {
        teeSet = newTee;
    }

    public boolean isManaged() {
        return managed;
    }

    public void setManaged(boolean newManaged) {
        managed = newManaged;
    }

    @SuppressWarnings("unchecked")
    public List<Tee> getTees() {
        return teeSet.getTees() == null ? Collections.EMPTY_LIST : new ArrayList<Tee>(teeSet.getTees());
    }
}
