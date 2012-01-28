package org.open18.action;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.open18.model.Golfer;

@Named
@ApplicationScoped
public class NewGolfersList {
    private int poolSize = 25;
    private int displaySize = 5;

    @Inject protected EntityManager entityManager;
    protected List<Golfer> newGolfers;

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
    }

    @Inject
    public void onCreate() {
        fetchNewGolfers();
    }

    public List<Golfer> getNewGolfers() {
        return newGolfers;
    }

//    @Observer(value = { "golferRegistered", "golferDeleted" }, create = false)
    synchronized public void fetchNewGolfers() {
        List<Golfer> results = entityManager
			.createQuery(
				"select g from Golfer g order by g.dateJoined desc")
            .setMaxResults(poolSize)
			.getResultList();
        Collections.shuffle(results);
        Random random = new Random();
        while (results.size() > displaySize) {
            results.remove(random.nextInt(results.size()));
        }
        newGolfers = results;
    }
}

