package ru.leoltron.tau.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import ru.leoltron.tau.TauStuff;

import java.util.List;

public class CustomPotionItem extends Item {

	private PotionEffect[] effects;
	private int rarity = 0;

	public CustomPotionItem(int rarity, PotionEffect... effects) {
		this(effects);
		this.rarity = rarity;
	}

	public CustomPotionItem(PotionEffect... effects) {
		this.setMaxStackSize(1);
		this.effects = effects;
		this.setCreativeTab(TauStuff.tauStuffCreativeTab);
	}

	@Override
	public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List infoList, boolean p_77624_4_) {
		for (PotionEffect potioneffect : this.effects) {
			String s1 = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
			Potion potion = Potion.potionTypes[potioneffect.getPotionID()];

			if (potioneffect.getAmplifier() > 0) {
				s1 = s1 + " " + StatCollector.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
			}

			if (potioneffect.getDuration() > 20) {
				s1 = s1 + " (" + Potion.getDurationString(potioneffect) + ")";
			}

			if (potion.isBadEffect()) {
				infoList.add(EnumChatFormatting.RED + s1);
			} else {
				infoList.add(EnumChatFormatting.GRAY + s1);
			}
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack p_77661_1_) {
		return EnumAction.drink;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return 32;
	}

	@Override
	public EnumRarity getRarity(ItemStack p_77613_1_) {
		switch (this.rarity) {
			case 0:
				return EnumRarity.common;
			case 1:
				return EnumRarity.uncommon;
			case 2:
				return EnumRarity.rare;
			case 3:
				return EnumRarity.epic;
		}
		return super.getRarity(p_77613_1_);
	}

//	@Override
//	@SideOnly(Side.CLIENT)
//	public boolean hasEffect(ItemStack par1ItemStack)
//	{
//		return true;
//	}

	@Override
	public ItemStack onEaten(ItemStack is, World world, EntityPlayer player) {
		if (!player.capabilities.isCreativeMode) {
			--is.stackSize;
		}
		if (!world.isRemote) {
			for (PotionEffect e : this.effects)
				player.addPotionEffect(new PotionEffect(e));

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
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
        p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
        return p_77659_1_;
    }

    @Override
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        return false;
    }

    public CustomPotionItem setUnlocalizedNameAndRegister(String name){
        this.setUnlocalizedName(name);
        GameRegistry.registerItem(this,this.getUnlocalizedName());
        return this;
    }

}
