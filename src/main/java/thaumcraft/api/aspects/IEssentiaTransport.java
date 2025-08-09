package thaumcraft.api.aspects;

import net.minecraft.util.Direction;

public interface IEssentiaTransport {
    boolean isConnectable(Direction face);
    
    boolean canInputFrom(Direction face);
    
    boolean canOutputTo(Direction face);
    
    void setSuction(Aspect aspect, int amount);
    
    Aspect getSuctionType(Direction face);
    
    int getSuctionAmount(Direction face);
    
    int takeEssentia(Aspect aspect, int amount, Direction face);
    
    int addEssentia(Aspect aspect, int amount, Direction face);
    
    Aspect getEssentiaType(Direction face);
    
    int getEssentiaAmount(Direction face);
    
    int getMinimumSuction();
    
    int getSuction();
    
    void setSuction(int amount);
}


