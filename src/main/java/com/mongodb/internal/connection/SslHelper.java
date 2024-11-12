/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.SniSslHelper;
import java.lang.reflect.InvocationTargetException;
import javax.net.ssl.SSLParameters;

public final class SslHelper {
    private static final SniSslHelper SNI_SSL_HELPER;

    public static void enableHostNameVerification(SSLParameters sslParameters) {
        sslParameters.setEndpointIdentificationAlgorithm("HTTPS");
    }

    public static void enableSni(String host, SSLParameters sslParameters) {
        if (SNI_SSL_HELPER != null) {
            SNI_SSL_HELPER.enableSni(host, sslParameters);
        }
    }

    private SslHelper() {
    }

    static {
        SniSslHelper sniSslHelper;
        try {
            sniSslHelper = (SniSslHelper)Class.forName("com.mongodb.internal.connection.Java8SniSslHelper").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (ClassNotFoundException e2) {
            throw new ExceptionInInitializerError(e2);
        } catch (InstantiationException e3) {
            throw new ExceptionInInitializerError(e3);
        } catch (IllegalAccessException e4) {
            throw new ExceptionInInitializerError(e4);
        } catch (NoSuchMethodException e5) {
            throw new ExceptionInInitializerError(e5);
        } catch (InvocationTargetException e6) {
            throw new ExceptionInInitializerError(e6.getTargetException());
        } catch (LinkageError t2) {
            sniSslHelper = null;
        }
        SNI_SSL_HELPER = sniSslHelper;
    }
}

