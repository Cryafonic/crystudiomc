package net.crystudio.crystudiomod;

import net.crystudio.crystudiomod.Item.SickleDispenserBehavior;
import net.crystudio.crystudiomod.Item.SickleItem;
import net.fabricmc.api.ModInitializer;
import org.slf4j.LoggerFactory;

public class CrystudioMod implements ModInitializer {
	public static final String MOD_ID = "crystudiomod";
	@Override
	public void onInitialize() {
		SickleItem.Init();
		SickleDispenserBehavior.Init();
	}
}
