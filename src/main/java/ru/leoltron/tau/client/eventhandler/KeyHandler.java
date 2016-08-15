package ru.leoltron.tau.client.eventhandler;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.MouseInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import ru.leoltron.tau.packet.LeftClickAirMessage;
import ru.leoltron.tau.packet.PacketDispatcher;

public class KeyHandler {
	
	public KeyHandler() {
		
	}

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {
		if (!FMLClientHandler.instance().isGUIOpen(GuiChat.class)) {
			if(RenderManager.debugBoundingBox)
				RenderManager.debugBoundingBox = false;
			
		}
	}
	
	@SubscribeEvent
	public void onMouseInput(MouseInputEvent event) {
		try{
		if(!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode && Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && Minecraft.getMinecraft().objectMouseOver.typeOfHit == MovingObjectType.MISS)
			PacketDispatcher.sendToServer(new LeftClickAirMessage());
		}catch(NullPointerException e){
			e.printStackTrace();
		}
	}
}
