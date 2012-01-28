package org.open18.model.meta;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.open18.model.Course;
import org.open18.model.Hole;
import org.open18.model.Tee;

@StaticMetamodel(Hole.class)
public abstract class Hole_ {

	public static volatile SetAttribute<Hole, Tee> tees;
	public static volatile SingularAttribute<Hole, Course> course;
	public static volatile SingularAttribute<Hole, Long> id;
	public static volatile SingularAttribute<Hole, Integer> mensPar;
	public static volatile SingularAttribute<Hole, Integer> mensHandicap;
	public static volatile SingularAttribute<Hole, Integer> ladiesHandicap;
	public static volatile SingularAttribute<Hole, String> name;
	public static volatile SingularAttribute<Hole, Integer> number;
	public static volatile SingularAttribute<Hole, Integer> ladiesPar;

}

