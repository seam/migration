package org.open18.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Tee.class)
public abstract class Tee_ {

    public static volatile SingularAttribute<Tee, TeeId> id;
    public static volatile SingularAttribute<Tee, Integer> distance;
    public static volatile SingularAttribute<Tee, TeeSet> teeSet;
    public static volatile SingularAttribute<Tee, Hole> hole;

}

