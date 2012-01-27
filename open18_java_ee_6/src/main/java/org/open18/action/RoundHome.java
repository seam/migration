package org.open18.action;

import javax.persistence.NoResultException;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityHome;
import org.open18.model.Golfer;
import org.open18.model.Round;
import org.open18.model.TeeSet;
import org.open18.model.enums.Weather;

@Name("roundHome")
public class RoundHome extends EntityHome<Round> {

	@In(required = false)
	private Golfer currentGolfer;

	@In(create = true)
	private TeeSetHome teeSetHome;

	// As an alternative to a page parameter, the courseId request
	// parameter can be injected using the @RequestParameter annotation.
	//@RequestParameter
	//public void setRoundId(Long id) {
	//	setId(id);
	//}

	@Factory(value = "round", scope = ScopeType.EVENT)
	@Override
	public Round getInstance() {
		return super.getInstance();
	}

	@Factory(value = "weatherCategories", scope = ScopeType.CONVERSATION)
	public Weather[] getWeatherCategories() {
		return Weather.values();
	}
	
	// As an alternative to XML-based configuration, the round prototype can
	// be configured here
	//@Override
	//protected Round createInstance() {
	//	Round round = super.createInstance();
	//	if (round.getGolfer() == null) {
	//		round.setGolfer(currentGolfer);
	//	}
	//	round.setDate(new java.sql.Date(System.currentTimeMillis()));
	//	return round;
	//}
	
	/**
	 * Eagerly fetch associations needed on view/edit page.
	 */
	@Override
	protected Round loadInstance() {
		try {
			return (Round) getEntityManager()
				.createQuery(
					"select r from Round r " +
					"join fetch r.golfer g " +
					"join fetch r.teeSet ts " +
					"join fetch ts.course c " +
					"where r.id = #{roundHome.id}")
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void wire() {
		TeeSet teeSet = teeSetHome.getDefinedInstance();
		// only set it if the tee set does not share object identity (hence a real change)
		if (teeSet != null && !teeSet.equals(getInstance().getTeeSet())) {
			getInstance().setTeeSet(teeSet);
		}
	}

	public boolean isWired() {
		if (getInstance().getTeeSet() == null) {
			return false;
		}
		return true;
	}
	
	@Transactional
	public String revert() {
		getEntityManager().refresh(getInstance());
		teeSetHome.clearInstance();
		return "reverted";
	}
	
}
