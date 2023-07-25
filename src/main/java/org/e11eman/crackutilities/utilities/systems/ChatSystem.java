package org.e11eman.crackutilities.utilities.systems;
@SuppressWarnings({"unchecked", "deprecation"})
public class ChatSystem {
    public ChatSystem() {
        CClient.events.register("clientChatSendEvent", "chatSystem", (Data) -> {
            String msg = Data[0].toString();

            if (msg.startsWith("/") || msg.isEmpty()) return;

            CallbackInfoReturnable<Boolean> cancelable = (CallbackInfoReturnable<Boolean>) Data[1];

            JsonObject config = CClient.configSystem.getConfig();
            JsonObject commandSystem = CClient.configSystem.getCategory(config, "commandSystem");
            JsonObject chatFormatting = CClient.configSystem.getCategory(config, "chatFormatting");

            String prefix = commandSystem.get("prefix").getAsString();

            if (CClient.commandSystem.executeCommandIfFound(msg)) {
                MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(msg);
                cancelable.setReturnValue(true);
                return;
            } else {
                if (msg.startsWith(prefix)) {
                    Player.alertClient(MessagePresets.errorTextPreset("Command not found!"));
                    MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(msg);
                    cancelable.setReturnValue(true);
                    return;
                }
            }

            if(chatFormatting.get("enabled").getAsBoolean()) {
                JsonArray format = chatFormatting.getAsJsonArray("formatting");
                String username = Player.getPlayer().getGameProfile().getName();

                CClient.commandCoreSystem.run(String.format("minecraft:tellraw @a %s", format.toString()
                        .replace("%username%", username)
                        .replace("%message%", StringEscapeUtils.escapeJava(msg))));

                MinecraftClient.getInstance().inGameHud.getChatHud().addToMessageHistory(msg);
                cancelable.setReturnValue(true);
            }
        });
    }
}
