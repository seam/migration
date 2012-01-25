package org.open18.action;

import org.jboss.seam.annotations.In;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.RaiseEvent;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.open18.auth.PasswordBean;
import org.open18.auth.PasswordManager;
import org.open18.model.Golfer;
import org.open18.validator.GolferValidator;

@Name("registerAction")
public class RegisterAction {

	@Logger	private Log log;

	@In protected EntityManager entityManager;

	@In protected FacesMessages facesMessages;

	@In protected PasswordManager passwordManager;

	@In protected Golfer newGolfer;

	@In protected PasswordBean passwordBean;
	
	@In protected GolferValidator golferValidator;
	
	protected String[] proStatusTypes = {};

	protected List<String> specialtyTypes = new ArrayList<String>();

	public String[] getProStatusTypes() {
		return this.proStatusTypes;
	}

	public void setProStatusTypes(String[] types) {
		this.proStatusTypes = types;
	}

	public List<String> getSpecialtyTypes() {
		return specialtyTypes;
	}

	public void setSpecialtyTypes(List<String> specialtyTypes) {
		this.specialtyTypes = specialtyTypes;
	}
	
	//@RaiseEvent("golferRegistered") // not nearly as flexible as using the Events API
	public String register() {
		log.info("Registering golfer #{newGolfer.username}");

		if (!golferValidator.validate(newGolfer, passwordBean)) {
			log.info("Invalid registration request");
			facesMessages.addToControls(golferValidator.getInvalidValues());
			return null;
		}

		newGolfer.setPasswordHash(passwordManager.hash(passwordBean.getPassword()));
		entityManager.persist(newGolfer);
		if (Events.exists()) {
			Events.instance().raiseTransactionSuccessEvent("golferRegistered", newGolfer);
		}
		facesMessages.addFromResourceBundle("registration.welcome", newGolfer.getName());
		Identity identity = Identity.instance();
		identity.setUsername(newGolfer.getUsername());
		identity.setPassword(passwordBean.getPassword());
		// could also do Events.instance().raiseTransactionSuccessEvent("attemptLogin"); and write an observer
		// quietLogin() doesn't add messages or throw exceptions
		identity.quietLogin();
		return "success";
	}
	
	public boolean isUsernameAvailable(String username) {
        return entityManager.createQuery(
            "select m from Member m where m.username = :username")
            .setParameter("username", username)
            .getResultList().size() == 0;
    }
	
	public boolean isEmailRegistered(String email) {
        return entityManager.createQuery(
            "select m from Member m where m.emailAddress = :email")
            .setParameter("email", email)
            .getResultList().size() > 0;
    }
}
