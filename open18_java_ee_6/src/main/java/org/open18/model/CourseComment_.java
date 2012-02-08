package org.open18.model;

import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.open18.model.Course;
import org.open18.model.CourseComment;
import org.open18.model.Golfer;

@StaticMetamodel(CourseComment.class)
public abstract class CourseComment_ {

	public static volatile SingularAttribute<CourseComment, Course> course;
	public static volatile SingularAttribute<CourseComment, Long> id;
	public static volatile SingularAttribute<CourseComment, String> text;
	public static volatile SingularAttribute<CourseComment, Date> datePosted;
	public static volatile SingularAttribute<CourseComment, Golfer> golfer;
	public static volatile SingularAttribute<CourseComment, Integer> version;

}

