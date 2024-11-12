/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.internal;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import java.util.function.Consumer;

final class Java8ForEachHelper {
    static <TResult> void forEach(MongoIterable<TResult> iterable, Consumer<? super TResult> block) {
        MongoCursor<TResult> cursor = iterable.iterator();
        try {
            while (cursor.hasNext()) {
                block.accept(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }

    private Java8ForEachHelper() {
    }
}

