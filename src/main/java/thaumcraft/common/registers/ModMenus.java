package thaumcraft.common.registers;

import net.minecraft.inventory.container.ContainerType;
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
import thaumcraft.common.menu.SealContainer;
import thaumcraft.common.menu.ResearchTableMenu;

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

    public static final RegistryObject<ContainerType<SealContainer>> SEAL =
            RegistryManager.MENUS.register("seal",
                    () -> IForgeContainerType.create((windowId, inv, data) -> SealContainer.fromNetwork(windowId, inv, data)));

    public static final RegistryObject<ContainerType<ResearchTableMenu>> RESEARCH_TABLE =
            RegistryManager.MENUS.register("research_table",
                    () -> IForgeContainerType.create((windowId, inv, data) -> {
                        net.minecraft.util.math.BlockPos pos = data.readBlockPos();
                        net.minecraft.world.World w = inv.player.level;
                        net.minecraft.util.IWorldPosCallable call = new net.minecraft.util.IWorldPosCallable() {
                            @Override
                            public <T> java.util.Optional<T> evaluate(java.util.function.BiFunction<net.minecraft.world.World, net.minecraft.util.math.BlockPos, T> func) {
                                return java.util.Optional.ofNullable(func.apply(w, pos));
                            }
                            @Override
                            public void execute(java.util.function.BiConsumer<net.minecraft.world.World, net.minecraft.util.math.BlockPos> consumer) {
                                consumer.accept(w, pos);
                            }
                        };
                        return new ResearchTableMenu(windowId, inv, call);
                    }));
}


 
