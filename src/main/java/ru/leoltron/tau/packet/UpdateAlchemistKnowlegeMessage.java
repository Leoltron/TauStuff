package ru.leoltron.tau.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import ru.leoltron.tau.TauStuff;
import ru.leoltron.tau.recipe.AlchemistCraftingManager;

import java.util.Arrays;

import static ru.leoltron.tau.recipe.AlchemistCraftingManager.RECIPES_TAG;

public class UpdateAlchemistKnowlegeMessage implements IMessage {

    private byte[] knowlegeBytes;

    public UpdateAlchemistKnowlegeMessage() {
        knowlegeBytes = new byte[0];
    }

    public UpdateAlchemistKnowlegeMessage(EntityPlayer player) {
        AlchemistCraftingManager.updateRecipeTag(player);
        NBTTagCompound tag = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag(RECIPES_TAG);
        knowlegeBytes = new byte[AlchemistCraftingManager.getInstance().getRecipeCount()];
        for (int i = 0; i < knowlegeBytes.length; i++)
            knowlegeBytes[i] = (byte) (tag.getBoolean(String.valueOf(i)) ? 1 : 0);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        knowlegeBytes = buf.readBytes(knowlegeBytes).array();
        knowlegeBytes = Arrays.copyOfRange(knowlegeBytes, 1, knowlegeBytes.length);
        String a = "";
        for (byte b : knowlegeBytes)
            a = a + b + " ";
    }

    @Override
    public void toBytes(ByteBuf buf) {
        String a = "";
        for (byte b : knowlegeBytes)
            a = a + b + " ";
        buf.writeBytes(knowlegeBytes);
    }

    public static class Handler extends AbstractClientMessageHandler<UpdateAlchemistKnowlegeMessage> {
        @Override
        public IMessage onMessage(UpdateAlchemistKnowlegeMessage message, MessageContext ctx) {
            if (ctx.side.isClient()) {
                return handleClientMessage(TauStuff.proxy.getPlayerEntity(ctx), message, ctx);
            } else {
                return handleServerMessage(TauStuff.proxy.getPlayerEntity(ctx), message, ctx);
            }
        }

        @Override
        public IMessage handleClientMessage(EntityPlayer player_, UpdateAlchemistKnowlegeMessage message, MessageContext ctx) {
            String m = "";
            for (byte b : message.knowlegeBytes)
                m = m + b + " ";
            EntityPlayer player = TauStuff.proxy.getMinecraftPlayer();
            AlchemistCraftingManager.updateRecipeTag(player);
            NBTTagCompound tag = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag(RECIPES_TAG);
            NBTTagCompound recipeTags = tag.getCompoundTag(RECIPES_TAG);

            for (int i = 0; i < message.knowlegeBytes.length; i++)
                recipeTags.setBoolean(String.valueOf(i), message.knowlegeBytes[i] != 0);

            tag.setTag(RECIPES_TAG, recipeTags);
            player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, tag);
            return null;
        }
    }

}
