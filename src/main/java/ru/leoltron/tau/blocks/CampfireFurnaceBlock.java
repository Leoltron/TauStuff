package ru.leoltron.tau.blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import ru.leoltron.tau.ModInfo;
import ru.leoltron.tau.TauStuff;
import ru.leoltron.tau.tileentity.TileEntityBlazeRodCampfire;
import ru.leoltron.tau.tileentity.TileEntityCampfireFurnace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CampfireFurnaceBlock extends BlockContainer {

    private static boolean isUpdating;
    private final Random rand = new Random();

    public static final String[] subblocks = {"wood", "blaze"};

    public CampfireFurnaceBlock() {
        super(Material.grass);
        this.setStepSound(soundTypeWood);
        this.setBlockTextureName(ModInfo.modId + ":campfire_icon");
        this.setHardness(0.7f);
        this.setCreativeTab(TauStuff.tauStuffCreativeTab);
        this.setLightLevel(0.9375F / 2);
        this.setBlockName("campfireFurnace");
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
        return Item.getItemFromBlock(TauStuff.CampfireFurnace);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
        return Item.getItemFromBlock(TauStuff.CampfireFurnace);
    }

    @Override
    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float HitY, float hitZ) {
        if (!world.isRemote) {
//			if(player instanceof EntityPlayerMP)
//				PacketDispatcher.sendTo(new UpdateAlchemistKnowlegeMessage(player), (EntityPlayerMP) player);
            boolean haveMortar = player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem().equals(TauStuff.mortarAndPestleItem);
            int meta = world.getBlockMetadata(x, y, z);
            switch (meta) {
                case 0:
                    FMLNetworkHandler.openGui(player, TauStuff.instance, haveMortar ? TauStuff.guiIDAlchemistCrafting : TauStuff.guiIDCampfireFurnace, world, x, y, z);
                    break;
                case 1:
                    if (haveMortar)
                        FMLNetworkHandler.openGui(player, TauStuff.instance, TauStuff.guiIDAlchemistCrafting, world, x, y, z);
                    break;
            }
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {
        for (int i = 0; i < 2; i++) {
            subItems.add(new ItemStack(Item.getItemFromBlock(this), 1, i));
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (!isUpdating && meta != 1) {
            TileEntityCampfireFurnace tileentityfurnace = (TileEntityCampfireFurnace) world.getTileEntity(x, y, z);

            if (tileentityfurnace != null) {
                for (int i1 = 0; i1 < tileentityfurnace.getSizeInventory(); ++i1) {
                    ItemStack itemstack = tileentityfurnace.getStackInSlot(i1);

                    if (itemstack != null) {
                        float f = this.rand.nextFloat() * 0.8F + 0.1F;
                        float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                        float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                        while (itemstack.stackSize > 0) {
                            int j1 = this.rand.nextInt(21) + 10;

                            if (j1 > itemstack.stackSize) {
                                j1 = itemstack.stackSize;
                            }

                            itemstack.stackSize -= j1;
                            EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                            if (itemstack.hasTagCompound()) {
                                entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                            }

                            float f3 = 0.05F;
                            entityitem.motionX = (double) ((float) this.rand.nextGaussian() * f3);
                            entityitem.motionY = (double) ((float) this.rand.nextGaussian() * f3 + 0.2F);
                            entityitem.motionZ = (double) ((float) this.rand.nextGaussian() * f3);
                            world.spawnEntityInWorld(entityitem);
                        }
                    }
                }

                world.func_147453_f(x, y, z, block);
            }
        }

        world.removeTileEntity(x, y, z);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        switch (meta) {
            case 0:
                return new TileEntityCampfireFurnace();
            case 1:
                return new TileEntityBlazeRodCampfire();
        }

        return null;

    }

    @Override
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        float f = (float) x + 0.5F;
        float f1 = (float) y + 0.6F + rand.nextFloat() * 6.0F / 16.0F;
        float f2 = (float) z + 0.5F;
        world.spawnParticle("smoke", (double) (f), (double) f1, (double) (f2), 0.0D, 0.0D, 0.0D);
        world.spawnParticle("flame", (double) (f), (double) f1, (double) (f2), 0.0D, 0.0D, 0.0D);
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#canPlaceBlockAt(net.minecraft.world.World, int, int, int)
     */
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return !world.getBlock(x, y - 1, z).equals(Blocks.air);//world.getBlock(x, y, z).equals(Blocks.air) ? !world.getBlock(x, y-1, z).equals(Blocks.air) && !world.getBlock(x, y-1, z).isFlammable(world, x, y-1, z, ForgeDirection.UP) && world.getBlock(x, y-1, z).isOpaqueCube()  : false;
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#onEntityCollidedWithBlock(net.minecraft.world.World, int, int, int, net.minecraft.entity.Entity)
     */
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (world.getBlockMetadata(x, y, z) != 1)
            entity.setFire(5);
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#canBlockStay(net.minecraft.world.World, int, int, int)
     */
    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return !world.getBlock(x, y - 1, z).equals(Blocks.air); //? world.getBlock(x, y-1, z).isFlammable(world, x, y-1, z, ForgeDirection.UP) : false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox((double) x + this.minX + offsetXZ, (double) y + this.minY, (double) z + this.minZ + offsetXZ, (double) x + this.maxX - offsetXZ, (double) y + this.maxY + offsetYMax, (double) z + this.maxZ - offsetXZ);
    }

    private final double offsetXZ = 0.17;
    private final double offsetYMax = -0.3;

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox((double) x + this.minX + offsetXZ, (double) y + this.minY, (double) z + this.minZ + offsetXZ, (double) x + this.maxX - offsetXZ, (double) y + this.maxY + offsetYMax, (double) z + this.maxZ - offsetXZ);
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#damageDropped(int)
     */
    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#getDrops(net.minecraft.world.World, int, int, int, int, int)
     */
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();
        switch (metadata) {
            case 1:
                list.addAll(super.getDrops(world, x, y, z, metadata, fortune));
        }
        return list;
    }
}
