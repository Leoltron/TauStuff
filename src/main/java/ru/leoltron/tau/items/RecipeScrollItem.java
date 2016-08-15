package ru.leoltron.tau.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import ru.leoltron.tau.ModInfo;
import ru.leoltron.tau.TauStuff;
import ru.leoltron.tau.recipe.AlchemistCraftingManager;

import java.util.List;

public class RecipeScrollItem extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon learnedIcon;

    public RecipeScrollItem() {
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setCreativeTab(TauStuff.tauStuffCreativeTab);
        this.setTextureName(ModInfo.modId + ":recipeScroll");

        GameRegistry.registerItem(this, getUnlocalizedName());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(ModInfo.modId + ":recipeScroll");
        learnedIcon = register.registerIcon(ModInfo.modId + ":recipeScroll_learned");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if (AlchemistCraftingManager.learnRecipe(player, stack.getItemDamage())) {
                stack.stackSize--;
                IChatComponent c = new ChatComponentTranslation("msg.recipeLearned");
                c.getChatStyle().setColor(EnumChatFormatting.GREEN);
                player.addChatComponentMessage(c);
            } else {
                IChatComponent c = new ChatComponentTranslation("msg.recipeAlreadyLearned");
                c.getChatStyle().setColor(EnumChatFormatting.GOLD);
                player.addChatComponentMessage(c);
            }
        }
        return stack;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item.recipeScroll." + AlchemistCraftingManager.getInstance().getRecipeById(stack.getItemDamage()).getName();
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.uncommon;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < AlchemistCraftingManager.getInstance().getRecipeCount(); i++)
            list.add(new ItemStack(item, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return AlchemistCraftingManager.isRecipeAvailable(Minecraft.getMinecraft().thePlayer, damage) ? this.learnedIcon : this.itemIcon;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p_77624_4_) {
        if (AlchemistCraftingManager.isRecipeAvailable(player, stack.getItemDamage()))
            list.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("item.desc.learned"));
    }


}
