package org.open18.auth;

import java.security.MessageDigest;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.util.Hex;
import org.jboss.seam.util.Base64;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;

@Name("org.open18.auth.passwordManager")
@BypassInterceptors
public class PasswordManager {
	private String digestAlgorithm;
	private String charset;
	private String saltPhrase;
	public enum Encoding { base64, hex }
	private Encoding encoding = Encoding.hex;

	public String getDigestAlgorithm() {
		return this.digestAlgorithm;
	}

	public void setDigestAlgorithm(String algorithm) {
		this.digestAlgorithm = algorithm;
	}

	public String getCharset() {
		return this.charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Encoding getEncoding() {
		return this.encoding;
	}

	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
	}

	public String getSaltPhrase() {
		return this.saltPhrase;
	}

	public void setSaltPhrase(String saltPhrase) {
		this.saltPhrase = saltPhrase;
	}

	public String hash(String plainTextPassword) {
		try {
			MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
			if (saltPhrase != null) {
				digest.update(saltPhrase.getBytes(charset));
				byte[] salt = digest.digest();
				digest.reset();
				digest.update(plainTextPassword.getBytes(charset));
				digest.update(salt);
			}
			else {
				digest.update(plainTextPassword.getBytes(charset));
			}
			byte[] rawHash = digest.digest();
			if (encoding != null && encoding.equals(Encoding.base64)) {
				return Base64.encodeBytes(rawHash);
			}
			else {
				return new String(Hex.encodeHex(rawHash));
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static PasswordManager instance() {
		return (PasswordManager) Component.getInstance(PasswordManager.class, ScopeType.EVENT);
	}
}
