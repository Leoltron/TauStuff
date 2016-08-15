package ru.leoltron.tau.common.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.AnvilUpdateEvent;

public class AnvilUpdateHandler {
	
	@SubscribeEvent
	public void onAnvilUpdate(AnvilUpdateEvent event){
		if(event.right.isItemEnchanted() && event.left.isItemEnchanted()){
			event.setCanceled(true);			
		}
	}
}
