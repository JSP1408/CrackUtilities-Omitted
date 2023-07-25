package org.e11eman.crackutilities.mixins;

@Mixin(net.minecraft.client.network.ClientPlayerEntity.class)
public class ClientPlayerEntity {
    @Inject(method = "move", at = @At("HEAD"))
    public void move(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        CClient.events.invoke("playerMovement", Player.getPlayer().getPos());
    }
}
