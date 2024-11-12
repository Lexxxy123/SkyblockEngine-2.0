/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.diagnostics.logging;

public interface Logger {
    public String getName();

    public boolean isTraceEnabled();

    public void trace(String var1);

    public void trace(String var1, Throwable var2);

    public boolean isDebugEnabled();

    public void debug(String var1);

    public void debug(String var1, Throwable var2);

    public boolean isInfoEnabled();

    public void info(String var1);

    public void info(String var1, Throwable var2);

    public boolean isWarnEnabled();

    public void warn(String var1);

    public void warn(String var1, Throwable var2);

    public boolean isErrorEnabled();

    public void error(String var1);

    public void error(String var1, Throwable var2);
}

