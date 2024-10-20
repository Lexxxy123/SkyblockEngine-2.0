/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.AuthenticationMechanism;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.MongoSecurityException;
import com.mongodb.ServerAddress;
import com.mongodb.internal.connection.MongoCredentialWithCache;
import com.mongodb.internal.connection.SaslAuthenticator;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

class GSSAPIAuthenticator
extends SaslAuthenticator {
    private static final String GSSAPI_MECHANISM_NAME = "GSSAPI";
    private static final String GSSAPI_OID = "1.2.840.113554.1.2.2";
    private static final String SERVICE_NAME_DEFAULT_VALUE = "mongodb";
    private static final Boolean CANONICALIZE_HOST_NAME_DEFAULT_VALUE = false;

    GSSAPIAuthenticator(MongoCredentialWithCache credential) {
        super(credential);
        if (this.getMongoCredential().getAuthenticationMechanism() != AuthenticationMechanism.GSSAPI) {
            throw new MongoException("Incorrect mechanism: " + this.getMongoCredential().getMechanism());
        }
    }

    @Override
    public String getMechanismName() {
        return GSSAPI_MECHANISM_NAME;
    }

    @Override
    protected SaslClient createSaslClient(ServerAddress serverAddress) {
        MongoCredential credential = this.getMongoCredential();
        try {
            SaslClient saslClient;
            HashMap<String, Object> saslClientProperties = credential.getMechanismProperty("JAVA_SASL_CLIENT_PROPERTIES", null);
            if (saslClientProperties == null) {
                saslClientProperties = new HashMap<String, Object>();
                saslClientProperties.put("javax.security.sasl.maxbuffer", "0");
                saslClientProperties.put("javax.security.sasl.credentials", this.getGSSCredential(credential.getUserName()));
            }
            if ((saslClient = Sasl.createSaslClient(new String[]{AuthenticationMechanism.GSSAPI.getMechanismName()}, credential.getUserName(), credential.getMechanismProperty("SERVICE_NAME", SERVICE_NAME_DEFAULT_VALUE), this.getHostName(serverAddress), saslClientProperties, null)) == null) {
                throw new MongoSecurityException(credential, String.format("No platform support for %s mechanism", new Object[]{AuthenticationMechanism.GSSAPI}));
            }
            return saslClient;
        } catch (SaslException e2) {
            throw new MongoSecurityException(credential, "Exception initializing SASL client", (Throwable)e2);
        } catch (GSSException e3) {
            throw new MongoSecurityException(credential, "Exception initializing GSSAPI credentials", (Throwable)e3);
        } catch (UnknownHostException e4) {
            throw new MongoSecurityException(credential, "Unable to canonicalize host name + " + serverAddress);
        }
    }

    private GSSCredential getGSSCredential(String userName) throws GSSException {
        Oid krb5Mechanism = new Oid(GSSAPI_OID);
        GSSManager manager = GSSManager.getInstance();
        GSSName name = manager.createName(userName, GSSName.NT_USER_NAME);
        return manager.createCredential(name, Integer.MAX_VALUE, krb5Mechanism, 1);
    }

    private String getHostName(ServerAddress serverAddress) throws UnknownHostException {
        return this.getNonNullMechanismProperty("CANONICALIZE_HOST_NAME", CANONICALIZE_HOST_NAME_DEFAULT_VALUE) != false ? InetAddress.getByName(serverAddress.getHost()).getCanonicalHostName() : serverAddress.getHost();
    }
}

