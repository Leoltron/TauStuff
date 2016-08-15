package ru.leoltron.tau.common.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import ru.leoltron.tau.packet.PacketDispatcher;
import ru.leoltron.tau.packet.UpdateAlchemistKnowlegeMessage;

public class PlayerLogInEventHandler {
	
	@SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		PacketDispatcher.sendTo(new UpdateAlchemistKnowlegeMessage(event.player), (EntityPlayerMP) event.player);
	}
	
}
