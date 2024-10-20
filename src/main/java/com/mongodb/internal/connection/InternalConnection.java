/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.connection.BufferProvider;
import com.mongodb.connection.ConnectionDescription;
import com.mongodb.internal.connection.CommandMessage;
import com.mongodb.internal.connection.ResponseBuffers;
import com.mongodb.session.SessionContext;
import java.util.List;
import org.bson.ByteBuf;
import org.bson.codecs.Decoder;

public interface InternalConnection
extends BufferProvider {
    public ConnectionDescription getDescription();

    public void open();

    public void openAsync(SingleResultCallback<Void> var1);

    public void close();

    public boolean opened();

    public boolean isClosed();

    public <T> T sendAndReceive(CommandMessage var1, Decoder<T> var2, SessionContext var3);

    public <T> void sendAndReceiveAsync(CommandMessage var1, Decoder<T> var2, SessionContext var3, SingleResultCallback<T> var4);

    public void sendMessage(List<ByteBuf> var1, int var2);

    public ResponseBuffers receiveMessage(int var1);

    public void sendMessageAsync(List<ByteBuf> var1, int var2, SingleResultCallback<Void> var3);

    public void receiveMessageAsync(int var1, SingleResultCallback<ResponseBuffers> var2);
}

