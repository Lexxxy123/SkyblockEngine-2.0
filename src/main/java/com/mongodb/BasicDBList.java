/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.BasicBSONList;

public class BasicDBList
extends BasicBSONList
implements DBObject {
    private static final long serialVersionUID = -4415279469780082174L;
    private boolean _isPartialObject;

    @Override
    public boolean isPartialObject() {
        return this._isPartialObject;
    }

    @Override
    public void markAsPartialObject() {
        this._isPartialObject = true;
    }

    public Object copy() {
        BasicDBList newobj = new BasicDBList();
        for (int i2 = 0; i2 < this.size(); ++i2) {
            Object val = this.get(i2);
            if (val instanceof BasicDBObject) {
                val = ((BasicDBObject)val).copy();
            } else if (val instanceof BasicDBList) {
                val = ((BasicDBList)val).copy();
            }
            newobj.add(val);
        }
        return newobj;
    }
}

