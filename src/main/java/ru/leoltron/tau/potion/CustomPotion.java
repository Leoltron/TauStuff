package ru.leoltron.tau.potion;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class CustomPotion extends Potion {

    private ResourceLocation customIconLocation;

    public CustomPotion(int id, boolean isBad, int color) {
        this(id, isBad, color, null);
    }

    public CustomPotion(int id, boolean isBad, int color, ResourceLocation customIconLocation) {
        super(id, isBad, color);
        this.customIconLocation = customIconLocation;
    }

    @Override
    public Potion setIconIndex(int p_76399_1_, int p_76399_2_) {
        return super.setIconIndex(p_76399_1_, p_76399_2_);
    }

    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        if (customIconLocation != null) {
            mc.renderEngine.bindTexture(customIconLocation);
            float scale = 13;
            int size = 256;
            int offset = 6;
            GL11.glScalef(1 / scale, 1 / scale, 1 / scale);
            mc.currentScreen.drawTexturedModalRect((int) ((x + offset) * scale), (int) ((y + offset) * scale), 0, 0, size, size);
            GL11.glScalef(scale, scale, scale);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasStatusIcon() {
        return customIconLocation != null || super.hasStatusIcon();
    }

}
