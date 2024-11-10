/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.core;

import net.sf.cglib.core.ClassInfo;
import net.sf.cglib.core.Signature;
import org.objectweb.asm.Type;

public abstract class MethodInfo {
    protected MethodInfo() {
    }

    public abstract ClassInfo getClassInfo();

    public abstract int getModifiers();

    public abstract Signature getSignature();

    public abstract Type[] getExceptionTypes();

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof MethodInfo)) {
            return false;
        }
        return this.getSignature().equals(((MethodInfo)o).getSignature());
    }

    public int hashCode() {
        return this.getSignature().hashCode();
    }

    public String toString() {
        return this.getSignature().toString();
    }
}

