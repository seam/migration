package org.open18.model;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;

import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.annotations.Name;
import org.open18.model.enums.Weather;

@Entity
@Table(name = "ROUND")
@Name("round")
public class Round implements Serializable {

	private Long id;
	private Integer version;
	private Date date;
	private String notes;
	private Golfer golfer;
	private TeeSet teeSet;
	private Integer totalScore;
	private Weather weather;
	private boolean selected;

	@Id @GeneratedValue
	public Long getId() {
		return this.id;
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
	
	@Temporal(TemporalType.DATE)
	@NotNull
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Lob
	@Length(max = 50000)
	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String comment) {
		this.notes = comment;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEE_SET_ID", nullable = false)
	@NotNull
	public TeeSet getTeeSet() {
		return teeSet;
	}

	public void setTeeSet(TeeSet teeSet) {
		this.teeSet = teeSet;
	}

	@Column(name = "TOTAL_SCORE")
	@NotNull
	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	@Enumerated(EnumType.STRING)
	@NotNull
	public Weather getWeather() {
		return this.weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}

	@Transient
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@PreUpdate @PreRemove
	@Restrict
	public void restrict() {}

}
