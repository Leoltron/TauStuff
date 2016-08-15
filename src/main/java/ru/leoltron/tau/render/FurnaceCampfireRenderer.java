package ru.leoltron.tau.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import ru.leoltron.tau.ModInfo;
import ru.leoltron.tau.client.model.FurnaceCampfireModel;
import ru.leoltron.tau.tileentity.TileEntityCampfireFurnace;

public class FurnaceCampfireRenderer extends TileEntitySpecialRenderer {

	private static final String[] textures = { "campfireFurance.png", "blazeRodCampfire.png" };

	private FurnaceCampfireModel model;

	public FurnaceCampfireRenderer() {
		model = new FurnaceCampfireModel();
	}

	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f, int type) {
		if (!(tile instanceof TileEntityCampfireFurnace))
			return;
		int id = type >= 0 ? type : ((TileEntityCampfireFurnace) tile).blockMetadata;

		if (id < 0 || id >= textures.length)
			id = 0;
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
		GL11.glRotatef(180, 0.1f, 0f, 0f);
		bindTexture(new ResourceLocation(ModInfo.modId, "textures/models/blocks/" + textures[id]));
		GL11.glPushMatrix();
		model.renderModel(0.0625F);
		GL11.glPopMatrix();
		GL11.glPopMatrix();

	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
		this.renderTileEntityAt(tile, x, y, z, f, tile.getBlockMetadata());
	}

}
