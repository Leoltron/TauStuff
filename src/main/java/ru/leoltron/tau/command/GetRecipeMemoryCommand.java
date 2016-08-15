package ru.leoltron.tau.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import ru.leoltron.tau.recipe.AlchemistCraftingManager;

public class GetRecipeMemoryCommand implements ICommand {

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "getRecipeMemory";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/getRecipeMemory <nickname>";
	}

	@Override
	public List getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length == 0){
			sender.addChatMessage(new ChatComponentText(this.getCommandUsage(sender)));
			return;
		}
		
		EntityPlayer entityPlayer = findPlayerByName(args[0]);
		if(entityPlayer != null){
			final IChatComponent divider =new ChatComponentText("----------------------------------------");
			divider.getChatStyle().setColor(EnumChatFormatting.DARK_AQUA);
			sender.addChatMessage(divider);
			IChatComponent nickname = new ChatComponentTranslation("msg.recipesOfPlayer");
			nickname.appendText(" "+args[0]);
			sender.addChatMessage(nickname);
			sender.addChatMessage(divider);
			AlchemistCraftingManager alchemist = AlchemistCraftingManager.getInstance();
			AlchemistCraftingManager.updateRecipeTag(entityPlayer);
			NBTTagCompound tag = entityPlayer.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).getCompoundTag(AlchemistCraftingManager.RECIPES_TAG);
			for(int i=0; i < alchemist.getRecipeCount();i++){
				boolean isLearned = tag.getBoolean(String.valueOf(i));
				IChatComponent chatComponent = new ChatComponentTranslation(alchemist.getRecipeById(i).getRecipeOutput().getDisplayName());
				chatComponent.appendText(" - ");
				chatComponent.getChatStyle().setColor(EnumChatFormatting.YELLOW);
				IChatComponent learnedCC = new ChatComponentTranslation(isLearned ? "item.desc.learned" : "item.desc.not_learned");
				learnedCC.getChatStyle().setColor(isLearned ? EnumChatFormatting.GREEN : EnumChatFormatting.RED);
				chatComponent.appendSibling(learnedCC);
				sender.addChatMessage(chatComponent);				
			}
			sender.addChatMessage(divider);
		}else{
			IChatComponent c = new ChatComponentTranslation("msg.noPlayerName");
			c.appendText(" "+args[0]);
			c.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(c);
		}		
	}
	
	private EntityPlayer findPlayerByName(String name){
		EntityPlayer player = null;
		List<EntityPlayer> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		for(EntityPlayer pl : players)
			if(pl.getDisplayName().equals(name))
				player = pl;
		return player;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length >= 1 ? CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
    }

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int ind) {
		return ind == 0;
	}

}
