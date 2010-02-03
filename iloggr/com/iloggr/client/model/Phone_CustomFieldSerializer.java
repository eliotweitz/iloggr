/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;


import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;


public class Phone_CustomFieldSerializer {


	public static void serialize(SerializationStreamWriter writer, Phone entity) throws SerializationException {
		writer.writeLong(entity.getId());
		writer.writeString(entity.getClientID());
	}

	public static void deserialize(SerializationStreamReader reader, Phone entity) throws SerializationException {
		entity.setId(reader.readLong());
		entity.setClientID(reader.readString());
	}
}

