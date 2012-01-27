package org.open18.auth;

import org.jboss.seam.annotations.intercept.BypassInterceptors;

@BypassInterceptors
public class PasswordBean {
	private String password;
	private String confirm;

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirm() {
		return confirm;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public boolean verify() {
		return confirm != null && confirm.equals(password);
	}
	
	@Override
	public String toString() {
		return new StringBuffer()
			.append(getClass().getName()).append("[")
			.append("password=").append(password).append(",")
			.append("confirm=").append(confirm)
			.append("]").toString();
	}

}
