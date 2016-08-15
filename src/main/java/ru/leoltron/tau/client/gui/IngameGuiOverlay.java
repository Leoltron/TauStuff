package ru.leoltron.tau.client.gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import org.lwjgl.opengl.GL11;
import ru.leoltron.tau.ModInfo;
import ru.leoltron.tau.TauStuff;

import java.util.ArrayList;
import java.util.List;

public class IngameGuiOverlay extends Gui {

	private static final ResourceLocation icon = new ResourceLocation(ModInfo.modId,"textures/gui/stamina_bar_icon.png");

	private static final int BUFF_ICON_SIZE = 18;
	private static final int BUFF_ICON_SPACING = BUFF_ICON_SIZE + 2; // 2 pixels between buff icons
	private static final int BUFF_ICON_BASE_U_OFFSET = 0;
	private static final int BUFF_ICON_BASE_V_OFFSET = 198;
	private static final int BUFF_ICONS_PER_ROW = 8;

	private static final int ITEM_ICON_SIZE = 16;
	private static final int ITEM_ICON_SPACING = 5;
	private static final int ITEM_OFFSET = 5;

	private int tick = 0; 
	private int sec = 0;
    private int staminaRecoverPotionMaxDuration = 0;

	private Minecraft mc;
	private RenderItem itemRender = new RenderItem();
    private ScaledResolution scaledresolution;


    public IngameGuiOverlay(Minecraft mc)
	{   
		super();
		this.mc = mc;
	}

	@SubscribeEvent
	public void onRenderGameOverlayEvent(RenderGameOverlayEvent event){

        scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        if(event.isCancelable() || event.type != ElementType.EXPERIENCE)
		{      
			if(event.type == ElementType.EXPERIENCE && !Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode){

				int k = scaledresolution.getScaledWidth();
				int l = scaledresolution.getScaledHeight();

                drawStaminaBar();

				if(this.mc.thePlayer.isPotionActive(TauStuff.combatRagePotion)){
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0f);
					mc.renderEngine.bindTexture(new ResourceLocation(ModInfo.modId,"textures/gui/potion/combatRage.png"));
					float scale = 13;
					int size = 256;
//					int offset = 6;
					GL11.glScalef(1/scale,1/scale,1/scale);
					drawTexturedModalRect((int)(k*scale/2-101*scale)-size/2,(int)((l-22)*scale),0,0, size, size);
					drawTexturedModalRect((int)(k*scale/2+101*scale)-size/2,(int)((l-22)*scale),0,0, size, size);

					GL11.glScalef(scale,scale,scale);
				}

				this.mc.getTextureManager().bindTexture(Gui.icons);
			}
			return;
		}

//		int k = scaledresolution.getScaledWidth();
//		int l = scaledresolution.getScaledHeight();

		if(tick < Integer.MAX_VALUE)
            tick++;
        else
            tick = 0;


        drawItemsDurability();
        drawKillLog();

        //		    NBTTagCompound tag = mc.thePlayer.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag(AlchemistCraftingManager.RECIPES_TAG);
		//			NBTTagCompound recipeTags = tag.getCompoundTag(AlchemistCraftingManager.RECIPES_TAG);
		//			
		//			for(int i=0; i < AlchemistCraftingManager.getInstance().getRecipeCount();i++){
		//				this.drawString(this.mc.fontRenderer, AlchemistCraftingManager.getInstance().getRecipeById(i).getName()+" - "+String.valueOf(tag.getBoolean(String.valueOf(i))), 5, 5 + i*10, 0xffffff);
		//			}

		//		    int xPos = 2;
		//		    int yPos = 2;
		//		    Collection collection = this.mc.thePlayer.getActivePotionEffects();
		//		    if (!collection.isEmpty())
		//		    {
		//		      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		//		      GL11.glDisable(GL11.GL_LIGHTING);      
		//		      this.mc.renderEngine.bindTexture(Gui.statIcons);      
		//
		//		      for (Iterator iterator = this.mc.thePlayer.getActivePotionEffects()
		//		          .iterator(); iterator.hasNext(); xPos += BUFF_ICON_SPACING)
		//		      {
		//		        PotionEffect potioneffect = (PotionEffect) iterator.next();
		//		        Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
		//
		//		        if (potion.hasStatusIcon())
		//		        {
		//		          int iconIndex = potion.getStatusIconIndex();
		//		          this.drawTexturedModalRect(
		//		              xPos, yPos, 
		//		              BUFF_ICON_BASE_U_OFFSET + iconIndex % BUFF_ICONS_PER_ROW * BUFF_ICON_SIZE, BUFF_ICON_BASE_V_OFFSET + iconIndex / BUFF_ICONS_PER_ROW * BUFF_ICON_SIZE,
		//		              BUFF_ICON_SIZE, BUFF_ICON_SIZE);
		//		        }       
		//		      }
		//		    }


	}

    private void drawItemsDurability() {
        if(TauStuff.showDurability){
            int k = scaledresolution.getScaledWidth();
            int l = scaledresolution.getScaledHeight();

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            List<ItemStack> equippedItems = new ArrayList<ItemStack>();
            if(mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().isItemStackDamageable())
                equippedItems.add(mc.thePlayer.getCurrentEquippedItem());
            for(ItemStack is : mc.thePlayer.inventory.armorInventory)
                if(is != null)
                    equippedItems.add(is);

            for(int i=0; i < equippedItems.size(); i++){
                ItemStack is = equippedItems.get(i);
                itemRender.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, is,
                        k-ITEM_OFFSET-ITEM_ICON_SIZE, l-(ITEM_OFFSET+ITEM_ICON_SIZE)*(i+1), true);

                GL11.glDisable(GL11.GL_LIGHTING);


//				int damage = is.getItemDamageForDisplay();
//				int maxDamage = is.getMaxDamage();
                int color;
                float per = (float)(is.getMaxDamage()-is.getItemDamage())/(float)is.getMaxDamage();
                if(per <= 0.1f)
                    color=0xCC3300;
                else if(per <= 0.5f)
                    color=0xFFCC33;
                else
                    color=0xffffff;

                mc.fontRenderer.drawStringWithShadow(
                        String.valueOf(is.getMaxDamage()-is.getItemDamage()),
                        k-ITEM_OFFSET*2-ITEM_ICON_SIZE-mc.fontRenderer.getStringWidth(String.valueOf(is.getMaxDamage()-is.getItemDamage())),
                        l-(ITEM_OFFSET+ITEM_ICON_SIZE)*i-ITEM_OFFSET-12,
                        color);
            }

        }
    }

    private void drawKillLog() {
        if(TauStuff.showKillLog){
//            while(TauStuff.killLog.size() < 2)
//                TauStuff.killLog.add(new GuiKillMessage("Player"+((int)(Math.random()*1000)),"Player"+((int)(Math.random()*1000)),null,"",200));

            int k = scaledresolution.getScaledWidth();
            int offsetY = 10;
            int offsetX = 5;

            final int iconOffset = 5;
            final int spacing = 20;

            for(int n=0; n < TauStuff.killLog.size(); n++){
                int i = TauStuff.killLog.size()-1-n;
                GuiKillMessage m = TauStuff.killLog.get(i);

                this.mc.fontRenderer.drawStringWithShadow(m.getKilledName(),
                        k-(offsetX+this.mc.fontRenderer.getStringWidth(m.getKilledName())),
                        offsetY+spacing*i,
                        0xCC3300);

                this.mc.fontRenderer.drawStringWithShadow(m.getKillerAndWeaponName(),
                        k-(offsetX+this.mc.fontRenderer.getStringWidth(m.killedName)+this.mc.fontRenderer.getStringWidth(m.getKillerAndWeaponName())+2*iconOffset+ITEM_ICON_SIZE),
                        offsetY+spacing*i,
                        m.getKillerName().equals(mc.thePlayer.getDisplayName()) ? 0x0094FF : 0xffffff);

                int x = k-(offsetX+this.mc.fontRenderer.getStringWidth(m.killedName) + iconOffset + IngameGuiOverlay.ITEM_ICON_SIZE);
                int y = offsetY+spacing*i-4;
                if(m.weapon != null){
                    itemRender.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, m.weapon, x, y);
                }else{
                    itemRender.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(Items.skull), x, y);
                }
                GL11.glDisable(GL11.GL_LIGHTING);
            }
        }
    }

    private void drawStaminaBar() {
        int k = scaledresolution.getScaledWidth();
        int l = scaledresolution.getScaledHeight();

        int offsetY = -19;

        ItemStack[] armor = this.mc.thePlayer.inventory.armorInventory;
        if(armor[0] != null || armor[1] != null || armor[2] != null || armor[3] != null || this.mc.thePlayer.isInsideOfMaterial(Material.water))
            offsetY -= 10;

//				IAttributeInstance iattributeinstance = this.mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
        float f1 = this.mc.thePlayer.getAbsorptionAmount();
        int j2 = MathHelper.ceiling_float_int(f1 / 2.0F / 10.0F);
        int k2 = Math.max(10 - (j2 - 2), 3);
        offsetY = offsetY - (j2 - 1) * k2 - 10;

        mc.getTextureManager().bindTexture(icon);
        int i2;
        int l1;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i1 = k / 2 - 91;

        short short1 = 182;

        i2 = l - 32 + 3 + offsetY;
        this.drawTexturedModalRect(i1, i2, 0, 64, short1, 5);

        //System.out.println("pte: "+TauStuff.getStaminaLevel(Minecraft.getMinecraft().thePlayer));
        float level = TauStuff.getStaminaLevel(Minecraft.getMinecraft().thePlayer);


        if(level*2 < TauStuff.maxStamina){
            GL11.glEnable(GL11.GL_BLEND);
            mc.getTextureManager().bindTexture(icon);
            final int length = 40;
            float transparecy = (float)Math.abs((tick%length)-length/2)/((float)length/2);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, transparecy);
            this.drawTexturedModalRect(i1, i2, 0, 74, short1, 5);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
        }

        l1 = (int)((level/TauStuff.maxStamina) * (float)(short1 + 0.9));
        if (l1 > 0)
            this.drawTexturedModalRect(i1, i2, 0, 69, l1, 5);

        if(mc.thePlayer.isPotionActive(TauStuff.staminaRecoveryBoostPotion)){
            GL11.glEnable(GL11.GL_BLEND);
            int duration = mc.thePlayer.getActivePotionEffect(TauStuff.staminaRecoveryBoostPotion).getDuration();
            staminaRecoverPotionMaxDuration = Math.max(duration,staminaRecoverPotionMaxDuration);

            l1 = (int)(((float)duration/staminaRecoverPotionMaxDuration) * (float)(short1 + 0.9));
            this.drawTexturedModalRect(i1, i2, 0, 79, l1, 5);
            GL11.glDisable(GL11.GL_BLEND);
        }else
            staminaRecoverPotionMaxDuration = 0;

        String text = String.valueOf(level)+"/"+TauStuff.maxStamina;
        this.mc.fontRenderer.drawStringWithShadow(text, (k-this.mc.fontRenderer.getStringWidth(text))/2,i2-10, 16764736);

        if(level == 0){
            //			            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.renderEngine.bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
            int iconIndex = Potion.weakness.getStatusIconIndex();
            this.drawTexturedModalRect(
                    k/2-BUFF_ICON_SIZE/2, i2-30,
                    BUFF_ICON_BASE_U_OFFSET + iconIndex % BUFF_ICONS_PER_ROW * BUFF_ICON_SIZE, BUFF_ICON_BASE_V_OFFSET + iconIndex / BUFF_ICONS_PER_ROW * BUFF_ICON_SIZE,
                    BUFF_ICON_SIZE, BUFF_ICON_SIZE);
            //				        GL11.glDisable(GL11.GL_BLEND);
        }
    }

}
