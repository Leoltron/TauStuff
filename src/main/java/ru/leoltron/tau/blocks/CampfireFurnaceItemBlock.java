package ru.leoltron.tau.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class CampfireFurnaceItemBlock extends ItemBlock {

	public CampfireFurnaceItemBlock(Block block) {
		super(block);
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata (int damageValue) {
		return damageValue;
	}

	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List list, boolean p_77624_4_) {
		super.addInformation(is, player, list, p_77624_4_);
		switch(is.getItemDamage()){
		case 0:
			list.add(StatCollector.translateToLocal("itemBlock.campfire0.desc"));
			break;
		case 1:
			list.add(StatCollector.translateToLocal("itemBlock.campfire1_0.desc"));
			list.add(StatCollector.translateToLocal("itemBlock.campfire1_1.desc"));
			break;
		}
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is) {
		return super.getUnlocalizedName(is)+(is.getItemDamage() >= 0 && is.getItemDamage() < CampfireFurnaceBlock.subblocks.length ? "_"+CampfireFurnaceBlock.subblocks[is.getItemDamage()] : "");
	}
	
	

}
