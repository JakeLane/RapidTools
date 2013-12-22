package me.jakelane.rapidtools;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RapidPranksExecutor implements CommandExecutor {

	private RapidTools plugin;
	private static CommandSender sender;
	private static String[] args;

	public RapidPranksExecutor(RapidTools plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		RapidPranksExecutor.args = args;
		RapidPranksExecutor.sender = sender;
		// Spawn command
		if (cmd.getName().equalsIgnoreCase("pranks")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				// Define player
				Player player = (Player) sender;
				// Check if player has permissions
				if (player.hasPermission("rapidtools.pranks")) {
					if (RapidPranksExecutor.getArgs().length != 0
							&& (args[0].equals("enable") || args[0].equals("disable") || args[0].equals("list"))) {
						RapidPranks r1 = new RapidPranks(plugin);
						Thread t1 = new Thread(r1);
						t1.start();
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static CommandSender getSender() {
		return sender;
	}

	public static String[] getArgs() {
		return args;
	}
}
