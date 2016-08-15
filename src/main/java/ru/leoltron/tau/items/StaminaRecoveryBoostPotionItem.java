package ru.leoltron.tau.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.leoltron.tau.ModInfo;
import ru.leoltron.tau.TauStuff;

import java.util.List;

public class StaminaRecoveryBoostPotionItem extends Item {
    public StaminaRecoveryBoostPotionItem() {
        this.setMaxStackSize(1);
        this.setCreativeTab(TauStuff.tauStuffCreativeTab);
        this.setTextureName(ModInfo.modId + ":stamina_recovery_boost_potion_item");
        this.setUnlocalizedName("staminaRecoveryBoostPotion");

        GameRegistry.registerItem(this, this.getUnlocalizedName());

    }

    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack) {
        return true;
    }

    @Override
    public ItemStack onEaten(ItemStack is, World world, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) {
            --is.stackSize;
        }
        if (!world.isRemote) {
            player.addPotionEffect(new PotionEffect(TauStuff.staminaRecoveryBoostPotion.getId(), 20 * 60 * 4, 0));

        }

        if (!player.capabilities.isCreativeMode) {
            if (is.stackSize <= 0) {
                return new ItemStack(Items.glass_bottle);
            }

            if (!player.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle))) {
                player.dropItem(Items.glass_bottle, 1);
            }
        }

        return is;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
        return p_77659_1_;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_) {
        return EnumAction.drink;
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean p_77624_4_) {
        super.addInformation(is, player, list, p_77624_4_);
        list.add(StatCollector.translateToLocal("item.staminaRecoveryBoostPotion.desc"));
    }
}
