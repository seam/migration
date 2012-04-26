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

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.open18.model.Tee;
import org.open18.model.TeeId;
import org.open18.model.dao.TeeDao;

/**
 *
 */
@ConversationScoped
@Named
@Stateful
public class TeeAction implements Serializable {

    private static final long serialVersionUID = 2281839629956903065L;

    @Inject
    private TeeDao dao;

    @Inject
    private transient Conversation conversation;

    private Long teeHoleId;

    private Long teeSetId;

    private Tee tee;

    private boolean managed;

    @Inject
    private void init() {
        tee = new Tee();
    }

    public void loadTee() {
        if (this.teeSetId != null && this.teeHoleId != null && !FacesContext.getCurrentInstance().isPostback()) {
            this.tee = this.dao.findBy(new TeeId(this.teeSetId, this.teeHoleId));
            this.managed = true;
        }
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
        dao.saveAndFlush(tee);
        endConversation();
        return "/CourseList.xhtml";
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String update() {
        dao.saveAndFlush(tee);
        return "/Course.xhtml?courseId=" + tee.getId();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String remove() {
        dao.remove(tee);
        endConversation();
        return "/CourseList.xhtml";
    }

    public Long getTeeSetId() {
        return this.teeSetId;
    }

    public void setTeeSetId(Long newTeeSetId) {
        if (newTeeSetId != null) {
            this.teeSetId = newTeeSetId;
        }
    }

    public Tee getTee() {
        return tee;
    }

    public void setTee(Tee newTee) {
        tee = newTee;
    }

    public boolean isManaged() {
        return managed;
    }

    public void setManaged(boolean newManaged) {
        managed = newManaged;
    }

    public Long getTeeHoleId() {
        return teeHoleId;
    }

    public void setTeeHoleId(Long teeHoleId) {
        if (teeHoleId != null) {
            this.teeHoleId = teeHoleId;
        }
    }
}
