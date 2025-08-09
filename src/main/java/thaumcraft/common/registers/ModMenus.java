package thaumcraft.common.registers;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import thaumcraft.common.menu.ArcaneWorkbenchMenu;
import thaumcraft.common.menu.ThaumatoriumMenu;

public class ModMenus {
    public static final RegistryObject<ContainerType<ArcaneWorkbenchMenu>> ARCANE_WORKBENCH =
            RegistryManager.MENUS.register("arcane_workbench",
                    () -> IForgeContainerType.create((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        World w = inv.player.level;
                        IWorldPosCallable call = new IWorldPosCallable() {
                            @Override
                            public <T> Optional<T> evaluate(BiFunction<World, BlockPos, T> func) {
                                return Optional.ofNullable(func.apply(w, pos));
                            }
                            @Override
                            public void execute(BiConsumer<World, BlockPos> consumer) {
                                consumer.accept(w, pos);
                            }
                        };
                        return new ArcaneWorkbenchMenu(windowId, inv, call);
                    }));

    public static final RegistryObject<ContainerType<ThaumatoriumMenu>> THAUMATORIUM =
            RegistryManager.MENUS.register("thaumatorium",
                    () -> IForgeContainerType.create((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        World w = inv.player.level;
                        IWorldPosCallable call = new IWorldPosCallable() {
                            @Override
                            public <T> Optional<T> evaluate(BiFunction<World, BlockPos, T> func) {
                                return Optional.ofNullable(func.apply(w, pos));
                            }
                            @Override
                            public void execute(BiConsumer<World, BlockPos> consumer) {
                                consumer.accept(w, pos);
                            }
                        };
                        return new ThaumatoriumMenu(windowId, inv, call);
                    }));
}


 
