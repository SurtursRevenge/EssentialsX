package net.ess3.nms.legacy;

import net.ess3.nms.PotionMetaProvider;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LegacyPotionMetaProvider extends PotionMetaProvider {
    @Override
    public ItemStack createPotionItem(int effectId) {
        ItemStack potion = new ItemStack(Material.POTION, 1);
        potion.setDurability((short) effectId);
        return potion;
    }

    @Override
    public String getHumanName() {
        return "legacy potion meta provider";
    }
}
