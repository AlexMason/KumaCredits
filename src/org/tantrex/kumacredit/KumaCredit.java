package org.tantrex.kumacredit;

import org.tantrex.kumacredit.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = "kumacredit", name="KumaCredits", version="1.0.1")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class KumaCredit {

	@Instance
	public static KumaCredit instance;
	
	@SidedProxy(clientSide = "org.tantrex.kumacredit.proxy.ClientProxy", serverSide = "org.tantrex.kumacredit.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@Init
	public void load(FMLInitializationEvent event) {
		proxy.load();
	}
	
	@ServerStarting
	public void serverStarting(FMLServerStartingEvent event) {
		proxy.registerCmds(event);
	}
	
	public CommonProxy getProxy() {
		return this.instance.proxy;
	}
	
}
