package org.e11eman.crackutilities.mixins;

@Mixin(net.minecraft.client.network.ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandler {
    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    public void onOnGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        CClient.events.invoke("serverGameMessage", packet, ci);
    }

    @Inject(method = "onPlaySound", at = @At("HEAD"))
    public void onPlaySound(PlaySoundS2CPacket packet, CallbackInfo ci) {
        CClient.events.invoke("playSound", packet);
    }
}
