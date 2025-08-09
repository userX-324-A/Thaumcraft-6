package thaumcraft.common.blocks.world;

import thaumcraft.common.registers.ModBlockEntities;

public class EssentiaJarVoidBlockEntity extends EssentiaJarBlockEntity {
    public EssentiaJarVoidBlockEntity() {
        super();
        this.blockEntity = ModBlockEntities.ESSENTIA_JAR_VOID.get();
    }
}
