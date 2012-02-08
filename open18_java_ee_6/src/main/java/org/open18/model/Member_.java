package org.open18.model;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

import org.open18.model.Member;
import org.open18.model.Role;

@StaticMetamodel(Member.class)
public abstract class Member_ {

	public static volatile SingularAttribute<Member, Long> id;
	public static volatile SingularAttribute<Member, String> username;
	public static volatile SetAttribute<Member, Role> roles;
	public static volatile SingularAttribute<Member, String> emailAddress;
	public static volatile SingularAttribute<Member, String> passwordHash;

}

