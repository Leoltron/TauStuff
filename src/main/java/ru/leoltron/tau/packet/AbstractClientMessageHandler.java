package ru.leoltron.tau.packet;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public abstract class AbstractClientMessageHandler<T extends IMessage> extends AbstractMessageHandler<T> {
    @Override
    public final IMessage handleServerMessage(EntityPlayer player, T message, MessageContext ctx) {
        return null;
    }
}