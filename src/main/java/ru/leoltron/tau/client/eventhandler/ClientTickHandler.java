package ru.leoltron.tau.client.eventhandler;

import java.util.Iterator;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import ru.leoltron.tau.TauStuff;
import ru.leoltron.tau.client.gui.GuiKillMessage;

public class ClientTickHandler {
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event){
		Iterator<GuiKillMessage> iterator = TauStuff.killLog.iterator();
		while(iterator.hasNext()){
			GuiKillMessage message = iterator.next();
			message.update();
			if(message.isNeedToRemove())
				iterator.remove();
		}
	}
}
