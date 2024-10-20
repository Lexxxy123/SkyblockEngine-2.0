/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection.tlschannel.impl;

import com.mongodb.diagnostics.logging.Logger;
import com.mongodb.diagnostics.logging.Loggers;
import com.mongodb.internal.connection.tlschannel.NeedsReadException;
import com.mongodb.internal.connection.tlschannel.NeedsTaskException;
import com.mongodb.internal.connection.tlschannel.NeedsWriteException;
import com.mongodb.internal.connection.tlschannel.TrackingAllocator;
import com.mongodb.internal.connection.tlschannel.WouldBlockException;
import com.mongodb.internal.connection.tlschannel.impl.BufferHolder;
import com.mongodb.internal.connection.tlschannel.impl.ByteBufferSet;
import com.mongodb.internal.connection.tlschannel.util.TlsChannelCallbackException;
import com.mongodb.internal.connection.tlschannel.util.Util;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

public class TlsChannelImpl
implements ByteChannel {
    private static final Logger LOGGER = Loggers.getLogger("connection.tls");
    private static final int BUFFERS_INITIAL_SIZE = 4096;
    static final int MAX_TLS_PACKET_SIZE = 17408;
    private final ReadableByteChannel readChannel;
    private final WritableByteChannel writeChannel;
    private final SSLEngine engine;
    private BufferHolder inEncrypted;
    private final Consumer<SSLSession> initSessionCallback;
    private final boolean runTasks;
    private final TrackingAllocator encryptedBufAllocator;
    private final TrackingAllocator plainBufAllocator;
    private final boolean waitForCloseConfirmation;
    private final Lock initLock = new ReentrantLock();
    private final Lock readLock = new ReentrantLock();
    private final Lock writeLock = new ReentrantLock();
    private volatile boolean negotiated = false;
    private volatile boolean invalid = false;
    private volatile boolean shutdownSent = false;
    private volatile boolean shutdownReceived = false;
    private BufferHolder inPlain;
    private BufferHolder outEncrypted;
    private final ByteBufferSet dummyOut = new ByteBufferSet(new ByteBuffer[0]);

    public TlsChannelImpl(ReadableByteChannel readChannel, WritableByteChannel writeChannel, SSLEngine engine, Optional<BufferHolder> inEncrypted, Consumer<SSLSession> initSessionCallback, boolean runTasks, TrackingAllocator plainBufAllocator, final TrackingAllocator encryptedBufAllocator, final boolean releaseBuffers, boolean waitForCloseConfirmation) {
        this.readChannel = readChannel;
        this.writeChannel = writeChannel;
        this.engine = engine;
        this.inEncrypted = inEncrypted.orElseGet(new Supplier<BufferHolder>(){

            @Override
            public BufferHolder get() {
                return new BufferHolder("inEncrypted", encryptedBufAllocator, 4096, 17408, false, releaseBuffers);
            }
        });
        this.initSessionCallback = initSessionCallback;
        this.runTasks = runTasks;
        this.plainBufAllocator = plainBufAllocator;
        this.encryptedBufAllocator = encryptedBufAllocator;
        this.waitForCloseConfirmation = waitForCloseConfirmation;
        this.inPlain = new BufferHolder("inPlain", plainBufAllocator, 4096, 17408, true, releaseBuffers);
        this.outEncrypted = new BufferHolder("outEncrypted", encryptedBufAllocator, 4096, 17408, false, releaseBuffers);
    }

    public Consumer<SSLSession> getSessionInitCallback() {
        return this.initSessionCallback;
    }

    public TrackingAllocator getPlainBufferAllocator() {
        return this.plainBufAllocator;
    }

    public TrackingAllocator getEncryptedBufferAllocator() {
        return this.encryptedBufAllocator;
    }

    /*
     * Exception decompiling
     */
    public long read(ByteBufferSet dest) throws IOException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [8[CASE]], but top level block is 5[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:538)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         *     at async.DecompilerRunnable.cfrDecompilation(DecompilerRunnable.java:348)
         *     at async.DecompilerRunnable.call(DecompilerRunnable.java:309)
         *     at async.DecompilerRunnable.call(DecompilerRunnable.java:31)
         *     at java.util.concurrent.FutureTask.run(FutureTask.java:266)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
         *     at java.lang.Thread.run(Thread.java:750)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private void handleTask() throws NeedsTaskException {
        if (!this.runTasks) {
            throw new NeedsTaskException(this.engine.getDelegatedTask());
        }
        this.engine.getDelegatedTask().run();
    }

    private int transferPendingPlain(ByteBufferSet dstBuffers) {
        this.inPlain.buffer.flip();
        int bytes = dstBuffers.putRemaining(this.inPlain.buffer);
        this.inPlain.buffer.compact();
        boolean disposed = this.inPlain.release();
        if (!disposed) {
            this.inPlain.zeroRemaining();
        }
        return bytes;
    }

    private UnwrapResult unwrapLoop(Optional<ByteBufferSet> dest, SSLEngineResult.HandshakeStatus statusCondition, boolean closing) throws SSLException {
        ByteBufferSet effDest = dest.orElseGet(new Supplier<ByteBufferSet>(){

            @Override
            public ByteBufferSet get() {
                TlsChannelImpl.this.inPlain.prepare();
                return new ByteBufferSet(((TlsChannelImpl)TlsChannelImpl.this).inPlain.buffer);
            }
        });
        while (true) {
            Util.assertTrue(this.inPlain.nullOrEmpty());
            SSLEngineResult result = this.callEngineUnwrap(effDest);
            if (result.bytesProduced() > 0 || result.getStatus() == SSLEngineResult.Status.BUFFER_UNDERFLOW || !closing && result.getStatus() == SSLEngineResult.Status.CLOSED || result.getHandshakeStatus() != statusCondition) {
                boolean wasClosed = result.getStatus() == SSLEngineResult.Status.CLOSED;
                return new UnwrapResult(result.bytesProduced(), result.getHandshakeStatus(), wasClosed);
            }
            if (result.getStatus() != SSLEngineResult.Status.BUFFER_OVERFLOW) continue;
            if (dest.isPresent() && effDest == dest.get()) {
                this.inPlain.prepare();
                this.ensureInPlainCapacity(Math.min((int)dest.get().remaining() * 2, 17408));
            } else {
                this.inPlain.enlarge();
            }
            effDest = new ByteBufferSet(this.inPlain.buffer);
        }
    }

    private SSLEngineResult callEngineUnwrap(ByteBufferSet dest) throws SSLException {
        this.inEncrypted.buffer.flip();
        try {
            SSLEngineResult result = this.engine.unwrap(this.inEncrypted.buffer, dest.array, dest.offset, dest.length);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(String.format("engine.unwrap() result [%s]. Engine status: %s; inEncrypted %s; inPlain: %s", new Object[]{Util.resultToString(result), result.getHandshakeStatus(), this.inEncrypted, dest}));
            }
            SSLEngineResult sSLEngineResult = result;
            return sSLEngineResult;
        } catch (SSLException e2) {
            this.invalid = true;
            throw e2;
        } finally {
            this.inEncrypted.buffer.compact();
        }
    }

    private int readFromChannel() throws IOException, EofException {
        try {
            return TlsChannelImpl.readFromChannel(this.readChannel, this.inEncrypted.buffer);
        } catch (WouldBlockException e2) {
            throw e2;
        } catch (IOException e3) {
            this.invalid = true;
            throw e3;
        }
    }

    public static int readFromChannel(ReadableByteChannel readChannel, ByteBuffer buffer) throws IOException, EofException {
        Util.assertTrue(buffer.hasRemaining());
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Reading from channel");
        }
        int c2 = readChannel.read(buffer);
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("Read from channel; response: %s, buffer: %s", c2, buffer));
        }
        if (c2 == -1) {
            throw new EofException();
        }
        if (c2 == 0) {
            throw new NeedsReadException();
        }
        return c2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public long write(ByteBufferSet source) throws IOException {
        this.handshake();
        this.writeLock.lock();
        try {
            if (this.invalid || this.shutdownSent) {
                throw new ClosedChannelException();
            }
            long l2 = this.wrapAndWrite(source);
            return l2;
        } finally {
            this.writeLock.unlock();
        }
    }

    private long wrapAndWrite(ByteBufferSet source) throws IOException {
        long bytesToConsume = source.remaining();
        long bytesConsumed = 0L;
        this.outEncrypted.prepare();
        try {
            while (true) {
                this.writeToChannel();
                if (bytesConsumed == bytesToConsume) {
                    long l2 = bytesToConsume;
                    return l2;
                }
                WrapResult res = this.wrapLoop(source);
                bytesConsumed += (long)res.bytesConsumed;
            }
        } finally {
            this.outEncrypted.release();
        }
    }

    private WrapResult wrapLoop(ByteBufferSet source) throws SSLException {
        SSLEngineResult result;
        block5: while (true) {
            result = this.callEngineWrap(source);
            switch (result.getStatus()) {
                case OK: 
                case CLOSED: {
                    return new WrapResult(result.bytesConsumed(), result.getHandshakeStatus());
                }
                case BUFFER_OVERFLOW: {
                    Util.assertTrue(result.bytesConsumed() == 0);
                    this.outEncrypted.enlarge();
                    continue block5;
                }
                case BUFFER_UNDERFLOW: {
                    throw new IllegalStateException();
                }
            }
            break;
        }
        throw new AssertionError((Object)("Unexpected status: " + (Object)((Object)result.getStatus())));
    }

    private SSLEngineResult callEngineWrap(ByteBufferSet source) throws SSLException {
        try {
            SSLEngineResult result = this.engine.wrap(source.array, source.offset, source.length, this.outEncrypted.buffer);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(String.format("engine.wrap() result: [%s]; engine status: %s; srcBuffer: %s, outEncrypted: %s", new Object[]{Util.resultToString(result), result.getHandshakeStatus(), source, this.outEncrypted}));
            }
            return result;
        } catch (SSLException e2) {
            this.invalid = true;
            throw e2;
        }
    }

    private void ensureInPlainCapacity(int newCapacity) {
        if (this.inPlain.buffer.capacity() < newCapacity) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(String.format("inPlain buffer too small, increasing from %s to %s", this.inPlain.buffer.capacity(), newCapacity));
            }
            this.inPlain.resize(newCapacity);
        }
    }

    private void writeToChannel() throws IOException {
        if (this.outEncrypted.buffer.position() == 0) {
            return;
        }
        this.outEncrypted.buffer.flip();
        try {
            try {
                TlsChannelImpl.writeToChannel(this.writeChannel, this.outEncrypted.buffer);
            } catch (WouldBlockException e2) {
                throw e2;
            } catch (IOException e3) {
                this.invalid = true;
                throw e3;
            }
        } finally {
            this.outEncrypted.buffer.compact();
        }
    }

    private static void writeToChannel(WritableByteChannel channel, ByteBuffer src) throws IOException {
        while (src.hasRemaining()) {
            LOGGER.trace(String.format("Writing to channel: %s", src));
            int c2 = channel.write(src);
            if (c2 != 0) continue;
            throw new NeedsWriteException();
        }
    }

    public void renegotiate() throws IOException {
        try {
            this.doHandshake(true);
        } catch (EofException e2) {
            throw new ClosedChannelException();
        }
    }

    public void handshake() throws IOException {
        try {
            this.doHandshake(false);
        } catch (EofException e2) {
            throw new ClosedChannelException();
        }
    }

    private void doHandshake(boolean force) throws IOException, EofException {
        block7: {
            if (!force && this.negotiated) {
                return;
            }
            if (this.invalid || this.shutdownSent) {
                throw new ClosedChannelException();
            }
            this.initLock.lock();
            try {
                if (!force && this.negotiated) break block7;
                this.engine.beginHandshake();
                LOGGER.trace("Called engine.beginHandshake()");
                this.handshake(Optional.<ByteBufferSet>empty(), Optional.<SSLEngineResult.HandshakeStatus>empty());
                try {
                    this.initSessionCallback.accept(this.engine.getSession());
                } catch (Exception e2) {
                    LOGGER.trace("client code threw exception in session initialization callback", e2);
                    throw new TlsChannelCallbackException("session initialization callback failed", e2);
                }
                this.negotiated = true;
            } finally {
                this.initLock.unlock();
            }
        }
    }

    /*
     * Exception decompiling
     */
    private int handshake(Optional<ByteBufferSet> dest, Optional<SSLEngineResult.HandshakeStatus> handshakeStatus) throws IOException, EofException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:538)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         *     at async.DecompilerRunnable.cfrDecompilation(DecompilerRunnable.java:348)
         *     at async.DecompilerRunnable.call(DecompilerRunnable.java:309)
         *     at async.DecompilerRunnable.call(DecompilerRunnable.java:31)
         *     at java.util.concurrent.FutureTask.run(FutureTask.java:266)
         *     at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
         *     at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
         *     at java.lang.Thread.run(Thread.java:750)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private int handshakeLoop(Optional<ByteBufferSet> dest, Optional<SSLEngineResult.HandshakeStatus> handshakeStatus) throws IOException, EofException {
        Util.assertTrue(this.inPlain.nullOrEmpty());
        SSLEngineResult.HandshakeStatus status = handshakeStatus.orElseGet(new Supplier<SSLEngineResult.HandshakeStatus>(){

            @Override
            public SSLEngineResult.HandshakeStatus get() {
                return TlsChannelImpl.this.engine.getHandshakeStatus();
            }
        });
        block7: while (true) {
            switch (status) {
                case NEED_WRAP: {
                    Util.assertTrue(this.outEncrypted.nullOrEmpty());
                    WrapResult wrapResult = this.wrapLoop(this.dummyOut);
                    status = wrapResult.lastHandshakeStatus;
                    this.writeToChannel();
                    continue block7;
                }
                case NEED_UNWRAP: {
                    UnwrapResult res = this.readAndUnwrap(dest, SSLEngineResult.HandshakeStatus.NEED_UNWRAP, false);
                    status = res.lastHandshakeStatus;
                    if (res.bytesProduced <= 0) continue block7;
                    return res.bytesProduced;
                }
                case NOT_HANDSHAKING: {
                    return 0;
                }
                case NEED_TASK: {
                    this.handleTask();
                    status = this.engine.getHandshakeStatus();
                    continue block7;
                }
                case FINISHED: {
                    return 0;
                }
            }
            break;
        }
        throw new AssertionError((Object)("Unexpected status: " + (Object)((Object)status)));
    }

    private UnwrapResult readAndUnwrap(Optional<ByteBufferSet> dest, SSLEngineResult.HandshakeStatus statusCondition, boolean closing) throws IOException, EofException {
        this.inEncrypted.prepare();
        try {
            while (true) {
                Util.assertTrue(this.inPlain.nullOrEmpty());
                UnwrapResult res = this.unwrapLoop(dest, statusCondition, closing);
                if (res.bytesProduced > 0 || res.lastHandshakeStatus != statusCondition || !closing && res.wasClosed) {
                    if (res.wasClosed) {
                        this.shutdownReceived = true;
                    }
                    UnwrapResult unwrapResult = res;
                    return unwrapResult;
                }
                if (!this.inEncrypted.buffer.hasRemaining()) {
                    this.inEncrypted.enlarge();
                }
                this.readFromChannel();
            }
        } finally {
            this.inEncrypted.release();
        }
    }

    @Override
    public void close() throws IOException {
        this.tryShutdown();
        this.writeChannel.close();
        this.readChannel.close();
        this.readLock.lock();
        try {
            this.writeLock.lock();
            try {
                this.freeBuffers();
            } finally {
                this.writeLock.unlock();
            }
        } finally {
            this.readLock.unlock();
        }
    }

    private void tryShutdown() {
        block12: {
            if (!this.readLock.tryLock()) {
                return;
            }
            try {
                if (!this.writeLock.tryLock()) {
                    return;
                }
                try {
                    if (this.shutdownSent) break block12;
                    try {
                        boolean closed = this.shutdown();
                        if (!closed && this.waitForCloseConfirmation) {
                            this.shutdown();
                        }
                    } catch (Throwable e2) {
                        LOGGER.debug(String.format("error doing TLS shutdown on close(), continuing: %s", e2.getMessage()));
                    }
                } finally {
                    this.writeLock.unlock();
                }
            } finally {
                this.readLock.unlock();
            }
        }
    }

    public boolean shutdown() throws IOException {
        this.readLock.lock();
        try {
            block16: {
                this.writeLock.lock();
                try {
                    if (this.invalid) {
                        throw new ClosedChannelException();
                    }
                    if (this.shutdownSent) break block16;
                    this.shutdownSent = true;
                    this.outEncrypted.prepare();
                    try {
                        this.writeToChannel();
                        this.engine.closeOutbound();
                        this.wrapLoop(this.dummyOut);
                        this.writeToChannel();
                    } finally {
                        this.outEncrypted.release();
                    }
                    if (this.shutdownReceived) {
                        this.freeBuffers();
                    }
                    boolean bl2 = this.shutdownReceived;
                    this.writeLock.unlock();
                    return bl2;
                } catch (Throwable throwable) {
                    this.writeLock.unlock();
                    throw throwable;
                }
            }
            if (!this.shutdownReceived) {
                try {
                    this.readAndUnwrap(Optional.<ByteBufferSet>empty(), SSLEngineResult.HandshakeStatus.NEED_UNWRAP, true);
                    Util.assertTrue(this.shutdownReceived);
                } catch (EofException e2) {
                    throw new ClosedChannelException();
                }
            }
            this.freeBuffers();
            boolean bl3 = true;
            this.writeLock.unlock();
            return bl3;
        } finally {
            this.readLock.unlock();
        }
    }

    private void freeBuffers() {
        if (this.inEncrypted != null) {
            this.inEncrypted.dispose();
            this.inEncrypted = null;
        }
        if (this.inPlain != null) {
            this.inPlain.dispose();
            this.inPlain = null;
        }
        if (this.outEncrypted != null) {
            this.outEncrypted.dispose();
            this.outEncrypted = null;
        }
    }

    @Override
    public boolean isOpen() {
        return !this.invalid && this.writeChannel.isOpen() && this.readChannel.isOpen();
    }

    public static void checkReadBuffer(ByteBufferSet dest) {
        if (dest.isReadOnly()) {
            throw new IllegalArgumentException();
        }
    }

    public SSLEngine engine() {
        return this.engine;
    }

    public boolean getRunTasks() {
        return this.runTasks;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return (int)this.read(new ByteBufferSet(dst));
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        return (int)this.write(new ByteBufferSet(src));
    }

    public boolean shutdownReceived() {
        return this.shutdownReceived;
    }

    public boolean shutdownSent() {
        return this.shutdownSent;
    }

    public ReadableByteChannel plainReadableChannel() {
        return this.readChannel;
    }

    public WritableByteChannel plainWritableChannel() {
        return this.writeChannel;
    }

    public static class EofException
    extends Exception {
        private static final long serialVersionUID = -9215047770779892445L;

        @Override
        public Throwable fillInStackTrace() {
            return this;
        }
    }

    private static class WrapResult {
        final int bytesConsumed;
        final SSLEngineResult.HandshakeStatus lastHandshakeStatus;

        WrapResult(int bytesConsumed, SSLEngineResult.HandshakeStatus lastHandshakeStatus) {
            this.bytesConsumed = bytesConsumed;
            this.lastHandshakeStatus = lastHandshakeStatus;
        }
    }

    private static class UnwrapResult {
        final int bytesProduced;
        final SSLEngineResult.HandshakeStatus lastHandshakeStatus;
        final boolean wasClosed;

        UnwrapResult(int bytesProduced, SSLEngineResult.HandshakeStatus lastHandshakeStatus, boolean wasClosed) {
            this.bytesProduced = bytesProduced;
            this.lastHandshakeStatus = lastHandshakeStatus;
            this.wasClosed = wasClosed;
        }
    }
}

