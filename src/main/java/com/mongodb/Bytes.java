/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.lang.Nullable;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import org.bson.BSON;
import org.bson.types.BSONTimestamp;
import org.bson.types.Code;
import org.bson.types.CodeWScope;
import org.bson.types.ObjectId;

@Deprecated
public class Bytes
extends BSON {
    public static final ByteOrder ORDER = ByteOrder.LITTLE_ENDIAN;
    public static final int QUERYOPTION_TAILABLE = 2;
    public static final int QUERYOPTION_SLAVEOK = 4;
    public static final int QUERYOPTION_OPLOGREPLAY = 8;
    public static final int QUERYOPTION_NOTIMEOUT = 16;
    public static final int QUERYOPTION_AWAITDATA = 32;
    public static final int QUERYOPTION_EXHAUST = 64;
    public static final int QUERYOPTION_PARTIAL = 128;
    public static final int RESULTFLAG_CURSORNOTFOUND = 1;
    public static final int RESULTFLAG_ERRSET = 2;
    public static final int RESULTFLAG_SHARDCONFIGSTALE = 4;
    public static final int RESULTFLAG_AWAITCAPABLE = 8;

    public static byte getType(Object object) {
        if (object == null) {
            return 10;
        }
        if (object instanceof Integer || object instanceof Short || object instanceof Byte || object instanceof AtomicInteger) {
            return 16;
        }
        if (object instanceof Long || object instanceof AtomicLong) {
            return 18;
        }
        if (object instanceof Number) {
            return 1;
        }
        if (object instanceof String) {
            return 2;
        }
        if (object instanceof List) {
            return 4;
        }
        if (object instanceof byte[]) {
            return 5;
        }
        if (object instanceof ObjectId) {
            return 7;
        }
        if (object instanceof Boolean) {
            return 8;
        }
        if (object instanceof Date) {
            return 9;
        }
        if (object instanceof BSONTimestamp) {
            return 17;
        }
        if (object instanceof Pattern) {
            return 11;
        }
        if (object instanceof DBObject || object instanceof DBRef) {
            return 3;
        }
        if (object instanceof CodeWScope) {
            return 15;
        }
        if (object instanceof Code) {
            return 13;
        }
        return -1;
    }

    static class OptionHolder {
        final OptionHolder _parent;
        int _options = 0;
        boolean _hasOptions = false;

        OptionHolder(@Nullable OptionHolder parent) {
            this._parent = parent;
        }

        void set(int options) {
            this._options = options;
            this._hasOptions = true;
        }

        int get() {
            if (this._hasOptions) {
                return this._options;
            }
            if (this._parent == null) {
                return 0;
            }
            return this._parent.get();
        }

        void add(int option) {
            this.set(this.get() | option);
        }

        void reset() {
            this._hasOptions = false;
        }
    }
}

