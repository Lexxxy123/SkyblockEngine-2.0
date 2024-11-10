/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.transform.impl;

import org.objectweb.asm.Type;

public interface InterceptFieldFilter {
    public boolean acceptRead(Type var1, String var2);

    public boolean acceptWrite(Type var1, String var2);
}

