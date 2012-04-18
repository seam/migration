package org.open18.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(TeeId.class)
public abstract class TeeId_ {

    public static volatile SingularAttribute<TeeId, Long> holeId;
    public static volatile SingularAttribute<TeeId, Long> teeSetId;
    public static volatile SingularAttribute<TeeId, Integer> hashCode;

}

