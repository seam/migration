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
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.open18.model.Round;
import org.open18.model.dao.RoundDao;
import org.open18.model.dao.TeeSetDao;
import org.open18.model.enums.Weather;

/**
 *
 */
@ConversationScoped
@Named
@Stateful
public class RoundAction implements Serializable {

    private static final long serialVersionUID = 2281839629956903065L;

    @Inject
    private RoundDao dao;

    @Inject
    private TeeSetDao teeSetDao;

    @Inject
    private transient Conversation conversation;

    private Long roundId;

    private Round round;

    private boolean managed;

    @Inject
    private void init() {
        round = new Round();
    }

    public void loadCourse() {
        if (this.roundId != null && !FacesContext.getCurrentInstance().isPostback()) {
            this.round = this.dao.findBy(this.roundId);
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
        dao.saveAndFlush(round);
        endConversation();
        return "/RoundList.xhtml";
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String update() {
        dao.saveAndFlush(round);
        return "/Round.xhtml?roundId=" + round.getId();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String remove() {
        dao.remove(round);
        endConversation();
        return "/RoundList.xhtml";
    }

    @Produces
    @Named
    public Weather[] getWeatherCategories() {
        return Weather.values();
    }

//    public List<TeeSet> getTeeSetsByCourse() {
//        TeeSet example = new TeeSet();
//        example.setCourse(this.round.getTeeSet().getCourse());
//        return teeSetDao.findBy(example);
//    }

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long newRoundId) {
        if (newRoundId != null && !newRoundId.equals(round.getId())) {
            roundId = newRoundId;
            round = dao.findBy(newRoundId);
            managed = true;

            if (round == null) {
                managed = false;
                round = new Round();
                final FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Round found with id " + newRoundId, ""));
            }

            this.beginConversation();
        }
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round newRound) {
        round = newRound;
    }

    public boolean isManaged() {
        return managed;
    }

    public void setManaged(boolean newManaged) {
        managed = newManaged;
    }
}
