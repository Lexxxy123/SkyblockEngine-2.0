package vn.giakhanhvn.skysim.command;

import java.util.ArrayList;
import java.util.List;

public class CommandLoader
{
    private final List<SCommand> commands;
    
    public CommandLoader() {
        this.commands = new ArrayList<SCommand>();
    }
    
    public void register(final SCommand command) {
        this.commands.add(command);
        command.register();
    }
    
    public int getCommandAmount() {
        return this.commands.size();
    }
}
