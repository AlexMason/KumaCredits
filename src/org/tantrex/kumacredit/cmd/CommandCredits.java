package org.tantrex.kumacredit.cmd;

import org.tantrex.kumacredit.KumaCredit;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandCredits extends CommandBase {

	@Override
	public String getCommandName() {
		
		return "credits";
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
	public void processCommand(ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayerMP) {
			boolean isOp = KumaCredit.proxy.isOp((EntityPlayerMP) sender);
			
			if (args.length == 0) {
				if (isOp) {
					sender.sendChatToPlayer("Credits: "+KumaCredit.proxy.getCreditManager().getCredits(((EntityPlayerMP) sender).username));
					sender.sendChatToPlayer("Commands: /credits [add|set|remove]");
				} else {
					sender.sendChatToPlayer("Credits: "+KumaCredit.proxy.getCreditManager().getCredits(((EntityPlayerMP) sender).username));
				}
			} else if (args.length == 1) {
				if (isOp) {
					if (args[0].equalsIgnoreCase("add")) {
						sender.sendChatToPlayer("Syntax: /credits add <name> <amount>");
					} else if (args[0].equalsIgnoreCase("set")) {
						sender.sendChatToPlayer("Syntax: /credits set <name> <amount>");
					} else if (args[0].equalsIgnoreCase("remove")) {
						sender.sendChatToPlayer("Syntax: /credits remove <name> <amount>");
					}
				}
			} else if (args.length == 3) {
				if (isOp) {
					if (args[0].equalsIgnoreCase("add")) {
						//sender.sendChatToPlayer("Syntax: /credits add <name> <amount>");
						if (isInteger(args[2])) {
							KumaCredit.proxy.getCreditManager().addCredits(args[1], Integer.parseInt(args[2]));
							sender.sendChatToPlayer(args[1] + " now has " + KumaCredit.proxy.getCreditManager().getCredits(args[1]) + " credits.");
						} else {
							sender.sendChatToPlayer("The amount must be a number.");
						}
					} else if (args[0].equalsIgnoreCase("set")) {
						if (isInteger(args[2])) {
							KumaCredit.proxy.getCreditManager().setCredits(args[1], Integer.parseInt(args[2]));
							sender.sendChatToPlayer(args[1] + " now has " + KumaCredit.proxy.getCreditManager().getCredits(args[1]) + " credits.");
						} else {
							sender.sendChatToPlayer("The amount must be a number.");
						}
					} else if (args[0].equalsIgnoreCase("remove")) {
						if (isInteger(args[2])) {
							if (!KumaCredit.proxy.getCreditManager().removeCredits(args[1], Integer.parseInt(args[2]))) {
								sender.sendChatToPlayer("Player cannot have a negative credit balance.");
							} else {
								sender.sendChatToPlayer(args[1] + " now has " + KumaCredit.proxy.getCreditManager().getCredits(args[1]) + " credits.");
							}
						} else {
							sender.sendChatToPlayer("The amount must be a number.");
						}
					}
				}
			}
		}
	}
	
	public boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c <= '/' || c >= ':') {
				return false;
			}
		}
		return true;
	}

}
