
/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;



import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public class LocationFix_CustomFieldSerializer {


	public static void serialize(SerializationStreamWriter writer, LocationFix entity) throws SerializationException {
		writer.writeLong(entity.getId());
		writer.writeDouble(entity.getLatitude());
		writer.writeDouble(entity.getLongitude());
	}

	public static void deserialize(SerializationStreamReader reader, LocationFix entity) throws SerializationException {
		entity.setId(reader.readLong());
		entity.setLatitude(reader.readDouble());
		entity.setLongitude(reader.readDouble());
	}
}




