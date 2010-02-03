/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public class ProvisioningParameter_CustomFieldSerializer {
		
	public static void serialize(SerializationStreamWriter writer, ProvisioningParameter entity) throws SerializationException {
			writer.writeLong(entity.getId());
			writer.writeString(entity.getName());
			writer.writeString(entity.getValue());
			writer.writeString(entity.getType());
			writer.writeBoolean(entity.isActive());
		}

		public static void deserialize(SerializationStreamReader reader, ProvisioningParameter entity) throws SerializationException {
			entity.setId(reader.readLong());
		  	entity.setName(reader.readString());
		  	entity.setValue(reader.readString());
		  	entity.setType(reader.readString());
		  	entity.setActive(reader.readBoolean());
		}

}
