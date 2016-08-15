package ru.leoltron.tau.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import ru.leoltron.tau.ModInfo;
import ru.leoltron.tau.container.ContainerCampfireFurnace;
import ru.leoltron.tau.tileentity.TileEntityCampfireFurnace;

public class GuiCampfireFurnace extends GuiContainer {

    public static final ResourceLocation bground = new ResourceLocation(ModInfo.modId, "textures/gui/campfire_furnace_gui.png");

    public TileEntityCampfireFurnace furnace;

    public GuiCampfireFurnace(InventoryPlayer inentoryPlayer, TileEntityCampfireFurnace entity) {
        super(new ContainerCampfireFurnace(inentoryPlayer, entity));

        this.furnace = entity;

        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        String s = this.furnace.hasCustomInventoryName() ? this.furnace.getInventoryName() : I18n.format(this.furnace.getInventoryName());
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(bground);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        int i1 = this.furnace.getBurnTimeRemainingScaled(13) + 1;
        this.drawTexturedModalRect(k + 83, l + 55 - i1 + 17, 176, 13 - i1, 25, i1 + 1);
        i1 = this.furnace.getCookProgressScaled(24);
        this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);

    }


}
