/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.diagnostics.logging;

import com.mongodb.diagnostics.logging.Logger;
import org.slf4j.LoggerFactory;

class SLF4JLogger
implements Logger {
    private final org.slf4j.Logger delegate;

    SLF4JLogger(String name) {
        this.delegate = LoggerFactory.getLogger(name);
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return this.delegate.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        this.delegate.trace(msg);
    }

    @Override
    public void trace(String msg, Throwable t2) {
        this.delegate.trace(msg, t2);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.delegate.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        this.delegate.debug(msg);
    }

    @Override
    public void debug(String msg, Throwable t2) {
        this.delegate.debug(msg, t2);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.delegate.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        this.delegate.info(msg);
    }

    @Override
    public void info(String msg, Throwable t2) {
        this.delegate.info(msg, t2);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.delegate.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        this.delegate.warn(msg);
    }

    @Override
    public void warn(String msg, Throwable t2) {
        this.delegate.warn(msg, t2);
    }

    @Override
    public boolean isErrorEnabled() {
        return this.delegate.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        this.delegate.error(msg);
    }

    @Override
    public void error(String msg, Throwable t2) {
        this.delegate.error(msg, t2);
    }
}

