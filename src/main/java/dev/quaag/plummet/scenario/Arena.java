package dev.quaag.plummet.scenario;

import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public final class Arena {
    public static final int SIZE = 24;

    private Arena() {}

    public static int build(ServerWorld world, ServerPlayerEntity player) {
        BlockPos center = player.getBlockPos();
        int y = center.getY() - 1;
        int startX = center.getX() - SIZE / 2;
        int startZ = center.getZ() - SIZE / 2;

        int placed = 0;
        BlockPos.Mutable pos = new BlockPos.Mutable();
        for (int dx = 0; dx < SIZE; dx++) {
            for (int dz = 0; dz < SIZE; dz++) {
                pos.set(startX + dx, y, startZ + dz);
                if (world.getBlockState(pos).isAir()) {
                    world.setBlockState(pos, Blocks.SMOOTH_STONE.getDefaultState());
                    placed++;
                }
            }
        }
        return placed;
    }
}
