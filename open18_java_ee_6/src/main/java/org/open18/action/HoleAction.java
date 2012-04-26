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
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.open18.model.Course;
import org.open18.model.Hole;
import org.open18.model.Tee;
import org.open18.model.dao.HoleDao;

/**
 *
 */
@ConversationScoped
@Named
@Stateful
public class HoleAction implements Serializable {

    private static final long serialVersionUID = 2281839629956903065L;

    @Inject
    private HoleDao dao;

    @Inject
    private transient Conversation conversation;

    private Long holeId;

    private Hole hole;

    private boolean managed;

    @Inject
    private void init() {
        hole = new Hole();
    }

    public void loadHole() {
        if (this.holeId != null && !FacesContext.getCurrentInstance().isPostback()) {
            this.hole = this.dao.findBy(this.holeId);
            this.managed = true;
        }
        this.beginConversation();
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
        dao.saveAndFlush(hole);
        endConversation();
        return "/HoleList.xhtml";
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String update() {
        dao.saveAndFlush(hole);
        return "/Hole.xhtml?holeId=" + hole.getId();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String remove() {
        dao.remove(hole);
        endConversation();
        return "/HoleList.xhtml";
    }

    public Long getHoleId() {
        return holeId;
    }

    public void setHoleId(Long newHoleId) {
        if (newHoleId != null && !newHoleId.equals(hole.getId())) {
            holeId = newHoleId;
            hole = dao.findBy(newHoleId);
            managed = true;

            if (hole == null) {
                managed = false;
                hole = new Hole();
                final FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Hole found with id " + newHoleId, ""));
            }

            this.beginConversation();
        }
    }

    public Hole getHole() {
        return hole;
    }

    public void setHole(Hole newCourse) {
        hole = newCourse;
    }

    public boolean isManaged() {
        return managed;
    }

    public void setManaged(boolean newManaged) {
        managed = newManaged;
    }

    public void selectCourse(Course course) {
        this.hole.setCourse(course);
    }

    @SuppressWarnings("unchecked")
    public List<Tee> getTees() {
        return hole.getTees() == null ? Collections.EMPTY_LIST : new ArrayList<Tee>(hole.getTees());
    }
}
