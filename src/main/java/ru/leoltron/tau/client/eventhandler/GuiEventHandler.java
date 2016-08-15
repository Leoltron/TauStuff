package ru.leoltron.tau.client.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiOpenEvent;
import ru.leoltron.tau.client.gui.GuiIngameMenuOverride;
import ru.leoltron.tau.client.gui.GuiMainMenuOverride;
import ru.leoltron.tau.client.gui.GuiModOptions;

public class GuiEventHandler {
	public static boolean debug = false;
	
	@SubscribeEvent
	public void onOpen(GuiOpenEvent event){
		if(event.gui == null)
			return;		
		
		if(event.gui instanceof GuiBrewingStand || event.gui instanceof GuiEnchantment){
			event.setCanceled(true);
			return;
		}
		
		if(event.gui instanceof GuiIngameMenu)
				event.gui = new GuiIngameMenuOverride();
		else if(event.gui instanceof GuiMainMenu){			
			if(debug){
				debug = false;
			}else
				event.gui = new GuiMainMenuOverride();
		}else if(event.gui instanceof GuiOptions && (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu || Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu)){
			event.gui = new GuiOptions(Minecraft.getMinecraft().currentScreen,Minecraft.getMinecraft().gameSettings){
				@Override
				public void initGui() {
					super.initGui();
					buttonList.add(new GuiButton(256,this.width / 2 - 155, this.height / 6 + 42, 150, 20,I18n.format("options.taustuff.title")));

				}
				
				@Override
				public void actionPerformed(GuiButton button){
					super.actionPerformed(button);
					if(button.id == 256)
						this.mc.displayGuiScreen(new GuiModOptions(this));
				}
            	
            };
		}
			
	}
	
}
