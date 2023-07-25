package org.e11eman.crackutilities.commands;

public class RefillCommand extends Command {
    public RefillCommand() {
        super("refill", "Refill command core", "refill");
    }

    @Override
    public void execute(ArrayList<String> arguments) {
        CClient.commandCoreSystem.update();
        CClient.commandCoreSystem.fillCore();

        Player.alertClient(MessagePresets.normalTextPreset("Successfully refilled core"));
    }
}
