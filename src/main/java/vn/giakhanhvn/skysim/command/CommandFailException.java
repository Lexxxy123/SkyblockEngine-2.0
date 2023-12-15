package vn.giakhanhvn.skysim.command;

public class CommandFailException extends RuntimeException {
    public CommandFailException(final String message) {
        super(message);
    }
}
