/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public final class DecimalFormatHelper {
    private static final DecimalFormatSymbols DECIMAL_FORMAT_SYMBOLS = DecimalFormatHelper.initializeDecimalFormatSymbols();

    private DecimalFormatHelper() {
    }

    private static DecimalFormatSymbols initializeDecimalFormatSymbols() {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        return decimalFormatSymbols;
    }

    public static String format(String pattern, double number) {
        return new DecimalFormat(pattern, DECIMAL_FORMAT_SYMBOLS).format(number);
    }
}

