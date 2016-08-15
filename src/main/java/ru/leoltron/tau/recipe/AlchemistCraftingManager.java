package ru.leoltron.tau.recipe;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import ru.leoltron.tau.TauStuff;
import ru.leoltron.tau.packet.PacketDispatcher;
import ru.leoltron.tau.packet.UpdateAlchemistKnowlegeMessage;

import java.util.ArrayList;
import java.util.List;

public class AlchemistCraftingManager {

    public static class AlchemistCraftingRecipe implements IRecipe {

        private ItemStack[] slotsCrafting;
        private ItemStack output;
        private String name;

        public AlchemistCraftingRecipe(String name, ItemStack output, ItemStack... input) {
            this.name = name;

            slotsCrafting = new ItemStack[4];

            for (int i = 0; i < slotsCrafting.length; i++)
                if (input.length <= i)
                    slotsCrafting[i] = null;
                else
                    slotsCrafting[i] = input[i];

            this.output = output;

        }

        @Override
        public boolean matches(InventoryCrafting inv, World world) {

            if (inv.getSizeInventory() != slotsCrafting.length)
                return false;

            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack is = inv.getStackInSlot(i);
                if ((slotsCrafting[i] != null && is == null)) {
                    return false;
                }
                if ((slotsCrafting[i] == null && is != null)) {
                    return false;
                }
                if (is != null && slotsCrafting[i] != null && !is.isItemEqual(slotsCrafting[i])) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public ItemStack getCraftingResult(InventoryCrafting ic) {
            return output.copy();
        }

        @Override
        public int getRecipeSize() {
            return slotsCrafting.length;
        }

        public ItemStack[] getRecipeInput() {
            return slotsCrafting.clone();
        }

        @Override
        public ItemStack getRecipeOutput() {
            return output;
        }

        public String getName() {
            return name;
        }

    }

    private static AlchemistCraftingManager instance;

    private List<AlchemistCraftingRecipe> recipes;

    public AlchemistCraftingManager() {
        instance = this;
        recipes = new ArrayList<AlchemistCraftingRecipe>();

        registerRecipes();
    }

    public void registerRecipe(AlchemistCraftingRecipe recipe) {
        recipes.add(recipe);
    }

    private void registerRecipes() {
        ItemStack awkwardPotionIS = new ItemStack(Items.potionitem, 1, 16);

        registerRecipe(new AlchemistCraftingRecipe("swallowPotion", new ItemStack(TauStuff.swallowPotionItem), new ItemStack(GameRegistry.findItem("customnpcs", "npcBrokenOrb"), 1, 12), new ItemStack(Items.potionitem), new ItemStack(Blocks.yellow_flower), new ItemStack(GameRegistry.findItem("customnpcs", "npcSkull"))));
        registerRecipe(new AlchemistCraftingRecipe("blizzardPotion", new ItemStack(TauStuff.blizzardPotionItem), new ItemStack(Blocks.red_mushroom), new ItemStack(Items.potionitem), new ItemStack(Blocks.red_flower, 1, 6), new ItemStack(GameRegistry.findItem("customnpcs", "npcFireElement"))));
        registerRecipe(new AlchemistCraftingRecipe("whiteRaffardsDecoctionPotion", new ItemStack(TauStuff.whiteRaffardsDecoctionPotionItem), new ItemStack(GameRegistry.findItem("customnpcs", "npcBrokenOrb"), 1, 14), new ItemStack(Items.potionitem), new ItemStack(Blocks.waterlily), new ItemStack(GameRegistry.findItem("customnpcs", "npcHeart"))));
        registerRecipe(new AlchemistCraftingRecipe("catPotion", new ItemStack(TauStuff.catPotionItem), new ItemStack(Blocks.red_flower, 1, 3), new ItemStack(Items.potionitem), new ItemStack(Items.dye, 1, 3), new ItemStack(GameRegistry.findItem("customnpcs", "npcWaterElement"))));
        registerRecipe(new AlchemistCraftingRecipe("orcaPotion", new ItemStack(TauStuff.orcaPotionItem), new ItemStack(Items.nether_wart), new ItemStack(Items.potionitem), new ItemStack(Blocks.red_flower, 1, 1), new ItemStack(GameRegistry.findItem("customnpcs", "npcSeveredEar"))));
        registerRecipe(new AlchemistCraftingRecipe("thunderboltPotion", new ItemStack(TauStuff.thunderboltPotionItem), new ItemStack(Items.ghast_tear), new ItemStack(Items.potionitem), new ItemStack(Blocks.brown_mushroom), new ItemStack(GameRegistry.findItem("customnpcs", "npcBag"))));
        registerRecipe(new AlchemistCraftingRecipe("awkwardPotion", awkwardPotionIS, new ItemStack(Items.wheat), new ItemStack(Items.wheat), new ItemStack(Items.potionitem), new ItemStack(Blocks.red_mushroom)));
        registerRecipe(new AlchemistCraftingRecipe("staminaRecoverBoostPotion", new ItemStack(TauStuff.StaminaRecoverBoostPotionItem), awkwardPotionIS, new ItemStack(Blocks.red_flower, 1, 3), new ItemStack(GameRegistry.findItem("customnpcs", "npcNatureSpell")), new ItemStack(Items.wheat_seeds)));
        registerRecipe(new AlchemistCraftingRecipe("pureVisionPotion", new ItemStack(TauStuff.pureVisionPotionItem), awkwardPotionIS, new ItemStack(Items.ghast_tear), new ItemStack(Blocks.double_plant, 1, 5), new ItemStack(GameRegistry.findItem("customnpcs", "npcSeveredEar"))));
        registerRecipe(new AlchemistCraftingRecipe("lightLegsPotion", new ItemStack(TauStuff.lightLegsPotionItem), awkwardPotionIS, new ItemStack(Blocks.red_mushroom), new ItemStack(Blocks.red_flower), new ItemStack(Blocks.double_plant, 1, 1)));


    }

    public ItemStack findMatchingRecipe(InventoryCrafting ic, World world, EntityPlayer player) {
        for (int i = 0; i < recipes.size(); i++)
            if (recipes.get(i).matches(ic, world) && isRecipeAvailable(player, i)) {
                return recipes.get(i).getCraftingResult(ic);
            }

        return null;
    }

    public static AlchemistCraftingManager getInstance() {
        return instance;
    }

    public AlchemistCraftingRecipe getRecipeById(int id) {
        return this.recipes.get(id);
    }

    public int getRecipeCount() {
        return this.recipes.size();
    }

    public static final String RECIPES_TAG = "learnedAlchemicRecipes";

    public static void updateRecipeTag(EntityPlayer player) {
        NBTTagCompound tag = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        if (tag.hasKey(RECIPES_TAG))
            return;
        NBTTagCompound recipeTags = new NBTTagCompound();
        tag.setTag(RECIPES_TAG, recipeTags);
        player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, tag);

    }

    public static boolean isRecipeAvailable(EntityPlayer player, int recipeid) {
        updateRecipeTag(player);
        NBTTagCompound tag = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag(RECIPES_TAG);
        return tag.getBoolean(String.valueOf(recipeid));
    }

    /**
     * @return false, if recipe is already learned
     */
    public static boolean learnRecipe(EntityPlayer player, int recipeid) {
        if (isRecipeAvailable(player, recipeid))
            return false;
        else {
            NBTTagCompound tag = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            NBTTagCompound recipeTags = tag.getCompoundTag(RECIPES_TAG);
            recipeTags.setBoolean(String.valueOf(recipeid), true);

            tag.setTag(RECIPES_TAG, recipeTags);
            player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, tag);

            if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
                PacketDispatcher.sendTo(new UpdateAlchemistKnowlegeMessage(player), (EntityPlayerMP) player);
        }
        return true;
    }

    public static void forgetRecipes(EntityPlayer player) {
        NBTTagCompound tag = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        NBTTagCompound recipeTags = new NBTTagCompound();
        tag.setTag(RECIPES_TAG, recipeTags);
        player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, tag);
    }

}
