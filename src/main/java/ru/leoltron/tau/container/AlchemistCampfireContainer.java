package ru.leoltron.tau.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.leoltron.tau.recipe.AlchemistCraftingManager;
import ru.leoltron.tau.tileentity.TileEntityCampfireFurnace;

public class AlchemistCampfireContainer extends Container {

    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 4, 1);
    public IInventory craftResult = new InventoryCraftResult();

    private EntityPlayer player;
    private TileEntityCampfireFurnace campfireFurnace;
    private World worldObj;
    private int posX;
    private int posY;
    private int posZ;


    public AlchemistCampfireContainer(EntityPlayer player, TileEntityCampfireFurnace entity) {
        int offsetY = 14;

        this.player = player;
        this.campfireFurnace = entity;
        this.worldObj = entity.getWorldObj();
        this.posX = entity.xCoord;
        this.posY = entity.yCoord;
        this.posZ = entity.zCoord;
        this.addSlotToContainer(new SlotCrafting(player, this.craftMatrix, this.craftResult, 0, 80, 35 + offsetY));
        int i;
        int k;

        this.addSlotToContainer(new Slot(this.craftMatrix, 0, 53, 8 + offsetY));
        this.addSlotToContainer(new Slot(this.craftMatrix, 1, 107, 8 + offsetY));
        this.addSlotToContainer(new Slot(this.craftMatrix, 2, 53, 62 + offsetY));
        this.addSlotToContainer(new Slot(this.craftMatrix, 3, 107, 62 + offsetY));

        for (i = 0; i < 3; ++i) {
            for (k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18 + offsetY));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142 + offsetY));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        this.craftResult.setInventorySlotContents(0, AlchemistCraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj, this.player));
        this.detectAndSendChanges();
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!this.worldObj.isRemote) {
            for (int i = 0; i < craftMatrix.getSizeInventory(); ++i) {
                ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);

                if (itemstack != null) {
                    player.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.campfireFurnace.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotNumber);

        int e = 5;

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotNumber == 0) {
                if (!this.mergeItemStack(itemstack1, 10 - e, 46 - e, true)) {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotNumber >= 10 - e && slotNumber < 37 - e) {
                if (!this.mergeItemStack(itemstack1, 37 - e, 46 - e, false)) {
                    return null;
                }
            } else if (slotNumber >= 37 - e && slotNumber < 46 - e) {
                if (!this.mergeItemStack(itemstack1, 10 - e, 37 - e, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 10 - e, 46 - e, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean func_94530_a(ItemStack is, Slot slot) {
        return slot.inventory != this.craftResult && super.func_94530_a(is, slot);
    }

    public int lastBurnTime = 0;

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (Object crafter : this.crafters) {
            ICrafting icrafting = (ICrafting) crafter;

            if (this.lastBurnTime != this.campfireFurnace.furnaceBurnTime) {
                icrafting.sendProgressBarUpdate(this, 1, this.campfireFurnace.furnaceBurnTime);
            }

        }
        this.lastBurnTime = this.campfireFurnace.furnaceBurnTime;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int slot, int newValue) {
        if (slot == 1) {
            this.campfireFurnace.furnaceBurnTime = newValue;
        }
    }
}
