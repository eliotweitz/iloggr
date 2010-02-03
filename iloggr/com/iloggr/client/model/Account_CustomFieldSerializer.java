/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;


import java.util.HashSet;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public class Account_CustomFieldSerializer {

	/*
	    private String password;
	    private String securityToken;
	    private String emailToken;
	    private Date lastContactTime;
	    private int status;
	    private int notification;
	    private Set<Application> applications = new HashSet<Application>();
	*/

	public static void serialize(SerializationStreamWriter writer, Account entity) throws SerializationException {
        writer.writeLong(entity.getId());
        writer.writeString(entity.getEmail());
        writer.writeString(entity.getName());
        writer.writeString(entity.getPhoneNumber());
        writer.writeString(entity.getDescription());
        writer.writeString(entity.getSecurityToken());
        HashSet<Application> apps = new HashSet<Application>();
        for (Application a : entity.getApplications()) apps.add(a);
        writer.writeObject(apps);
    }

	@SuppressWarnings("unchecked")
	public static void deserialize(SerializationStreamReader reader, Account entity) throws SerializationException {
		entity.setId(reader.readLong());
    	entity.setEmail(reader.readString());
    	entity.setName(reader.readString());
    	entity.setPhoneNumber(reader.readString());
    	entity.setDescription(reader.readString());
    	entity.setSecurityToken(reader.readString());
    	entity.setApplications((HashSet<Application>)reader.readObject());
 	}
}
