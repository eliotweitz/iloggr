/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;



/**
 * The Action class is used to send text and email notifications to users.  For instance, when a user signs
 * up for the iLoggr service, an action is created that is put into a table for processing by the ActionEngine
 * that will generate and send an email to the user to activate their account.
 *
 * @author eliot
 * @version 1.0
 * @see com.iloggr.server.actionengine.ActionManager
 * @see com.iloggr.server.actionengine.ActionEngine
 *
 *
 */
@Entity
public class Action {

    private Long id;
    public static final int TYPE_EMAIL = 0;
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_STATUS = 2;
    private int type;
    private Date date;
    private Account person;
//    private String returnAddress; TODO - authSNMP does not allow user spoofing
    private String subject;
    private String message;
 //   private ActionEvent actionEvent;
    private boolean executed;



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Account getPerson() {
        return person;
    }

    public void setPerson(Account person) {
        this.person = person;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(length=2048)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /*
    @ManyToOne
    public ActionEvent getEvent() {
        return actionEvent;
    }

    public void setEvent(ActionEvent actionEvent) {
        this.actionEvent = actionEvent;
    }
*/

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    /*
    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }
*/
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Action() {
    	// preserve
    }


    public Action(int type, Date when, Account person, String subject, String message/*, ActionEvent actionEvent*/) {
        this.type = type;
        this.date = when;
        this.person = person;
        this.subject = subject;
        this.message = message;
    //    this.actionEvent = actionEvent;
        this.executed = false;
    }

    public Action(int type, Date when/*, ActionEvent actionEvent*/) {
        this.type = type;
        this.date = when;
  //      this.actionEvent = actionEvent;
        this.executed = false;
    }
}