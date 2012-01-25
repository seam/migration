package org.open18.action;

import org.open18.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.List;
import java.util.Arrays;

@Name("teeList")
public class TeeList extends EntityQuery {

	private static final String[] RESTRICTIONS = {};

	private Tee tee;

	public TeeList() {
		tee = new Tee();
		tee.setId(new TeeId());
	}

	@Override
	public String getEjbql() {
		return "select tee from Tee tee";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	public Tee getTee() {
		return tee;
	}

	@Override
	public List<String> getRestrictions() {
		return Arrays.asList(RESTRICTIONS);
	}

}
