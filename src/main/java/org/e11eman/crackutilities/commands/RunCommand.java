package org.e11eman.crackutilities.commands;

public class RunCommand extends Command {
    public RunCommand() {
        super("run", "Use command core to run command", "run <command>");
    }

    @Override
    public void execute(ArrayList<String> arguments) {
        CClient.commandCoreSystem.run(ArrayTools.join(arguments, ' '));
    }
}
