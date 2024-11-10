/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.transform;

import net.sf.cglib.transform.AbstractClassFilterTransformer;
import net.sf.cglib.transform.ClassFilter;
import net.sf.cglib.transform.ClassTransformer;

public class ClassFilterTransformer
extends AbstractClassFilterTransformer {
    private ClassFilter filter;

    public ClassFilterTransformer(ClassFilter filter, ClassTransformer pass) {
        super(pass);
        this.filter = filter;
    }

    protected boolean accept(int version, int access, String name, String signature, String superName, String[] interfaces) {
        return this.filter.accept(name.replace('/', '.'));
    }
}

