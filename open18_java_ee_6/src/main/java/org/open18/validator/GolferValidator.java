package org.open18.validator;

import org.open18.action.*;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;

import org.open18.auth.PasswordBean;
import org.open18.model.Golfer;

// TODO: This needs to be a new type of bean validator validator
public class GolferValidator {

	@Inject
    private RegisterAction registerAction;

	private List<ConstraintViolation> invalidValues = new ArrayList<ConstraintViolation>();

	public boolean validate(Golfer newGolfer, PasswordBean passwordBean) {

		if (!passwordBean.verify()) {
			addInvalidValue("confirm", PasswordBean.class,
				"Confirmation password does not match");
		}

		if (!registerAction.isUsernameAvailable(newGolfer.getUsername())) {
			addInvalidValue("username", Golfer.class,
				"Username is already taken");
		}

		if (registerAction.isEmailRegistered(newGolfer.getEmailAddress())) {
			addInvalidValue("emailAddress", Golfer.class,
				"Email address is already registered");
		}

		return !hasInvalidValues();
	}

	public ConstraintViolation[] getInvalidValues() {
		return invalidValues.toArray(new ConstraintViolation[invalidValues.size()]);
	}

	public boolean hasInvalidValues() {
		return invalidValues.size() > 0;
	}

	public void reset() {
		invalidValues = new ArrayList<ConstraintViolation>();
	}

	protected void addInvalidValue(String property, Class beanClass, String message) {
		invalidValues.add(new ConstraintViolation(message, beanClass, property, null, null) {});
	}

}
