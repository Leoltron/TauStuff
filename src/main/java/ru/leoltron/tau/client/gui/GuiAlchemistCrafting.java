package ru.leoltron.tau.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.leoltron.tau.ModInfo;
import ru.leoltron.tau.container.AlchemistCampfireContainer;
import ru.leoltron.tau.recipe.AlchemistCraftingManager;
import ru.leoltron.tau.recipe.AlchemistCraftingManager.AlchemistCraftingRecipe;
import ru.leoltron.tau.tileentity.TileEntityCampfireFurnace;

import java.util.ArrayList;
import java.util.List;

public class GuiAlchemistCrafting extends GuiContainer {
    private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation(ModInfo.modId, "textures/gui/alchemist_campfire_gui.png");

    public TileEntityCampfireFurnace furnace;

    private int currentRecipeID = 0;

    private AlchemistCraftingManager recipesInstance;
    private List<AlchemistCraftingRecipe> playerRecipes;

    public GuiAlchemistCrafting(EntityPlayer player, TileEntityCampfireFurnace entity) {
        super(new AlchemistCampfireContainer(player, entity));

        recipesInstance = AlchemistCraftingManager.getInstance();

        furnace = entity;
        xSize = 176;
        ySize = 180; //166

        playerRecipes = new ArrayList<AlchemistCraftingRecipe>();

        AlchemistCraftingManager.updateRecipeTag(player);
        NBTTagCompound tag = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag(AlchemistCraftingManager.RECIPES_TAG);
        for (int i = 0; i < recipesInstance.getRecipeCount(); i++)
            if (tag.getBoolean(String.valueOf(i)))
                playerRecipes.add(recipesInstance.getRecipeById(i));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        int size = 15;

        if (!playerRecipes.isEmpty()) {
            GuiButton left = new GuiButton(0, guiLeft + 6, guiTop + 5, size, 20, EnumChatFormatting.BOLD + "<");
            left.enabled = playerRecipes.size() != 1;
            buttonList.add(left);

            GuiButton right = new GuiButton(1, guiLeft + xSize - 6 - size, guiTop + 5, size, 20, EnumChatFormatting.BOLD + ">");
            right.enabled = playerRecipes.size() != 1;
            buttonList.add(right);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                if (currentRecipeID == 0)
                    currentRecipeID = playerRecipes.size() - 1;
                else
                    currentRecipeID--;
                break;
            case 1:
                if (currentRecipeID == playerRecipes.size() - 1)
                    currentRecipeID = 0;
                else
                    currentRecipeID++;
                break;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_) {
        if (fontRendererObj.getStringWidth(I18n.format("container.inventory")) <= 45)
            fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);

        final int maxNameSize = 134;

        if (!playerRecipes.isEmpty()) {
            String name = fontRendererObj.trimStringToWidth(I18n.format(playerRecipes.get(currentRecipeID).getRecipeOutput().getDisplayName()), maxNameSize);

            if (name.length() > 0)
                fontRendererObj.drawString(name, (xSize - fontRendererObj.getStringWidth(name)) / 2, 8, 4210752);
        }

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int x, int y) {
        mc.getTextureManager().bindTexture(craftingTableGuiTextures);
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (!playerRecipes.isEmpty()) {
            AlchemistCraftingRecipe currentRecipe = playerRecipes.get(currentRecipeID);

            for (int i = 0; i < currentRecipe.getRecipeSize(); i++)
                itemRender.renderItemIntoGUI(fontRendererObj, mc.renderEngine, currentRecipe.getRecipeInput()[i], guiLeft + 53 + 54 * (i % 2), guiTop + 22 + 54 * (i / 2));

            itemRender.renderItemIntoGUI(fontRendererObj, mc.renderEngine, currentRecipe.getRecipeOutput(), guiLeft + 80, guiTop + 49);
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        zLevel += 50;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        mc.getTextureManager().bindTexture(craftingTableGuiTextures);
        this.drawTexturedModalRect(k + 53, l + 22, 53, 22, 72, 72);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i1 = furnace.getBurnTimeRemainingScaled(13) + 1;
        this.drawTexturedModalRect(k + 81, l + 9 - i1 + 17 + 14, 176, 13 - i1, 25, i1 + 1);
        GL11.glDisable(GL11.GL_BLEND);


    }
}
