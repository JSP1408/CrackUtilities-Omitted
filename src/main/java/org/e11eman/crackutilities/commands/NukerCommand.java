package org.e11eman.crackutilities.commands;

public class NukerCommand extends Command {
    public NukerCommand() {
        super("nuker", "Destroys blocks in funny way", "nuker <radius>");
    }

    @Override
    public void execute(ArrayList<String> arguments) {
        int radius = Integer.parseInt(arguments.get(0));
        Vec3d position = Player.getPosition();

        for(int xoffset = -radius; xoffset < radius; xoffset++) {
            for(int zoffset = -radius; zoffset < radius; zoffset++) {
                BlockPos block = tryGetNextBlockPos(
                        position.getX() + xoffset,
                        position.getY() + position.getY(),
                        position.getZ() + zoffset
                );


                Player.getInteractionManager().attackBlock(block, Direction.DOWN);
            }
        }

    }

    public static BlockPos tryGetNextBlockPos(double x, double y, double z) {
        BlockPos blockPos = new BlockPos(
                (int) x,
                (int) y,
                (int) z
        );

        Block block = Player.getBlock(blockPos);

        if (block == Blocks.AIR || block == Blocks.CAVE_AIR) {
            return tryGetNextBlockPos(x, y - 1, z);
        }

        return blockPos;
    }
}
