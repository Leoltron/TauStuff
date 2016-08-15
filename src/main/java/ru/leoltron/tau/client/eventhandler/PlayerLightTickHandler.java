package ru.leoltron.tau.client.eventhandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import ru.leoltron.tau.TauStuff;

import java.util.ArrayList;
import java.util.List;

public class PlayerLightTickHandler {

    private ArrayList<Location> darkList = new ArrayList<Location>();

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerTickEvent(PlayerTickEvent event) {
        int rRange = Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16;
        if (event.phase.equals(TickEvent.Phase.START) && event.player.worldObj.isRemote) {

            ArrayList<Location> tempList = new ArrayList<Location>();
            if (TauStuff.itemsLight) {
                List entityList = event.player.worldObj.loadedEntityList;
                for (Object anEntityList : entityList)
                    if (anEntityList != null) {
                        Entity entity = (Entity) anEntityList;

                        if (event.player.getDistance(entity.posX, entity.posY, entity.posZ) > rRange)
                            continue;

                        if (entity instanceof EntityPlayer) {
                            EntityPlayer player = (EntityPlayer) entity;
                            if (!isItemStackLight(player.getCurrentEquippedItem()))
                                continue;
                        } else if (entity instanceof EntityItem) {
                            EntityItem entityItem = (EntityItem) entity;
                            if (!isItemStackLight(entityItem.getEntityItem()))
                                continue;
                        } else
                            continue;

                        int x = (int) entity.posX - 1;
                        int y = (int) entity.posY;
                        int z = (int) entity.posZ;
                        event.player.worldObj.setLightValue(EnumSkyBlock.Block, x, y, z, 15);
                        event.player.worldObj.markBlockRangeForRenderUpdate(x, y, z, 12, 12, 12);
                        event.player.worldObj.markBlockForUpdate(x, y, z);

                        event.player.worldObj.updateLightByType(EnumSkyBlock.Block, x + 1, y, z);
                        event.player.worldObj.updateLightByType(EnumSkyBlock.Block, x - 1, y, z);
                        event.player.worldObj.updateLightByType(EnumSkyBlock.Block, x, y + 1, z);
                        event.player.worldObj.updateLightByType(EnumSkyBlock.Block, x, y - 1, z);
                        event.player.worldObj.updateLightByType(EnumSkyBlock.Block, x, y, z + 1);
                        event.player.worldObj.updateLightByType(EnumSkyBlock.Block, x, y, z - 1);

                        tempList.add(new Location(entity.worldObj, x, y, z));

                    }

            }
            while (!darkList.isEmpty()) {
                if (darkList.get(0) != null) {
                    Location dl = darkList.get(0);
                    if (!tempList.contains(dl))
                        event.player.worldObj.updateLightByType(EnumSkyBlock.Block, dl.x, dl.y, dl.z);
                }
                darkList.remove(0);

            }
            darkList.addAll(tempList);
        }

    }

    private static boolean isLightItem(Item item) {
        return item != null && (item.equals(Items.lava_bucket) ||
                item.equals(Item.getItemFromBlock(Blocks.torch)) ||
                item.equals(Items.nether_star));
    }

    private static boolean isItemStackLight(ItemStack itemstack) {
        return itemstack != null && isLightItem(itemstack.getItem());
    }

    private class Location {
        public int x;
        public int y;
        public int z;
        public World world;

        public Location(World world, int x, int y, int z) {
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Location(World world, double posX, double posY, double posZ) {
            this(world, MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Location && this.equals((Location) obj);
        }

        boolean equals(Location loc) {
            return x == loc.x && y == loc.y && z == loc.z && this.world.equals(loc.world);
        }
    }
}
