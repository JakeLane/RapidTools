package me.jakelane.rapidtools;

import java.util.TimeZone;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RapidTimezone implements CommandExecutor {

	private RapidTools plugin;

	public RapidTimezone(RapidTools plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Spawn command
		if (cmd.getName().equalsIgnoreCase("timezone")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				// Define player
				Player player = (Player) sender;
				// Set spawn if player has permissions
				if (player.hasPermission("rapidtools.timezone")) {
					// If args are used
					if (args.length == 1) {
						String playerTimezone = args[0];
						String[] validIDs = TimeZone.getAvailableIDs();
						for (String str : validIDs) {
							if (str != null && str.equals(playerTimezone)) {
								String playerTimezonePath = player.getName() + ".Timezone";
								plugin.getConfig().set(playerTimezonePath, playerTimezone);
								plugin.saveConfig();
								sender.sendMessage("Timezone set to " + playerTimezone);
								return true;
							}
						}
						return false;
					}
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
