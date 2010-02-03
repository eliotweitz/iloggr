/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;


public class UserReportData_CustomFieldSerializer {
	public static void serialize(SerializationStreamWriter writer, UserReportData entity) throws SerializationException {
		writer.writeString(entity.getDescription());
		int type = entity.getType();
		writer.writeInt(type);
		if (type == UserReportData.LONG) {
			writer.writeLong(entity.getLongdata());
		} else {
			writer.writeFloat(entity.getFloatdata());
		}
	}

	public static void deserialize(SerializationStreamReader reader, UserReportData entity) throws SerializationException {
		entity.setDescription(reader.readString());
		int type = reader.readInt();
		entity.setType(type);
		if (type == UserReportData.LONG) {
			entity.setLongdata(reader.readLong());
		} else {
			entity.setFloatdata(reader.readFloat());
		}
	}

}
