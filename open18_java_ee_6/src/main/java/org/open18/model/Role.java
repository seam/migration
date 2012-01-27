package org.open18.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Represents a security role.
 * 
 * @author Dan Allen
 */
@Entity
@Table(name = "ROLE", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Role implements Serializable {
	
	private Long id;
	private String name;

	@Id @GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
