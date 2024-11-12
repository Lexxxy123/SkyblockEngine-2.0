/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBObject;
import com.mongodb.lang.Nullable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.bson.BSONObject;

@Deprecated
public abstract class ReflectionDBObject
implements DBObject {
    JavaWrapper _wrapper;
    Object _id;
    private static final Map<Class, JavaWrapper> _wrappers = Collections.synchronizedMap(new HashMap());
    private static final Set<String> IGNORE_FIELDS = new HashSet<String>();

    @Override
    @Nullable
    public Object get(String key) {
        return this.getWrapper().get(this, key);
    }

    @Override
    public Set<String> keySet() {
        return this.getWrapper().keySet();
    }

    @Override
    public boolean containsKey(String key) {
        return this.containsField(key);
    }

    @Override
    public boolean containsField(String fieldName) {
        return this.getWrapper().containsKey(fieldName);
    }

    @Override
    public Object put(String key, Object v2) {
        return this.getWrapper().set(this, key, v2);
    }

    @Override
    public void putAll(Map m2) {
        for (Map.Entry entry : m2.entrySet()) {
            this.put(entry.getKey().toString(), entry.getValue());
        }
    }

    @Override
    public void putAll(BSONObject o2) {
        for (String k2 : o2.keySet()) {
            this.put(k2, o2.get(k2));
        }
    }

    public Object get_id() {
        return this._id;
    }

    public void set_id(Object id) {
        this._id = id;
    }

    @Override
    public boolean isPartialObject() {
        return false;
    }

    @Override
    public Map toMap() {
        HashMap<String, Object> m2 = new HashMap<String, Object>();
        for (String s2 : this.keySet()) {
            m2.put(s2, this.get(s2 + ""));
        }
        return m2;
    }

    @Override
    public void markAsPartialObject() {
        throw new RuntimeException("ReflectionDBObjects can't be partial");
    }

    @Override
    public Object removeField(String key) {
        throw new UnsupportedOperationException("can't remove from a ReflectionDBObject");
    }

    JavaWrapper getWrapper() {
        if (this._wrapper != null) {
            return this._wrapper;
        }
        this._wrapper = ReflectionDBObject.getWrapper(this.getClass());
        return this._wrapper;
    }

    @Nullable
    public static JavaWrapper getWrapperIfReflectionObject(Class c2) {
        if (ReflectionDBObject.class.isAssignableFrom(c2)) {
            return ReflectionDBObject.getWrapper(c2);
        }
        return null;
    }

    public static JavaWrapper getWrapper(Class c2) {
        JavaWrapper w2 = _wrappers.get(c2);
        if (w2 == null) {
            w2 = new JavaWrapper(c2);
            _wrappers.put(c2, w2);
        }
        return w2;
    }

    static {
        IGNORE_FIELDS.add("Int");
    }

    static class FieldInfo {
        final String name;
        final Class<? extends DBObject> clazz;
        Method getter;
        Method setter;

        FieldInfo(String name, Class<? extends DBObject> clazz) {
            this.name = name;
            this.clazz = clazz;
        }

        boolean ok() {
            return this.getter != null && this.setter != null;
        }
    }

    public static class JavaWrapper {
        final Class clazz;
        final String name;
        final Map<String, FieldInfo> fields;
        final Set<String> keys;

        JavaWrapper(Class c2) {
            this.clazz = c2;
            this.name = c2.getName();
            this.fields = new TreeMap<String, FieldInfo>();
            for (Method m2 : c2.getMethods()) {
                String name;
                if (!m2.getName().startsWith("get") && !m2.getName().startsWith("set") || (name = m2.getName().substring(3)).length() == 0 || IGNORE_FIELDS.contains(name)) continue;
                Class<?> type = m2.getName().startsWith("get") ? m2.getReturnType() : m2.getParameterTypes()[0];
                FieldInfo fi = this.fields.get(name);
                if (fi == null) {
                    fi = new FieldInfo(name, type);
                    this.fields.put(name, fi);
                }
                if (m2.getName().startsWith("get")) {
                    fi.getter = m2;
                    continue;
                }
                fi.setter = m2;
            }
            HashSet<String> names = new HashSet<String>(this.fields.keySet());
            for (String name : names) {
                if (this.fields.get(name).ok()) continue;
                this.fields.remove(name);
            }
            this.keys = Collections.unmodifiableSet(this.fields.keySet());
        }

        public Set<String> keySet() {
            return this.keys;
        }

        @Deprecated
        public boolean containsKey(String key) {
            return this.keys.contains(key);
        }

        @Nullable
        public Object get(ReflectionDBObject document, String fieldName) {
            FieldInfo i2 = this.fields.get(fieldName);
            if (i2 == null) {
                return null;
            }
            try {
                return i2.getter.invoke(document, new Object[0]);
            } catch (Exception e2) {
                throw new RuntimeException("could not invoke getter for [" + fieldName + "] on [" + this.name + "]", e2);
            }
        }

        public Object set(ReflectionDBObject document, String fieldName, Object value) {
            FieldInfo i2 = this.fields.get(fieldName);
            if (i2 == null) {
                throw new IllegalArgumentException("no field [" + fieldName + "] on [" + this.name + "]");
            }
            try {
                return i2.setter.invoke(document, value);
            } catch (Exception e2) {
                throw new RuntimeException("could not invoke setter for [" + fieldName + "] on [" + this.name + "]", e2);
            }
        }

        @Nullable
        Class<? extends DBObject> getInternalClass(List<String> path) {
            String cur = path.get(0);
            FieldInfo fi = this.fields.get(cur);
            if (fi == null) {
                return null;
            }
            if (path.size() == 1) {
                return fi.clazz;
            }
            JavaWrapper w2 = ReflectionDBObject.getWrapperIfReflectionObject(fi.clazz);
            if (w2 == null) {
                return null;
            }
            return w2.getInternalClass(path.subList(1, path.size()));
        }
    }
}

