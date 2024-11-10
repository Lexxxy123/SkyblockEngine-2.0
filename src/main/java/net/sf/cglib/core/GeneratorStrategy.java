/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.core;

import net.sf.cglib.core.ClassGenerator;

public interface GeneratorStrategy {
    public byte[] generate(ClassGenerator var1) throws Exception;

    public boolean equals(Object var1);
}

