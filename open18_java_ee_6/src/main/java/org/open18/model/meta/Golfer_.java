package org.open18.model.meta;

import java.util.Date;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.open18.model.CourseComment;
import org.open18.model.Gender;
import org.open18.model.Golfer;
import org.open18.model.Round;

@StaticMetamodel(Golfer.class)
public abstract class Golfer_ extends Member_ {

	public static volatile SingularAttribute<Golfer, Date> dateOfBirth;
	public static volatile SingularAttribute<Golfer, String> lastName;
	public static volatile SingularAttribute<Golfer, String> location;
	public static volatile SingularAttribute<Golfer, String> proStatus;
	public static volatile SingularAttribute<Golfer, Gender> gender;
	public static volatile SetAttribute<Golfer, Round> rounds;
	public static volatile SetAttribute<Golfer, CourseComment> courseComments;
	public static volatile SingularAttribute<Golfer, Date> dateJoined;
	public static volatile SingularAttribute<Golfer, String> firstName;
	public static volatile SingularAttribute<Golfer, String> specialty;

}

