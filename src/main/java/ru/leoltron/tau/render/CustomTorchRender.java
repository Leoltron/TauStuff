package ru.leoltron.tau.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import ru.leoltron.tau.ModInfo;
import ru.leoltron.tau.client.model.CustomTorchModel;

public class CustomTorchRender implements IItemRenderer {

    private final ResourceLocation resloc = new ResourceLocation(ModInfo.modId, "textures/models/blocks/custom_torch.png");

    private RenderBlocks renderer;
    private CustomTorchModel model;

    public CustomTorchRender() {
        renderer = new RenderBlocks();
        model = new CustomTorchModel();
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

        float scale = 2f;

        if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON)
            GL11.glRotatef(20F, 1.0f, 1.0f, 0.0f);

        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glScalef(scale, scale, scale);
            GL11.glRotatef(30F, 0.0f, 1.0f, 0.0f);
            GL11.glTranslatef(-0.5f, -0.4f, -0.5F);
            this.renderModelAt(0.0D, 0.0625D, 0.0D, 0.0F);
        } else if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glTranslatef(-0.5f, 0.0f, -0.5F);
//			scale *= 1.5F;
            GL11.glScalef(scale, scale, scale);
            this.renderModelAt(-0.25D, 0.0D, -0.25D, 0.0F);

        } else {
            scale *= 1.5F;
            GL11.glScalef(scale, scale, scale);
            GL11.glRotatef(20.0f, -1.0f, 0.0f, 1.0f);
            this.renderModelAt(-0.25D + 0.0625D, 0.0D, -0.25D + 0.0625D, 0.0F);
        }

    }

    public void renderModelAt(double x, double y, double z, float f) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GL11.glRotatef(180, 0.1f, 0f, 0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resloc);
        GL11.glPushMatrix();
        model.renderModel(0.0625F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

}
