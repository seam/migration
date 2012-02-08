package org.open18.model;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.open18.model.Course;
import org.open18.model.Facility;
import org.open18.model.Golfer;

@StaticMetamodel(Facility.class)
public abstract class Facility_ {

	public static volatile SingularAttribute<Facility, String> zip;
	public static volatile SingularAttribute<Facility, String> phone;
	public static volatile SetAttribute<Facility, Course> courses;
	public static volatile SingularAttribute<Facility, String> state;
	public static volatile SingularAttribute<Facility, String> type;
	public static volatile SingularAttribute<Facility, String> uri;
	public static volatile SingularAttribute<Facility, String> city;
	public static volatile SingularAttribute<Facility, String> country;
	public static volatile SingularAttribute<Facility, Long> id;
	public static volatile SingularAttribute<Facility, Integer> priceRange;
	public static volatile SingularAttribute<Facility, String> address;
	public static volatile SingularAttribute<Facility, String> county;
	public static volatile SingularAttribute<Facility, String> description;
	public static volatile SingularAttribute<Facility, String> name;
	public static volatile SingularAttribute<Facility, Golfer> owner;

}

