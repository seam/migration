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
import java.util.Comparator;
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
import org.open18.model.Facility;
import org.open18.model.Hole;
import org.open18.model.TeeSet;
import org.open18.model.dao.CourseDao;

/**
 *
 */
@ConversationScoped
@Named
@Stateful
public class CourseAction implements Serializable {

    private static final long serialVersionUID = 2281839629956903065L;

    @Inject
    private CourseDao dao;

    @Inject
    private Conversation conversation;

    private Long courseId;

    private Course course;

    private boolean managed;

    @Inject
    private void init() {
        course = new Course();
    }

    public void loadCourse() {
        if (this.courseId != null && !FacesContext.getCurrentInstance().isPostback()) {
            this.course = this.dao.findBy(this.courseId);
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
        dao.saveAndFlush(course);
        endConversation();
        return "/CourseList.xhtml";
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String update() {
        dao.saveAndFlush(course);
        return "/Course.xhtml?courseId=" + course.getId();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String remove() {
        dao.remove(course);
        endConversation();
        return "/CourseList.xhtml";
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long newCourseId) {
        if (newCourseId != null && !newCourseId.equals(course.getId())) {
            courseId = newCourseId;
            course = dao.findBy(newCourseId);
            managed = true;

            if (course == null) {
                managed = false;
                course = new Course();
                final FacesContext fc = FacesContext.getCurrentInstance();
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No Course found with id " + newCourseId, ""));
            }

            this.beginConversation();
        }
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course newCourse) {
        course = newCourse;
    }

    public boolean isManaged() {
        return managed;
    }

    public void setManaged(boolean newManaged) {
        managed = newManaged;
    }

    public List<Hole> getHoles() {
        if (!this.managed) {
            return Collections.emptyList();
        }

        List<Hole> holes =
                new ArrayList<Hole>(this.course.getHoles());
        Collections.sort(holes, new Comparator<Hole>() {

            public int compare(Hole a, Hole b) {
                return Integer.valueOf(a.getNumber()).compareTo(b.getNumber());
            }
        });

        return holes;
    }

    public List<TeeSet> getTeeSets() {
        if (!this.managed) {
            return Collections.emptyList();
        }

        List<TeeSet> teeSets =
                new ArrayList<TeeSet>(this.course.getTeeSets());
        Collections.sort(teeSets, new Comparator<TeeSet>() {

            public int compare(TeeSet a, TeeSet b) {
                return a.getPosition() == null ||
                        b.getPosition() == null ? 0 : a.getPosition().compareTo(b.getPosition());
            }
        });

        return teeSets;
    }

    public void selectFacility(Facility facility) {
        this.course.setFacility(facility);
    }
}
