package ru.leoltron.tau.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ru.leoltron.tau.ModInfo;
import ru.leoltron.tau.TauStuff;

public class CactusSword extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    private static final int icons_amount = 5;

    public CactusSword() {
        this.setCreativeTab(TauStuff.tauStuffCreativeTab);
        this.setUnlocalizedName("cactus_sword");
        this.setMaxDamage(4 * 20);
        GameRegistry.registerItem(this, getUnlocalizedName());
    }


    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (stack.getItemDamage() != 0)
            return true;
        if (player.worldObj.rand.nextBoolean() && player.worldObj.rand.nextBoolean()) {
            player.addChatComponentMessage(new ChatComponentTranslation("item.cactus_sword.msg"));
            player.attackEntityFrom(DamageSource.cactus, 2.0F);
        }
        entity.attackEntityFrom(DamageSource.cactus, 2.0F);
        stack.setItemDamage(stack.getMaxDamage());
        return false;
    }


    @Override
    public IIcon getIconFromDamage(int damage) {
        return icons[MathHelper.floor_float((1 - (float) damage / this.getMaxDamage()) * (icons.length - 1))];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }


    @Override
    public void onUpdate(ItemStack stack, World world, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
        if (!world.isRemote && stack.getItemDamage() != 0)
            stack.setItemDamage(stack.getItemDamage() - 1);
    }


    @Override
    public void registerIcons(IIconRegister register) {
        icons = new IIcon[icons_amount];
        for (int i = 0; i < icons_amount; i++)
            icons[i] = register.registerIcon(ModInfo.modId + ":cactus_sword_" + i);
    }


    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.block;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
        return p_77659_1_;
    }
}
