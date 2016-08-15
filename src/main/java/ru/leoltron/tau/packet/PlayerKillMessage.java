package ru.leoltron.tau.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.leoltron.tau.TauStuff;
import ru.leoltron.tau.client.gui.GuiKillMessage;

import java.nio.charset.Charset;

public class PlayerKillMessage implements IMessage {

	private String killerName;
	private String killedName;

	private ItemStack weapon;
	private String customWeaponName;

	public PlayerKillMessage() {
		killerName = "";
		killedName = "";
		customWeaponName = "";

		weapon = null;
	}

	public PlayerKillMessage(EntityPlayer playerKiller, EntityPlayer playerKilled, ItemStack weapon) {
		this(playerKiller.getDisplayName(), playerKilled.getDisplayName(), weapon, weapon != null && weapon.hasDisplayName() ? weapon.getDisplayName() : "");
	}


	public PlayerKillMessage(String killerName, String killedName, ItemStack weapon, String customWeaponName) {
		this.killerName = killerName;
		this.killedName = killedName;
		this.weapon = weapon;
		this.customWeaponName = customWeaponName;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		int killerNameBytesLength = buf.readInt();
		byte[] killerNameBytes = new byte[killerNameBytesLength];
		buf.readBytes(killerNameBytes);
		killerName = new String(killerNameBytes, Charset.forName("UTF-8"));

		int killedNameBytesLength = buf.readInt();
		byte[] killedNameBytes = new byte[killedNameBytesLength];
		buf.readBytes(killedNameBytes);
		killedName = new String(killedNameBytes, Charset.forName("UTF-8"));

		boolean notNull = buf.readBoolean();
		if (notNull) {
			int id = buf.readInt();
			int damage = buf.readInt();
			weapon = new ItemStack(Item.getItemById(id), 1, damage);

			boolean haveCustomName = buf.readBoolean();
			if (haveCustomName) {
				int customWeaponNameBytesLength = buf.readInt();
				byte[] customWeaponNameBytes = new byte[customWeaponNameBytesLength];
				buf.readBytes(customWeaponNameBytes);
				customWeaponName = new String(customWeaponNameBytes, Charset.forName("UTF-8"));
			}
		} else
			weapon = null;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		byte[] killerNameBytes = killerName.getBytes(Charset.forName("UTF-8"));
		int killerNameBytesLength = killerNameBytes.length;

		byte[] killedNameBytes = killedName.getBytes(Charset.forName("UTF-8"));
		int killedNameBytesLength = killedNameBytes.length;

		buf.writeInt(killerNameBytesLength);
		buf.writeBytes(killerNameBytes);

		buf.writeInt(killedNameBytesLength);
		buf.writeBytes(killedNameBytes);

		boolean notNull = this.weapon != null;
		buf.writeBoolean(notNull);

		if (notNull) {
			int id = Item.getIdFromItem(this.weapon.getItem());
			int damage = this.weapon.getItemDamage();
			buf.writeInt(id);
			buf.writeInt(damage);

			buf.writeBoolean(haveCustomWeaponName());
			if (haveCustomWeaponName()) {
				byte[] customWeaponNameBytes = customWeaponName.getBytes(Charset.forName("UTF-8"));
				int customWeaponNameBytesLength = customWeaponNameBytes.length;

				buf.writeInt(customWeaponNameBytesLength);
				buf.writeBytes(customWeaponNameBytes);
			}
		}
	}


	public String getKillerName() {
		return killerName;
	}

	public String getKilledName() {
		return killedName;
	}

	public ItemStack getWeapon() {
		return weapon;
	}

	public String getCustomWeaponName() {
		return customWeaponName;
	}

	public boolean haveCustomWeaponName() {
		return customWeaponName.length() > 0;
	}


	public static class Handler extends AbstractClientMessageHandler<PlayerKillMessage> {
		@Override
		public IMessage onMessage(PlayerKillMessage message, MessageContext ctx) {
			if (ctx.side.isClient()) {
				return handleClientMessage(TauStuff.proxy.getPlayerEntity(ctx), message, ctx);
             } else {
                 return handleServerMessage(TauStuff.proxy.getPlayerEntity(ctx), message, ctx);
             }
         }

        @Override
         public IMessage handleClientMessage(EntityPlayer player_, PlayerKillMessage message, MessageContext ctx)
         {
            TauStuff.killLog.add(new GuiKillMessage(message.getKillerName(),message.getKilledName(),message.getWeapon(),message.getCustomWeaponName()));
//			TauStuff.instance.info(message.getKillerName()+" killed "+message.getKilledName()+message.getWeapon() != null? (" with a "+message.getWeapon().getUnlocalizedName()) : "");
            return null;
         }
     }

}
