package org.open18.action;

import org.open18.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.List;
import java.util.Arrays;

@Name("teeSetList")
public class TeeSetList extends EntityQuery {

	private static final String[] RESTRICTIONS = {
			"lower(teeSet.color) like concat(lower(#{teeSetList.teeSet.color}),'%')",
			"lower(teeSet.name) like concat(lower(#{teeSetList.teeSet.name}),'%')",};

	private TeeSet teeSet = new TeeSet();

	@Override
	public String getEjbql() {
		return "select teeSet from TeeSet teeSet";
	}

	@Override
	public Integer getMaxResults() {
		return 25;
	}

	public TeeSet getTeeSet() {
		return teeSet;
	}

	@Override
	public List<String> getRestrictions() {
		return Arrays.asList(RESTRICTIONS);
	}

}
