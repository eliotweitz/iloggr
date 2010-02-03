package com.iloggr.client.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Index;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;


@Entity
public class InvitationCode {
	   private Long id;
	   private String name;
	   private String code;
	   private Account account;
	   private String comments;
	   
	   
	public InvitationCode() {
	}


	public InvitationCode(Long id, String name, String code, Account account) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.account = account;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty
	@NotNull
	@Index(name="INVITENAME")
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	@NotEmpty
	@NotNull
	@Index(name="INVITECODE")
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	@OneToOne(cascade=CascadeType.ALL)
	public Account getAccount() {
		return account;
	}


	public void setAccount(Account account) {
		this.account = account;
	}


	public String getComments() {
		return comments;
	}


	public void setComments(String comments) {
		this.comments = comments;
	}
	
		

}
