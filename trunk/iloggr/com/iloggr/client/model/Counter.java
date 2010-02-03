package com.iloggr.client.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Index;

@Entity
public class Counter extends iLoggrObject implements Comparable {
	private Long id;
	private String name;
	private Application application;
	private Date lastRecordTime;
	private long count;

	public Counter() {
		super();
	}
	

	public Counter(Long id, String name, Application application,
			Date lastRecordTime, long count) {
		this.id = id;
		this.name = name;
		this.application = application;
		this.lastRecordTime = lastRecordTime;
		this.count = count;
	}


	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

	@ManyToOne
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
	
	@Index(name="RECORDTIME")
	public Date getRecordTime() {
		return lastRecordTime;
	}
	public void setRecordTime(Date when) {
		this.lastRecordTime = when;
	}
	
	public void resetCount() {
		this.count = 0l;
	}
	
	public long incrementCount() {
		return ++this.count;
	}
	
	public long decrementCount() {
		return --this.count;
	}
	
	public long getCount() {
		return this.count;
	}
	
	public void setCount(long count) {
		this.count = count;
	}
	
	public int compareTo(Object o) {
		Counter c = (Counter) o;
		return this.name.compareTo(c.name);
	}

	public boolean equals(Object o) {
		Counter c = (Counter) o;
		if (id.equals(c.getId()))
			return true;
		return false;
	}


}
