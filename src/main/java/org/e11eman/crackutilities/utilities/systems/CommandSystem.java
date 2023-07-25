package org.e11eman.crackutilities.utilities.systems;
public class CommandSystem {
    public final Command[] commands = {
            new ConfigCommand(),
            new HelpCommand(),
            new RunCommand(),
            new CloopCommand(),
            new ConnectionCommand(),
            new UsernameCommand(),
            new RefillCommand(),
            new IrcCommand(),
            new SudoAllCommand(),
            new ScreenShareCommand(),
            new ShuffleCommand(),
            new ItemImageRendererCommand(),
            new NukerCommand()
    };

    public boolean executeCommandIfFound(String text) {
        JsonObject config = CClient.configSystem.getConfig();
        JsonObject category = CClient.configSystem.getCategory(config, "commandSystem");

        Pattern commandPattern = Pattern.compile("[" + category.get("prefix").getAsString() + "](.*)");
        Matcher matchCheck = commandPattern.matcher(text);

        if (matchCheck.matches()) {
            String[] parsedText = matchCheck.group(1).split(" ");

            for (Command command : commands) {
                if (command.name.matches(parsedText[0])) {
                    try {
                        command.execute(ArrayTools.shift(new ArrayList<>(Arrays.asList(parsedText)), 1));
                    } catch (Exception e) {
                        Player.alertClient(MessagePresets.errorTextPreset("Something has gone wrong with that command!"));
                        e.printStackTrace();
                    }
                    return true;
                }
            }

            return false;
        }

        return false;
    }
}
