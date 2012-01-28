package org.open18.model.meta;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.open18.model.Course;
import org.open18.model.CourseComment;
import org.open18.model.Facility;
import org.open18.model.Hole;
import org.open18.model.TeeSet;

@StaticMetamodel(Course.class)
public abstract class Course_ {

	public static volatile SingularAttribute<Course, String> greens;
	public static volatile SetAttribute<Course, TeeSet> teeSets;
	public static volatile SingularAttribute<Course, String> designer;
	public static volatile SingularAttribute<Course, Integer> yearBuilt;
	public static volatile SingularAttribute<Course, String> fairways;
	public static volatile SingularAttribute<Course, Integer> numHoles;
	public static volatile SingularAttribute<Course, Long> id;
	public static volatile SingularAttribute<Course, Long> signatureHole;
	public static volatile SingularAttribute<Course, Facility> facility;
	public static volatile SingularAttribute<Course, String> description;
	public static volatile SingularAttribute<Course, String> name;
	public static volatile SetAttribute<Course, Hole> holes;
	public static volatile SetAttribute<Course, CourseComment> comments;

}

