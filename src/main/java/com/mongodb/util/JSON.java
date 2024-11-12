/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.util;

import com.mongodb.util.JSONParser;
import com.mongodb.util.JSONSerializers;
import org.bson.BSONCallback;

@Deprecated
public class JSON {
    public static String serialize(Object object) {
        StringBuilder buf = new StringBuilder();
        JSON.serialize(object, buf);
        return buf.toString();
    }

    public static void serialize(Object object, StringBuilder buf) {
        JSONSerializers.getLegacy().serialize(object, buf);
    }

    public static Object parse(String jsonString) {
        return JSON.parse(jsonString, null);
    }

    public static Object parse(String s2, BSONCallback c2) {
        if (s2 == null || s2.trim().equals("")) {
            return null;
        }
        JSONParser p2 = new JSONParser(s2, c2);
        return p2.parse();
    }

    static void string(StringBuilder a2, String s2) {
        a2.append("\"");
        for (int i2 = 0; i2 < s2.length(); ++i2) {
            char c2 = s2.charAt(i2);
            if (c2 == '\\') {
                a2.append("\\\\");
                continue;
            }
            if (c2 == '\"') {
                a2.append("\\\"");
                continue;
            }
            if (c2 == '\n') {
                a2.append("\\n");
                continue;
            }
            if (c2 == '\r') {
                a2.append("\\r");
                continue;
            }
            if (c2 == '\t') {
                a2.append("\\t");
                continue;
            }
            if (c2 == '\b') {
                a2.append("\\b");
                continue;
            }
            if (c2 < ' ') continue;
            a2.append(c2);
        }
        a2.append("\"");
    }
}

