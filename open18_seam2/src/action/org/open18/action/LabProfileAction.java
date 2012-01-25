package org.open18.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.open18.model.Golfer;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import org.jboss.seam.annotations.Install;
import org.open18.ProfileNotFoundException;

@Name("profileAction")
@Scope(ScopeType.CONVERSATION) // let's us redirect without losing @Out
@Install(false)
public class LabProfileAction {
    @Logger private Log log;

    @In FacesMessages facesMessages;

    protected int newGolferListDisplaySize = 3;
	
	protected int newGolferListPoolSize = 10;

    @In
    protected EntityManager entityManager;

    @DataModelSelection
    @Out(required=false)
    protected Golfer selectedGolfer;

	@Out
	protected boolean profileLoaded = false;

	@DataModel(scope = ScopeType.PAGE)
    protected List<Golfer> newGolfers;
	
    public String view() {
        log.info("profileAction.view() action called");
		assert selectedGolfer != null && selectedGolfer.getId() != null;
		profileLoaded = true;
		//return "profile";
		return "/profile.xhtml";
    }

	public void standaloneLoadProfile(Long id) throws ProfileNotFoundException {
		assert selectedGolfer == null || selectedGolfer.getId() == null;
		selectedGolfer = (Golfer) entityManager.find(Golfer.class, id);
		if (selectedGolfer == null) {
			throw new ProfileNotFoundException(id);
		}
		else {
			profileLoaded = true;
		}
	}

	public void blanketLoadProfile(Long id) throws ProfileNotFoundException {
		if (selectedGolfer != null && selectedGolfer.getId() != null) {
			return;
		}

		if (id > 0) {
			selectedGolfer = (Golfer) entityManager.find(Golfer.class, id);
		}

		if (selectedGolfer == null) {
			throw new ProfileNotFoundException(id);
		}
		else {
			profileLoaded = true;
		}
	}

	@Factory("newGolfers")
    public void findNewGolfers() {
        newGolfers = entityManager
          .createQuery("select g from Golfer g order by dateJoined desc")
          .setMaxResults(newGolferListPoolSize)
          .getResultList();

        Random rnd = new Random(System.currentTimeMillis());

        while (newGolfers.size() > newGolferListDisplaySize) {
            newGolfers.remove(rnd.nextInt(newGolfers.size()));
        }
    }

    public int getNewGolferListDisplaySize() {
        return newGolferListDisplaySize;
    }
    public void setNewGolferListDisplaySize(int newGolferListDisplaySize) {
        this.newGolferListDisplaySize = newGolferListDisplaySize;
    }
	
	public int getNewGolferListPoolSize() {
		return newGolferListPoolSize;
	}
	public void setNewGolferListPoolSize(int newGolferListPoolSize) {
		this.newGolferListPoolSize = newGolferListPoolSize;
	}
}
