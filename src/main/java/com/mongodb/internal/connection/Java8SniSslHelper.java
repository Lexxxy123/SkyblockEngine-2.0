/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.internal.connection.SniSslHelper;
import java.util.Collections;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLParameters;

final class Java8SniSslHelper
implements SniSslHelper {
    @Override
    public void enableSni(String host, SSLParameters sslParameters) {
        try {
            SNIHostName sniHostName = new SNIHostName(host);
            sslParameters.setServerNames(Collections.singletonList(sniHostName));
        } catch (IllegalArgumentException illegalArgumentException) {
            // empty catch block
        }
    }

    Java8SniSslHelper() {
    }

    static {
        try {
            Class.forName("javax.net.ssl.SNIHostName");
        } catch (ClassNotFoundException e2) {
            throw new ExceptionInInitializerError(e2);
        }
    }
}

