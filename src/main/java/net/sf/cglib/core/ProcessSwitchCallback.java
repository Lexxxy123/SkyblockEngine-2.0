/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.core;

import org.objectweb.asm.Label;

public interface ProcessSwitchCallback {
    public void processCase(int var1, Label var2) throws Exception;

    public void processDefault() throws Exception;
}

