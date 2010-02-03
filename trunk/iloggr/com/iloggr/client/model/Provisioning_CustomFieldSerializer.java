/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client.model;

import java.util.Date;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;


public class Provisioning_CustomFieldSerializer {
	public static void serialize(SerializationStreamWriter writer, Provisioning entity) throws SerializationException {
		writer.writeLong(entity.getId());
		writer.writeString(entity.getVersion());
		writer.writeObject(entity.getLastUpdate());
//	    HashSet<ProvisioningParameter> parameters = new HashSet<ProvisioningParameter>();
//	    for (ProvisioningParameter pp : entity.getParameters()) parameters.add(pp);
//	    writer.writeObject(parameters);
	}

//	@SuppressWarnings("unchecked")
	public static void deserialize(SerializationStreamReader reader, Provisioning entity) throws SerializationException {
		entity.setId(reader.readLong());
	  	entity.setVersion(reader.readString());
	  	entity.setLastUpdate((Date) reader.readObject());
//	   	entity.setParameters((HashSet<ProvisioningParameter>)reader.readObject());
	}
}
