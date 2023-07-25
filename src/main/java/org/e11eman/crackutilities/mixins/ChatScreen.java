package org.e11eman.crackutilities.mixins;

@Mixin(net.minecraft.client.gui.screen.ChatScreen.class)
public class ChatScreen {
    @Inject(at = @At("HEAD"), method = "sendMessage", cancellable = true)
    public void sendMessage(String chatText, boolean addToHistory, CallbackInfoReturnable<Boolean> cir) {
        CClient.events.invoke("clientChatSendEvent", chatText, cir);
    }
}
