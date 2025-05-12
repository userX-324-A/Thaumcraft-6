package thaumcraft.common.items;
import net.minecraft.item.Item;


public interface IThaumcraftItems
{
    Item getItem();
    
    String[] getVariantNames();
    
    int[] getVariantMeta();
    
    // ItemMeshDefinition getCustomMesh();
    
    // ModelResourceLocation getCustomModelResourceLocation(String p0);
}
