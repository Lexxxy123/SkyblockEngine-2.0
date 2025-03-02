/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.skyblock.skyblock.reflections.vfs;

import com.skyblock.skyblock.reflections.Reflections;
import com.skyblock.skyblock.reflections.ReflectionsException;
import com.skyblock.skyblock.reflections.vfs.JarInputFile;
import com.skyblock.skyblock.reflections.vfs.Vfs;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class JarInputDir
implements Vfs.Dir {
    private final URL url;
    JarInputStream jarInputStream;
    long cursor = 0L;
    long nextCursor = 0L;

    public JarInputDir(URL url) {
        this.url = url;
    }

    @Override
    public String getPath() {
        return this.url.getPath();
    }

    @Override
    public Iterable<Vfs.File> getFiles() {
        return () -> new Iterator<Vfs.File>(){
            Vfs.File entry;
            {
                try {
                    JarInputDir.this.jarInputStream = new JarInputStream(JarInputDir.this.url.openConnection().getInputStream());
                } catch (Exception e2) {
                    throw new ReflectionsException("Could not open url connection", e2);
                }
                this.entry = null;
            }

            @Override
            public boolean hasNext() {
                return this.entry != null || (this.entry = this.computeNext()) != null;
            }

            @Override
            public Vfs.File next() {
                Vfs.File next = this.entry;
                this.entry = null;
                return next;
            }

            private Vfs.File computeNext() {
                try {
                    JarEntry entry;
                    do {
                        if ((entry = JarInputDir.this.jarInputStream.getNextJarEntry()) == null) {
                            return null;
                        }
                        long size = entry.getSize();
                        if (size < 0L) {
                            size = 0xFFFFFFFFL + size;
                        }
                        JarInputDir.this.nextCursor += size;
                    } while (entry.isDirectory());
                    return new JarInputFile(entry, JarInputDir.this, JarInputDir.this.cursor, JarInputDir.this.nextCursor);
                } catch (IOException e2) {
                    throw new ReflectionsException("could not get next zip entry", e2);
                }
            }
        };
    }

    @Override
    public void close() {
        block3: {
            try {
                if (this.jarInputStream != null) {
                    ((InputStream)this.jarInputStream).close();
                }
            } catch (IOException e2) {
                if (Reflections.log == null) break block3;
                Reflections.log.warn("Could not close InputStream", e2);
            }
        }
    }
}

