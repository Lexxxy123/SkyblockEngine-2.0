/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.util;

import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.util.AbstractObjectSerializer;
import com.mongodb.util.ClassMapBasedObjectSerializer;
import com.mongodb.util.JSON;
import com.mongodb.util.ObjectSerializer;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.UUID;
import java.util.regex.Pattern;
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
import org.bson.types.Symbol;

@Deprecated
public class JSONSerializers {
    private JSONSerializers() {
    }

    public static ObjectSerializer getLegacy() {
        ClassMapBasedObjectSerializer serializer = JSONSerializers.addCommonSerializers();
        serializer.addObjectSerializer(Date.class, new LegacyDateSerializer(serializer));
        serializer.addObjectSerializer(BSONTimestamp.class, new LegacyBSONTimestampSerializer(serializer));
        serializer.addObjectSerializer(Binary.class, new LegacyBinarySerializer());
        serializer.addObjectSerializer(byte[].class, new LegacyBinarySerializer());
        return serializer;
    }

    public static ObjectSerializer getStrict() {
        ClassMapBasedObjectSerializer serializer = JSONSerializers.addCommonSerializers();
        serializer.addObjectSerializer(Date.class, new DateSerializer(serializer));
        serializer.addObjectSerializer(BSONTimestamp.class, new BSONTimestampSerializer(serializer));
        serializer.addObjectSerializer(Binary.class, new BinarySerializer(serializer));
        serializer.addObjectSerializer(byte[].class, new ByteArraySerializer(serializer));
        return serializer;
    }

    static ClassMapBasedObjectSerializer addCommonSerializers() {
        ClassMapBasedObjectSerializer serializer = new ClassMapBasedObjectSerializer();
        serializer.addObjectSerializer(Object[].class, new ObjectArraySerializer(serializer));
        serializer.addObjectSerializer(Boolean.class, new ToStringSerializer());
        serializer.addObjectSerializer(Code.class, new CodeSerializer(serializer));
        serializer.addObjectSerializer(CodeWScope.class, new CodeWScopeSerializer(serializer));
        serializer.addObjectSerializer(DBObject.class, new DBObjectSerializer(serializer));
        serializer.addObjectSerializer(DBRef.class, new DBRefBaseSerializer(serializer));
        serializer.addObjectSerializer(Iterable.class, new IterableSerializer(serializer));
        serializer.addObjectSerializer(Map.class, new MapSerializer(serializer));
        serializer.addObjectSerializer(MaxKey.class, new MaxKeySerializer(serializer));
        serializer.addObjectSerializer(MinKey.class, new MinKeySerializer(serializer));
        serializer.addObjectSerializer(Number.class, new ToStringSerializer());
        serializer.addObjectSerializer(ObjectId.class, new ObjectIdSerializer(serializer));
        serializer.addObjectSerializer(Pattern.class, new PatternSerializer(serializer));
        serializer.addObjectSerializer(String.class, new StringSerializer());
        serializer.addObjectSerializer(Symbol.class, new SymbolSerializer(serializer));
        serializer.addObjectSerializer(UUID.class, new UuidSerializer(serializer));
        serializer.addObjectSerializer(BsonUndefined.class, new UndefinedSerializer(serializer));
        serializer.addObjectSerializer(Decimal128.class, new Decimal128Serializer(serializer));
        return serializer;
    }

    private static class Decimal128Serializer
    extends CompoundObjectSerializer {
        Decimal128Serializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            this.serializer.serialize(new BasicDBObject("$numberDecimal", obj.toString()), buf);
        }
    }

    private static class UndefinedSerializer
    extends CompoundObjectSerializer {
        UndefinedSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            BasicDBObject temp = new BasicDBObject();
            temp.put("$undefined", true);
            this.serializer.serialize(temp, buf);
        }
    }

    private static class ByteArraySerializer
    extends BinarySerializerBase {
        ByteArraySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            this.serialize((byte[])obj, (byte)0, buf);
        }
    }

    private static class BinarySerializer
    extends BinarySerializerBase {
        BinarySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            Binary bin = (Binary)obj;
            this.serialize(bin.getData(), bin.getType(), buf);
        }
    }

    private static abstract class BinarySerializerBase
    extends CompoundObjectSerializer {
        BinarySerializerBase(ObjectSerializer serializer) {
            super(serializer);
        }

        protected void serialize(byte[] bytes, byte type, StringBuilder buf) {
            BasicDBObject temp = new BasicDBObject();
            temp.put("$binary", Base64.encode(bytes));
            temp.put("$type", (Object)type);
            this.serializer.serialize(temp, buf);
        }
    }

    private static class DateSerializer
    extends CompoundObjectSerializer {
        DateSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            Date d2 = (Date)obj;
            this.serializer.serialize(new BasicDBObject("$date", (Object)d2.getTime()), buf);
        }
    }

    private static class BSONTimestampSerializer
    extends CompoundObjectSerializer {
        BSONTimestampSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            BSONTimestamp t2 = (BSONTimestamp)obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("t", t2.getTime());
            temp.put("i", t2.getInc());
            BasicDBObject timestampObj = new BasicDBObject();
            timestampObj.put("$timestamp", temp);
            this.serializer.serialize(timestampObj, buf);
        }
    }

    private static class UuidSerializer
    extends CompoundObjectSerializer {
        UuidSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            UUID uuid = (UUID)obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$uuid", uuid.toString());
            this.serializer.serialize(temp, buf);
        }
    }

    private static class SymbolSerializer
    extends CompoundObjectSerializer {
        SymbolSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            Symbol symbol = (Symbol)obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$symbol", symbol.toString());
            this.serializer.serialize(temp, buf);
        }
    }

    private static class StringSerializer
    extends AbstractObjectSerializer {
        private StringSerializer() {
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            JSON.string(buf, (String)obj);
        }
    }

    private static class PatternSerializer
    extends CompoundObjectSerializer {
        PatternSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            BasicDBObject externalForm = new BasicDBObject();
            externalForm.put("$regex", obj.toString());
            if (((Pattern)obj).flags() != 0) {
                externalForm.put("$options", Bytes.regexFlags(((Pattern)obj).flags()));
            }
            this.serializer.serialize(externalForm, buf);
        }
    }

    private static class ObjectIdSerializer
    extends CompoundObjectSerializer {
        ObjectIdSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            this.serializer.serialize(new BasicDBObject("$oid", obj.toString()), buf);
        }
    }

    private static class MinKeySerializer
    extends CompoundObjectSerializer {
        MinKeySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            this.serializer.serialize(new BasicDBObject("$minKey", (Object)1), buf);
        }
    }

    private static class MaxKeySerializer
    extends CompoundObjectSerializer {
        MaxKeySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            this.serializer.serialize(new BasicDBObject("$maxKey", (Object)1), buf);
        }
    }

    private static class MapSerializer
    extends CompoundObjectSerializer {
        MapSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            boolean first = true;
            buf.append("{ ");
            Map m2 = (Map)obj;
            Iterator iterator = m2.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry o2;
                Map.Entry entry = o2 = iterator.next();
                if (first) {
                    first = false;
                } else {
                    buf.append(" , ");
                }
                JSON.string(buf, entry.getKey().toString());
                buf.append(" : ");
                this.serializer.serialize(entry.getValue(), buf);
            }
            buf.append("}");
        }
    }

    private static class IterableSerializer
    extends CompoundObjectSerializer {
        IterableSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            boolean first = true;
            buf.append("[ ");
            for (Object o2 : (Iterable)obj) {
                if (first) {
                    first = false;
                } else {
                    buf.append(" , ");
                }
                this.serializer.serialize(o2, buf);
            }
            buf.append("]");
        }
    }

    private static class DBRefBaseSerializer
    extends CompoundObjectSerializer {
        DBRefBaseSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            DBRef ref = (DBRef)obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$ref", ref.getCollectionName());
            temp.put("$id", ref.getId());
            if (ref.getDatabaseName() != null) {
                temp.put("$db", ref.getDatabaseName());
            }
            this.serializer.serialize(temp, buf);
        }
    }

    private static class DBObjectSerializer
    extends CompoundObjectSerializer {
        DBObjectSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            boolean first = true;
            buf.append("{ ");
            DBObject dbo = (DBObject)obj;
            Iterator<String> iterator = dbo.keySet().iterator();
            while (iterator.hasNext()) {
                String s2;
                String name = s2 = iterator.next();
                if (first) {
                    first = false;
                } else {
                    buf.append(" , ");
                }
                JSON.string(buf, name);
                buf.append(" : ");
                this.serializer.serialize(dbo.get(name), buf);
            }
            buf.append("}");
        }
    }

    private static class LegacyDateSerializer
    extends CompoundObjectSerializer {
        LegacyDateSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            Date d2 = (Date)obj;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
            this.serializer.serialize(new BasicDBObject("$date", format.format(d2)), buf);
        }
    }

    private static class CodeWScopeSerializer
    extends CompoundObjectSerializer {
        CodeWScopeSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            CodeWScope c2 = (CodeWScope)obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$code", c2.getCode());
            temp.put("$scope", c2.getScope());
            this.serializer.serialize(temp, buf);
        }
    }

    private static class CodeSerializer
    extends CompoundObjectSerializer {
        CodeSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            Code c2 = (Code)obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$code", c2.getCode());
            this.serializer.serialize(temp, buf);
        }
    }

    private static class LegacyBSONTimestampSerializer
    extends CompoundObjectSerializer {
        LegacyBSONTimestampSerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            BSONTimestamp t2 = (BSONTimestamp)obj;
            BasicDBObject temp = new BasicDBObject();
            temp.put("$ts", t2.getTime());
            temp.put("$inc", t2.getInc());
            this.serializer.serialize(temp, buf);
        }
    }

    private static class ToStringSerializer
    extends AbstractObjectSerializer {
        private ToStringSerializer() {
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            buf.append(obj.toString());
        }
    }

    private static class ObjectArraySerializer
    extends CompoundObjectSerializer {
        ObjectArraySerializer(ObjectSerializer serializer) {
            super(serializer);
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            buf.append("[ ");
            for (int i2 = 0; i2 < Array.getLength(obj); ++i2) {
                if (i2 > 0) {
                    buf.append(" , ");
                }
                this.serializer.serialize(Array.get(obj, i2), buf);
            }
            buf.append("]");
        }
    }

    private static class LegacyBinarySerializer
    extends AbstractObjectSerializer {
        private LegacyBinarySerializer() {
        }

        @Override
        public void serialize(Object obj, StringBuilder buf) {
            buf.append("<Binary Data>");
        }
    }

    private static abstract class CompoundObjectSerializer
    extends AbstractObjectSerializer {
        protected final ObjectSerializer serializer;

        CompoundObjectSerializer(ObjectSerializer serializer) {
            this.serializer = serializer;
        }
    }
}

