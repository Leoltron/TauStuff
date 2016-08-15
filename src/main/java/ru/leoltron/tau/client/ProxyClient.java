package ru.leoltron.tau.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import ru.leoltron.tau.TauStuff;
import ru.leoltron.tau.client.eventhandler.ClientTickHandler;
import ru.leoltron.tau.client.eventhandler.GuiEventHandler;
import ru.leoltron.tau.client.eventhandler.KeyHandler;
import ru.leoltron.tau.client.eventhandler.PlayerLightTickHandler;
import ru.leoltron.tau.client.gui.GuiKillMessage;
import ru.leoltron.tau.client.gui.IngameGuiOverlay;
import ru.leoltron.tau.common.ProxyCommon;
import ru.leoltron.tau.common.eventhandler.AnvilUpdateHandler;
import ru.leoltron.tau.render.CustomTorchRender;
import ru.leoltron.tau.render.FurnaceCampfireRenderer;
import ru.leoltron.tau.render.ItemRendererFurnaceCampfireBlock;
import ru.leoltron.tau.render.SemiTransparentRenderer;
import ru.leoltron.tau.tileentity.TileEntityBlazeRodCampfire;
import ru.leoltron.tau.tileentity.TileEntityCampfireFurnace;

import java.util.ArrayList;

public class ProxyClient extends ProxyCommon {

    @Override
    public void registerThings() {
        super.registerThings();

        FurnaceCampfireRenderer rendererFurnaceCampfire = new FurnaceCampfireRenderer();
        IItemRenderer semiTransparentItemRenderer = new SemiTransparentRenderer();

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCampfireFurnace.class, rendererFurnaceCampfire);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlazeRodCampfire.class, rendererFurnaceCampfire);

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TauStuff.CampfireFurnace), new ItemRendererFurnaceCampfireBlock(rendererFurnaceCampfire, new TileEntityCampfireFurnace()));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(Blocks.torch), new CustomTorchRender());
        MinecraftForgeClient.registerItemRenderer(TauStuff.StaminaRecoverBoostPotionItem, semiTransparentItemRenderer);
        MinecraftForgeClient.registerItemRenderer(TauStuff.swallowPotionItem, semiTransparentItemRenderer);
        MinecraftForgeClient.registerItemRenderer(TauStuff.blizzardPotionItem, semiTransparentItemRenderer);
        MinecraftForgeClient.registerItemRenderer(TauStuff.whiteRaffardsDecoctionPotionItem, semiTransparentItemRenderer);
        MinecraftForgeClient.registerItemRenderer(TauStuff.catPotionItem, semiTransparentItemRenderer);
        MinecraftForgeClient.registerItemRenderer(TauStuff.orcaPotionItem, semiTransparentItemRenderer);
        MinecraftForgeClient.registerItemRenderer(TauStuff.thunderboltPotionItem, semiTransparentItemRenderer);

        MinecraftForge.EVENT_BUS.register(new GuiEventHandler());
        MinecraftForge.EVENT_BUS.register(new IngameGuiOverlay(Minecraft.getMinecraft()));
        MinecraftForge.EVENT_BUS.register(new AnvilUpdateHandler());

        TauStuff.instance.keyHandler = new KeyHandler();
        FMLCommonHandler.instance().bus().register(TauStuff.instance.keyHandler);
        FMLCommonHandler.instance().bus().register(new PlayerLightTickHandler());
        FMLCommonHandler.instance().bus().register(new ClientTickHandler());

        TauStuff.killLog = new ArrayList<GuiKillMessage>();
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
    }

    @Override
    public EntityPlayer getMinecraftPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public void loadProxyConfig(Configuration config) {
        TauStuff.showDurability = config.getBoolean("showDurability", "gui", true, "If true, shows durability of the equipped items.");
        TauStuff.itemsLight = config.getBoolean("itemsLight", "world", true, "If true, some items lights when in player's hand or on ground.");
        TauStuff.showKillLog = config.getBoolean("showKillLog", "gui", true, "If true, shows player-player kill log");
    }

}
