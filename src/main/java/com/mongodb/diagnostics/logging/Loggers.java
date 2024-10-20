/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.diagnostics.logging;

import com.mongodb.assertions.Assertions;
import com.mongodb.diagnostics.logging.JULLogger;
import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.SLF4JLogger;

public final class Loggers {
    public static final String PREFIX = "org.mongodb.driver";
    private static final boolean USE_SLF4J = Loggers.shouldUseSLF4J();

    public static Logger getLogger(String suffix) {
        Assertions.notNull("suffix", suffix);
        if (suffix.startsWith(".") || suffix.endsWith(".")) {
            throw new IllegalArgumentException("The suffix can not start or end with a '.'");
        }
        String name = "org.mongodb.driver." + suffix;
        if (USE_SLF4J) {
            return new SLF4JLogger(name);
        }
        return new JULLogger(name);
    }

    private Loggers() {
    }

    private static boolean shouldUseSLF4J() {
        try {
            Class.forName("org.slf4j.Logger");
            return true;
        } catch (ClassNotFoundException e2) {
            return false;
        }
    }
}

