package org.open18.action;

import org.open18.model.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("teeHome")
public class TeeHome extends EntityHome<Tee> {

	@In(create = true)
	HoleHome holeHome;
	@In(create = true)
	TeeSetHome teeSetHome;

	public void setTeeId(TeeId id) {
		setId(id);
	}

	public TeeId getTeeId() {
		return (TeeId) getId();
	}

	public TeeHome() {
		setTeeId(new TeeId());
	}

	@Override
	public boolean isIdDefined() {
		if (getTeeId().getHoleId() == 0)
			return false;
		if (getTeeId().getTeeSetId() == 0)
			return false;

		return true;
	}

	@Override
	protected Tee createInstance() {
		Tee tee = new Tee();
		tee.setId(new TeeId());
		return tee;
	}

	public void wire() {
		getInstance();
		Hole hole = holeHome.getDefinedInstance();
		if (hole != null) {
			getInstance().setHole(hole);
		}
		TeeSet teeSet = teeSetHome.getDefinedInstance();
		if (teeSet != null) {
			getInstance().setTeeSet(teeSet);
		}
	}

	public boolean isWired() {
		if (getInstance().getHole() == null)
			return false;
		if (getInstance().getTeeSet() == null)
			return false;
		return true;
	}

	public Tee getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
