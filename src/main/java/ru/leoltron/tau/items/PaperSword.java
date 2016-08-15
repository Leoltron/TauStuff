package ru.leoltron.tau.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.EnumHelper;
import ru.leoltron.tau.ModInfo;
import ru.leoltron.tau.TauStuff;

public class PaperSword extends ItemSword {

	public static final ToolMaterial PAPER = EnumHelper.addToolMaterial("PAPER", 0, 1, 2.0F,-2.0F, 22);

	public PaperSword() {
		super(PAPER);
		this.setCreativeTab(TauStuff.tauStuffCreativeTab);
		this.setUnlocalizedName("paper_sword");
		this.setTextureName(ModInfo.modId +":paper_sword");
		GameRegistry.registerItem(this, getUnlocalizedName());
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if(player.worldObj.rand.nextBoolean()){
			player.addChatComponentMessage(new ChatComponentTranslation("item.paper_sword.msg"));
			player.attackEntityFrom(DamageSource.generic, 2.0F);
		}
		return false;
	}
	
	@Override
	public boolean hitEntity(ItemStack p_77644_1_, EntityLivingBase p_77644_2_, EntityLivingBase p_77644_3_)
    {
        p_77644_1_.damageItem(2, p_77644_3_);
        return true;
    }

}
