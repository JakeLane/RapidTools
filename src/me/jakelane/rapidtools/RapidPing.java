package me.jakelane.rapidtools;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RapidPing implements CommandExecutor {

	public RapidPing(RapidTools plugin) {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Ping command
		if (cmd.getName().equalsIgnoreCase("ping")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				// Define player
				Player player = (Player) sender;
				// Check if player has permissions
				if (player.hasPermission("rapidtools.ping")) {
					player.sendMessage("Pong");
				}
			}
			return true;
		}
		return false;
	}
}
