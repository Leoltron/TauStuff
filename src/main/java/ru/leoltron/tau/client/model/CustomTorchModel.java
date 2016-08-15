package ru.leoltron.tau.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class CustomTorchModel extends ModelBase {
	ModelRenderer Shape1;

	public CustomTorchModel() {
		textureWidth = 16;
		textureHeight = 16;

		Shape1 = new ModelRenderer(this, 4, 4);
		Shape1.addBox(-1F, 0F, -1F, 2, 10, 2);
		Shape1.setRotationPoint(0F, 14F, 0F);
		Shape1.setTextureSize(16, 16);
		Shape1.mirror = true;
		setRotation(Shape1, 0F, 0.7853982F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		Shape1.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}

	public void renderModel(float f) {
		Shape1.render(f);
	}

}
