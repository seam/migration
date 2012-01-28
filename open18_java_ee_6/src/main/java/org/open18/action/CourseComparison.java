package org.open18.action;

import java.io.Serializable;

//@Name("courseComparison")
//@Scope(ScopeType.CONVERSATION)
public class CourseComparison implements Serializable {
/*
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
*/
}
