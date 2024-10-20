/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

import com.mongodb.DBEncoder;
import com.mongodb.WriteConcern;
import com.mongodb.lang.Nullable;

public final class InsertOptions {
    private WriteConcern writeConcern;
    private boolean continueOnError;
    private DBEncoder dbEncoder;
    private Boolean bypassDocumentValidation;

    public InsertOptions writeConcern(@Nullable WriteConcern writeConcern) {
        this.writeConcern = writeConcern;
        return this;
    }

    public InsertOptions continueOnError(boolean continueOnError) {
        this.continueOnError = continueOnError;
        return this;
    }

    public InsertOptions dbEncoder(@Nullable DBEncoder dbEncoder) {
        this.dbEncoder = dbEncoder;
        return this;
    }

    @Nullable
    public WriteConcern getWriteConcern() {
        return this.writeConcern;
    }

    public boolean isContinueOnError() {
        return this.continueOnError;
    }

    @Nullable
    public DBEncoder getDbEncoder() {
        return this.dbEncoder;
    }

    @Nullable
    public Boolean getBypassDocumentValidation() {
        return this.bypassDocumentValidation;
    }

    public InsertOptions bypassDocumentValidation(@Nullable Boolean bypassDocumentValidation) {
        this.bypassDocumentValidation = bypassDocumentValidation;
        return this;
    }
}

