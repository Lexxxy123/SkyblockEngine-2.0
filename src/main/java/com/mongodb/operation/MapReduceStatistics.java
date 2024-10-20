/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.operation;

@Deprecated
public class MapReduceStatistics {
    private final int inputCount;
    private final int outputCount;
    private final int emitCount;
    private final int duration;

    public MapReduceStatistics(int inputCount, int outputCount, int emitCount, int duration) {
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.emitCount = emitCount;
        this.duration = duration;
    }

    public int getInputCount() {
        return this.inputCount;
    }

    public int getOutputCount() {
        return this.outputCount;
    }

    public int getEmitCount() {
        return this.emitCount;
    }

    public int getDuration() {
        return this.duration;
    }
}

