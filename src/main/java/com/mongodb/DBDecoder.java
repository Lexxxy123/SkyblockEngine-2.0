/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBCallback;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.io.IOException;
import java.io.InputStream;
import org.bson.BSONDecoder;

public interface DBDecoder
extends BSONDecoder {
    public DBCallback getDBCallback(DBCollection var1);

    public DBObject decode(InputStream var1, DBCollection var2) throws IOException;

    public DBObject decode(byte[] var1, DBCollection var2);
}

