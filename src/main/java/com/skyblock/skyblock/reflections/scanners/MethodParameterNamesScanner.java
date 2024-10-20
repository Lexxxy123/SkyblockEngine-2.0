/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.skyblock.skyblock.reflections.scanners;

import com.skyblock.skyblock.reflections.scanners.Scanner;
import com.skyblock.skyblock.reflections.util.JavassistHelper;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class MethodParameterNamesScanner
implements Scanner {
    @Override
    public List<Map.Entry<String, String>> scan(ClassFile classFile) {
        ArrayList<Map.Entry<String, String>> entries = new ArrayList<Map.Entry<String, String>>();
        for (MethodInfo method : classFile.getMethods()) {
            String key = JavassistHelper.methodName(classFile, method);
            String value = this.getString(method);
            if (value.isEmpty()) continue;
            entries.add(this.entry(key, value));
        }
        return entries;
    }

    private String getString(MethodInfo method) {
        CodeAttribute codeAttribute = method.getCodeAttribute();
        LocalVariableAttribute table = codeAttribute != null ? (LocalVariableAttribute)codeAttribute.getAttribute("LocalVariableTable") : null;
        int length = JavassistHelper.getParameters(method).size();
        if (length > 0) {
            int shift = Modifier.isStatic(method.getAccessFlags()) ? 0 : 1;
            return IntStream.range(shift, length + shift).mapToObj(i2 -> method.getConstPool().getUtf8Info(table.nameIndex(i2))).filter(name -> !name.startsWith("this$")).collect(Collectors.joining(", "));
        }
        return "";
    }
}

