package org.open18.action;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.open18.model.Golfer;
import org.open18.model.dao.GolferDao;

@Named
@ConversationScoped
@Stateful
public class ProfileAction implements Serializable {
	@PersistenceContext
    protected EntityManager entityManager;

    @Inject
    transient GolferDao golferDao;

//  @RequestParameter protected Long golferId;

	protected Golfer selectedGolfer;

	private int newGolferDisplaySize = 5;

//    @Inject
//    private void init() {
//        this.golferDao.setEntityManager(entityManager);
//    }

	public String view() {
		assert selectedGolfer != null && selectedGolfer.getId() != null;
		return "/profile.xhtml";
	}
//
//	public void load() {
//		if (selectedGolfer != null && selectedGolfer.getId() != null) {
//			return;
//		}
//
//		if (golferId != null && golferId > 0) {
//			selectedGolfer = entityManager.find(Golfer.class, golferId);
//		}
//
//		if (selectedGolfer == null) {
//			throw new ProfileNotFoundException(golferId);
//		}
//	}
//
    @Produces @RequestScoped @Named("newGolfers") @TransactionAttribute
	public List<Golfer> findNewGolfers() {
        final List<Golfer> newGolfers = golferDao.findNewGolfers();

		final Random rnd = new Random(System.currentTimeMillis());
		while (newGolfers.size() > newGolferDisplaySize) {
			newGolfers.remove(rnd.nextInt(newGolfers.size()));
		}

		return newGolfers;
	}

    public Golfer getSelectedGolfer() {
        return selectedGolfer;
    }

    public void setSelectedGolfer(Golfer selectedGolfer) {
        this.selectedGolfer = selectedGolfer;
    }
}
