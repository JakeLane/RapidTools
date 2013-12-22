package me.jakelane.rapidtools;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RapidEventsExecutor implements CommandExecutor {

	private RapidTools plugin;
	private static CommandSender sender;

	public RapidEventsExecutor(RapidTools plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		RapidEventsExecutor.sender = sender;
		// Spawn command
		if (cmd.getName().equalsIgnoreCase("events")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				// Define player
				Player player = (Player) sender;
				// Check if player has permissions
				if (player.hasPermission("rapidtools.events")) {
					RapidEvents r1 = new RapidEvents(plugin);
					Thread t1 = new Thread(r1);
					t1.start();
				}
				return true;
			}
			return true;
		}
		return false;
	}

	public static CommandSender getSender() {
		return sender;
	}
}
