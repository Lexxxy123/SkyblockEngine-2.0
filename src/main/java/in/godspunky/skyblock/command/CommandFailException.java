package in.godspunky.skyblock.command;

public class CommandFailException extends RuntimeException {
    public CommandFailException(final String message) {
        super(message);
    }
}
