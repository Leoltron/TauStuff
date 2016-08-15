package ru.leoltron.tau.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import ru.leoltron.tau.TauStuff;

import static ru.leoltron.tau.common.eventhandler.ActionsEventHandler.EMPTY_HAND_PSEUDOUNLOCALIZED_NAME;

public class LeftClickAirMessage implements IMessage {

    public LeftClickAirMessage() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler extends AbstractServerMessageHandler<LeftClickAirMessage> {
        @Override
        public IMessage onMessage(LeftClickAirMessage message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                return handleClientMessage(TauStuff.proxy.getPlayerEntity(ctx), message, ctx);
            } else {
                return handleServerMessage(TauStuff.proxy.getPlayerEntity(ctx), message, ctx);
            }
        }

        @Override
        public IMessage handleServerMessage(EntityPlayer player, LeftClickAirMessage message, MessageContext ctx) {
            if (player.getCurrentEquippedItem() != null && !(player.getCurrentEquippedItem().getItem() instanceof ItemBow) && TauStuff.itemStamina.containsKey(player.getCurrentEquippedItem().getUnlocalizedName())) {
                float nes = TauStuff.itemStamina.get(player.getCurrentEquippedItem().getUnlocalizedName());
                TauStuff.decreaseStaminaLevel(player, nes);
            } else if (player.getCurrentEquippedItem() == null && TauStuff.itemStamina.containsKey(EMPTY_HAND_PSEUDOUNLOCALIZED_NAME)) {
                float nes = TauStuff.itemStamina.get(EMPTY_HAND_PSEUDOUNLOCALIZED_NAME);
                TauStuff.decreaseStaminaLevel(player, nes);
            }
            return null;
        }
    }

}
