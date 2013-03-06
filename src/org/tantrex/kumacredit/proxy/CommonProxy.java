package org.tantrex.kumacredit.proxy;

import org.tantrex.kumacredit.cmd.CommandCredits;
import org.tantrex.kumacredit.cmd.CommandShop;
import org.tantrex.kumacredit.credit.CreditManager;
import org.tantrex.kumacredit.shop.ShopManager;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommonProxy {

	private ShopManager shopManager;
	private CreditManager creditManager;
	
	public void load() {
		shopManager = new ShopManager();
		creditManager = new CreditManager();
		
		//TODO: Create commands for /shop and /credits
		
		//registerCmds();
	}
	
	public void registerCmds(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandShop());
		event.registerServerCommand(new CommandCredits());
	}
	
	public boolean isOp(EntityPlayerMP player) {
		return MinecraftServer.getServer().getConfigurationManager().getOps().contains(player.username.toLowerCase().trim());
	}

	public ShopManager getShopManager() {
		return shopManager;
	}

	public CreditManager getCreditManager() {
		return creditManager;
	}
	
}
