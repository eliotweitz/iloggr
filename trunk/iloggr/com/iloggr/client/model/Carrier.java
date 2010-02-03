/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;




/**
 * Jul 27, 2007
 * 
 */
@Entity
public class Carrier extends iLoggrObject {

    private Long id;
    private String name;
    private String textGateway;

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

    public String getTextGateway() {
        return textGateway;
    }

    public void setTextGateway(String textGateway) {
        this.textGateway = textGateway;
    }

    public Carrier() {
    	// preserve
    }

    public Carrier(String name, String textGateway) {
        this.name = name;
        this.textGateway = textGateway;
    }
}

