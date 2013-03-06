package org.tantrex.kumacredit.credit;

import java.io.File;

import net.minecraftforge.common.Configuration;

public class CreditManager {

	private String creditsFile = "./config/KumaCredits/credits.dat";
	private Configuration creditsConfig;
	private String creditsCatogory = "players";
	private int defaultCredits = 0;
	
	public CreditManager() {
		//TODO: Setup default credit amount config.
		load();
	}
	
	public void load() {
		creditsConfig = new Configuration(new File(creditsFile));
		
		creditsConfig.load();
		creditsConfig.save();
	}
	
	public int getCredits(String name) {
		name = name.toLowerCase();
		
		creditsConfig.load();
		int credits = creditsConfig.get(creditsCatogory, name, defaultCredits).getInt();
		creditsConfig.save();
		
		return credits;
	}
	
	public void setCredits(String name, int amount) {
		name = name.toLowerCase();
		
		creditsConfig.load();
		creditsConfig.get(creditsCatogory, name, defaultCredits).value = ""+amount;
		creditsConfig.save();
		
	}
	
	public void addCredits(String name, int amount) {
		name = name.toLowerCase();
		
		creditsConfig.load();
		int current = creditsConfig.get(creditsCatogory, name, 0).getInt();
		creditsConfig.get(creditsCatogory, name, defaultCredits).value = ""+(current+amount);
		creditsConfig.save();
	}
	
	public boolean removeCredits(String name, int amount) {
		name = name.toLowerCase();
		
		creditsConfig.load();
		int current = creditsConfig.get(creditsCatogory, name, defaultCredits).getInt();
		
		if (amount > current) {
			return false;
		}
	
		creditsConfig.get(creditsCatogory, name, defaultCredits).value = ""+(current-amount);
		creditsConfig.save();
		return true;
	}
	
}
