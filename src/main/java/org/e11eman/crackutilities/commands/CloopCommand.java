package org.e11eman.crackutilities.commands;

public class CloopCommand extends Command {

    public CloopCommand() {
        super("cloop", "Set command loops with command core", "\n cloop <add> <delay> <command> \n cloop <remove> <number>");
    }

    @Override
    public void execute(ArrayList<String> arguments) {
        switch(arguments.get(0)) {
            case "add" -> {
                int num;

                try {
                    num = Integer.parseInt(arguments.get(1));
                } catch (ClassCastException e) {
                    Player.alertClient(MessagePresets.errorTextPreset("Not a integer!"));
                    return;
                }

                CClient.cloopSystem.addCloop(num, ArrayTools.join(ArrayTools.shift(arguments, 2), ' '));
                Player.alertClient(MessagePresets.normalTextPreset("Created new cloop with number: " + (CClient.cloopSystem.loops.size() - 1)));
            }

            case "remove" -> {
                int num;

                try {
                    num = Integer.parseInt(arguments.get(1));
                } catch (ClassCastException e) {
                    Player.alertClient(MessagePresets.errorTextPreset("Not a integer!"));
                    return;
                }

                if(CClient.cloopSystem.loops.size() - 1 >= num) {
                    CClient.cloopSystem.removeCloop(num);
                    Player.alertClient(MessagePresets.normalTextPreset("Removed cloop with number: " + num));
                } else {
                    Player.alertClient(MessagePresets.errorTextPreset("Cloop does not exist!"));
                }
            }
        }
    }
}
