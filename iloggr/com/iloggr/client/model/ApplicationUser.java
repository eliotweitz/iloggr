package com.iloggr.client.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Index;

/**
 * @author eliot
 * TODO - for future use to manage users
 * This class will map a user id for an application back to a phone using the phone's unique
 * client Identifier
 *
 */
@Entity
public class ApplicationUser extends iLoggrObject{
	private Long id;
	String userID;
	Application application;
	Phone phone;
	String clientID; // store here for convenience
	
	
	public ApplicationUser() {
	}


	public ApplicationUser(Long id, String userID, Application application,
			Phone phone, String clientID) {
		super();
		this.id = id;
		this.userID = userID;
		this.application = application;
		this.phone = phone;
		this.clientID = clientID;
	}


	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Index(name="USERID")
	public String getUserID() {
		return userID;
	}


	public void setUserID(String userID) {
		this.userID = userID;
	}

	@ManyToOne
	public Application getApplication() {
		return application;
	}


	public void setApplication(Application application) {
		this.application = application;
	}

	@ManyToOne
	public Phone getPhone() {
		return phone;
	}


	public void setPhone(Phone phone) {
		this.phone = phone;
	}


	@Index(name="CLIENTID")
	public String getClientID() {
		return clientID;
	}


	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	
	
	
	

}
