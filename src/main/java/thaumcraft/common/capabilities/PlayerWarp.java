package thaumcraft.common.capabilities;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import thaumcraft.api.capabilities.IPlayerWarp;

/**
 * Basic implementation of the player warp capability for 1.16.5 port.
 */
public class PlayerWarp implements IPlayerWarp {
    private int permanent;
    private int normal;
    private int temporary;
    private int counter;

    @Override
    public void clear() {
        permanent = normal = temporary = 0;
        counter = 0;
    }

    @Override
    public int get(EnumWarpType type) {
        switch (type) {
            case PERMANENT: return permanent;
            case NORMAL: return normal;
            case TEMPORARY: return temporary;
        }
        return 0;
    }

    @Override
    public void set(EnumWarpType type, int amount) {
        amount = Math.max(0, amount);
        switch (type) {
            case PERMANENT: permanent = amount; break;
            case NORMAL: normal = amount; break;
            case TEMPORARY: temporary = amount; break;
        }
    }

    @Override
    public int add(EnumWarpType type, int amount) {
        if (amount == 0) return get(type);
        switch (type) {
            case PERMANENT:
                permanent = Math.max(0, permanent + amount);
                return permanent;
            case NORMAL:
                normal = Math.max(0, normal + amount);
                return normal;
            case TEMPORARY:
                temporary = Math.max(0, temporary + amount);
                return temporary;
        }
        return 0;
    }

    @Override
    public int reduce(EnumWarpType type, int amount) {
        return add(type, -Math.abs(amount));
    }

    @Override
    public void sync(ServerPlayerEntity player) {
        thaumcraft.common.network.NetworkHandler.sendTo(player,
                new thaumcraft.common.network.msg.ClientSyncWarpMessage(serializeNBT()));
    }

    @Override
    public int getCounter() {
        return counter;
    }

    @Override
    public void setCounter(int amount) {
        counter = Math.max(0, amount);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("perm", permanent);
        tag.putInt("norm", normal);
        tag.putInt("temp", temporary);
        tag.putInt("ctr", counter);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt == null) return;
        permanent = Math.max(0, nbt.getInt("perm"));
        normal = Math.max(0, nbt.getInt("norm"));
        temporary = Math.max(0, nbt.getInt("temp"));
        counter = Math.max(0, nbt.getInt("ctr"));
    }
}



