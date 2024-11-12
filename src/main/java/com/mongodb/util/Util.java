/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.util;

import com.mongodb.internal.HexUtils;
import java.nio.ByteBuffer;

@Deprecated
public class Util {
    public static String toHex(byte[] bytes) {
        return HexUtils.toHex(bytes);
    }

    public static String hexMD5(byte[] data) {
        return HexUtils.hexMD5(data);
    }

    public static String hexMD5(ByteBuffer buf, int offset, int len) {
        return HexUtils.hexMD5(buf, offset, len);
    }
}

