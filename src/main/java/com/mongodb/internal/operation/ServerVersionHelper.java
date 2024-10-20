/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.operation;

import com.mongodb.connection.ConnectionDescription;

public final class ServerVersionHelper {
    public static final int THREE_DOT_ZERO_WIRE_VERSION = 3;
    public static final int THREE_DOT_TWO_WIRE_VERSION = 4;
    public static final int THREE_DOT_FOUR_WIRE_VERSION = 5;
    public static final int THREE_DOT_SIX_WIRE_VERSION = 6;
    public static final int FOUR_DOT_ZERO_WIRE_VERSION = 7;
    public static final int FOUR_DOT_TWO_WIRE_VERSION = 8;

    public static boolean serverIsAtLeastVersionThreeDotZero(ConnectionDescription description) {
        return description.getMaxWireVersion() >= 3;
    }

    public static boolean serverIsAtLeastVersionThreeDotTwo(ConnectionDescription description) {
        return description.getMaxWireVersion() >= 4;
    }

    public static boolean serverIsAtLeastVersionThreeDotFour(ConnectionDescription description) {
        return description.getMaxWireVersion() >= 5;
    }

    public static boolean serverIsAtLeastVersionThreeDotSix(ConnectionDescription description) {
        return description.getMaxWireVersion() >= 6;
    }

    public static boolean serverIsAtLeastVersionFourDotZero(ConnectionDescription description) {
        return description.getMaxWireVersion() >= 7;
    }

    public static boolean serverIsAtLeastVersionFourDotTwo(ConnectionDescription description) {
        return description.getMaxWireVersion() >= 8;
    }

    public static boolean serverIsLessThanVersionThreeDotZero(ConnectionDescription description) {
        return description.getMaxWireVersion() < 3;
    }

    public static boolean serverIsLessThanVersionThreeDotTwo(ConnectionDescription description) {
        return description.getMaxWireVersion() < 4;
    }

    public static boolean serverIsLessThanVersionThreeDotFour(ConnectionDescription description) {
        return description.getMaxWireVersion() < 5;
    }

    public static boolean serverIsLessThanVersionThreeDotSix(ConnectionDescription description) {
        return description.getMaxWireVersion() < 6;
    }

    public static boolean serverIsLessThanVersionFourDotZero(ConnectionDescription description) {
        return description.getMaxWireVersion() < 7;
    }

    public static boolean serverIsLessThanVersionFourDotTwo(ConnectionDescription description) {
        return description.getMaxWireVersion() < 8;
    }

    private ServerVersionHelper() {
    }
}

