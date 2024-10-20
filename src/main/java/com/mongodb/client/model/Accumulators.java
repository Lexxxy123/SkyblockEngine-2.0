/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.client.model;

import com.mongodb.client.model.BsonField;
import com.mongodb.client.model.SimpleExpression;

public final class Accumulators {
    public static <TExpression> BsonField sum(String fieldName, TExpression expression) {
        return Accumulators.accumulator("$sum", fieldName, expression);
    }

    public static <TExpression> BsonField avg(String fieldName, TExpression expression) {
        return Accumulators.accumulator("$avg", fieldName, expression);
    }

    public static <TExpression> BsonField first(String fieldName, TExpression expression) {
        return Accumulators.accumulator("$first", fieldName, expression);
    }

    public static <TExpression> BsonField last(String fieldName, TExpression expression) {
        return Accumulators.accumulator("$last", fieldName, expression);
    }

    public static <TExpression> BsonField max(String fieldName, TExpression expression) {
        return Accumulators.accumulator("$max", fieldName, expression);
    }

    public static <TExpression> BsonField min(String fieldName, TExpression expression) {
        return Accumulators.accumulator("$min", fieldName, expression);
    }

    public static <TExpression> BsonField push(String fieldName, TExpression expression) {
        return Accumulators.accumulator("$push", fieldName, expression);
    }

    public static <TExpression> BsonField addToSet(String fieldName, TExpression expression) {
        return Accumulators.accumulator("$addToSet", fieldName, expression);
    }

    public static <TExpression> BsonField stdDevPop(String fieldName, TExpression expression) {
        return Accumulators.accumulator("$stdDevPop", fieldName, expression);
    }

    public static <TExpression> BsonField stdDevSamp(String fieldName, TExpression expression) {
        return Accumulators.accumulator("$stdDevSamp", fieldName, expression);
    }

    private static <TExpression> BsonField accumulator(String name, String fieldName, TExpression expression) {
        return new BsonField(fieldName, new SimpleExpression<TExpression>(name, expression));
    }

    private Accumulators() {
    }
}

