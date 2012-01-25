package org.open18.model;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;

import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Length;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Role;

@Entity
@PrimaryKeyJoinColumn(name = "MEMBER_ID")
@Table(name = "GOLFER")
@Name("golfer")
@Role(name = "currentGolfer", scope = ScopeType.SESSION)
public class Golfer extends Member {

	private String lastName;
	private String firstName;
	private Gender gender;
	private Date dateJoined;
	private Date dateOfBirth;
	private String location;
	private String specialty;
	private String proStatus;
	private Set<Round> rounds = new HashSet<Round>(0);
	private Set<CourseComment> courseComments = new HashSet<CourseComment>(0);

	@Column(name = "last_name", nullable = false)
	@NotNull
	@Length(max = 40)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "first_name", nullable = false)
	@NotNull
	@Length(max = 40)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Transient
	public String getName() {
		return firstName + ' ' + lastName;
	}

	@Transient
	public String getNameLastFirst() {
		return lastName + ", " + firstName;
	}

	@Enumerated(EnumType.STRING)
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "joined", nullable = false, updatable = false)
	@NotNull
	public Date getDateJoined() {
		return dateJoined;
	}

	public void setDateJoined(Date dateJoined) {
		this.dateJoined = dateJoined;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "dob")
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	@Column(name = "pro_status")
	public String getProStatus() {
		return proStatus;
	}
	public void setProStatus(String proStatus) {
		this.proStatus = proStatus;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "golfer")
	public Set<Round> getRounds() {
		return rounds;
	}

	public void setRounds(Set<Round> rounds) {
		this.rounds = rounds;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "golfer")
	public Set<CourseComment> getCourseComments() {
		return courseComments;
	}

	public void setCourseComments(Set<CourseComment> courseComments) {
		this.courseComments = courseComments;
	}

}
