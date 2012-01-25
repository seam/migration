package org.open18.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;
import org.open18.model.Golfer;

@Name("registrationBookkeeper")
@Scope(ScopeType.APPLICATION)
public class RegistrationBookKeeper {
    @Logger private Log log;
    private int cnt = 0;
	
    @Observer("golferRegistered")
    synchronized public void record(Golfer golfer) {
        cnt++;
        log.info("Golfer registered – username: " + golfer.getUsername());
        log.info(cnt + " golfers have registered since the last restart");
    }
}
