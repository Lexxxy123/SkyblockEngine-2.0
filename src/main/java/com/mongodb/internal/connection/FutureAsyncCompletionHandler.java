/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.MongoException;
import com.mongodb.MongoInternalException;
import com.mongodb.MongoInterruptedException;
import com.mongodb.connection.AsyncCompletionHandler;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

class FutureAsyncCompletionHandler<T>
implements AsyncCompletionHandler<T> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile T result;
    private volatile Throwable error;

    FutureAsyncCompletionHandler() {
    }

    @Override
    public void completed(T result) {
        this.result = result;
        this.latch.countDown();
    }

    @Override
    public void failed(Throwable t2) {
        this.error = t2;
        this.latch.countDown();
    }

    public void getOpen() throws IOException {
        this.get("Opening");
    }

    public void getWrite() throws IOException {
        this.get("Writing to");
    }

    public T getRead() throws IOException {
        return this.get("Reading from");
    }

    private T get(String prefix) throws IOException {
        try {
            this.latch.await();
        } catch (InterruptedException e2) {
            throw new MongoInterruptedException(prefix + " the AsynchronousSocketChannelStream failed", e2);
        }
        if (this.error != null) {
            if (this.error instanceof IOException) {
                throw (IOException)this.error;
            }
            if (this.error instanceof MongoException) {
                throw (MongoException)this.error;
            }
            throw new MongoInternalException(prefix + " the AsynchronousSocketChannelStream failed", this.error);
        }
        return this.result;
    }
}

