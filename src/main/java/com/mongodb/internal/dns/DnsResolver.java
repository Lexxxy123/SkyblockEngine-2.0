/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.dns;

import java.util.List;

public interface DnsResolver {
    public List<String> resolveHostFromSrvRecords(String var1);

    public String resolveAdditionalQueryParametersFromTxtRecords(String var1);
}

