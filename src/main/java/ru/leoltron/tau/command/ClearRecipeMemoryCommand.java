package ru.leoltron.tau.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import ru.leoltron.tau.packet.PacketDispatcher;
import ru.leoltron.tau.packet.UpdateAlchemistKnowlegeMessage;
import ru.leoltron.tau.recipe.AlchemistCraftingManager;

public class ClearRecipeMemoryCommand implements ICommand {

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "clearRecipeMemory";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/clearRecipeMemory <nickname>";
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
			AlchemistCraftingManager.forgetRecipes(entityPlayer);
			PacketDispatcher.sendTo(new UpdateAlchemistKnowlegeMessage(entityPlayer), (EntityPlayerMP) entityPlayer);
			sender.addChatMessage(new ChatComponentTranslation("msg.recipeMemoryCleared"));
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
