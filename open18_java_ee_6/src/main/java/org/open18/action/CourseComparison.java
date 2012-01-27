package org.open18.action;

import java.io.Serializable;


import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.annotations.web.RequestParameter;
import org.open18.model.Course;

@Name("courseComparison")
@Scope(ScopeType.CONVERSATION)
public class CourseComparison implements Serializable {

	@In protected EntityManager entityManager;
	
	@RequestParameter protected Long courseId;
	
	@Out("readyToCompare")
	protected boolean ready = false;
	
	@DataModel("comparedCourses")
	protected Set<Course> courses = new HashSet<Course>();

	public void mark() {
		Course course = entityManager.find(Course.class, courseId);
		if (course == null) {
			return;
		}
		courses.add(course);
		ready = courses.size() >= 2;
	}
	
	// NOTE: cannot use @BypassInterceptors because
	// @DataModel field is cleared by ManagedEntityIdentityInterceptor
	public boolean isMarked(Course course) {
		return courses.contains(course);
	}
	
	public void reset() {
		courses.clear();
		ready = false;
	}
	
	@BypassInterceptors
	public String validate() {
		return ready ? "valid" : "invalid";
	}
	
	// NOTE: cannot use @BypassInterceptors because
	// @DataModel field is cleared by ManagedEntityIdentityInterceptor
	public String getCourseNames() {
		System.out.println("here");
		String names = "";
		int i = 0;
		for (Course course : courses) {
			if (i++ > 0) {
				names += ", ";
			}
			names += course.getName();
		}
		
		return names;
	}

}
