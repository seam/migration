package org.open18.model;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

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

