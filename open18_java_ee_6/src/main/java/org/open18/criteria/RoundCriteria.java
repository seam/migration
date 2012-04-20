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

package org.open18.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Named;

import org.open18.extension.ViewScoped;
import org.open18.model.Course;

@Named
@ViewScoped
public class RoundCriteria implements Serializable {
    private static final long serialVersionUID = -5411248134346962848L;

    private String golferFirstName;
    private String golferLastName;
    private Date beforeDate;
    private Date afterDate;
    private boolean self = false;
    private List<Course> courses = new ArrayList<Course>();
    private List<Course> allCourses;
    private String teeSetColor;

    public String getGolferFirstName() { return this.golferFirstName; }
    public void setGolferFirstName(String name) { this.golferFirstName = name; }

    public void setBeforeDate(Date date) { this.beforeDate = date; }
    public Date getBeforeDate() { return this.beforeDate; }

    public String getGolferLastName() { return golferLastName; }
    public void setGolferLastName(String golferLastName) { this.golferLastName = golferLastName; }

    public Date getAfterDate() { return this.afterDate; }
    public void setAfterDate(Date date) { this.afterDate = date; }

    public boolean isSelf () { return this.self; }
    public void setSelf (boolean self) { this.self = self; }

    public List<Course> getCourses() { return this.courses; }
    public void setCourses(List<Course> courses) { this.courses = courses; }

    public String getTeeSetColor() { return teeSetColor; }
    public void setTeeSetColor(String teeSetColor) { this.teeSetColor = teeSetColor; }
}

