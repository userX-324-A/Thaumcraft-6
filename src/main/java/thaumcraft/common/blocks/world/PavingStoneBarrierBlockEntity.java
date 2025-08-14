package thaumcraft.common.blocks.world;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import thaumcraft.common.registers.ModBlockEntities;
import thaumcraft.common.registers.ModBlocks;

import java.util.List;

/**
 * Port of 1.12 TileBarrierStone logic: every few ticks, apply a gentle push to airborne non-players.
 * Every ~5 ticks, when not powered, ensure up to 2 blocks of barrier above are present.
 */
public class PavingStoneBarrierBlockEntity extends TileEntity implements ITickableTileEntity {
    private int tickCounter = 0;

    public PavingStoneBarrierBlockEntity() {
        super(ModBlockEntities.PAVING_STONE_BARRIER.get());
    }

    private boolean isPowered() {
        return level != null && level.hasNeighborSignal(worldPosition);
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) return;
        tickCounter++;
        // Stagger start similar to 1.12 randomization
        if (tickCounter == 1) tickCounter = level.random.nextInt(100);

        if (tickCounter % 5 == 0 && !isPowered()) {
            AxisAlignedBB box = new AxisAlignedBB(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), worldPosition.getX() + 1, worldPosition.getY() + 3, worldPosition.getZ() + 1).inflate(0.1, 0.1, 0.1);
            List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, box);
            for (LivingEntity e : targets) {
                if (!e.isOnGround() && !(e instanceof PlayerEntity)) {
                    float yaw = e.yRot + 180.0f;
                    double pushX = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * 0.2;
                    double pushZ = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * 0.2;
                    e.push(pushX, -0.08, pushZ);
                }
            }
        }

        if (tickCounter % 10 == 0 && !isPowered()) {
            // Ensure up to 2 barrier blocks above
            for (int dy = 1; dy <= 2; dy++) {
                if (level.isEmptyBlock(worldPosition.above(dy))) {
                    level.setBlock(worldPosition.above(dy), ModBlocks.BARRIER.get().defaultBlockState(), 3);
                }
            }
        }
    }
}



