package vn.giakhanhvn.skysim.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SLog {
    private static final Logger LOGGER;
    private static final String PREFIX = "[SkySim Engine]";

    private static void log(final Object o, final Level l) {
        SLog.LOGGER.log(l, "[SkySim Engine] " + o);
    }

    public static void info(final Object o) {
        log(o, Level.INFO);
    }

    public static void warn(final Object o) {
        log(o, Level.WARNING);
    }

    public static void severe(final Object o) {
        log(o, Level.SEVERE);
    }

    static {
        LOGGER = Logger.getLogger("Minecraft");
    }
}
