/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.transform;

import net.sf.cglib.core.Constants;
import net.sf.cglib.transform.ClassTransformer;
import org.objectweb.asm.ClassVisitor;

public abstract class AbstractClassTransformer
extends ClassTransformer {
    protected AbstractClassTransformer() {
        super(Constants.ASM_API);
    }

    public void setTarget(ClassVisitor target) {
        this.cv = target;
    }
}

