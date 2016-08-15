package ru.leoltron.tau.common.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import ru.leoltron.tau.TauStuff;
import ru.leoltron.tau.packet.PacketDispatcher;
import ru.leoltron.tau.packet.PlayerKillMessage;

public class ActionsEventHandler {
    public static final String EMPTY_HAND_PSEUDOUNLOCALIZED_NAME = "hand";

    @SubscribeEvent
    public void onPlayerJump(LivingJumpEvent event) {
        if (event.entityLiving instanceof EntityPlayer && !event.entity.worldObj.isRemote)
            TauStuff.decreaseStaminaLevel((EntityPlayer) event.entityLiving, TauStuff.jumpStamina);
    }

    @SubscribeEvent
    public void onPlayerConstruction(EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            DataWatcher dw = event.entity.getDataWatcher();
            dw.addObject(TauStuff.STAMINA_LEVEL_DATAWATCHER_ID, event.entity.getEntityData().getFloat(TauStuff.STAMINA_LEVEL));
        }
    }

    @SubscribeEvent
    public void onPlayerKill(LivingDeathEvent event) {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer && event.source instanceof EntityDamageSource && event.source.getEntity() instanceof EntityPlayer) {
            EntityPlayer playerKilled = (EntityPlayer) event.entity;
            EntityPlayer playerKiller = (EntityPlayer) event.source.getEntity();
            String type = event.source.damageType;

            ItemStack weapon = null;

            if (type.equals("player"))
                weapon = playerKiller.getCurrentEquippedItem();
            else if (type.equals("arrow"))
                weapon = new ItemStack(Items.bow);
            else if (type.equals("indirectMagic"))
                weapon = new ItemStack(Items.potionitem, 1, 16460);

            PacketDispatcher.sendToAll(new PlayerKillMessage(playerKiller, playerKilled, weapon));
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        TauStuff.increaseStaminaLevel(event.player, TauStuff.maxStamina);
    }


    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (!event.entityPlayer.worldObj.isRemote) {

            float level = TauStuff.getStaminaLevel(event.entityPlayer);

            if (event.entityPlayer.getCurrentEquippedItem() != null && TauStuff.itemStamina.containsKey(event.entityPlayer.getCurrentEquippedItem().getUnlocalizedName())) {
                float nes = TauStuff.itemStamina.get(event.entityPlayer.getCurrentEquippedItem().getUnlocalizedName());
                if (level < nes)
                    event.setCanceled(true);
                TauStuff.decreaseStaminaLevel(event.entityPlayer, nes);

            } else if (event.entityPlayer.getCurrentEquippedItem() == null && TauStuff.itemStamina.containsKey(EMPTY_HAND_PSEUDOUNLOCALIZED_NAME)) {
                float nes = TauStuff.itemStamina.get(EMPTY_HAND_PSEUDOUNLOCALIZED_NAME);
                if (level < nes)
                    event.setCanceled(true);
                TauStuff.decreaseStaminaLevel(event.entityPlayer, nes);

            }

        }
    }

    @SubscribeEvent
    public void onUseTick(PlayerUseItemEvent.Tick event) {
        if (!event.entityPlayer.worldObj.isRemote && isPullable(event.entityPlayer.getCurrentEquippedItem())) {
            float level = TauStuff.getStaminaLevel(event.entityPlayer);
            if (level == 0)
                event.setCanceled(true);
        }
    }

    public static boolean isPullable(ItemStack stack) {
        return stack != null && (
                stack.getUnlocalizedName().contains("npcSlingshot")
                        || stack.getUnlocalizedName().contains("item.bow")
                        || (stack.getUnlocalizedName().contains("crossbow") && stack.getItemUseAction() == EnumAction.block)
                        || (stack.getUnlocalizedName().contains("blowgun") && stack.getItemUseAction() == EnumAction.bow)
                        || stack.getUnlocalizedName().contains("boomerang"));
    }

}
