package thaumcraft.common.lib.research.theorycraft;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.research.theorycraft.ITheorycraftAid;
import thaumcraft.api.research.theorycraft.TheorycraftCard;


public class AidBrainInAJar implements ITheorycraftAid
{
    @Override
    public Object getAidObject() { return null; }
    
    @Override
    public Class<TheorycraftCard>[] getCards() {
        return new Class[] { CardDarkWhispers.class };
    }
}


