/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.hypixel.skyblock.nms.nmsutil.apihelper.exception;

public class HostRegistrationException
extends RuntimeException {
    public HostRegistrationException() {
    }

    public HostRegistrationException(String message) {
        super(message);
    }

    public HostRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public HostRegistrationException(Throwable cause) {
        super(cause);
    }

    public HostRegistrationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

