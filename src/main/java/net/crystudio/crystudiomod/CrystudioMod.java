package net.crystudio.crystudiomod;

import net.crystudio.crystudiomod.CustomDispenserBehavior.SickleDispenserBehavior;
import net.crystudio.crystudiomod.Item.SickleItem;
import net.fabricmc.api.ModInitializer;

public class CrystudioMod implements ModInitializer {
	public static final String MOD_ID = "crystudiomod";
	@Override
	public void onInitialize() {
		SickleItem.Init();
		SickleDispenserBehavior.Init();
	}
}
