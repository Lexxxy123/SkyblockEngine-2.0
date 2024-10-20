/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection.tlschannel;

import com.mongodb.internal.connection.tlschannel.BufferAllocator;
import java.nio.channels.ByteChannel;
import java.util.function.Consumer;
import javax.net.ssl.SSLSession;

public abstract class TlsChannelBuilder<T extends TlsChannelBuilder<T>> {
    final ByteChannel underlying;
    Consumer<SSLSession> sessionInitCallback = new Consumer<SSLSession>(){

        @Override
        public void accept(SSLSession session) {
        }
    };
    boolean runTasks = true;
    BufferAllocator plainBufferAllocator = null;
    BufferAllocator encryptedBufferAllocator = null;
    boolean releaseBuffers = true;
    boolean waitForCloseConfirmation = false;

    TlsChannelBuilder(ByteChannel underlying) {
        this.underlying = underlying;
    }

    abstract T getThis();

    public T withRunTasks(boolean runTasks) {
        this.runTasks = runTasks;
        return this.getThis();
    }

    public T withPlainBufferAllocator(BufferAllocator bufferAllocator) {
        this.plainBufferAllocator = bufferAllocator;
        return this.getThis();
    }

    public T withEncryptedBufferAllocator(BufferAllocator bufferAllocator) {
        this.encryptedBufferAllocator = bufferAllocator;
        return this.getThis();
    }

    public T withSessionInitCallback(Consumer<SSLSession> sessionInitCallback) {
        this.sessionInitCallback = sessionInitCallback;
        return this.getThis();
    }

    public T withReleaseBuffers(boolean releaseBuffers) {
        this.releaseBuffers = releaseBuffers;
        return this.getThis();
    }

    public T withWaitForCloseConfirmation(boolean waitForCloseConfirmation) {
        this.waitForCloseConfirmation = waitForCloseConfirmation;
        return this.getThis();
    }
}

