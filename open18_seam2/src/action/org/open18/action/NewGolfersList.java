package org.open18.action;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Unwrap;
import org.open18.model.Golfer;

@Name("newGolfersList")
@Scope(ScopeType.APPLICATION)
public class NewGolfersList {
    private int poolSize = 25;
    private int displaySize = 5;
                                                   
    @In protected EntityManager entityManager;
    protected List<Golfer> newGolfers;
	
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
    }
                
    @Create
    public void onCreate() {
        fetchNewGolfers();
    }
                
    @Unwrap
    public List<Golfer> getNewGolfers() {
        return newGolfers;
    }
             
    @Observer(value = { "golferRegistered", "golferDeleted" }, create = false)
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

