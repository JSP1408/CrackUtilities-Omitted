package org.e11eman.crackutilities.commands;

public class ConfigCommand extends Command {
    public ConfigCommand() {
        super("config", "Manage clients config", "\n config <list> <path/to/category> \n config <reload> \n config <setPath> <path/to/value> <type> <value> \n config <save>");
    }

    @Override
    public void execute(ArrayList<String> arguments) throws IOException {
        switch (arguments.get(0)) {
            case "reload" -> {
                CClient.configSystem.updateConfig();

                Player.alertClient(MessagePresets.normalTextPreset("Successfully reloaded config!"));
            }

            case "list" -> {
                MutableText message = Text.literal("--------------------\n").formatted(Formatting.DARK_GRAY);

                if (arguments.size() == 1) {
                    getMessage(message, CClient.configSystem.getConfig());
                } else {
                    JsonObject lastCategory = CClient.configSystem.getConfig();

                    String[] path = arguments.get(1).split("/");
                    for (String s : path) {
                        if (lastCategory.keySet().contains(lowercase(s))) {
                            if (lastCategory.get(lowercase(s)).isJsonObject()) {
                                lastCategory = lastCategory.get(lowercase(s)).getAsJsonObject();
                            } else if(lastCategory.get(lowercase(s)).isJsonArray()) {
                                Player.alertClient(MessagePresets.errorTextPreset("Not a category type!"));
                                return;
                            }
                        } else {
                            Player.alertClient(MessagePresets.errorTextPreset("Unknown category!"));
                            return;
                        }
                    }

                    getMessage(message, lastCategory);
                }

                message.append(Text.literal("\n--------------------").formatted(Formatting.DARK_GRAY));

                Player.alertClient(message);
            }

            case "setPath" -> {
                JsonObject lastCategory = CClient.configSystem.getConfig();
                StringBuilder le = new StringBuilder();

                String[] path = arguments.get(1).split("/");
                for (String s : path) {
                    if (lastCategory.keySet().contains(lowercase(s))) {
                        if (lastCategory.get(lowercase(s)).isJsonObject()) {
                            lastCategory = lastCategory.get(lowercase(s)).getAsJsonObject();
                        } else {
                            le.delete(0, le.length());
                            le.append(s);
                        }
                    } else {
                        Player.alertClient(MessagePresets.errorTextPreset("Unknown category!"));
                        return;
                    }
                }
                System.out.println(le + " " + lowercase(arguments.get(arguments.size() - 1)));

                if (!lastCategory.has(lowercase(le.toString()))) {
                    Player.alertClient(MessagePresets.errorTextPreset("Unknown category!"));
                    return;
                }

                switch (arguments.get(2)) {
                    case "number" ->
                            lastCategory.add(lowercase(le.toString()), new JsonPrimitive(Float.parseFloat(arguments.get(arguments.size() - 1))));

                    case "string" -> {
                        StringBuilder stringBuilder = new StringBuilder();

                        for (int i = 3; i < arguments.size(); i++) {
                            stringBuilder.append(arguments.get(i)).append(" ");
                        }

                        lastCategory.add(lowercase(le.toString().trim()), new JsonPrimitive(stringBuilder.toString().trim()));
                    }

                    case "boolean" ->
                            lastCategory.add(lowercase(le.toString()), new JsonPrimitive(Boolean.parseBoolean(arguments.get(arguments.size() - 1))));

                    default -> {
                        Player.alertClient(MessagePresets.errorTextPreset("Unknown type! Options are \"number, string and boolean!\" "));
                        return;
                    }
                }

                Player.alertClient(MessagePresets.normalTextPreset("Successfully set value!"));
            }

            case "save" -> {

                try {
                    FileWriter writer = new FileWriter(CClient.configSystem.configPath);
                    writer.write(CClient.configSystem.getConfig().toString());
                    writer.close();

                    Player.alertClient(MessagePresets.normalTextPreset("Successfully saved config to file!"));
                } catch (IOException e) {
                    CClient.configSystem.fixConfig();
                }
            }
        }
    }


    public String capitalize(String str) {
        if (str == null || str.length() == 0) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public String lowercase(String str) {
        if (str == null || str.length() == 0) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public void getMessage(MutableText start, JsonObject object) {
        int i = 0;
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            i++;

            String newLine = "\n";

            if (i == object.entrySet().size()) newLine = "";

            if (entry.getValue().isJsonObject()) {
                start.append(Text.literal(capitalize(entry.getKey())).formatted(Formatting.BLUE)
                        .append(Text.literal(": ").formatted(Formatting.GRAY))
                        .append(Text.literal("(category)" + newLine)));

            } else if (entry.getValue().isJsonArray()) {
                start.append(Text.literal(capitalize(entry.getKey())).formatted(Formatting.BLUE)
                        .append(Text.literal(": " + entry.getValue().toString() + " ").formatted(Formatting.GRAY))
                        .append(Text.literal("(array (has to be edited via config file))" + newLine)));

            } else if (entry.getValue().isJsonPrimitive()) {

                if (entry.getValue().getAsJsonPrimitive().isBoolean()) {
                    start.append(Text.literal(capitalize(entry.getKey())).formatted(Formatting.BLUE)
                            .append(Text.literal(": " + entry.getValue().toString() + " ").formatted(Formatting.GRAY))
                            .append(Text.literal("(bool)" + newLine)));

                } else if (entry.getValue().getAsJsonPrimitive().isNumber()) {
                    start.append(Text.literal(capitalize(entry.getKey())).formatted(Formatting.BLUE)
                            .append(Text.literal(": " + entry.getValue().toString() + " ").formatted(Formatting.GRAY))
                            .append(Text.literal("(num)" + newLine)));

                } else if (entry.getValue().getAsJsonPrimitive().isString()) {
                    start.append(Text.literal(capitalize(entry.getKey())).formatted(Formatting.BLUE)
                            .append(Text.literal(": " + entry.getValue().toString() + " ").formatted(Formatting.GRAY))
                            .append(Text.literal("(str)" + newLine)));

                }

            } else if (entry.getValue().isJsonNull()) {
                start.append(Text.literal(capitalize(entry.getKey())).formatted(Formatting.BLUE)
                        .append(Text.literal(": " + entry.getValue().toString() + " ").formatted(Formatting.GRAY))
                        .append(Text.literal("(null)" + newLine)));
            }
        }
    }
}