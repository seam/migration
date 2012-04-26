package org.open18.action;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.open18.extension.ViewScoped;
import org.open18.model.Golfer;
import org.open18.model.dao.GolferDao;

@Named
@ConversationScoped
@Stateful
public class ProfileAction implements Serializable {
    private static final long serialVersionUID = 6581903565825140682L;

    @Inject
    transient GolferDao golferDao;

	protected Golfer selectedGolfer;

	private int newGolferDisplaySize = 5;

    @Produces @ViewScoped @Named("newGolfers") @TransactionAttribute
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
