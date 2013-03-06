package org.tantrex.kumacredit.cmd;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.tantrex.kumacredit.KumaCredit;
import org.tantrex.kumacredit.shop.ItemPack;
import org.tantrex.kumacredit.util.FunctionHelper;
import org.tantrex.kumacredit.util.UnicodeColor;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class CommandShop extends CommandBase {

	@Override
	public String getCommandName() {
		return "shop";
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}
	
	@Override
	public void processCommand(ICommandSender sender , String[] args) {
		if (sender instanceof EntityPlayerMP) {
			
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("create")) {
					this.createPack(sender, FunctionHelper.objectToStringArray(FunctionHelper.trimArrayStart(1, args)));
				} else if (args[0].equalsIgnoreCase("setcost")) {
					this.setCostPack(sender, FunctionHelper.objectToStringArray(FunctionHelper.trimArrayStart(1, args)));
				} else if (args[0].equalsIgnoreCase("additem")) {
					this.addItem(sender, FunctionHelper.objectToStringArray(FunctionHelper.trimArrayStart(1, args)));
				} else if (args[0].equalsIgnoreCase("pack")) {
					this.getPack(sender, FunctionHelper.objectToStringArray(FunctionHelper.trimArrayStart(1, args)));
				} else if (args[0].equalsIgnoreCase("list")) {
					this.getList(sender);
				} else if (args[0].equalsIgnoreCase("remove")) {
					this.removePack(sender, FunctionHelper.objectToStringArray(FunctionHelper.trimArrayStart(1, args)));
				} else {
					if (KumaCredit.proxy.isOp((EntityPlayerMP) sender)) {
						sender.sendChatToPlayer(UnicodeColor.RED.toString() + "Syntax:" + UnicodeColor.RESET.toString() + " /shop [pack|list|create|additem|remove|setcost]");
					} else {
						sender.sendChatToPlayer(UnicodeColor.RED.toString() + "Syntax:" + UnicodeColor.RESET.toString() + " /shop [list|pack]");
					}
				}
			}
			
			if (args.length == 0) {
				if (KumaCredit.proxy.isOp((EntityPlayerMP) sender)) {
					sender.sendChatToPlayer(UnicodeColor.RED.toString() + "Syntax:" + UnicodeColor.RESET.toString() + " /shop [pack|list|create|additem|remove|setcost]");
				} else {
					sender.sendChatToPlayer(UnicodeColor.RED.toString() + "Syntax:" + UnicodeColor.RESET.toString() + " /shop [list|pack]");
				}
			}
		}
	}
	
	public void getList(ICommandSender sender) {
		sender.sendChatToPlayer("=====================================================");
		String[] itemPackList = new String[0];
		Collection c = KumaCredit.proxy.getShopManager().getItemPacks().values();
		Iterator it = c.iterator();
	
		while (it.hasNext()) {
			ItemPack itemp = (ItemPack) it.next();
			String[] tempList = new String[itemPackList.length + 1];
			
			for (int i = 0; i < itemPackList.length; i++) {
				tempList[i+1] = itemPackList[i];
			}
			
			tempList[0] = UnicodeColor.GREEN.toString() + itemp.getName() + " - " + itemp.getCost() + UnicodeColor.RESET.toString();
			
			itemPackList = tempList;
		}
		
		for (int i = 0; i < itemPackList.length; i++) {
			sender.sendChatToPlayer(itemPackList[i]);
		}
		sender.sendChatToPlayer("=====================================================");
	}
	
	public void getPack(ICommandSender sender, String[] args) {
		if (args.length == 1) {//shop pack name - display content
			ItemPack ip = (ItemPack) KumaCredit.proxy.getShopManager().getItemPacks().get(args[0]);
			if (ip != null) {
				sender.sendChatToPlayer("=====================================================");
				sender.sendChatToPlayer("Package Name: " + ip.getName());
				sender.sendChatToPlayer("Cost: " + ip.getCost() + " credit");
				sender.sendChatToPlayer("=====================================================");
				sender.sendChatToPlayer("Items Included:");
				
				ItemStack stack;
				String[] itemList = ip.getItems();
				String itemls = "";
				
				for (String item : itemList) {
					String[] itemData = item.split("-");
					String[] itemData2 = itemData[0].split(":");
					stack = new ItemStack(Integer.parseInt(itemData2[0]), 1, Integer.parseInt(itemData2[1]));
					String displayName = stack.getDisplayName();
					if (displayName.equals(null) || displayName.equals("")) {
						displayName = stack.getItemName();
					}
					if (displayName.equals(null) || displayName.equals("")) {
						displayName = "" + stack.itemID + stack.getItemDamage();
					}
					sender.sendChatToPlayer(itemData[1] + " x " + displayName);
				}
				
				sender.sendChatToPlayer("=====================================================");
				sender.sendChatToPlayer("You currently have "+KumaCredit.proxy.getCreditManager().getCredits(((EntityPlayerMP) sender).username)+" credits.");
				sender.sendChatToPlayer("Type \"/shop pack "+args[0]+" buy\" to purchase a package.");
			} else {
				sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "Error: Could not find a pack with the name of " + args[0] + ".");
			}
		} else if (args.length == 2) {// shop pack name buy
			ItemPack ip = (ItemPack) KumaCredit.proxy.getShopManager().getItemPacks().get(args[0]);
			if (ip != null) {
				if (args[1].equalsIgnoreCase("buy")) {
					int senderCredits = KumaCredit.proxy.getCreditManager().getCredits(((EntityPlayerMP)sender).username);
					if (KumaCredit.proxy.getCreditManager().removeCredits(((EntityPlayerMP)sender).username, ip.getCost())) {
						String[] itemList = ip.getItems();
						ItemStack stack;
						for (String item : itemList) {
							String[] itemData = item.split("-");
							String[] itemData2 = itemData[0].split(":");
							stack = new ItemStack(Integer.parseInt(itemData2[0]), Integer.parseInt(itemData[1]), Integer.parseInt(itemData2[1]));
							((EntityPlayerMP) sender).inventory.addItemStackToInventory(stack);
						}
						sender.sendChatToPlayer(UnicodeColor.GREEN.toString() + "You now have " + KumaCredit.proxy.getCreditManager().getCredits(((EntityPlayerMP)sender).username) + " credits remaining.");
					} else {
						sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "Error: You do not have enough credits to purchase " + args[0] + ".");
					}
				} else {
					sender.sendChatToPlayer(UnicodeColor.RED.toString() + "Syntax: " + UnicodeColor.RESET.toString() + "/shop pack <name> buy");
				}
			} else {
				sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "Error: Could not find a pack with the name of " + args[0] + ".");
			}
		} else {
			sender.sendChatToPlayer(UnicodeColor.RED.toString() + "Syntax: " + UnicodeColor.RESET.toString() + "/shop pack <name>");
		}
	}
	
	public void createPack(ICommandSender sender, String[] args) {
		if (KumaCredit.proxy.isOp((EntityPlayerMP) sender)) {
			if (args.length == 2) {
				if (FunctionHelper.isInteger(args[1])) {
					if (KumaCredit.proxy.getShopManager().createPack(args[0], Integer.parseInt(args[1]))) {
						sender.sendChatToPlayer(UnicodeColor.GREEN.toString() + "Pack " + args[0] + " was successfully created with a cost of " + args[1] + ".");
					} else {
						sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "Error: There is already a pack with the name of " + args[0] + ".");
					}
				} else {
					sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "Error: The cost must be an integer.");
				}
			} else {
				sender.sendChatToPlayer(UnicodeColor.RED.toString() + "Syntax: " + UnicodeColor.RESET.toString() + "/shop create <name> <cost>");
			}
		} else {
			sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "You do not have permission to use this command.");
		}
	}
	
	public void addItem(ICommandSender sender, String[] args) {
		if (KumaCredit.proxy.isOp((EntityPlayerMP) sender)) {
			if (args.length == 3) {
				if (FunctionHelper.isInteger(args[2])) {
					String[] itemData = args[1].split(":");
					boolean itemIsInteger = true;
					if (itemData.length > 1) {
						if (!FunctionHelper.isInteger(itemData[0]) || !FunctionHelper.isInteger(itemData[1])) {
							itemIsInteger = false;
						}
						
					} else {
						if (!FunctionHelper.isInteger(itemData[0])) {
							itemIsInteger = false;
						}
					}
					if (itemIsInteger) {
						if (KumaCredit.proxy.getShopManager().addItemToPack(args[0], args[1], Integer.parseInt(args[2]))) {
							ItemStack stack = null;
							String displayName = "";
							if (itemData.length == 1) {
								stack = new ItemStack(Integer.parseInt(itemData[0]), 1, 0);
								displayName = stack.getDisplayName();
								if (displayName.equals(null) || displayName.equals("")) {
									displayName = stack.getItemName();
								}
								if (displayName.equals(null) || displayName.equals("")) {
									displayName = "" + stack.itemID + stack.getItemDamage();
								}
							} else {
								stack = new ItemStack(Integer.parseInt(itemData[0]), 1, Integer.parseInt(itemData[1]));
								if (displayName.equals(null) || displayName.equals("")) {
									displayName = stack.getItemName();
								}
								if (displayName.equals(null) || displayName.equals("")) {
									displayName = "" + stack.itemID + stack.getItemDamage();
								}
							}
							sender.sendChatToPlayer(UnicodeColor.GREEN.toString() + "Item '" + displayName + "' was succesfully added to the pack "+args[0]+".");
						} else {
							sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "Error: Could not find a pack with the name of " + args[0] + ".");
						}
					} else {
						sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "Error: The item must be an integer or in the format of '17:3'.");
					}
				} else {
					sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "Error: The amount must be an integer.");
				}
			} else {
				sender.sendChatToPlayer(UnicodeColor.RED.toString() + "Syntax: " + UnicodeColor.RESET.toString() + "/shop additem <name> <itemId> <itemAmt>");
			}
		} else {
			sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "You do not have permission to use this command.");
		}
	}
	
	public void removePack(ICommandSender sender, String[] args) {
		if (KumaCredit.proxy.isOp((EntityPlayerMP) sender)) {
			if (args.length == 1) {
				if (KumaCredit.proxy.getShopManager().removePack(args[0])) {
					sender.sendChatToPlayer(UnicodeColor.GREEN.toString() + "Pack " + args[0] + " was succesfully removed.");
				} else {
					sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "Error: Could not find a pack with the name of " + args[0] + ".");
				}
			} else {
				sender.sendChatToPlayer(UnicodeColor.RED.toString() + "Syntax: " + UnicodeColor.RESET.toString() + "/shop remove <name>");
			}
		} else {
			sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "You do not have permission to use this command.");
		}
	}
	
	public void setCostPack(ICommandSender sender, String[] args) {
		if (KumaCredit.proxy.isOp((EntityPlayerMP) sender)) {
			if (args.length == 2) {
				if (FunctionHelper.isInteger(args[1])) {
					if (KumaCredit.proxy.getShopManager().setPackCost(args[0], Integer.parseInt(args[1]))) {
						sender.sendChatToPlayer(UnicodeColor.GREEN.toString() + "Pack " + args[0] + "'s cost was successfully changed to" + args[1] + ".");
					} else {
						sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "Error: Could not find a pack with the name of " + args[0] + ".");
					}
				} else {
					sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "Error: The cost must be an integer.");
				}
			} else {
				sender.sendChatToPlayer(UnicodeColor.RED.toString() + "Syntax: " + UnicodeColor.RESET.toString() + "/shop setcost <name> <cost>");
			}
		} else {
			sender.sendChatToPlayer(UnicodeColor.DARKRED.toString() + "You do not have permission to use this command.");
		}
	}
	
}
