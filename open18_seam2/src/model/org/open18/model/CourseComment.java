package org.open18.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "COURSE_COMMENT")
public class CourseComment implements Serializable {

	private Long id;
	private Integer version;
	private Date datePosted;
	private String text;
	private Course course;
	private Golfer golfer;

	@Id	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Version
	public Integer getVersion() {
		return version;
	}

	private void setVersion(Integer version) {
		this.version = version;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_POSTED", nullable = false)
	@NotNull
	public Date getDatePosted() {
		return datePosted;
	}

	public void setDatePosted(Date date) {
		this.datePosted = date;
	}

	@Lob
	@NotNull
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID", nullable = false)
	@NotNull
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GOLFER_ID", nullable = false)
	@NotNull
	public Golfer getGolfer() {
		return golfer;
	}

	public void setGolfer(Golfer golfer) {
		this.golfer = golfer;
	}
}