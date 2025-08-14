package thaumcraft.api.golems;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;

public class ProvisionRequest {
    private int id;
    private ItemStack stack;
    private BlockPos pos;
    private Direction side;
    private Entity anEntity;
    private boolean invalid;
    
    public ProvisionRequest(int id, ItemStack stack, BlockPos pos, Direction side, Entity anEntity) {
        this.id = id;
        this.stack = stack;
        this.pos = pos;
        this.side = side;
        this.anEntity = anEntity;
        this.invalid = false;
    }
    
    ProvisionRequest(BlockPos pos, Direction side, ItemStack stack) {
        this.stack = stack;
        this.pos = pos;
        this.side = side;
        this.invalid = false;
    }
    
    public ProvisionRequest(Entity anEntity, ItemStack stack) {
        this.stack = stack;
        this.anEntity = anEntity;
        this.invalid = false;
    }
    
    public int getId() {
        return this.id;
    }
    
    public boolean isInvalid() {
        return this.invalid;
    }
    
    public void setInvalid() {
        this.invalid = true;
    }
    
    public ItemStack getStack() {
        return this.stack;
    }
    
    public Entity getEntity() {
        return this.anEntity;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public Direction getSide() {
        return this.side;
    }
    
    public void setSide(Direction side) {
        this.side = side;
    }
}

