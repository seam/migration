package org.open18.model.meta;

import java.util.Date;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.open18.model.Golfer;
import org.open18.model.Round;
import org.open18.model.TeeSet;
import org.open18.model.enums.Weather;

@StaticMetamodel(Round.class)
public abstract class Round_ {

	public static volatile SingularAttribute<Round, Long> id;
	public static volatile SingularAttribute<Round, Integer> totalScore;
	public static volatile SingularAttribute<Round, Golfer> golfer;
	public static volatile SingularAttribute<Round, TeeSet> teeSet;
	public static volatile SingularAttribute<Round, Weather> weather;
	public static volatile SingularAttribute<Round, String> notes;
	public static volatile SingularAttribute<Round, Date> date;
	public static volatile SingularAttribute<Round, Integer> version;

}

