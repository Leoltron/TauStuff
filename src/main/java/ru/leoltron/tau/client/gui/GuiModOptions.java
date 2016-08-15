package ru.leoltron.tau.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import ru.leoltron.tau.TauStuff;

public class GuiModOptions extends GuiScreen {
	
	private final GuiScreen prevScreen;
	
	public GuiModOptions(GuiScreen prevScreen){
		this.prevScreen = prevScreen;
	}
	
	@Override
	public void initGui()
    {
        buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done")));
        
        buttonList.add(new GuiButton(201, this.width / 2 - 155, this.height / 12 * 2 - 12,310,20, I18n.format("options.taustuff.showDurability") + ": "+
        (TauStuff.showDurability ? I18n.format("options.on") : I18n.format("options.off"))));
        buttonList.add(new GuiButton(202, this.width / 2 - 155, this.height / 12 * 3 - 12,310,20, I18n.format("options.taustuff.itemsLight") + ": "+
                (TauStuff.itemsLight ? I18n.format("options.on") : I18n.format("options.off"))));
        buttonList.add(new GuiButton(203, this.width / 2 - 155, this.height / 12 * 4 - 12,310,20, I18n.format("options.taustuff.showKillLog") + ": "+
                (TauStuff.showKillLog ? I18n.format("options.on") : I18n.format("options.off"))));
    }
	
	 @Override
	 protected void actionPerformed(GuiButton button)
	    {
	        if (button.enabled){	
	        	switch(button.id){
	        	case 200:
	            	TauStuff.instance.saveOptions();
	                this.mc.displayGuiScreen(prevScreen);  
	                break;
	            case 201:
	            	TauStuff.showDurability = !TauStuff.showDurability;
	            	button.displayString = I18n.format("options.taustuff.showDurability") + ": "+
	            	        (TauStuff.showDurability ? I18n.format("options.on") : I18n.format("options.off"));
	            break;
	            case 202:
	            	TauStuff.itemsLight = !TauStuff.itemsLight;
	            	button.displayString = I18n.format("options.taustuff.itemsLight") + ": "+
	            	        (TauStuff.itemsLight ? I18n.format("options.on") : I18n.format("options.off"));
	            break;
	            case 203:
	            	TauStuff.showKillLog = !TauStuff.showKillLog;
	            	button.displayString = I18n.format("options.taustuff.showKillLog") + ": "+
	            	        (TauStuff.showKillLog ? I18n.format("options.on") : I18n.format("options.off"));
	            break;
	        	}
	        }	        
	    }
	 
	 @Override
	 public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	    {
	        this.drawDefaultBackground();
	        this.drawCenteredString(this.fontRendererObj, I18n.format("options.taustuff.title"), this.width / 2, 15, 16777215);
	        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	    }

}
