package in.godspunky.skyblock.command;

public class CommandFailException extends RuntimeException {
    public CommandFailException(String message) {
        super(message);
    }
}
