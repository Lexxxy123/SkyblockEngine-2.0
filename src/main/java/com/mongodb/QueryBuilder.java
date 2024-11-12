/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.assertions.Assertions;
import com.mongodb.lang.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class QueryBuilder {
    private final DBObject _query = new BasicDBObject();
    private String _currentKey;
    private boolean _hasNot;

    public static QueryBuilder start() {
        return new QueryBuilder();
    }

    public static QueryBuilder start(String key) {
        return new QueryBuilder().put(key);
    }

    public QueryBuilder put(String key) {
        this._currentKey = key;
        if (this._query.get(key) == null) {
            this._query.put(this._currentKey, new NullObject());
        }
        return this;
    }

    public QueryBuilder and(String key) {
        return this.put(key);
    }

    public QueryBuilder greaterThan(Object object) {
        this.addOperand("$gt", object);
        return this;
    }

    public QueryBuilder greaterThanEquals(Object object) {
        this.addOperand("$gte", object);
        return this;
    }

    public QueryBuilder lessThan(Object object) {
        this.addOperand("$lt", object);
        return this;
    }

    public QueryBuilder lessThanEquals(Object object) {
        this.addOperand("$lte", object);
        return this;
    }

    public QueryBuilder is(Object object) {
        this.addOperand(null, object);
        return this;
    }

    public QueryBuilder notEquals(Object object) {
        this.addOperand("$ne", object);
        return this;
    }

    public QueryBuilder in(Object object) {
        this.addOperand("$in", object);
        return this;
    }

    public QueryBuilder notIn(Object object) {
        this.addOperand("$nin", object);
        return this;
    }

    public QueryBuilder mod(Object object) {
        this.addOperand("$mod", object);
        return this;
    }

    public QueryBuilder all(Object object) {
        this.addOperand("$all", object);
        return this;
    }

    public QueryBuilder size(Object object) {
        this.addOperand("$size", object);
        return this;
    }

    public QueryBuilder exists(Object object) {
        this.addOperand("$exists", object);
        return this;
    }

    public QueryBuilder regex(Pattern regex) {
        this.addOperand(null, regex);
        return this;
    }

    public QueryBuilder elemMatch(DBObject match) {
        this.addOperand("$elemMatch", match);
        return this;
    }

    public QueryBuilder withinCenter(double x2, double y2, double radius) {
        this.addOperand("$within", new BasicDBObject("$center", Arrays.asList(Arrays.asList(x2, y2), radius)));
        return this;
    }

    public QueryBuilder near(double x2, double y2) {
        this.addOperand("$near", Arrays.asList(x2, y2));
        return this;
    }

    public QueryBuilder near(double x2, double y2, double maxDistance) {
        this.addOperand("$near", Arrays.asList(x2, y2));
        this.addOperand("$maxDistance", maxDistance);
        return this;
    }

    public QueryBuilder nearSphere(double longitude, double latitude) {
        this.addOperand("$nearSphere", Arrays.asList(longitude, latitude));
        return this;
    }

    public QueryBuilder nearSphere(double longitude, double latitude, double maxDistance) {
        this.addOperand("$nearSphere", Arrays.asList(longitude, latitude));
        this.addOperand("$maxDistance", maxDistance);
        return this;
    }

    public QueryBuilder withinCenterSphere(double longitude, double latitude, double maxDistance) {
        this.addOperand("$within", new BasicDBObject("$centerSphere", Arrays.asList(Arrays.asList(longitude, latitude), maxDistance)));
        return this;
    }

    public QueryBuilder withinBox(double x2, double y2, double x22, double y22) {
        this.addOperand("$within", new BasicDBObject("$box", new Object[]{new Double[]{x2, y2}, new Double[]{x22, y22}}));
        return this;
    }

    public QueryBuilder withinPolygon(List<Double[]> points) {
        Assertions.notNull("points", points);
        if (points.isEmpty() || points.size() < 3) {
            throw new IllegalArgumentException("Polygon insufficient number of vertices defined");
        }
        this.addOperand("$within", new BasicDBObject("$polygon", this.convertToListOfLists(points)));
        return this;
    }

    private List<List<Double>> convertToListOfLists(List<Double[]> points) {
        ArrayList<List<Double>> listOfLists = new ArrayList<List<Double>>(points.size());
        for (Double[] cur : points) {
            ArrayList list = new ArrayList(cur.length);
            Collections.addAll(list, cur);
            listOfLists.add(list);
        }
        return listOfLists;
    }

    public QueryBuilder text(String search) {
        return this.text(search, null);
    }

    public QueryBuilder text(String search, @Nullable String language) {
        if (this._currentKey != null) {
            throw new QueryBuilderException("The text operand may only occur at the top-level of a query. It does not apply to a specific element, but rather to a document as a whole.");
        }
        this.put("$text");
        this.addOperand("$search", search);
        if (language != null) {
            this.addOperand("$language", language);
        }
        return this;
    }

    public QueryBuilder not() {
        this._hasNot = true;
        return this;
    }

    public QueryBuilder or(DBObject ... ors) {
        ArrayList l2 = (ArrayList)this._query.get("$or");
        if (l2 == null) {
            l2 = new ArrayList();
            this._query.put("$or", l2);
        }
        Collections.addAll(l2, ors);
        return this;
    }

    public QueryBuilder and(DBObject ... ands) {
        ArrayList l2 = (ArrayList)this._query.get("$and");
        if (l2 == null) {
            l2 = new ArrayList();
            this._query.put("$and", l2);
        }
        Collections.addAll(l2, ands);
        return this;
    }

    public DBObject get() {
        for (String key : this._query.keySet()) {
            if (!(this._query.get(key) instanceof NullObject)) continue;
            throw new QueryBuilderException("No operand for key:" + key);
        }
        return this._query;
    }

    private void addOperand(@Nullable String op, Object value) {
        BasicDBObject operand;
        Object valueToPut = value;
        if (op == null) {
            if (this._hasNot) {
                valueToPut = new BasicDBObject("$not", valueToPut);
                this._hasNot = false;
            }
            this._query.put(this._currentKey, valueToPut);
            return;
        }
        Object storedValue = this._query.get(this._currentKey);
        if (!(storedValue instanceof DBObject)) {
            operand = new BasicDBObject();
            if (this._hasNot) {
                BasicDBObject notOperand = new BasicDBObject("$not", operand);
                this._query.put(this._currentKey, notOperand);
                this._hasNot = false;
            } else {
                this._query.put(this._currentKey, operand);
            }
        } else {
            operand = (BasicDBObject)this._query.get(this._currentKey);
            if (operand.get("$not") != null) {
                operand = (BasicDBObject)operand.get("$not");
            }
        }
        operand.put(op, valueToPut);
    }

    private static class NullObject {
        private NullObject() {
        }
    }

    static class QueryBuilderException
    extends RuntimeException {
        QueryBuilderException(String message) {
            super(message);
        }
    }
}

