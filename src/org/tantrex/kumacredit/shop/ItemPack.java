package org.tantrex.kumacredit.shop;

import java.util.ArrayList;

public class ItemPack {

	private String name;
	private String[] items;
	private int cost;
	
	public ItemPack(String name, String[] items, int cost) {
		this.name = name;
		this.items = items;
		this.cost = cost;
	}
	
	public String getName() {
		return name;
	}

	public String[] getItems() {
		return items;
	}
	
	public int getCost() {
		return cost;
	}
	
}
