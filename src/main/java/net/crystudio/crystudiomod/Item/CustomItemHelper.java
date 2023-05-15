package net.crystudio.crystudiomod.Item;

import net.crystudio.crystudiomod.CrystudioMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomItemHelper {
    public static Item RegisterItem(String itemName, Item item) {
        return Registry.register(Registries.ITEM,new Identifier(CrystudioMod.MOD_ID,itemName), item);
    }

    public static void AddToItemGroup(ItemGroup itemGroup,Item item) {
        ItemGroupEvents.modifyEntriesEvent(itemGroup).register(x -> x.add(item));
    }

}
