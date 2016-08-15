package ru.leoltron.tau.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import ru.leoltron.tau.tileentity.TileEntityCampfireFurnace;

public class ContainerCampfireFurnace extends Container {

private TileEntityCampfireFurnace campfireFurnace;
	
	public int lastCookTime = 0;
	public int lastBurnTime = 0;

    public ContainerCampfireFurnace(InventoryPlayer inventory,TileEntityCampfireFurnace entity){
		this.campfireFurnace = entity;
		
		this.addSlotToContainer(new Slot(entity,0,54,35));
		this.addSlotToContainer(new SlotFurnace(inventory.player,entity,1,80+15+21,19+16));
		
		
			for(int i = 0; i< 3; i++){
				for(int g = 0; g<9; g++){
					this.addSlotToContainer(new Slot(inventory,g+i*9+9,8+g*18,84+i*18));
				}
			}
		
		
		for(int i = 0;i<9;i++){
			this.addSlotToContainer(new Slot(inventory,i,8+i*18,142));
		}
	}
	
	@Override
    public void addCraftingToCrafters(ICrafting icrafting){
		super.addCraftingToCrafters(icrafting);
		icrafting.sendProgressBarUpdate(this, 0, this.campfireFurnace.furnaceCookTime);
		icrafting.sendProgressBarUpdate(this, 1, this.campfireFurnace.furnaceBurnTime);
	}
	
	@Override
    public void detectAndSendChanges(){
		super.detectAndSendChanges();
        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (this.lastCookTime != this.campfireFurnace.furnaceCookTime) {
                icrafting.sendProgressBarUpdate(this, 0, this.campfireFurnace.furnaceCookTime);
            }

            if (this.lastBurnTime != this.campfireFurnace.furnaceBurnTime) {
                icrafting.sendProgressBarUpdate(this, 1, this.campfireFurnace.furnaceBurnTime);
            }

        }
		this.lastCookTime = this.campfireFurnace.furnaceCookTime;
        this.lastBurnTime = this.campfireFurnace.furnaceBurnTime;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
	public void updateProgressBar(int slot, int newValue){
		if(slot == 0){
			this.campfireFurnace.furnaceCookTime = newValue;
		}else if(slot == 1){
			this.campfireFurnace.furnaceBurnTime = newValue;
		}
	}
	
	
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.campfireFurnace.isUseableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber)
    {
		
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotNumber);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            if (slotNumber == 1)
            {
            	if (!this.mergeItemStack(itemstack1, 2, 38, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (slotNumber != 0)
            {
            	if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (slotNumber > 1 && slotNumber < 29)
                {
                    if (!this.mergeItemStack(itemstack1, 29, 38, false))
                    {
                        return null;
                    }
                }
                else if (slotNumber > 28 && slotNumber < 38 && !this.mergeItemStack(itemstack1, 2, 29, false))
                {
                	return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 2, 38, false))
            {
            	return null;
            }
            
            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }
}
