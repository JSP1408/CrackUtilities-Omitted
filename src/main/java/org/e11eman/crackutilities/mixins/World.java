package org.e11eman.crackutilities.mixins;

@Mixin(net.minecraft.world.World.class)
public class World {
    @Inject(method = "onBlockChanged", at = @At("HEAD"))
    public void onBlockChanged(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo ci) {
        CClient.events.invoke("vanillaBlockBreak", pos, oldBlock, newBlock);
    }
}
