package ru.leoltron.tau.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

public class TileEntityCampfireFurnace extends TileEntity implements ISidedInventory{
	
	    private ItemStack[] furnaceItemStacks = new ItemStack[2]; 
	    public int furnaceBurnTime;
	    //public int cookTime;
	    public int furnaceCookTime;
	    private String customInventoryName;
	    
	    public static final int cookTime = TileEntityFurnace.getItemBurnTime(new ItemStack(Items.coal));
	    
	    public TileEntityCampfireFurnace(){
	    	this.furnaceBurnTime = cookTime;
	    }

	    @Override
        public int getSizeInventory()
	    {
	        return this.furnaceItemStacks.length;
	    }

	    @Override
        public ItemStack getStackInSlot(int slot)
	    {
	        return this.furnaceItemStacks[slot];
	    }

	    @Override
        public ItemStack decrStackSize(int slot, int amount)
	    {
	        if (this.furnaceItemStacks[slot] != null)
	        {
	            ItemStack itemstack;

	            if (this.furnaceItemStacks[slot].stackSize <= amount)
	            {
	                itemstack = this.furnaceItemStacks[slot];
	                this.furnaceItemStacks[slot] = null;
	                return itemstack;
	            }
	            else
	            {
	                itemstack = this.furnaceItemStacks[slot].splitStack(amount);

	                if (this.furnaceItemStacks[slot].stackSize == 0)
	                {
	                    this.furnaceItemStacks[slot] = null;
	                }

	                return itemstack;
	            }
	        }
	        else
	        {
	            return null;
	        }
	    }

	    @Override
        public ItemStack getStackInSlotOnClosing(int slot)
	    {
	        if (this.furnaceItemStacks[slot] != null)
	        {
	            ItemStack itemstack = this.furnaceItemStacks[slot];
	            this.furnaceItemStacks[slot] = null;
	            return itemstack;
	        }
	        else
	        {
	            return null;
	        }
	    }

	    @Override
        public void setInventorySlotContents(int slot, ItemStack stack)
	    {
	        this.furnaceItemStacks[slot] = stack;

	        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
	        {
	            stack.stackSize = this.getInventoryStackLimit();
	        }
	    }

	    @Override
        public String getInventoryName()
	    {
	        return this.hasCustomInventoryName() ? this.customInventoryName : "container.campfireFurnace";
	    }

	    @Override
        public boolean hasCustomInventoryName()
	    {
	        return this.customInventoryName != null && this.customInventoryName.length() > 0;
	    }

	    public void func_145951_a(String customInventoryName)
	    {
	        this.customInventoryName = customInventoryName;
	    }

	    @Override
        public void readFromNBT(NBTTagCompound tagCompound)
	    {
	        super.readFromNBT(tagCompound);
	        NBTTagList nbttaglist = tagCompound.getTagList("Items", 10);
	        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

	        for (int i = 0; i < nbttaglist.tagCount(); ++i)
	        {
	            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
	            byte b0 = nbttagcompound1.getByte("Slot");

	            if (b0 >= 0 && b0 < this.furnaceItemStacks.length)
	            {
	                this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
	            }
	        }

	        this.furnaceBurnTime = tagCompound.getShort("BurnTime");
	        this.furnaceCookTime = tagCompound.getShort("CookTime");

	        if (tagCompound.hasKey("CustomName", 8))
	        {
	            this.customInventoryName = tagCompound.getString("CustomName");
	        }
	    }

	    @Override
        public void writeToNBT(NBTTagCompound tagCompound)
	    {
	        super.writeToNBT(tagCompound);
	        tagCompound.setShort("BurnTime", (short)this.furnaceBurnTime);
	        tagCompound.setShort("CookTime", (short)this.furnaceCookTime);
	        NBTTagList nbttaglist = new NBTTagList();

	        for (int i = 0; i < this.furnaceItemStacks.length; ++i)
	        {
	            if (this.furnaceItemStacks[i] != null)
	            {
	                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
	                nbttagcompound1.setByte("Slot", (byte)i);
	                this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
	                nbttaglist.appendTag(nbttagcompound1);
	            }
	        }

	        tagCompound.setTag("Items", nbttaglist);

	        if (this.hasCustomInventoryName())
	        {
	            tagCompound.setString("CustomName", this.customInventoryName);
	        }
	    }

	    @Override
        public int getInventoryStackLimit()
	    {
	        return 64;
	    }

	   
	    @SideOnly(Side.CLIENT)
	    public int getCookProgressScaled(int scale)
	    {
	        return this.furnaceCookTime * scale / 200;
	    }

	    @SideOnly(Side.CLIENT)
	    public int getBurnTimeRemainingScaled(int p_145955_1_)
	    {
//	        if (this.currentItemBurnTime == 0)
//	        {
//	            this.currentItemBurnTime = 200;
//	        }

	        return this.furnaceBurnTime * p_145955_1_ / cookTime;
	    }

	    public boolean isBurning()
	    {
	        return this.furnaceBurnTime > 0;
	    }

	    @Override
        public void updateEntity()
	    {
//	        boolean flag = this.furnaceBurnTime > 0;
	        boolean flag1 = false;

	        if (this.furnaceBurnTime > 0)
	        {
	        	//for(int i=0; i < 10; i++)
	        		--this.furnaceBurnTime;
	        }

	        if (!this.worldObj.isRemote)
	        {
	            if (this.furnaceBurnTime != 0 || this.furnaceItemStacks[0] != null)
	            {
	                if (this.isBurning() && this.canSmelt())
	                {
	                    ++this.furnaceCookTime;

	                    if (this.furnaceCookTime == 200)
	                    {
	                        this.furnaceCookTime = 0;
	                        this.smeltItem();
	                        flag1 = true;
	                    }
	                }
	                else
	                {
	                    this.furnaceCookTime = 0;
	                }
	            }
	            
	            if(this.furnaceBurnTime <= 0){
	            	worldObj.setBlockToAir(xCoord, yCoord, zCoord);
	            }
	        }

	        if (flag1)
	        {
	            this.markDirty();
	        }
	    }

	   
	    private boolean canSmelt()
	    {
	        if (this.furnaceItemStacks[0] == null)
	        {
	            return false;
	        }
	        else
	        {
	            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
	            if (itemstack == null) return false;
	            if (this.furnaceItemStacks[1] == null) return true;
	            if (!this.furnaceItemStacks[1].isItemEqual(itemstack)) return false;
	            int result = furnaceItemStacks[1].stackSize + itemstack.stackSize;
	            return result <= getInventoryStackLimit() && result <= this.furnaceItemStacks[1].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
	        }
	    }

	    /**
	     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
	     */
	    public void smeltItem()
	    {
	        if (this.canSmelt())
	        {
	            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);

	            if (this.furnaceItemStacks[1] == null)
	            {
	                this.furnaceItemStacks[1] = itemstack.copy();
	            }
	            else if (this.furnaceItemStacks[1].getItem() == itemstack.getItem())
	            {
	                this.furnaceItemStacks[1].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
	            }

	            --this.furnaceItemStacks[0].stackSize;

	            if (this.furnaceItemStacks[0].stackSize <= 0)
	            {
	                this.furnaceItemStacks[0] = null;
	            }
	        }
	    }

	    /**
	     * Do not make give this method the name canInteractWith because it clashes with Container
	     */
	    @Override
        public boolean isUseableByPlayer(EntityPlayer player)
	    {
	        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
	    }

	    @Override
        public void openInventory() {}

	    @Override
        public void closeInventory() {}

	    /**
	     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
	     */
	    @Override
        public boolean isItemValidForSlot(int slot, ItemStack stack)
	    {
	        return slot != 1;
	    }

	    @Override
        public int[] getAccessibleSlotsFromSide(int p_94128_1_)
	    {
	        return new int[0];
	    }

	    @Override
        public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_)
	    {
	        return false;
	    }

	    @Override
        public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_)
	    {
	        return false;
	    }
	    
}
