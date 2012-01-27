package org.open18.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Conversational;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.FlushModeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.open18.model.Course;
import org.open18.model.Facility;
import org.open18.model.Hole;
import org.open18.model.enums.GrassType;

/**
 * Screen #1: select facility (opportunity for subflow here)
 * Screen #2: basic info (name, designer, golf pro, grass types, year built, num holes, num tee sets, has ladies par?)
 * Screen #3: description (nice big text area)
 * Screen #3: hole data (men's par, ladies' par, men's handicap, ladies' handicap, drop down for signature hole)
 * Screen #4: tee set info (name, color, ladies' slope/course, men's slope/course, position) | repeat until
 * Screen #5: tee distances for tee set                                                      | done
 * Screen #6: course photo
 */
@Name("courseWizard")
@Scope(ScopeType.CONVERSATION)
public class CourseWizard implements Serializable {
    
    @In protected EntityManager entityManager;
    
    @In protected FacesMessages facesMessages;

    @RequestParameter protected Long facilityId;
    
    @Out private Course course;

	@Out(required = false)
	private List<Hole> holes;
	
	@Out(required = false)
	private String gender;
	
	private boolean ladiesParUnique = false;
	
	private boolean ladiesHandicapUnique = false;

    @Begin(join = true, pageflow = "Course Wizard", flushMode = FlushModeType.MANUAL)
    //@Begin(join = true, flushMode = FlushModeType.MANUAL)
	@Restrict("#{identity.loggedIn}") // avoids having to add login redirect logic in flow (see chapter 11)
    public void addCourse() {
        course = new Course();
        course.setFacility(entityManager.find(Facility.class, facilityId));
		// setup some defaults
        course.setNumHoles(18);
        course.setFairways("BENT");
        course.setGreens("BENT");
		// NOTE: only persist here if not using auto-increment column
		//entityManager.persist(course);
    }

	@Begin(join = true, pageflow = "Course Wizard", flushMode = FlushModeType.MANUAL)
	//@Begin(join = true, flushMode = FlushModeType.MANUAL)
	@Restrict("#{identity.loggedIn}") // avoids having to add login redirect logic in flow (see chapter 11)
	public void editCourse(Long courseId) {
		course = entityManager.find(Course.class, courseId);
	}
	
	// only used in ad hoc model
    @Conversational
	public String submitBasicInfo() {
		return "next";
	}

	// only used in ad hoc model
    @Conversational
	public String submitDescription() {
		prepareHoleData();
		return "next";
	}
    
	// used by course wizard, so must be public
	@Conversational
	public void prepareHoleData() {
		if (entityManager.contains(course)) {
			holes = new ArrayList<Hole>(course.getHoles());
			Collections.sort(holes, new Comparator<Hole>() {
				public int compare(Hole a, Hole b) {
					return Integer.valueOf(a.getNumber()).compareTo(Integer.valueOf(b.getNumber()));
				}
			});
			// since we are not sure, just expose everything
			setLadiesParUnique(true);
			setLadiesHandicapUnique(true);
		}
		else {
			holes = new ArrayList<Hole>();
			for (int i = 1; i <= course.getNumHoles(); i++) {
				Hole hole = new Hole();
				hole.setNumber(i);
				hole.setCourse(course);
				holes.add(hole);
			}
		}

		gender = "Men";
	}
	
	@Conversational
	public String submitHoleData() {
		if ("Men".equals(gender)) {
			return prepareLadiesHoleData();
		}
		else {
			return setHoleData();
		}
	}
	
	protected String prepareLadiesHoleData() {
		for (Hole hole : holes) {
			// seed the ladies par
			if (!ladiesParUnique || hole.getLadiesPar() == 0) {
				hole.setLadiesPar(hole.getMensPar());
			}
			if (!ladiesHandicapUnique) {
				hole.setLadiesHandicap(hole.getMensHandicap());
			}
		}

		if (isLadiesHoleDataRequired()) {
			gender = "Ladies";
			return gender;
		} else {
			return setHoleData();
		}

	}

	@BypassInterceptors
	public boolean isLadiesHoleDataRequired() {
		return ladiesParUnique || ladiesHandicapUnique;
	}

	protected String setHoleData() {
		gender = null;
		course.setHoles(new LinkedHashSet<Hole>(holes));
		// NOTE: don't clear holes so that they can be used on review page
		//holes = null; 
		return "next";
	}

    @End
    @Conversational
    public String save() {
        try {
			if (!entityManager.contains(course)) {
				entityManager.persist(course);
				entityManager.flush();
				facesMessages.add("#{course.name} has been added to the directory.");	
			}
			else {
				entityManager.flush();
				facesMessages.add("#{course.name} has been updated.");
			}
            return "success";
        }
        catch (Exception e) {
            facesMessages.add("Saving the course failed.");
            // returning null indicates failure and will not end the conversation
            return null;
        }
    }

	public boolean isLadiesHandicapUnique() {
		return ladiesHandicapUnique;
	}

	public void setLadiesHandicapUnique(boolean ladiesHandicapUnique) {
		this.ladiesHandicapUnique = ladiesHandicapUnique;
	}

	public boolean isLadiesParUnique() {
		return ladiesParUnique;
	}

	public void setLadiesParUnique(boolean ladiesParUnique) {
		this.ladiesParUnique = ladiesParUnique;
	}
	
	@BypassInterceptors
	public List<String> getFairwayOptions() {
		List<String> options = new ArrayList<String>();
		for (GrassType type : GrassType.values()) {
			if (type.isValidForFairways()) {
				options.add(type.name());
			}
		}
		Collections.sort(options);
		return options;	
	}
	
	@BypassInterceptors
	public List<String> getGreensOptions() {
		List<String> options = new ArrayList<String>();
		for (GrassType type : GrassType.values()) {
			if (type.isValidForGreens()) {
				options.add(type.name());
			}
		}
		Collections.sort(options);
		return options;
	}
	
	@BypassInterceptors
    public List<Integer> getParOptions() {
        List<Integer> options = new ArrayList<Integer>();
        for (int i = 2; i <= 6; i++) {
            options.add(i);
        }
        return options;
    }
    
	@BypassInterceptors
    public List<Integer> getHandicapOptions() {
        List<Integer> options = new ArrayList<Integer>();
        for (int i = 1; i <= 18; i++) {
            options.add(i);
        }
        return options;
    }
}
