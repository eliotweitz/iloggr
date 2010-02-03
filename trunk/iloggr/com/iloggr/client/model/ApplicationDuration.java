package com.iloggr.client.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Index;


@Entity
public class ApplicationDuration extends iLoggrObject {
	private Long id;
	private Date recordTime;
	private Application application;
	private long secondsDuration;
	private Phone phone;
	
	public ApplicationDuration() {
		super();
	}
	
	public ApplicationDuration(Date recordTime, Application application, long secondsDuration,
			Phone phone) {
		this.recordTime = recordTime;
		this.application = application;
		this.secondsDuration = secondsDuration;
		this.phone = phone;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application app) {
		this.application = app;
	}
	
	public long getSecondsDuration() {
		return secondsDuration;
	}
	public void setSecondsDuration(long secondsDuration) {
		this.secondsDuration = secondsDuration;
	}
	
	@ManyToOne
	public Phone getPhone() {
		return phone;
	}
	public void setPhone(Phone phone) {
		this.phone = phone;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	@Index(name="RECORDTIME")
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	
}

