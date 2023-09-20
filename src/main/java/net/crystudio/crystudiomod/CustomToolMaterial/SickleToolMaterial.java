package net.crystudio.crystudiomod.CustomToolMaterial;

import net.crystudio.crystudiomod.Item.SickleItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class SickleToolMaterial implements ToolMaterial {
    @Override
    public int getDurability() {
        return 250;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 0F;
    }

    @Override
    public float getAttackDamage() {
        return 0F;
    }

    @Override
    public int getMiningLevel() {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return 10;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(SickleItem.Sickle);
    }
}
