/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.skyblock.skyblock.reflections.serializers;

import com.skyblock.skyblock.reflections.Reflections;
import java.io.File;
import java.io.InputStream;

public interface Serializer {
    public Reflections read(InputStream var1);

    public File save(Reflections var1, String var2);

    public static File prepareFile(String filename) {
        File file = new File(filename);
        File parent = file.getAbsoluteFile().getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        return file;
    }
}

