package ru.leoltron.tau;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;
import ru.leoltron.tau.blocks.CampfireFurnaceBlock;
import ru.leoltron.tau.blocks.CampfireFurnaceItemBlock;
import ru.leoltron.tau.client.eventhandler.KeyHandler;
import ru.leoltron.tau.client.gui.GuiKillMessage;
import ru.leoltron.tau.command.AddItemStaminaCommand;
import ru.leoltron.tau.command.ClearRecipeMemoryCommand;
import ru.leoltron.tau.command.GetRecipeMemoryCommand;
import ru.leoltron.tau.command.ItemInfoCommand;
import ru.leoltron.tau.common.ProxyCommon;
import ru.leoltron.tau.items.CactusSword;
import ru.leoltron.tau.items.CustomPotionItem;
import ru.leoltron.tau.items.PaperSword;
import ru.leoltron.tau.items.RecipeScrollItem;
import ru.leoltron.tau.packet.PacketDispatcher;
import ru.leoltron.tau.potion.CustomPotion;
import ru.leoltron.tau.recipe.AlchemistCraftingManager;
import ru.leoltron.tau.tileentity.TileEntityBlazeRodCampfire;
import ru.leoltron.tau.tileentity.TileEntityCampfireFurnace;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@Mod(modid = ModInfo.modId, name = ModInfo.name, version = ModInfo.version, dependencies = "required-after:customnpcs;after:randomadditions")
public class TauStuff {
    public List<String> craftBlackList;

    @SideOnly(Side.CLIENT)
    public static boolean showDurability;
    @SideOnly(Side.CLIENT)
    public static boolean itemsLight;
    @SideOnly(Side.CLIENT)
    public static List<GuiKillMessage> killLog;
    @SideOnly(Side.CLIENT)
    public static boolean showKillLog;

    public static HashMap<String, Float> itemStamina;

    public static float maxStamina;
    public static float sprintPerTickStamina;
    public static float jumpStamina;
    public static float itemDefaultStamina;

    public static float sprintStaminaRequirement;
    public static float jumpStaminaRequirement;

    public static int staminaRecoverDelay;
    public static float recoverPerTickStamina;

    public static final String STAMINA_LEVEL = "staminaLevel";
    public static final String STAMINA_RECOVER_DELAY = "staminaRecoverDelay";
    public static final int STAMINA_LEVEL_DATAWATCHER_ID = 25;

    public static final int guiIDCampfireFurnace = 0;
    public static final int guiIDAlchemistCrafting = 1;

    public static AlchemistCraftingManager recipes;

    public static Potion staminaRecoveryBoostPotion;
    public static Potion lightLegsPotion;
    public static Potion pureVisionPotion;
    public static Potion combatRagePotion;

    public static Item mortarAndPestleItem;
    public static Item recipeScrollItem;
    public static Item cactusSwordItem;
    public static Item paperSwordItem;

    public static Block CampfireFurnace;


    public static Item StaminaRecoverBoostPotionItem;
    public static Item lightLegsPotionItem;
    public static Item pureVisionPotionItem;
    public static Item swallowPotionItem;
    public static Item blizzardPotionItem;
    public static Item whiteRaffardsDecoctionPotionItem;
    public static Item catPotionItem;
    public static Item orcaPotionItem;
    public static Item thunderboltPotionItem;

    /**
     * Returns player stamina level, if exists, else creates a new one.
     *
     * @return Stamina level of player
     */
    public static float getStaminaLevel(EntityPlayer player) {
        float level;
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            level = player.getDataWatcher().getWatchableObjectFloat(STAMINA_LEVEL_DATAWATCHER_ID);
            player.getEntityData().setFloat(STAMINA_LEVEL, level);
        } else if (!player.getEntityData().hasKey(STAMINA_LEVEL)) {
            player.getEntityData().setFloat(STAMINA_LEVEL, maxStamina);
            level = maxStamina;
            player.getDataWatcher().updateObject(STAMINA_LEVEL_DATAWATCHER_ID, level);
        } else
            level = player.getEntityData().getFloat(STAMINA_LEVEL);
        return level;
    }

    /**
     * Increases player stamina level and update player's DataWatcher value of it.
     */
    public static void increaseStaminaLevel(EntityPlayer player, float inc) {
        if (player.getEntityData().getInteger(STAMINA_RECOVER_DELAY) == 0) {
            float level = getStaminaLevel(player);
            float incF = inc;
            if (player.isPotionActive(staminaRecoveryBoostPotion))
                incF *= 1 + (player.getActivePotionEffect(staminaRecoveryBoostPotion).getAmplifier() + 1) * 0.5;
            level = Math.min(maxStamina, level + incF);
            player.getEntityData().setFloat(STAMINA_LEVEL, level);
            player.getDataWatcher().updateObject(STAMINA_LEVEL_DATAWATCHER_ID, level);
        }

    }

    /**
     * Decreases player stamina level and update player's DataWatcher value of it.
     */
    public static void decreaseStaminaLevel(EntityPlayer player, float amount) {
        if (!player.capabilities.isCreativeMode) {
            float level = getStaminaLevel(player);
            level = Math.max(0, level - amount);
            player.getEntityData().setFloat(STAMINA_LEVEL, level);
            player.getDataWatcher().updateObject(STAMINA_LEVEL_DATAWATCHER_ID, level);
            player.getEntityData().setInteger(STAMINA_RECOVER_DELAY, staminaRecoverDelay);
        }
    }

    @Instance("TauStuff")
    public static TauStuff instance;
    private Logger logger;
    public Configuration config;

    @SideOnly(Side.CLIENT)
    public KeyHandler keyHandler;

    public void error(String message) {
        logger.error(message);
    }

    public void info(String message) {
        logger.info(message);
    }

    @SidedProxy(clientSide = "ru.leoltron.tau.client.ProxyClient", serverSide = "ru.leoltron.tau.common.ProxyCommon")
    public static ProxyCommon proxy = new ProxyCommon();
    public static PacketDispatcher net = new PacketDispatcher();

    private static class ItemStaminaValue {

        public String getName() {
            return name;
        }

        public float getValue() {
            return value;
        }

        private String name;
        private float value;

        public ItemStaminaValue(String name, float value) {
            this.name = name;
            this.value = value;
        }

        static final char separator = '|';

        /**
         * Transforms string to ItemStaminaValue object, using pattern "itemUnlocalizedName|staminaValue"
         *
         * @return If string is correct, ItemStaminaValue object, null otherwise.
         */
        static ItemStaminaValue parseValue(String val) throws NumberFormatException {
            int ind = val.indexOf(separator);
            if (ind > 0 && ind < val.length() - 1)
                return new ItemStaminaValue(val.substring(0, ind), Float.parseFloat(val.substring(ind + 1)));
            return null;
        }
    }

    public static CreativeTabs tauStuffCreativeTab = new CreativeTabs("tauStuffCreativeTab") {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return StaminaRecoverBoostPotionItem;
        }
    };

    @EventHandler
    public void preLoad(FMLPreInitializationEvent event) {
        instance = this;

        logger = event.getModLog();
        config = new Configuration(event.getSuggestedConfigurationFile());

        maxStamina = config.getFloat("maxStamina", "stamina", 100f, 0, Float.MAX_VALUE,
                "Maximum stamina level player can have.");
        recoverPerTickStamina = config.getFloat("recoverPerTickStamina", "stamina", 1f, 0, Float.MAX_VALUE,
                "Stamina amount that player recover per tick.");

        sprintPerTickStamina = config.getFloat("sprintPerTickStamina", "stamina", 0.5f, 0, Float.MAX_VALUE,
                "Stamina consumpition per tick when player is sprinting.");
        jumpStamina = config.getFloat("jumpStamina", "stamina", 1f, 0, Float.MAX_VALUE,
                "Stamina consumpition for jump.");

        sprintStaminaRequirement = config.getFloat("sprintStaminaRequirement", "stamina", 10f, 0, Float.MAX_VALUE,
                "Minimum stamina level required for sprint.");
        jumpStaminaRequirement = config.getFloat("jumpStaminaRequirement", "stamina", 10f, 0, Float.MAX_VALUE,
                "Minimum stamina level required for jump.");

        itemDefaultStamina = config.getFloat("attackDefaultStamina", "stamina", 1f, 0, Float.MAX_VALUE,
                "Stamina consumpition for attack using non-described items.");

        staminaRecoverDelay = config.getInt("staminaRecoverDelay", "stamina", 20, 0, Integer.MAX_VALUE,
                "Stamina recover delay in ticks (1/20 sec) after any stamina-requirable action.");

        String[] vals = config.getStringList("attackStamina", "stamina",
                new String[]{"item.example.unlocalizedName|10"},
                "Stamina consumpition for attack using particular items (for bows - per tick)");

        ItemStaminaValue isv;

        itemStamina = new HashMap<String, Float>();
        for (String val : vals) {
            isv = ItemStaminaValue.parseValue(val);
            if (isv != null)
                itemStamina.put(isv.name, isv.value);
        }

        proxy.loadProxyConfig(config);

        saveStaminaChanges();

        Potion[] potionTypes;

        for (Field f : Potion.class.getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a")) {
                    Field modfield = Field.class.getDeclaredField("modifiers");
                    modfield.setAccessible(true);
                    modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

                    potionTypes = (Potion[]) f.get(null);
                    final Potion[] newPotionTypes = new Potion[256];
                    System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
                    f.set(null, newPotionTypes);
                }
            } catch (Exception e) {
                // System.err.println("Severe error, please report this to the
                // mod author:");
                error(e.getMessage());
            }
        }
        // ref end

        PacketDispatcher.registerPackets();

        int ind = 32;

        staminaRecoveryBoostPotion = new CustomPotion(ind++, false, 0x8B33AB).setIconIndex(0, 0).setPotionName("potion.staminaRecoveryBoost");
        lightLegsPotion = new CustomPotion(ind++, false, 5362628).setIconIndex(2, 1).setPotionName("potion.lightLegs");
        pureVisionPotion = new CustomPotion(ind++, false, 11591910, new ResourceLocation(ModInfo.modId, "textures/gui/potion/pureVision.png")).setPotionName("potion.pureVision");
        combatRagePotion = new CustomPotion(ind++, false, 16262179, new ResourceLocation(ModInfo.modId, "textures/gui/potion/combatRage.png")).setPotionName("potion.combatRage");


        //Blocks
        CampfireFurnace = new CampfireFurnaceBlock();
        GameRegistry.registerBlock(CampfireFurnace, CampfireFurnaceItemBlock.class, "CampfireFurnace");

        GameRegistry.registerTileEntity(TileEntityCampfireFurnace.class, "CampfireFurnace");
        GameRegistry.registerTileEntity(TileEntityBlazeRodCampfire.class, "BlazeRodCampfire");

        StaminaRecoverBoostPotionItem = new CustomPotionItem(new PotionEffect(TauStuff.staminaRecoveryBoostPotion.getId(), 20 * 60 * 4, 0)).setUnlocalizedNameAndRegister("staminaRecoveryBoostPotion").setTextureName(ModInfo.modId + ":tawny_owl_potion_item");
        lightLegsPotionItem = new CustomPotionItem(new PotionEffect(TauStuff.lightLegsPotion.getId(), 20 * 60 * 4, 0)).setUnlocalizedNameAndRegister("lightLegsPotion").setTextureName(ModInfo.modId + ":light_legs_potion_item");
        pureVisionPotionItem = new CustomPotionItem(new PotionEffect(TauStuff.pureVisionPotion.getId(), 20 * 60 * 8, 0)).setUnlocalizedNameAndRegister("pureVisionPotion").setTextureName(ModInfo.modId + ":pure_vision_potion_item");
        swallowPotionItem = new CustomPotionItem(new PotionEffect(Potion.regeneration.getId(), 20 * 45, 0)).setUnlocalizedNameAndRegister("swallowPotion").setTextureName(ModInfo.modId + ":swallow_potion_item");
        blizzardPotionItem = new CustomPotionItem(new PotionEffect(Potion.moveSpeed.getId(), 20 * 60 * 3, 0)).setUnlocalizedNameAndRegister("blizzardPotion").setTextureName(ModInfo.modId + ":blizzard_potion_item");
        whiteRaffardsDecoctionPotionItem = new CustomPotionItem(new PotionEffect(Potion.heal.getId(), 0)).setUnlocalizedNameAndRegister("whiteRaffardsDecoctionPotion").setTextureName(ModInfo.modId + ":white_raffards_decoction_potion_item");
        catPotionItem = new CustomPotionItem(new PotionEffect(Potion.nightVision.getId(), 20 * 60 * 3, 0)).setUnlocalizedNameAndRegister("catPotion").setTextureName(ModInfo.modId + ":cat_potion_item");
        orcaPotionItem = new CustomPotionItem(new PotionEffect(Potion.waterBreathing.getId(), 20 * 60 * 8, 0)).setUnlocalizedNameAndRegister("orcaPotion").setTextureName(ModInfo.modId + ":orca_potion_item");
        thunderboltPotionItem = new CustomPotionItem(new PotionEffect(Potion.damageBoost.getId(), 20 * 60 * 3, 0)).setUnlocalizedNameAndRegister("thunderboltPotion").setTextureName(ModInfo.modId + ":thunderbolt_potion_item");

        recipeScrollItem = new RecipeScrollItem();

        mortarAndPestleItem = new Item().setCreativeTab(tauStuffCreativeTab).setMaxStackSize(1).setUnlocalizedName("mortarAndPestle").setTextureName(ModInfo.modId + ":mortarAndPestle");
        GameRegistry.registerItem(mortarAndPestleItem, mortarAndPestleItem.getUnlocalizedName());
        cactusSwordItem = new CactusSword();
        paperSwordItem = new PaperSword();

        GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(CampfireFurnace), 1, 0), "SSS", "S S", "SSS", 'S', Items.stick);
        GameRegistry.addRecipe(new ItemStack(Item.getItemFromBlock(CampfireFurnace), 1, 1), "SSS", "S S", "SSS", 'S', Items.blaze_rod);
        GameRegistry.addRecipe(new ItemStack(mortarAndPestleItem), "B", "B", "F", 'F', Items.flower_pot, 'B', Items.brick);
        GameRegistry.addShapelessRecipe(new ItemStack(Item.getItemFromBlock(Blocks.stonebrick), 4, 3), new ItemStack(Item.getItemFromBlock(Blocks.stonebrick)), new ItemStack(Item.getItemFromBlock(Blocks.stonebrick)), new ItemStack(Item.getItemFromBlock(Blocks.stonebrick)), new ItemStack(Item.getItemFromBlock(Blocks.stonebrick)));
        GameRegistry.addRecipe(new ItemStack(cactusSwordItem), "C", "C", "S", 'S', Items.stick, 'C', Item.getItemFromBlock(Blocks.cactus));
        GameRegistry.addRecipe(new ItemStack(paperSwordItem), "C", "C", "S", 'S', Items.stick, 'C', Items.paper);

    }

    @EventHandler
    public void LoadMod(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ProxyCommon());

        proxy.registerThings();

        initCraftBlackList();

        info("Removing recipes...");
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        Iterator<IRecipe> iterator = recipes.iterator();
        int recipesRemoved = 0;
        while (iterator.hasNext()) {
            ItemStack is = iterator.next().getRecipeOutput();
            if (is != null && (is.getItem() == Items.brewing_stand || isItemCraftBlackListed(is.getUnlocalizedName()))) {
                iterator.remove();
                recipesRemoved++;
            }
        }
        info("Removed " + recipesRemoved + (recipesRemoved == 1 ? " recipe." : " recipes."));

        TauStuff.recipes = new AlchemistCraftingManager();
    }

    private void initCraftBlackList() {
        craftBlackList = new ArrayList<String>();

        craftBlackList.add("item.ItemRA.battery1");
        craftBlackList.add("item.ItemRA.battery2");
        craftBlackList.add("item.ItemRA.battery3");

        craftBlackList.add("item.ItemRA.InputUpgrade1");
        craftBlackList.add("item.ItemRA.InputUpgrade2");
        craftBlackList.add("item.ItemRA.InputUpgrade3");

        craftBlackList.add("item.ItemRA.PowerUpgrade1");
        craftBlackList.add("item.ItemRA.PowerUpgrade2");
        craftBlackList.add("item.ItemRA.PowerUpgrade3");

        craftBlackList.add("item.ItemRA.SpeedUpgrade1");
        craftBlackList.add("item.ItemRA.SpeedUpgrade2");
        craftBlackList.add("item.ItemRA.SpeedUpgrade3");

        craftBlackList.add("item.ItemRA.InputUpgrade1");
        craftBlackList.add("item.ItemRA.InputUpgrade2");
        craftBlackList.add("item.ItemRA.InputUpgrade3");

        craftBlackList.add("item.ItemRA.StorageUpgrade1");
        craftBlackList.add("item.ItemRA.StorageUpgrade2");
        craftBlackList.add("item.ItemRA.StorageUpgrade3");

        craftBlackList.add("item.ItemRA.Battery3");
        craftBlackList.add("item.ItemRA.Battery4");
        craftBlackList.add("item.ItemRA.Battery5");
        craftBlackList.add("item.ItemRA.Battery6");
        craftBlackList.add("item.ItemRA.Battery7");

        craftBlackList.add("tile.BlockRAMachine.Lightning");
        craftBlackList.add("item.ItemRA.coil");
        craftBlackList.add("item.ItemRAIngot.obsidian");
        craftBlackList.add("tile.BlockRACable.Cable3");
        craftBlackList.add("tile.BlockRAMachine.CoolingPool");
        craftBlackList.add("tile.BlockRAProducer.HeatGenerator");
        craftBlackList.add("tile.BlockRAUpgrade.UpgradeTable");
        craftBlackList.add("tile.BlockRAUpgrade.CombineTable");
        craftBlackList.add("tile.BlockRAProducer.Creative");
        craftBlackList.add("tile.BlockRAUpgrade.RepairTable");
    }

    private boolean isItemCraftBlackListed(String unlocalizedName) {
        for (String un : craftBlackList)
            if (unlocalizedName.contains(un))
                return true;
        return false;
    }

    public void saveStaminaChanges() {
        config.get("stamina", "maxStamina", 100f).setValue(maxStamina);
        config.get("stamina", "recoverPerTickStamina", 1f).setValue(recoverPerTickStamina);
        config.get("stamina", "sprintPerTickStamina", 0.5f).setValue(sprintPerTickStamina);
        config.get("stamina", "jumpStamina", 1f).setValue(jumpStamina);
        config.get("stamina", "sprintStaminaRequirement", 10f).setValue(sprintStaminaRequirement);
        config.get("stamina", "jumpStaminaRequirement", 10f).setValue(jumpStaminaRequirement);
        config.get("stamina", "itemDefaultStamina", 1f).setValue(itemDefaultStamina);
        config.get("stamina", "staminaRecoverDelay", 20).setValue(staminaRecoverDelay);

        List<String> list = new ArrayList<String>();

        Set<String> set = itemStamina.keySet();
        for (String key : set) {
            list.add(key + ItemStaminaValue.separator + String.valueOf(itemStamina.get(key)));
        }
        config.get("stamina", "attackStamina", new String[]{"item.example.unlocalizedName|10"})
                .set(list.toArray(new String[0]));

        config.save();
    }

    @SideOnly(Side.CLIENT)
    public void saveOptions() {
        config.get("gui", "showDurability", true).set(showDurability);
        config.get("world", "itemsLight", true).set(itemsLight);
        config.get("gui", "showKillLog", true).set(showKillLog);

        config.save();
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new AddItemStaminaCommand());
        event.registerServerCommand(new ItemInfoCommand());
        event.registerServerCommand(new ClearRecipeMemoryCommand());
        event.registerServerCommand(new GetRecipeMemoryCommand());
    }

}
