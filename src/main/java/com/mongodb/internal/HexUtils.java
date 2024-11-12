/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HexUtils {
    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b2 : bytes) {
            String s2 = Integer.toHexString(0xFF & b2);
            if (s2.length() < 2) {
                sb.append("0");
            }
            sb.append(s2);
        }
        return sb.toString();
    }

    public static String hexMD5(byte[] data) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(data);
            byte[] digest = md5.digest();
            return HexUtils.toHex(digest);
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException("Error - this implementation of Java doesn't support MD5.");
        }
    }

    public static String hexMD5(ByteBuffer buf, int offset, int len) {
        byte[] b2 = new byte[len];
        for (int i2 = 0; i2 < len; ++i2) {
            b2[i2] = buf.get(offset + i2);
        }
        return HexUtils.hexMD5(b2);
    }
}

