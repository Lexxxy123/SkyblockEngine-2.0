/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import javax.net.ssl.SSLParameters;

interface SniSslHelper {
    public void enableSni(String var1, SSLParameters var2);
}

