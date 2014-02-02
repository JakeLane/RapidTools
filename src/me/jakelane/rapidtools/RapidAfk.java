package me.jakelane.rapidtools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

public class RapidAfk implements CommandExecutor, Listener {

	private RapidTools plugin;

	public RapidAfk(RapidTools plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Ping command
		if (cmd.getName().equalsIgnoreCase("afk")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				// Define player
				Player player = (Player) sender;
				// Check if player has permissions
				if (player.hasPermission("rapidtools.afk")) {
					Boolean afk = player.getMetadata("afk").get(0).asBoolean();
					if (!afk) {
						String playerListName = ChatColor.GRAY + player.getDisplayName();
						if(playerListName.length() > 15)
							playerListName = playerListName.substring(0, 15);
						player.setPlayerListName(playerListName);
						player.setMetadata("afk", new FixedMetadataValue(plugin, Boolean.valueOf(true)));
						Bukkit.broadcastMessage(player.getDisplayName() + " is now AFK.");
					}
					if (afk) {
						player.setPlayerListName(player.getDisplayName());
						player.setMetadata("afk", new FixedMetadataValue(plugin, Boolean.valueOf(false)));
						Bukkit.broadcastMessage(player.getDisplayName() + " is no longer AFK.");
					}
				}
			}
			return true;
		}
		return false;
	}
}
