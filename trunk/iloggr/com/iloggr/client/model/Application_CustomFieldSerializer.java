/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;

import java.util.Date;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;


public class Application_CustomFieldSerializer {


	public static void serialize(SerializationStreamWriter writer, Application entity) throws SerializationException {
		writer.writeLong(entity.getId());
		writer.writeString(entity.getAppID());
		writer.writeString(entity.getName());
		writer.writeObject(entity.getReleaseDate());
		writer.writeObject(entity.getProvisioning());
		writer.writeString(entity.getAnnouncement());
	}

	public static void deserialize(SerializationStreamReader reader, Application entity) throws SerializationException {
		entity.setId(reader.readLong());
		entity.setAppID(reader.readString());
	  	entity.setName(reader.readString());
	  	entity.setReleaseDate((Date) reader.readObject());
	  	entity.setProvisioning((Provisioning)reader.readObject());
	  	entity.setAnnouncement(reader.readString());
	}
}
