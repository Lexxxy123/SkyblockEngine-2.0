/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.skyblock.skyblock.reflections;

import com.skyblock.skyblock.reflections.scanners.Scanner;
import java.net.URL;
import java.util.Set;
import java.util.function.Predicate;

public interface Configuration {
    public Set<Scanner> getScanners();

    public Set<URL> getUrls();

    public Predicate<String> getInputsFilter();

    public boolean isParallel();

    public ClassLoader[] getClassLoaders();

    public boolean shouldExpandSuperTypes();
}

