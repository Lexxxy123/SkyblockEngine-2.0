/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb;

public enum CursorType {
    NonTailable{

        @Override
        public boolean isTailable() {
            return false;
        }
    }
    ,
    Tailable{

        @Override
        public boolean isTailable() {
            return true;
        }
    }
    ,
    TailableAwait{

        @Override
        public boolean isTailable() {
            return true;
        }
    };


    public abstract boolean isTailable();
}

