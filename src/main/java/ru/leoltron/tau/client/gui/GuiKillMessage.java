package ru.leoltron.tau.client.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import ru.leoltron.tau.packet.PlayerKillMessage;

public class GuiKillMessage {
    /**
     * lifetime in ticks
     */
    public static final int DEFAULT_LIFETIME = 200;
    protected String killerName;
    protected String killedName;

    protected ItemStack weapon;
    protected String customWeaponName;

    protected int lifetime;

    protected GuiKillMessage(String killerName, String killedName, ItemStack weapon, String customWeaponName, int lifetime) {
        this.killerName = killerName;
        this.killedName = killedName;
        this.weapon = weapon;
        this.customWeaponName = customWeaponName;
        this.lifetime = lifetime;
    }

    public GuiKillMessage(String killerName, String killedName, ItemStack weapon, String customWeaponName) {
        this(killerName, killedName, weapon, customWeaponName, DEFAULT_LIFETIME);
    }

    public GuiKillMessage(PlayerKillMessage message) {
        this(message.getKillerName(), message.getKilledName(), message.getWeapon(), message.getCustomWeaponName());
    }

    public void update() {
        lifetime--;
    }

    public boolean isNeedToRemove() {
        return lifetime <= 0;
    }

    public String getKillerAndWeaponName() {
        String s = this.killerName;
        if (this.customWeaponName.length() > 0)
            s = s + " (" + EnumChatFormatting.ITALIC + this.customWeaponName + EnumChatFormatting.RESET + ")";
        return s;
    }

    public String getKillerName() {
        return killerName;
    }

    public String getKilledName() {
        return killedName;
    }

    public ItemStack getWeapon() {
        return weapon;
    }

    public String getCustomWeaponName() {
        return this.customWeaponName;
    }
}
