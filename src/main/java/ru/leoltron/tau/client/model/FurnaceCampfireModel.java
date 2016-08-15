package ru.leoltron.tau.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;


public class FurnaceCampfireModel extends ModelBase
{
  //fields
    ModelRenderer Wood1;
    ModelRenderer Wood2;
    ModelRenderer Wood3;  
    ModelRenderer Wood4;
    ModelRenderer Wood5;
    ModelRenderer Wood6;
    ModelRenderer Wood7;
    ModelRenderer Wood8;
    ModelRenderer WoodSmallm135;
    ModelRenderer WoodSmallm90;
    ModelRenderer WoodSmallm45;
    ModelRenderer WoodSmall0;
    ModelRenderer WoodSmall45;
    ModelRenderer WoodSmall90;
    ModelRenderer WoodSmall135;
    ModelRenderer WoodSmall180;
  
  public FurnaceCampfireModel()
  {
    textureWidth = 8;
    textureHeight = 16;
    
      Wood1 = new ModelRenderer(this, 0, 0);
      Wood1.addBox(-0.5F, 1F, 0F, 1, 11, 1);
      Wood1.setRotationPoint(0F, 13F, 0F);
      Wood1.setTextureSize(8, 16);
      Wood1.mirror = true;
      setRotation(Wood1, 0.4089647F, -2.356194F, 0F);
      Wood2 = new ModelRenderer(this, 0, 0);
      Wood2.addBox(-0.5F, 1F, 0F, 1, 11, 1);
      Wood2.setRotationPoint(0F, 13F, 0F);
      Wood2.setTextureSize(8, 16);
      Wood2.mirror = true;
      setRotation(Wood2, 0.4089647F, -1.570796F, 0F);
      Wood3 = new ModelRenderer(this, 0, 0);
      Wood3.addBox(-0.5F, 1F, 0F, 1, 11, 1);
      Wood3.setRotationPoint(0F, 13F, 0F);
      Wood3.setTextureSize(8, 16);
      Wood3.mirror = true;
      setRotation(Wood3, 0.4089647F, -0.7853982F, 0F);
      Wood4 = new ModelRenderer(this, 0, 0);
      Wood4.addBox(-0.5F, 1F, 0F, 1, 11, 1);
      Wood4.setRotationPoint(0F, 13F, 0F);
      Wood4.setTextureSize(8, 16);
      Wood4.mirror = true;
      setRotation(Wood4, 0.4089647F, 0F, 0F);
      Wood5 = new ModelRenderer(this, 0, 0);
      Wood5.addBox(-0.5F, 1F, 0F, 1, 11, 1);
      Wood5.setRotationPoint(0F, 13F, 0F);
      Wood5.setTextureSize(8, 16);
      Wood5.mirror = true;
      setRotation(Wood5, 0.4089647F, 0.7853982F, 0F);
      Wood6 = new ModelRenderer(this, 0, 0);
      Wood6.addBox(-0.5F, 1F, 0F, 1, 11, 1);
      Wood6.setRotationPoint(0F, 13F, 0F);
      Wood6.setTextureSize(8, 16);
      Wood6.mirror = true;
      setRotation(Wood6, 0.4089647F, 1.570796F, 0F);
      Wood7 = new ModelRenderer(this, 0, 0);
      Wood7.addBox(-0.5F, 1F, 0F, 1, 11, 1);
      Wood7.setRotationPoint(0F, 13F, 0F);
      Wood7.setTextureSize(8, 16);
      Wood7.mirror = true;
      setRotation(Wood7, 0.4089647F, 2.356194F, 0F);
      Wood8 = new ModelRenderer(this, 0, 0);
      Wood8.addBox(-0.5F, 1F, 0F, 1, 11, 1);
      Wood8.setRotationPoint(0F, 13F, 0F);
      Wood8.setTextureSize(8, 16);
      Wood8.mirror = true;
      setRotation(Wood8, 0.4089647F, 3.141593F, 0F);
      WoodSmallm135 = new ModelRenderer(this, 0, 0);
      WoodSmallm135.addBox(-0.5F, 1F, 0F, 1, 7, 1);
      WoodSmallm135.setRotationPoint(0F, 16.7F, 0F);
      WoodSmallm135.setTextureSize(8, 16);
      WoodSmallm135.mirror = true;
      setRotation(WoodSmallm135, 0.4089647F, -2.748893F, 0F);
      WoodSmallm90 = new ModelRenderer(this, 0, 0);
      WoodSmallm90.addBox(-0.5F, 1F, 0F, 1, 7, 1);
      WoodSmallm90.setRotationPoint(0F, 16.7F, 0F);
      WoodSmallm90.setTextureSize(8, 16);
      WoodSmallm90.mirror = true;
      setRotation(WoodSmallm90, 0.4089647F, -1.963495F, 0F);
      WoodSmallm45 = new ModelRenderer(this, 0, 0);
      WoodSmallm45.addBox(-0.5F, 1F, 0F, 1, 7, 1);
      WoodSmallm45.setRotationPoint(0F, 16.7F, 0F);
      WoodSmallm45.setTextureSize(8, 16);
      WoodSmallm45.mirror = true;
      setRotation(WoodSmallm45, 0.4089647F, -1.178097F, 0F);
      WoodSmall0 = new ModelRenderer(this, 0, 0);
      WoodSmall0.addBox(-0.5F, 1F, 0F, 1, 7, 1);
      WoodSmall0.setRotationPoint(0F, 16.7F, 0F);
      WoodSmall0.setTextureSize(8, 16);
      WoodSmall0.mirror = true;
      setRotation(WoodSmall0, 0.4089647F, -0.3926991F, 0F);
      WoodSmall45 = new ModelRenderer(this, 0, 0);
      WoodSmall45.addBox(-0.5F, 1F, 0F, 1, 7, 1);
      WoodSmall45.setRotationPoint(0F, 16.7F, 0F);
      WoodSmall45.setTextureSize(8, 16);
      WoodSmall45.mirror = true;
      setRotation(WoodSmall45, 0.4089647F, 0.3926991F, 0F);
      WoodSmall90 = new ModelRenderer(this, 0, 0);
      WoodSmall90.addBox(-0.5F, 1F, 0F, 1, 7, 1);
      WoodSmall90.setRotationPoint(0F, 16.7F, 0F);
      WoodSmall90.setTextureSize(8, 16);
      WoodSmall90.mirror = true;
      setRotation(WoodSmall90, 0.4089647F, 1.178097F, 0F);
      WoodSmall135 = new ModelRenderer(this, 0, 0);
      WoodSmall135.addBox(-0.5F, 1F, 0F, 1, 7, 1);
      WoodSmall135.setRotationPoint(0F, 16.7F, 0F);
      WoodSmall135.setTextureSize(8, 16);
      WoodSmall135.mirror = true;
      setRotation(WoodSmall135, 0.4089647F, 1.963495F, 0F);
      WoodSmall180 = new ModelRenderer(this, 0, 0);
      WoodSmall180.addBox(-0.5F, 1F, 0F, 1, 7, 1);
      WoodSmall180.setRotationPoint(0F, 16.7F, 0F);
      WoodSmall180.setTextureSize(8, 16);
      WoodSmall180.mirror = true;
      setRotation(WoodSmall180, 0.4089647F, 3.141593F, 0F);
  }
  
  @Override
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5,entity);
    Wood1.render(f5);
    Wood2.render(f5);
    Wood3.render(f5);
    Wood4.render(f5);
    Wood5.render(f5);
    Wood6.render(f5);
    Wood7.render(f5);
    Wood8.render(f5);
    WoodSmallm135.render(f5);
    WoodSmallm90.render(f5);
    WoodSmallm45.render(f5);
    WoodSmall0.render(f5);
    WoodSmall45.render(f5);
    WoodSmall90.render(f5);
    WoodSmall135.render(f5);
    WoodSmall180.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  @Override
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }
  
  public void renderModel(float f){
	Wood1.render(f);
    Wood2.render(f);
    Wood3.render(f);
    Wood4.render(f);
    Wood5.render(f);
    Wood6.render(f);
    Wood7.render(f);
    Wood8.render(f);
    WoodSmallm135.render(f);
    WoodSmallm90.render(f);
    WoodSmallm45.render(f);
    WoodSmall0.render(f);
    WoodSmall45.render(f);
    WoodSmall90.render(f);
    WoodSmall135.render(f);
    WoodSmall180.render(f);
  }

}
