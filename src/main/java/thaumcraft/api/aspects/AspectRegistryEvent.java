package thaumcraft.api.aspects;

import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class AspectRegistryEvent extends Event {
    public List<Aspect> register;

    public AspectRegistryEvent(List<Aspect> register) {
        this.register = register;
    }
}

