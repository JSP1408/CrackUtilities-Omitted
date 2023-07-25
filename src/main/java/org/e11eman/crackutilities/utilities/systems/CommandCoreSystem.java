package org.e11eman.crackutilities.utilities.systems;
public class CommandCoreSystem {
    private final Vector3d core = new Vector3d(0, 0, 0);
    private final ArrayList<BlockPos> cantuseBlocks = new ArrayList<>();
    public Vector3d nextBlock = new Vector3d(0, 0, 0);
    private Vec3d playerPos;
    private Vec3d lastUpdate = new Vec3d(0, 0, 0);

    public CommandCoreSystem() {
        JsonObject options = CClient.configSystem.getCategory(CClient.configSystem.getConfig(), "commandCoreSystem");

        CClient.events.register("serverGameMessage", "commandCoreGameMessage", (Event) -> {
            GameMessageS2CPacket packet = (GameMessageS2CPacket) Event[0];
            CallbackInfo callbackInfo = (CallbackInfo) Event[1];

            if (packet.content().getString().startsWith("Command set:")) callbackInfo.cancel();
        });

        CClient.events.register("playerMovement", "commandCorePlayerMovement", (Event) -> {
            if (options.get("enabled").getAsBoolean()) {
                playerPos = (Vec3d) Event[0];

                double maxDistance = options.get("maxDistance").getAsDouble();
                double distanceBetweenLastUpdateAndPlayer = MathExtras.calculateDistanceBetweenPoints(playerPos.x, playerPos.z, lastUpdate.x, lastUpdate.z);

                if (distanceBetweenLastUpdateAndPlayer > maxDistance) {
                    CClient.scheduler.submit(() -> {
                        update();
                        fillCore();
                    });
                }
            }
        });

        ClientChunkEvents.CHUNK_LOAD.register((clientWorld, chunk) -> {
            if (
                    chunk.getPos().getStartX() == Math.round(core.x) && chunk.getPos().getStartZ() == Math.round(core.z) &&
                            chunk.getPos().getEndX() == (core.x + 15) && chunk.getPos().getEndZ() == (core.z + 15)
            ) {
                CClient.scheduler.submit(this::fillCore);
            }
        });

        CClient.events.register("vanillaBlockBreak", "commandCoreBlockUpdate", (Event) -> {
            BlockPos blockPos = (BlockPos) Event[0];
            BlockState newBlock = (BlockState) Event[2];

            WorldChunk chunk = Player.getPlayer().getWorld().getWorldChunk(blockPos);

            if (
                    chunk.getPos().getStartX() == Math.round(core.x) && chunk.getPos().getStartZ() == Math.round(core.z) &&
                            chunk.getPos().getEndX() == (core.x + 15) && chunk.getPos().getEndZ() == (core.z + 15) &&
                            blockPos.getY() < options.get("layers").getAsDouble() &&
                            blockPos.getY() > -1 &&
                            !(newBlock.getBlock().equals(Blocks.REPEATING_COMMAND_BLOCK) || newBlock.getBlock().equals(Blocks.COMMAND_BLOCK))
            ) {
                cantuseBlocks.add(blockPos);

                CClient.scheduler.submit(() -> run(String.format("setblock %s %s %s minecraft:command_block", blockPos.getX(), blockPos.getY(), blockPos.getZ())));
            }

            if (
                    chunk.getPos().getStartX() == Math.round(core.x) && chunk.getPos().getStartZ() == Math.round(core.z) &&
                            chunk.getPos().getEndX() == (core.x + 15) && chunk.getPos().getEndZ() == (core.z + 15) &&
                            blockPos.getY() < options.get("layers").getAsDouble() &&
                            blockPos.getY() > -1 &&
                            (newBlock.getBlock().equals(Blocks.REPEATING_COMMAND_BLOCK) || newBlock.getBlock().equals(Blocks.COMMAND_BLOCK))
            ) {
                cantuseBlocks.remove(blockPos);
            }
        });
    }

    public void update() {
        core.set(Math.floor(playerPos.x / 16) * 16, 0, Math.floor(playerPos.z / 16) * 16);
        lastUpdate = playerPos;
    }

    public void fillCore() {
        JsonObject options = CClient.configSystem.getCategory(CClient.configSystem.getConfig(), "commandCoreSystem");
        if (!options.get("enabled").getAsBoolean()) return;
        CClient.chatQueueSystem.addMessageToQueue("/fill " + Math.round(core.x) + " " + 0 + " " + Math.round(core.z) + " " + Math.round(core.x + 15) + " " + Math.round(options.get("layers").getAsDouble() - 1) + " " + Math.round(core.z + 15) + " command_block{CustomName:'[{\"text\":\"CrackUtilitiesCore\",\"bold\":true,\"color\":\"blue\"}]'} replace");
    }

    public void run(String command) {
        JsonObject options = CClient.configSystem.getCategory(CClient.configSystem.getConfig(), "commandCoreSystem");
        if (!options.get("enabled").getAsBoolean()) return;

        nextBlock.x++;

        if (nextBlock.x >= 16) {
            nextBlock.x = 0;
            nextBlock.y++;
        }

        if (nextBlock.y >= Math.round(options.get("layers").getAsDouble())) {
            nextBlock.y = 0;
            nextBlock.z++;
        }

        if (nextBlock.z >= 16) {
            nextBlock.z = 0;
        }

        if (cantuseBlocks.contains(new BlockPos((int) Math.floor(core.x + nextBlock.x), (int) nextBlock.y, (int) Math.floor(core.z + nextBlock.z)))) {
            fillCore();
        }


        UpdateCommandBlockC2SPacket packet = new UpdateCommandBlockC2SPacket(new BlockPos((int) Math.floor(core.x + nextBlock.x), (int) nextBlock.y, (int) Math.floor(core.z + nextBlock.z)), command, CommandBlockBlockEntity.Type.AUTO, false, false, true);
        Player.sendPacket(packet);
    }
}