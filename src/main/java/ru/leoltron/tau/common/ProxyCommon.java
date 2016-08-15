package ru.leoltron.tau.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import ru.leoltron.tau.TauStuff;
import ru.leoltron.tau.client.gui.GuiAlchemistCrafting;
import ru.leoltron.tau.client.gui.GuiCampfireFurnace;
import ru.leoltron.tau.common.eventhandler.*;
import ru.leoltron.tau.container.AlchemistCampfireContainer;
import ru.leoltron.tau.container.ContainerCampfireFurnace;
import ru.leoltron.tau.tileentity.TileEntityCampfireFurnace;

public class ProxyCommon implements IGuiHandler {

    private class TFuelHandler implements IFuelHandler {
        @Override
        public int getBurnTime(ItemStack fuel) {
            if (fuel.getItem().equals(Item.getItemFromBlock(TauStuff.CampfireFurnace))) {
                switch (fuel.getItemDamage()) {
                    case 0:
                        return 5 * 20 * 8;
                    case 1:
                        return 120 * 20 * 8;
                }
            }
            return 0;
        }
    }

    public void registerThings() {
        MinecraftForge.EVENT_BUS.register(new ActionsEventHandler());
        MinecraftForge.EVENT_BUS.register(new AnvilUpdateHandler());
        FMLCommonHandler.instance().bus().register(new PlayerTickHandler());
        FMLCommonHandler.instance().bus().register(new PlayerLogInEventHandler());
        GameRegistry.registerFuelHandler(new TFuelHandler());
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == TauStuff.guiIDCampfireFurnace) {
            TileEntity entity = world.getTileEntity(x, y, z);
            if ((entity != null) && entity instanceof TileEntityCampfireFurnace)
                return new ContainerCampfireFurnace(player.inventory, (TileEntityCampfireFurnace) entity);
        } else if (ID == TauStuff.guiIDAlchemistCrafting) {
            TileEntity entity = world.getTileEntity(x, y, z);
            if ((entity != null) && entity instanceof TileEntityCampfireFurnace)
                return new AlchemistCampfireContainer(player, (TileEntityCampfireFurnace) entity);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == TauStuff.guiIDCampfireFurnace) {
            TileEntity entity = world.getTileEntity(x, y, z);
            if ((entity != null) && entity instanceof TileEntityCampfireFurnace)
                return new GuiCampfireFurnace(player.inventory, (TileEntityCampfireFurnace) entity);
        } else if (ID == TauStuff.guiIDAlchemistCrafting) {
            TileEntity entity = world.getTileEntity(x, y, z);
            if ((entity != null) && entity instanceof TileEntityCampfireFurnace)
                return new GuiAlchemistCrafting(player, (TileEntityCampfireFurnace) entity);
        }
        return null;
    }

    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }

    public EntityPlayer getMinecraftPlayer() {
        return null;
    }

    public void loadProxyConfig(Configuration config) {
    }

}
