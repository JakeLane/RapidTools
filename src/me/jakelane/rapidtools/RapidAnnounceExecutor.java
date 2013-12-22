package me.jakelane.rapidtools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RapidAnnounceExecutor implements CommandExecutor {

	private RapidTools plugin;

	public RapidAnnounceExecutor(RapidTools plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Spawn command
		if (cmd.getName().equalsIgnoreCase("aup")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				// Define player
				Player player = (Player) sender;
				// Check if player has permissions
				if (player.hasPermission("rapidtools.aup")) {
					RapidAnnounceUpdater r1 = new RapidAnnounceUpdater(plugin);
					Thread t1 = new Thread(r1);
					t1.start();
					Bukkit.broadcastMessage(ChatColor.YELLOW + "Announcement list updated.");
				}
			}
		}
		return true;
	}
}
