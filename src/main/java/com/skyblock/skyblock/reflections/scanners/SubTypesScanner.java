/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.skyblock.skyblock.reflections.scanners;

import com.skyblock.skyblock.reflections.scanners.AbstractScanner;
import com.skyblock.skyblock.reflections.scanners.Scanners;
import java.util.List;
import java.util.Map;
import javassist.bytecode.ClassFile;

@Deprecated
public class SubTypesScanner
extends AbstractScanner {
    @Deprecated
    public SubTypesScanner() {
        super(Scanners.SubTypes);
    }

    @Deprecated
    public SubTypesScanner(boolean excludeObjectClass) {
        super(excludeObjectClass ? Scanners.SubTypes : Scanners.SubTypes.filterResultsBy(s2 -> true));
    }

    @Override
    public List<Map.Entry<String, String>> scan(ClassFile cls) {
        return this.scanner.scan(cls);
    }
}

