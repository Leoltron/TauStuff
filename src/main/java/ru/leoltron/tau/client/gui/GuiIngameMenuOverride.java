package ru.leoltron.tau.client.gui;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.PotionEffect;
import ru.leoltron.tau.TauStuff;

import java.net.URI;
import java.util.Iterator;

public class GuiIngameMenuOverride extends GuiIngameMenu {

    @Override
    public void initGui() {
        super.initGui();
        if (!this.mc.isIntegratedServerRunning()) {
            PotionEffect effect = this.mc.thePlayer.getActivePotionEffect(TauStuff.combatRagePotion);
            if (effect != null) {
                GuiButton disconnectButton = (GuiButton) this.buttonList.get(0);
                int ALETimeout = effect.getDuration() / 20;
                disconnectButton.displayString = I18n.format("menu.disconnect") + " (" + getTimeFormatted(ALETimeout) + ")";
                disconnectButton.enabled = false;
            }
        }

        Iterator<GuiButton> it = this.buttonList.iterator();
        while (it.hasNext()) {
            GuiButton button = it.next();
            switch (button.id) {
                case 7:
                    button.displayString = I18n.format("gui.stats");
                    button.enabled = true;
                    break;
                case 5:
                    button.displayString = I18n.format("gui.menu.website");
                    break;
                case 6:
                    button.displayString = I18n.format("gui.menu.forum");
                    break;
                case 0:
                    button.width = 200;
                    break;
                case 12:
                    it.remove();
                    break;
            }

        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
                button.enabled = false;
                TauStuff.killLog.clear();
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);
                this.mc.displayGuiScreen(new GuiMainMenu());
            case 2:
            case 3:
            default:
                break;
            case 4:
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                break;
            case 5:
                try {
                    Class oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null);
                    oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new URI(GuiMainMenuOverride.websiteUrl));
                } catch (Throwable throwable) {
                    GuiMainMenuOverride.logger.error("Couldn\'t open link", throwable);
                }
                break;
            case 6:
                try {
                    Class oclass = Class.forName("java.awt.Desktop");
                    Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null);
                    oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new URI(GuiMainMenuOverride.websiteUrl + "forum/index.php"));
                } catch (Throwable throwable) {
                    GuiMainMenuOverride.logger.error("Couldn\'t open link", throwable);
                }
                break;
            case 7:
                if (this.mc.thePlayer != null)
                    this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
                break;
            case 12:
                FMLClientHandler.instance().showInGameModOptions(this);
                break;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!this.mc.isIntegratedServerRunning()) {
            PotionEffect effect = this.mc.thePlayer.getActivePotionEffect(TauStuff.combatRagePotion);

            GuiButton disconnectButton = (GuiButton) this.buttonList.get(0);
            if (effect != null) {
                int ALETimeout = effect.getDuration() / 20;
                disconnectButton.displayString = I18n.format("menu.disconnect") + " (" + getTimeFormatted(ALETimeout) + ")";
                disconnectButton.enabled = false;
            } else if (!disconnectButton.enabled) {
                disconnectButton.displayString = I18n.format("menu.disconnect");
                disconnectButton.enabled = true;
            }
        }
    }

    private String getTimeFormatted(long timeSeconds) {
        String s = "";
        if (timeSeconds / 3600 >= 1) {
            s = s + ((int) (timeSeconds / 3600)) + ":";
        }

        if (timeSeconds / 60 >= 1) {
            s = s + ((int) (timeSeconds % 3600 / 60)) + ":";
        }

        s = s + timeSeconds % 60;

        return s;
    }
}
