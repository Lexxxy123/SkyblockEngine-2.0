/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBRef;
import org.bson.Document;
import org.bson.Transformer;

public final class DocumentToDBRefTransformer
implements Transformer {
    @Override
    public Object transform(Object value) {
        Document document;
        if (value instanceof Document && (document = (Document)value).containsKey("$id") && document.containsKey("$ref")) {
            return new DBRef((String)document.get("$db"), (String)document.get("$ref"), document.get("$id"));
        }
        return value;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        return o2 != null && this.getClass() == o2.getClass();
    }

    public int hashCode() {
        return 0;
    }
}

