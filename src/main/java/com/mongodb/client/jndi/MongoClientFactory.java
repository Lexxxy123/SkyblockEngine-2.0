/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.jndi;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

public class MongoClientFactory
implements ObjectFactory {
    private static final Logger LOGGER = Loggers.getLogger("client.jndi");
    private static final String CONNECTION_STRING = "connectionString";

    @Override
    public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception {
        String connectionString = null;
        if (environment.get(CONNECTION_STRING) instanceof String) {
            connectionString = (String)environment.get(CONNECTION_STRING);
        }
        if (connectionString == null || connectionString.isEmpty()) {
            LOGGER.debug(String.format("No '%s' property in environment.  Casting 'obj' to java.naming.Reference to look for a javax.naming.RefAddr with type equal to '%s'", CONNECTION_STRING, CONNECTION_STRING));
            if (obj instanceof Reference) {
                Enumeration<RefAddr> props = ((Reference)obj).getAll();
                while (props.hasMoreElements()) {
                    RefAddr addr = props.nextElement();
                    if (addr == null || !CONNECTION_STRING.equals(addr.getType()) || !(addr.getContent() instanceof String)) continue;
                    connectionString = (String)addr.getContent();
                    break;
                }
            }
        }
        if (connectionString == null || connectionString.isEmpty()) {
            throw new MongoException(String.format("Could not locate '%s' in either environment or obj", CONNECTION_STRING));
        }
        MongoClientURI uri = new MongoClientURI(connectionString);
        return new MongoClient(uri);
    }
}

