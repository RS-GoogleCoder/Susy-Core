package supersymmetry.common.item;

import gregtech.api.items.armor.ArmorMetaItem;
import gregtech.api.items.armor.IArmorLogic;
import gregtech.api.items.metaitem.stats.IItemBehaviour;

import gregtech.api.items.metaitem.stats.IItemDurabilityManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import supersymmetry.common.event.DimensionBreathabilityHandler;

public class SimpleGasMask implements IArmorLogic, IItemDurabilityManager {
    double damage = 1;

    @Override
    public EntityEquipmentSlot getEquipmentSlot(ItemStack itemStack) {
        return EntityEquipmentSlot.HEAD;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "gregtech:textures/armor/simple_gas_mask.json.png";
    }

    @Override
    public void addToolComponents(ArmorMetaItem.ArmorMetaValueItem mvi) {
        mvi.addComponents(new IItemBehaviour() {

            @Override
            public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
                return onRightClick(world, player, hand);
            }
        });
    }

    public ActionResult<ItemStack> onRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (player.getHeldItem(hand).getItem() instanceof ArmorMetaItem) {
            ItemStack armor = player.getHeldItem(hand);
            if (armor.getItem() instanceof ArmorMetaItem &&
                    player.inventory.armorInventory.get(EntityEquipmentSlot.HEAD.getIndex()).isEmpty() &&
                    !player.isSneaking()) {
                player.inventory.armorInventory.set(EntityEquipmentSlot.HEAD.getIndex(), armor.copy());
                player.setHeldItem(hand, ItemStack.EMPTY);
                player.playSound(new SoundEvent(new ResourceLocation("item.armor.equip_generic")), 1.0F, 1.0F);
                return ActionResult.newResult(EnumActionResult.SUCCESS, armor);
            }
        }

        return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (DimensionBreathabilityHandler.isInHazardousEnvironment(player)) {
            damage -= 1. / (20. * 60. * 15.); // 20 ticks per second * 60 seconds per minute * 15 minutes
        }
    }


    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return 0;
    }
}