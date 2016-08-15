package ru.leoltron.tau.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

public class ItemRendererFurnaceCampfireBlock implements IItemRenderer {

    FurnaceCampfireRenderer renderer;
    private TileEntity entity;


    public ItemRendererFurnaceCampfireBlock(FurnaceCampfireRenderer renderer, TileEntity entity) {
        this.renderer = renderer;
        this.entity = entity;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (type == IItemRenderer.ItemRenderType.ENTITY)
            GL11.glTranslatef(-0.5f, 0.0f, -0.5F);

        GL11.glScalef(1.5f, 1.5f, 1.5f);

        if (type == IItemRenderer.ItemRenderType.INVENTORY)
            this.renderer.renderTileEntityAt(entity, 0.0D, 0.125D, 0.0D, 0.0F, item.getItemDamage());
        else
            this.renderer.renderTileEntityAt(entity, -0.125D, 0.25D, -0.125D, 0.0F, item.getItemDamage());

    }

}
