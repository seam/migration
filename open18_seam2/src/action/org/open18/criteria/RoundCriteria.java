package org.open18.criteria;

import java.util.Date;
import java.util.List;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.open18.model.Course;
import java.io.Serializable;

@Name("roundCriteria")
@Scope(ScopeType.CONVERSATION)
public class RoundCriteria implements Serializable {
	private String golferName;
	private Date beforeDate;
	private Date afterDate;
	private boolean self = false;
	private List<Course> courses;

	public String getGolferName() { return this.golferName; }
	public void setGolferName(String name) { this.golferName = name; }
	
	public Date getBeforeDate() { return this.beforeDate; }
	public void setBeforeDate(Date date) { this.beforeDate = date; }

	public Date getAfterDate() { return this.afterDate; }
	public void setAfterDate(Date date) { this.afterDate = date; }

	public boolean isSelf () { return this.self; }
	public void setSelf (boolean self) { this.self = self; }

	public List<Course> getCourses() { return this.courses; }
	public void setCourses(List<Course> courses) { this.courses = courses; }
}
