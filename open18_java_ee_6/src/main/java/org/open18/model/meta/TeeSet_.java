package org.open18.model.meta;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.open18.model.Course;
import org.open18.model.Tee;
import org.open18.model.TeeSet;

@StaticMetamodel(TeeSet.class)
public abstract class TeeSet_ {

	public static volatile SetAttribute<TeeSet, Tee> tees;
	public static volatile SingularAttribute<TeeSet, Integer> position;
	public static volatile SingularAttribute<TeeSet, Double> mensSlopeRating;
	public static volatile SingularAttribute<TeeSet, Course> course;
	public static volatile SingularAttribute<TeeSet, Long> id;
	public static volatile SingularAttribute<TeeSet, Double> courseRating;
	public static volatile SingularAttribute<TeeSet, String> color;
	public static volatile SingularAttribute<TeeSet, Double> slopeRating;
	public static volatile SingularAttribute<TeeSet, String> name;
	public static volatile SingularAttribute<TeeSet, Double> ladiesCourseRating;
	public static volatile SingularAttribute<TeeSet, Double> ladiesSlopeRating;
	public static volatile SingularAttribute<TeeSet, Double> mensCourseRating;

}

