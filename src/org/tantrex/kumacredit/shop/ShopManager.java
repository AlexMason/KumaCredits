
package org.tantrex.kumacredit.shop;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import net.minecraftforge.common.Configuration;

public class ShopManager {

	private String itemPackDir = "./config/KumaCredits/itempacks/";
	
	private HashMap<String, ItemPack> itemPacks = new HashMap<String, ItemPack>();
	
	public ShopManager() {
		load();
	}
	
	public void load() {
		itemPacks = new HashMap<String, ItemPack>();
		File dir = new File(itemPackDir);
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		
		File[] files = dir.listFiles();
		if (files.length != 0 && files != null) {
			for (File file : files) {
				Configuration config = new Configuration(file);
				config.load();
				
				String name = config.get("pack", "name", "name").value;
				String[] items = config.get("pack", "items", new String[0]).valueList;
				int cost = config.get("pack", "cost", 0).getInt();
				
				itemPacks.put(name, new ItemPack(name, items, cost));
				
			}
		}
	}
	
	public boolean createPack(String name, String[] items, int cost) {
		name = name.toLowerCase();
		
		if (itemPacks.containsKey(name)) {
			return false;
		}
		
		itemPacks.put(name, new ItemPack(name, items, cost));
		
		Configuration config = new Configuration(new File(itemPackDir + name + ".cfg"));
		config.load();
		config.get("pack", "name", name);
		config.get("pack", "items", items);
		config.get("pack", "cost", cost);
		config.save();
		
		return true;
	}
	
	public boolean editPack(String name, String[] items, int cost) {
		name = name.toLowerCase();
		
		if (!itemPacks.containsKey(name)) {
			return false;
		}
		
		Configuration config = new Configuration(new File(itemPackDir + name + ".cfg"));
		config.load();
		config.get("pack", "name", name);
		config.get("pack", "items", items).valueList = items;
		config.get("pack", "cost", 0).value = ""+cost;
		config.save();
		
		return true;
	}
	
	public boolean setPackCost(String name, int cost) {
		name = name.toLowerCase();
		
		if (!itemPacks.containsKey(name)) {
			return false;
		}
		
		Configuration config = new Configuration(new File(itemPackDir + name + ".cfg"));
		config.load();
		config.get("pack", "cost", 0).value = ""+cost;
		config.save();
		
		return true;
	}
	
	public boolean removePack(String name) {
		name = name.toLowerCase();
		
		if (!itemPacks.containsKey(name)) {
			return false;
		}
		
		File f = new File(itemPackDir + name + ".cfg");
		if (!f.exists()) {
			return false;
		}
		if (!f.isFile()) {
			return false;
		}
		
		itemPacks.remove(name);
		
		return f.delete();
	}
	
	public boolean addItemToPack(String packname, String item, int amount) {
		packname = packname.toLowerCase();
		
		if (!itemPacks.containsKey(packname)) {
			return false;
		}
		
		Configuration config = new Configuration(new File(itemPackDir + packname + ".cfg"));
		config.load();
		
		String[] items = config.get("pack", "items", new String[0]).valueList;
		String[] newItems = new String[items.length+1];
		for (int i = 0; i<items.length; i++) {
			newItems[i+1] = items[i];
		}
		if (!item.contains(":")) {
			item = item + ":0";
		}
		
		newItems[0] = item + "-" + amount;
		
		config.get("pack", "items", new String[0]).valueList = newItems;
		config.save();
		
		itemPacks = null;
		load();
		
		return true;
	}
	
	public String getAllItemPacks() {
		Iterator it = itemPacks.values().iterator();
		
		String packs = "";
		int count = 0;
		
		while (it.hasNext()) {
			ItemPack ip = (ItemPack) it.next();
			
			if (count++ == 0) {
				packs = ip.getName();
			} else {
				packs = packs + ", " + ip.getName();
			}
		}
		
		return packs;
	}
	
	public HashMap getItemPacks() {
		return itemPacks;
	}

	public boolean createPack(String name, int cost) {
		return createPack(name, new String[0], cost);
	}
	
}
