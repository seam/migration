package org.open18.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.open18.model.Course;
import org.open18.model.dao.CourseDao;

@Named
@ConversationScoped
public class CourseComparison implements Serializable {
    private static final long serialVersionUID = -4881060214215467731L;

    @Inject
    private CourseDao courseDao;

    @Inject
    private transient Conversation conversation;

	private boolean ready = false;

	protected Set<Course> courses = new HashSet<Course>();

	public void mark(Long courseId) {
		Course course = courseDao.findBy(courseId);
		if (course == null) {
			return;
		}
		courses.add(course);
		ready = courses.size() >= 2;

        if (conversation.isTransient()) {
            conversation.begin();
        }
	}

    @Produces
    @Named("comparedCourses")
    @ConversationScoped
    public List<Course> getCourses() {
        return new ArrayList<Course>(courses);
    }

	public boolean isMarked(Course course) {
		return courses.contains(course);
	}

	public void reset() {
		courses.clear();
		ready = false;
	}

	public String validate() {
		return ready ? "valid" : "invalid";
	}

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
