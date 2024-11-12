/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.skyblock.skyblock.reflections.vfs;

import com.skyblock.skyblock.reflections.Reflections;
import com.skyblock.skyblock.reflections.vfs.Vfs;
import com.skyblock.skyblock.reflections.vfs.ZipFile;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ZipDir
implements Vfs.Dir {
    final java.util.zip.ZipFile jarFile;

    public ZipDir(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    @Override
    public String getPath() {
        return this.jarFile != null ? this.jarFile.getName().replace("\\", "/") : "/NO-SUCH-DIRECTORY/";
    }

    @Override
    public Iterable<Vfs.File> getFiles() {
        return () -> this.jarFile.stream().filter(entry -> !entry.isDirectory()).map(entry -> new ZipFile(this, (ZipEntry)entry)).iterator();
    }

    @Override
    public void close() {
        block2: {
            try {
                this.jarFile.close();
            } catch (IOException e2) {
                if (Reflections.log == null) break block2;
                Reflections.log.warn("Could not close JarFile", e2);
            }
        }
    }

    public String toString() {
        return this.jarFile.getName();
    }
}

