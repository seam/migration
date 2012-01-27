package org.open18.action;

import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.RaiseEvent;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.framework.EntityHome;

import org.open18.model.Golfer;
import org.open18.model.Role;

@Name("golferHome")
public class GolferHome extends EntityHome<Golfer> {

	@RequestParameter
	private Long golferId;

	@Override
	public Object getId() {
		if (golferId == null) {
			return super.getId();
		} else {
			return golferId;
		}
	}

	@Override
	@Begin(join = true)
	public void create() {
		super.create();
	}

	@Override
	@RaiseEvent("golferDeleted")
	public String remove() {
		return super.remove();
	}
	
	public List<Role> getRoles() {
		return getInstance() == null ? null : new ArrayList<Role>(getInstance().getRoles());
	}
	
	public void setRoles(List<Role> roles) {
		if (getInstance() != null) {
			getInstance().getRoles().clear();
			getInstance().getRoles().addAll(roles);
		}
	}
}
