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

import javax.ejb.Stateful;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.open18.extension.ViewScoped;
import org.open18.model.Course;
import org.open18.model.dao.CourseDao;

/**
 *
 */
@Stateful
@ViewScoped
@Named
public class CourseSearch implements Serializable {

    private static final long serialVersionUID = 2281839629956903065L;

    @Inject
    private CourseDao dao;

    private List<Course> resultList;

    private Course course;

    @Inject
    private void init() {
        course = new Course();
        resultList = Collections.emptyList();
    }

    public void search() {
        resultList = dao.findBy(course);
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course newCourse) {
        course = newCourse;
    }

    public List<Course> getResultList() {
        return resultList;
    }

    public void setResultList(List<Course> resultList) {
        this.resultList = resultList;
    }

    @Produces
    @Named("allCourses")
    @ViewScoped
    public List<Course> getAllCourses() {
        return dao.findAll();
    }
}
