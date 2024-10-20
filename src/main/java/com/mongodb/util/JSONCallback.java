/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.UUID;
import java.util.regex.Pattern;
import org.bson.BSON;
import org.bson.BSONObject;
import org.bson.BasicBSONCallback;
import org.bson.BsonUndefined;
import org.bson.internal.Base64;
import org.bson.types.BSONTimestamp;
import org.bson.types.Binary;
import org.bson.types.Code;
import org.bson.types.CodeWScope;
import org.bson.types.Decimal128;
import org.bson.types.MaxKey;
import org.bson.types.MinKey;
import org.bson.types.ObjectId;

@Deprecated
public class JSONCallback
extends BasicBSONCallback {
    private boolean _lastArray = false;
    public static final String _msDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String _secDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Override
    public BSONObject create() {
        return new BasicDBObject();
    }

    @Override
    protected BSONObject createList() {
        return new BasicDBList();
    }

    @Override
    public void arrayStart(String name) {
        this._lastArray = true;
        super.arrayStart(name);
    }

    @Override
    public void objectStart(String name) {
        this._lastArray = false;
        super.objectStart(name);
    }

    @Override
    public Object objectDone() {
        String name = this.curName();
        Object o2 = super.objectDone();
        if (this._lastArray) {
            return o2;
        }
        BSONObject b2 = (BSONObject)o2;
        if (b2.containsField("$oid")) {
            o2 = new ObjectId((String)b2.get("$oid"));
        } else if (b2.containsField("$date")) {
            if (b2.get("$date") instanceof Number) {
                o2 = new Date(((Number)b2.get("$date")).longValue());
            } else {
                SimpleDateFormat format = new SimpleDateFormat(_msDateFormat);
                format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
                o2 = format.parse(b2.get("$date").toString(), new ParsePosition(0));
                if (o2 == null) {
                    format = new SimpleDateFormat(_secDateFormat);
                    format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
                    o2 = format.parse(b2.get("$date").toString(), new ParsePosition(0));
                }
            }
        } else if (b2.containsField("$regex")) {
            o2 = Pattern.compile((String)b2.get("$regex"), BSON.regexFlags((String)b2.get("$options")));
        } else if (b2.containsField("$ts")) {
            Integer ts = ((Number)b2.get("$ts")).intValue();
            Integer inc = ((Number)b2.get("$inc")).intValue();
            o2 = new BSONTimestamp(ts, inc);
        } else if (b2.containsField("$timestamp")) {
            BSONObject tsObject = (BSONObject)b2.get("$timestamp");
            Integer ts = ((Number)tsObject.get("t")).intValue();
            Integer inc = ((Number)tsObject.get("i")).intValue();
            o2 = new BSONTimestamp(ts, inc);
        } else if (b2.containsField("$code")) {
            o2 = b2.containsField("$scope") ? new CodeWScope((String)b2.get("$code"), (DBObject)b2.get("$scope")) : new Code((String)b2.get("$code"));
        } else if (b2.containsField("$ref")) {
            o2 = new DBRef((String)b2.get("$ref"), b2.get("$id"));
        } else if (b2.containsField("$minKey")) {
            o2 = new MinKey();
        } else if (b2.containsField("$maxKey")) {
            o2 = new MaxKey();
        } else if (b2.containsField("$uuid")) {
            o2 = UUID.fromString((String)b2.get("$uuid"));
        } else if (b2.containsField("$binary")) {
            int type = b2.get("$type") instanceof String ? Integer.valueOf((String)b2.get("$type"), 16) : (Integer)b2.get("$type");
            byte[] bytes = Base64.decode((String)b2.get("$binary"));
            o2 = new Binary((byte)type, bytes);
        } else if (b2.containsField("$undefined") && b2.get("$undefined").equals(true)) {
            o2 = new BsonUndefined();
        } else if (b2.containsField("$numberLong")) {
            o2 = Long.valueOf((String)b2.get("$numberLong"));
        } else if (b2.containsField("$numberDecimal")) {
            o2 = Decimal128.parse((String)b2.get("$numberDecimal"));
        }
        if (!this.isStackEmpty()) {
            this._put(name, o2);
        } else {
            o2 = !BSON.hasDecodeHooks() ? o2 : BSON.applyDecodingHooks(o2);
            this.setRoot(o2);
        }
        return o2;
    }
}

