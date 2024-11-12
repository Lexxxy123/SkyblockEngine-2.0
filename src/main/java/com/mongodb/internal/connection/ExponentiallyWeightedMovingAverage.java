/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.assertions.Assertions;

@NotThreadSafe
class ExponentiallyWeightedMovingAverage {
    private final double alpha;
    private long average = -1L;

    ExponentiallyWeightedMovingAverage(double alpha) {
        Assertions.isTrueArgument("alpha >= 0.0 and <= 1.0", alpha >= 0.0 && alpha <= 1.0);
        this.alpha = alpha;
    }

    void reset() {
        this.average = -1L;
    }

    long addSample(long sample) {
        this.average = this.average == -1L ? sample : (long)(this.alpha * (double)sample + (1.0 - this.alpha) * (double)this.average);
        return this.average;
    }

    long getAverage() {
        return this.average == -1L ? 0L : this.average;
    }
}

