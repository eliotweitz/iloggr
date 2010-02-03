/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;

import java.util.Date;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public class Event_CustomFieldSerializer {

	/*
		private LocationFix fix;
		private Application application;
		private Phone phone;
	 */

	public static void serialize(SerializationStreamWriter writer, Event entity) throws SerializationException {
		writer.writeLong(entity.getId());
		writer.writeObject(entity.getRecordTime());
		writer.writeString(entity.getDescription());
		writer.writeDouble(entity.getData());
		writer.writeObject(entity.getPhone());
		writer.writeDouble(entity.getLatitude());
		writer.writeDouble(entity.getLongitude());
	}

	public static void deserialize(SerializationStreamReader reader, Event entity) throws SerializationException {
	  	entity.setId(reader.readLong());
	  	entity.setRecordTime((Date) reader.readObject());
	  	entity.setDescription(reader.readString());
	  	entity.setData(reader.readDouble());
	  	entity.setPhone((Phone)reader.readObject());
	  	entity.setLatitude(reader.readDouble());
	  	entity.setLongitude(reader.readDouble());
	}
}
