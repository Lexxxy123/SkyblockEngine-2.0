/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.transform;

import net.sf.cglib.core.Constants;
import net.sf.cglib.transform.ClassTransformer;
import net.sf.cglib.transform.ClassVisitorTee;
import org.objectweb.asm.ClassVisitor;

public class ClassTransformerTee
extends ClassTransformer {
    private ClassVisitor branch;

    public ClassTransformerTee(ClassVisitor branch) {
        super(Constants.ASM_API);
        this.branch = branch;
    }

    public void setTarget(ClassVisitor target) {
        this.cv = new ClassVisitorTee(this.branch, target);
    }
}

