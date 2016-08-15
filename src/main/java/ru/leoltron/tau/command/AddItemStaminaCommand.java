package ru.leoltron.tau.command;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import ru.leoltron.tau.TauStuff;
import ru.leoltron.tau.common.eventhandler.ActionsEventHandler;

public class AddItemStaminaCommand implements ICommand {

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "addItemStamina";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/addItemStamina <stamina>";
	}

	@Override
	public List getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(!(sender instanceof EntityPlayer)){
			IChatComponent chatComponent = new ChatComponentTranslation("msg.addItemStamina.notPlayer");
			chatComponent.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(chatComponent);
			return;
		}
		
		if (args.length > 0)
        {
		
		EntityPlayer player = (EntityPlayer) sender;
//		if(player.getCurrentEquippedItem() == null){
//			IChatComponent chatComponent = new ChatComponentTranslation("msg.addItemStamina.nullItem");
//			chatComponent.getChatStyle().setColor(EnumChatFormatting.RED);
//			sender.addChatMessage(chatComponent);
//			return;
//		}	
		
		try{			
			float stamina = Float.parseFloat(args[0]);
			String name;
			if(player.getCurrentEquippedItem() == null)
				name = ActionsEventHandler.EMPTY_HAND_PSEUDOUNLOCALIZED_NAME;
			else
				name= player.getCurrentEquippedItem().getUnlocalizedName();
			TauStuff.itemStamina.put(name,stamina);
			IChatComponent chatComponent = new ChatComponentTranslation("msg.addItemStamina.success1");
			chatComponent.appendText(" "+name+" ");
			chatComponent.appendSibling(new ChatComponentTranslation("msg.addItemStamina.success2"));
			chatComponent.appendText(" "+stamina);
			chatComponent.getChatStyle().setColor(EnumChatFormatting.GREEN);
			sender.addChatMessage(chatComponent);
			TauStuff.instance.saveStaminaChanges();
		}catch(NumberFormatException e){
			IChatComponent chatComponent = new ChatComponentText("'"+args[0]+"'"+" is not a float number");
			chatComponent.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(chatComponent);
		}
        }else
        {
            throw new WrongUsageException(getCommandUsage(sender));
        }
	
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
