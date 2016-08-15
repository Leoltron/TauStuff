package ru.leoltron.tau.command;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ItemInfoCommand implements ICommand {

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "getItemInfo";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/getItemInfo";
	}

	@Override
	public List getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] p_71515_2_) {
		if(!(sender instanceof EntityPlayer)){
			IChatComponent chatComponent = new ChatComponentTranslation("msg.addItemStamina.notPlayer");
			chatComponent.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(chatComponent);
			return;
		}
		
		EntityPlayer player = (EntityPlayer) sender;
		if(player.getCurrentEquippedItem() == null){
			IChatComponent chatComponent = new ChatComponentTranslation("msg.addItemStamina.nullItem");
			chatComponent.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(chatComponent);
			return;
		}
		
		player.addChatComponentMessage(new ChatComponentText(player.getCurrentEquippedItem().toString()+" tags:"+(player.getCurrentEquippedItem().hasTagCompound() ? player.getCurrentEquippedItem().stackTagCompound.toString():"none")));
		
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

}
