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
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.open18.model.Course;
import org.open18.model.dao.CourseDao;

/**
 *
 */
@Stateful
@ConversationScoped
@Named
public class CourseAction implements Serializable {

    private static final long serialVersionUID = 2281839629956903065L;

    @Inject
    private transient CourseDao dao;

    @Inject
    private transient Conversation conversation;

    private Long courseId;

    private Course course;

    private boolean managed;

    private List<Course> resultList;

    @Inject
    private void init() {
        course = new Course();

        if (!FacesContext.getCurrentInstance().isPostback()) {
            resultList = dao.findAll();
        }

        beginConversation();
    }

    public void search() {
        resultList = dao.findBy(course);
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

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long newCourseId) {
        courseId = newCourseId;
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

    public List<Course> getResultList() {
        return resultList;
    }

    public void setResultList(List<Course> newResultList) {
        resultList = newResultList;
    }
}
