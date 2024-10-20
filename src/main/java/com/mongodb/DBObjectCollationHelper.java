/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBObject;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationAlternate;
import com.mongodb.client.model.CollationCaseFirst;
import com.mongodb.client.model.CollationMaxVariable;
import com.mongodb.client.model.CollationStrength;
import com.mongodb.lang.Nullable;

final class DBObjectCollationHelper {
    @Nullable
    static Collation createCollationFromOptions(DBObject options) {
        if (options.get("collation") == null) {
            return null;
        }
        if (!(options.get("collation") instanceof DBObject)) {
            throw new IllegalArgumentException("collation options should be a document");
        }
        Collation.Builder builder = Collation.builder();
        DBObject collation = (DBObject)options.get("collation");
        if (collation.get("locale") == null) {
            throw new IllegalArgumentException("'locale' is required when providing collation options");
        }
        Object locale = collation.get("locale");
        if (!(locale instanceof String)) {
            throw new IllegalArgumentException("collation 'locale' should be a String");
        }
        builder.locale((String)locale);
        if (collation.get("caseLevel") != null) {
            Object caseLevel = collation.get("caseLevel");
            if (!(caseLevel instanceof Boolean)) {
                throw new IllegalArgumentException("collation 'caseLevel' should be a Boolean");
            }
            builder.caseLevel((Boolean)caseLevel);
        }
        if (collation.get("caseFirst") != null) {
            Object caseFirst = collation.get("caseFirst");
            if (!(caseFirst instanceof String)) {
                throw new IllegalArgumentException("collation 'caseFirst' should be a String");
            }
            builder.collationCaseFirst(CollationCaseFirst.fromString((String)caseFirst));
        }
        if (collation.get("strength") != null) {
            Object strength = collation.get("strength");
            if (!(strength instanceof Integer)) {
                throw new IllegalArgumentException("collation 'strength' should be an Integer");
            }
            builder.collationStrength(CollationStrength.fromInt((Integer)strength));
        }
        if (collation.get("numericOrdering") != null) {
            Object numericOrdering = collation.get("numericOrdering");
            if (!(numericOrdering instanceof Boolean)) {
                throw new IllegalArgumentException("collation 'numericOrdering' should be a Boolean");
            }
            builder.numericOrdering((Boolean)numericOrdering);
        }
        if (collation.get("alternate") != null) {
            Object alternate = collation.get("alternate");
            if (!(alternate instanceof String)) {
                throw new IllegalArgumentException("collation 'alternate' should be a String");
            }
            builder.collationAlternate(CollationAlternate.fromString((String)alternate));
        }
        if (collation.get("maxVariable") != null) {
            Object maxVariable = collation.get("maxVariable");
            if (!(maxVariable instanceof String)) {
                throw new IllegalArgumentException("collation 'maxVariable' should be a String");
            }
            builder.collationMaxVariable(CollationMaxVariable.fromString((String)maxVariable));
        }
        if (collation.get("normalization") != null) {
            Object normalization = collation.get("normalization");
            if (!(normalization instanceof Boolean)) {
                throw new IllegalArgumentException("collation 'normalization' should be a Boolean");
            }
            builder.normalization((Boolean)normalization);
        }
        if (collation.get("backwards") != null) {
            Object backwards = collation.get("backwards");
            if (!(backwards instanceof Boolean)) {
                throw new IllegalArgumentException("collation 'backwards' should be a Boolean");
            }
            builder.backwards((Boolean)backwards);
        }
        return builder.build();
    }

    private DBObjectCollationHelper() {
    }
}

