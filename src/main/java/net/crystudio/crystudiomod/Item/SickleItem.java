package net.crystudio.crystudiomod.Item;

import net.crystudio.crystudiomod.CustomHelpers.CustomItemHelper;
import net.crystudio.crystudiomod.CustomToolMaterial.SickleToolMaterial;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;

public class SickleItem extends MiningToolItem{
    private static final ToolMaterial material = new SickleToolMaterial();
    public static final Item Sickle = new SickleItem(material, material.getAttackDamage(), 0, new FabricItemSettings().maxCount(1));
    protected static final Map<Block, Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>> HARVESTING_ACTIONS;
    private static final IntProperty AGE = Properties.AGE_7;

    public SickleItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(attackDamage, attackSpeed, material, BlockTags.HOE_MINEABLE, new FabricItemSettings().maxCount(1));
    }

    public static void Init () {
        CustomItemHelper.RegisterItem("sickle", Sickle);
        CustomItemHelper.AddToItemGroup(ItemGroups.TOOLS, Sickle);
    }

   @Override
   public ActionResult useOnBlock(ItemUsageContext context) {
       World world = context.getWorld();
       BlockPos blockPos = context.getBlockPos();
       Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair = HARVESTING_ACTIONS.get(world.getBlockState(blockPos).getBlock());
       if (pair == null) {
           return ActionResult.PASS;
       } else {
           Predicate<ItemUsageContext> predicate = pair.getFirst();
           Consumer<ItemUsageContext> consumer = pair.getSecond();
           if (predicate.test(context)) {
               PlayerEntity playerEntity = context.getPlayer();
               world.playSound(playerEntity, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
               if (!world.isClient) {
                   consumer.accept(context);
                   if (playerEntity != null) {
                       context.getStack().damage(1, playerEntity, (p) -> {
                           p.sendToolBreakStatus(context.getHand());
                       });
                   }
               }

               return ActionResult.success(world.isClient);
           } else {
               return ActionResult.PASS;
           }
       }
   }

   public static Consumer<ItemUsageContext> dropItemProcedureWheat() {
       return (context) -> {
           int growthRange = (int)Math.round((Math.random() * 4) + 1);
           World world = context.getWorld();
           BlockState blockState = world.getBlockState(context.getBlockPos());
           world.setBlockState(context.getBlockPos(), blockState.with(AGE, growthRange));
           world.emitGameEvent(GameEvent.BLOCK_CHANGE, context.getBlockPos(), GameEvent.Emitter.of(context.getPlayer(), blockState));
           Block.dropStack(world, context.getBlockPos(), context.getSide(), new ItemStack(Items.WHEAT).copyWithCount(calculateWheatStackCountForFortune(context)));
       };
   }

   private static int calculateWheatStackCountForFortune(ItemUsageContext context) {
       Integer enchantmentLevel = EnchantmentHelper.getLevel(Enchantments.FORTUNE, context.getStack());
        if(enchantmentLevel > 0) {
            if(enchantmentLevel.equals(1)){
                return 2 + enchantmentLevel;
            } else if (enchantmentLevel.equals(2)) {
                return 2 + enchantmentLevel;
            } else {
                return 2 + enchantmentLevel;
            }
        } else {
            return 2;
        }
   }

   public static Consumer<ItemUsageContext> dropItemProcedureGrass(BlockState result) {
       return (context) -> {
            context.getWorld().breakBlock(context.getBlockPos(), false, context.getPlayer(), 1);//setBlockState(context.getBlockPos(), result, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            context.getWorld().emitGameEvent(GameEvent.BLOCK_DESTROY, context.getBlockPos(), GameEvent.Emitter.of(context.getPlayer(), result));
            Block.dropStack(context.getWorld(), context.getBlockPos(), context.getSide(), new ItemStack(Items.WHEAT_SEEDS));
       };
   }

    public static boolean canHarvest(ItemUsageContext context) {
       Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
       if (block.equals(Blocks.WHEAT)) {
            Integer ageState = (Integer)context.getWorld().getBlockState(context.getBlockPos()).getEntries().values().toArray()[0];
            return ageState.equals(7);
       } else if (block.equals(Blocks.GRASS)) {
           return true;
       } else if(block.equals(Blocks.TALL_GRASS)) {
            return true;
       }else {
           return false;
       }
   }

   static {
        HARVESTING_ACTIONS = Maps.newHashMap(ImmutableMap.of(Blocks.WHEAT, Pair.of(SickleItem::canHarvest, dropItemProcedureWheat()), Blocks.GRASS, Pair.of(SickleItem::canHarvest, dropItemProcedureGrass(Blocks.GRASS.getDefaultState())), Blocks.TALL_GRASS, Pair.of(SickleItem::canHarvest, dropItemProcedureGrass(Blocks.TALL_GRASS.getDefaultState()))));
   }
}
