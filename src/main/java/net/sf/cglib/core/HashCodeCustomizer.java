/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.core;

import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.KeyFactoryCustomizer;
import org.objectweb.asm.Type;

public interface HashCodeCustomizer
extends KeyFactoryCustomizer {
    public boolean customize(CodeEmitter var1, Type var2);
}

