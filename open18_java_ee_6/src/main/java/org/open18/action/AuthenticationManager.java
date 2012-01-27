package org.open18.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.open18.auth.PasswordManager;
import org.open18.model.Golfer;
import org.open18.model.Member;
import org.open18.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@Name("authenticationManager")
public class AuthenticationManager {

	@Logger private Log log;
	@In private EntityManager entityManager;
	@In private Identity identity;
	@In private PasswordManager passwordManager;
	@Out(required = false) private Golfer currentGolfer;

	@Transactional public boolean authenticate() {
		log.info("authenticating {0}", identity.getUsername());
		try {
			Member member = (Member) entityManager.createQuery(
				//"select m from Member m where m.username = :username")
				"select distinct m from Member m left join fetch m.roles where m.username = :username")
				.setParameter("username", identity.getUsername())
				.getSingleResult();

			if (!validatePassword(identity.getPassword(), member)) {
				return false;
			}

			identity.addRole("member");
			if (member.getRoles() != null) {
				for (Role role : member.getRoles()) {
					identity.addRole(role.getName());
				}
			}

			if (member instanceof Golfer) {
				currentGolfer = (Golfer) member;
				identity.addRole("golfer");
			}
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}

	public boolean validatePassword(String password, Member m) {
		return passwordManager.hash(password).equals(m.getPasswordHash());
	}
}
