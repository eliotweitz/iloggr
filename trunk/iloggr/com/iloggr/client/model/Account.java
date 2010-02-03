/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Index;
import org.hibernate.validator.Email;
import org.hibernate.validator.NotNull;




/**
 * This class contains all of the information about a user account including contact information and applications
 * that have been registered by this user.
 *
 * @author eliot
 * @version 1.0
 *
 */
@Entity
public class Account extends iLoggrObject {
    public final static int STATUS_INACTIVE = 0;
    public final static int STATUS_ACTIVE = 1;

    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String password;
    private String securityToken;
    private String emailToken;
    private Date lastContactTime;
    private int status;
    private int notification;
    private String description;
    private InvitationCode inviteCode;
    private Set<Application> applications = new HashSet<Application>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account() {
    	super();
    	// preserve
    }

    public Account(InvitationCode code, String name, String password, String email, String phone) {
    	this(0l, code, name, email, phone, password);
    }

    public Account(long id, InvitationCode code, String name, String email, String phoneNumber, String password) {
    	this.id = id;
    	this.inviteCode = code;
    	this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.status = STATUS_INACTIVE;
        this.notification = Action.TYPE_EMAIL;
  		this.description = "";
    }


    @Column(unique = true)
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull
    public String getName() {
        return name;
    }

	public void setName(String name) {
	    this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSecurityToken() {
        return securityToken;
    }

    public String getEmailToken() {
		return emailToken;
	}

	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}

	public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

	@Index(name="CONTACTTIME")
	public Date getLastContactTime() {
		return lastContactTime;
	}

	public void setLastContactTime(Date lastContactTime) {
		this.lastContactTime = lastContactTime;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNotification() {
		return notification;
	}

	public void setNotification(int notification) {
		this.notification = notification;
	}

	@OneToMany(cascade = {CascadeType.ALL})
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	public Set<Application> getApplications() {
		return applications;
	}

	public void setApplications(Set<Application> applications) {
		this.applications = applications;
	}
	
	public Application getApplicationNamed(String name) {
		for (Application app : applications) if (app.getName().equalsIgnoreCase(name)) return app;
		return null;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@OneToOne(cascade=CascadeType.ALL)
	public InvitationCode getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(InvitationCode inviteCode) {
		this.inviteCode = inviteCode;
	}

	public void addApplication(Application app) {
		if (app == null) return;
		app.setAccount(this);
		this.applications.add(app);
	}
	
	public void removeApplication(Application app) {
		applications.remove(app);
	}

	public int compareTo(Object o) {
		Account p = (Account) o;
		return this.name.compareTo(p.name);
	}

	public static String cleanPhoneNumber(String phone) {
		String numerals = "0123456789";
		StringBuffer newnum = new StringBuffer();
		for (char c : phone.toCharArray())
			if (numerals.indexOf(c) > -1) newnum.append(c);
		return newnum.toString();
	}

    public boolean equals(Account person) {
        if (person == null || person.getEmail() == null || this.name == null) return false;
        return (this.id != 0 && this.id == person.id) || (this.name.equalsIgnoreCase(person.getName()));
    }

}
