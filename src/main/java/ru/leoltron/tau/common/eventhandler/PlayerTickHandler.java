package ru.leoltron.tau.common.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import ru.leoltron.tau.TauStuff;

public class PlayerTickHandler {

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START) && !event.player.worldObj.isRemote) {

            if (event.player.isPotionActive(Potion.blindness) && event.player.isPotionActive(TauStuff.pureVisionPotion))
                event.player.removePotionEffect(Potion.blindness.getId());

            int delay = event.player.getEntityData().getInteger(TauStuff.STAMINA_RECOVER_DELAY);

            if (delay > 0)
                event.player.getEntityData().setInteger(TauStuff.STAMINA_RECOVER_DELAY, delay - 1);
            else
                TauStuff.increaseStaminaLevel(event.player, TauStuff.recoverPerTickStamina);

            float level = TauStuff.getStaminaLevel(event.player);

            if (event.player.isUsingItem() && ActionsEventHandler.isPullable(event.player.getCurrentEquippedItem()))
                if (level > 0) {
                    float BOW_USING_STAMINA_PER_TICK = 1F;

                    level = Math.max(0, level - BOW_USING_STAMINA_PER_TICK);
                    TauStuff.decreaseStaminaLevel(event.player, BOW_USING_STAMINA_PER_TICK);
                }

            boolean sprint = event.player.isSprinting();
            boolean slowdown = false;
            if (level < TauStuff.sprintStaminaRequirement) {
                slowdown = true;
            } else {
                if (sprint) {
                    float dec = TauStuff.sprintPerTickStamina;
                    if (event.player.isPotionActive(TauStuff.lightLegsPotion))
                        dec *= 1 - ((event.player.getActivePotionEffect(TauStuff.lightLegsPotion).getAmplifier() + 1) * 0.25);
                    TauStuff.decreaseStaminaLevel(event.player, dec);
                    level -= dec;
                }
            }
            if (slowdown)
                event.player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 20, 1, true));

            if (level == 0)
                event.player.addPotionEffect(new PotionEffect(Potion.weakness.getId(), 20, Byte.MAX_VALUE, true));
        }
    }
}
