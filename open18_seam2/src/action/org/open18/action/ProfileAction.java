package org.open18.action;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.open18.model.Golfer;

import javax.persistence.EntityManager;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.annotations.web.RequestParameter;
import org.open18.ProfileNotFoundException;

@Name("profileAction")
@Scope(ScopeType.CONVERSATION) // required to survive redirect
public class ProfileAction implements Serializable {
	@In	protected EntityManager entityManager;
	@RequestParameter protected Long golferId;
	@In(create = true) protected List<Golfer> newGolfersList;
	
	@DataModelSelection
	@Out(required = false)
	protected Golfer selectedGolfer;

	@DataModel(scope = ScopeType.PAGE)
	protected List<Golfer> newGolfers;
	
	// NOTE: getter/setter methods would need to declare @BypassInterceptors
	// if bound to a JSF form to avoid outjections from being enforced
	private int newGolferPoolSize = 25;
	private int newGolferDisplaySize = 5;
	
	public String view() {
		assert selectedGolfer != null && selectedGolfer.getId() != null;
		return "/profile.xhtml";
	}
	
	public void load() {           
		if (selectedGolfer != null && selectedGolfer.getId() != null) {
			return;
		}
		
		if (golferId != null && golferId > 0) {
			selectedGolfer = entityManager.find(Golfer.class, golferId);
		}
		
		if (selectedGolfer == null) {
			throw new ProfileNotFoundException(golferId);
		}
	}
	
	// NOTE: alternative to using a page action that invokes this method
	//@Factory("newGolfers")
	public void findNewGolfers() {
		// option #1: fetch it here
		/*
		newGolfers = entityManager
			.createQuery(
				"select g from Golfer g order by g.dateJoined desc")
			.setMaxResults(newGolferPoolSize)
			.getResultList();
		Random rnd = new Random(System.currentTimeMillis());
		while (newGolfers.size() > newGolferDisplaySize) {
			newGolfers.remove(rnd.nextInt(newGolfers.size()));
		}
		*/
		
		// option #2: use the one provided by the manager
		newGolfers = newGolfersList;
	}

}
