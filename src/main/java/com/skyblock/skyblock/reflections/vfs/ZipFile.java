/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.skyblock.skyblock.reflections.vfs;

import com.skyblock.skyblock.reflections.vfs.Vfs;
import com.skyblock.skyblock.reflections.vfs.ZipDir;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class ZipFile
implements Vfs.File {
    private final ZipDir root;
    private final ZipEntry entry;

    public ZipFile(ZipDir root, ZipEntry entry) {
        this.root = root;
        this.entry = entry;
    }

    @Override
    public String getName() {
        String name = this.entry.getName();
        return name.substring(name.lastIndexOf("/") + 1);
    }

    @Override
    public String getRelativePath() {
        return this.entry.getName();
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return this.root.jarFile.getInputStream(this.entry);
    }

    public String toString() {
        return this.root.getPath() + "!" + File.separatorChar + this.entry.toString();
    }
}

