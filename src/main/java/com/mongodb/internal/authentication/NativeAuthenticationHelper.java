/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.authentication;

import com.mongodb.internal.HexUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

public final class NativeAuthenticationHelper {
    private static final Charset UTF_8_CHARSET = Charset.forName("UTF-8");

    public static String createAuthenticationHash(String userName, char[] password) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(userName.length() + 20 + password.length);
        try {
            bout.write(userName.getBytes(UTF_8_CHARSET));
            bout.write(":mongo:".getBytes(UTF_8_CHARSET));
            bout.write(new String(password).getBytes(UTF_8_CHARSET));
        } catch (IOException ioe) {
            throw new RuntimeException("impossible", ioe);
        }
        return HexUtils.hexMD5(bout.toByteArray());
    }

    public static BsonDocument getAuthCommand(String userName, char[] password, String nonce) {
        return NativeAuthenticationHelper.getAuthCommand(userName, NativeAuthenticationHelper.createAuthenticationHash(userName, password), nonce);
    }

    public static BsonDocument getAuthCommand(String userName, String authHash, String nonce) {
        String key = nonce + userName + authHash;
        BsonDocument cmd = new BsonDocument();
        cmd.put("authenticate", new BsonInt32(1));
        cmd.put("user", new BsonString(userName));
        cmd.put("nonce", new BsonString(nonce));
        cmd.put("key", new BsonString(HexUtils.hexMD5(key.getBytes(UTF_8_CHARSET))));
        return cmd;
    }

    public static BsonDocument getNonceCommand() {
        return new BsonDocument("getnonce", new BsonInt32(1));
    }

    private NativeAuthenticationHelper() {
    }
}

