package me.jakelane.rapidtools;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class RapidFireworkArrowsExecutor implements CommandExecutor {

	private RapidTools plugin;

	public RapidFireworkArrowsExecutor(RapidTools plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Spawn command
		if (cmd.getName().equalsIgnoreCase("fwa")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				// Define player
				Player player = (Player) sender;
				// Check if player has permissions
				if (player.hasPermission("rapidtools.fwa")) {
					if (args.length == 1 && args[0].equalsIgnoreCase("enable")) {
						// Get the configuration
						Boolean playerFireworkArrows = player.getMetadata("FireworkArrows").get(0).asBoolean();
						if (!playerFireworkArrows) {
							// Set the metadata
							player.setMetadata("FireworkArrows", new FixedMetadataValue(plugin, "true"));
							// Set the configuration
							plugin.getConfig().set(player.getDisplayName() + ".FireworkArrows", true);
							plugin.saveConfig();
							sender.sendMessage(ChatColor.YELLOW + "Firework Arrows enabled.");
						} else {
							sender.sendMessage(ChatColor.YELLOW + "Firework Arrows are already enabled.");
						}
					} else if (args.length == 1 && args[0].equalsIgnoreCase("disable")) {
						// Get the configuration
						Boolean playerFireworkArrows = player.getMetadata("FireworkArrows").get(0).asBoolean();
						if (playerFireworkArrows) {
							// Set the metadata
							player.setMetadata("FireworkArrows", new FixedMetadataValue(plugin, "false"));
							// Set the config
							plugin.getConfig().set(player.getDisplayName() + ".FireworkArrows", false);
							plugin.saveConfig();
							sender.sendMessage(ChatColor.YELLOW + "Firework Arrows disabled.");
						} else {
							sender.sendMessage(ChatColor.YELLOW + "Firework Arrows are already disabled.");
						}
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
}
