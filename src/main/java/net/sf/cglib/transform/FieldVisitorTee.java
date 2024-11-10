/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.sf.cglib.transform;

import net.sf.cglib.core.Constants;
import net.sf.cglib.transform.AnnotationVisitorTee;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;

public class FieldVisitorTee
extends FieldVisitor {
    private FieldVisitor fv1;
    private FieldVisitor fv2;

    public FieldVisitorTee(FieldVisitor fv1, FieldVisitor fv2) {
        super(Constants.ASM_API);
        this.fv1 = fv1;
        this.fv2 = fv2;
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return AnnotationVisitorTee.getInstance(this.fv1.visitAnnotation(desc, visible), this.fv2.visitAnnotation(desc, visible));
    }

    public void visitAttribute(Attribute attr) {
        this.fv1.visitAttribute(attr);
        this.fv2.visitAttribute(attr);
    }

    public void visitEnd() {
        this.fv1.visitEnd();
        this.fv2.visitEnd();
    }

    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
        return AnnotationVisitorTee.getInstance(this.fv1.visitTypeAnnotation(typeRef, typePath, desc, visible), this.fv2.visitTypeAnnotation(typeRef, typePath, desc, visible));
    }
}

