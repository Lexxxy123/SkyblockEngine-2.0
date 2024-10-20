/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.diagnostics.logging;

import com.mongodb.diagnostics.logging.Logger;
import java.util.logging.Level;

class JULLogger
implements Logger {
    private final java.util.logging.Logger delegate;

    JULLogger(String name) {
        this.delegate = java.util.logging.Logger.getLogger(name);
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return this.isEnabled(Level.FINER);
    }

    @Override
    public void trace(String msg) {
        this.log(Level.FINER, msg);
    }

    @Override
    public void trace(String msg, Throwable t2) {
        this.log(Level.FINER, msg, t2);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.isEnabled(Level.FINE);
    }

    @Override
    public void debug(String msg) {
        this.log(Level.FINE, msg);
    }

    @Override
    public void debug(String msg, Throwable t2) {
        this.log(Level.FINE, msg, t2);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.delegate.isLoggable(Level.INFO);
    }

    @Override
    public void info(String msg) {
        this.log(Level.INFO, msg);
    }

    @Override
    public void info(String msg, Throwable t2) {
        this.log(Level.INFO, msg, t2);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.delegate.isLoggable(Level.WARNING);
    }

    @Override
    public void warn(String msg) {
        this.log(Level.WARNING, msg);
    }

    @Override
    public void warn(String msg, Throwable t2) {
        this.log(Level.WARNING, msg, t2);
    }

    @Override
    public boolean isErrorEnabled() {
        return this.delegate.isLoggable(Level.SEVERE);
    }

    @Override
    public void error(String msg) {
        this.log(Level.SEVERE, msg);
    }

    @Override
    public void error(String msg, Throwable t2) {
        this.log(Level.SEVERE, msg, t2);
    }

    private boolean isEnabled(Level level) {
        return this.delegate.isLoggable(level);
    }

    private void log(Level level, String msg) {
        this.delegate.log(level, msg);
    }

    public void log(Level level, String msg, Throwable t2) {
        this.delegate.log(level, msg, t2);
    }
}

