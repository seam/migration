package org.open18.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.open18.model.Hole;
import org.open18.model.Tee;
import org.open18.model.TeeId;
import org.open18.model.TeeSet;

@StaticMetamodel(Tee.class)
public abstract class Tee_ {

	public static volatile SingularAttribute<Tee, TeeId> id;
	public static volatile SingularAttribute<Tee, Integer> distance;
	public static volatile SingularAttribute<Tee, TeeSet> teeSet;
	public static volatile SingularAttribute<Tee, Hole> hole;

}

