package me.jakelane.rapidtools;

import java.net.InetAddress;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RapidPing implements CommandExecutor {

	private RapidTools plugin;

	public RapidPing(RapidTools plugin) {
		this.plugin = plugin;
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
					String host = Bukkit.getServer().getIp();
					try {
						long curTime = System.currentTimeMillis();
						boolean status = InetAddress.getByName(host).isReachable(2500);
						long elapsed = System.currentTimeMillis() - curTime - 1000L;
						if (elapsed <= 0L) elapsed = 0L;
						if (status) {
							sender.sendMessage("Your ping is: " + elapsed + "ms.");
						} else {
							sender.sendMessage("Could not contact server!");
						}
					} catch (Exception ex) {
						plugin.getLogger().info("Error: " + ex);
					}
				}
			}
			return true;
		}
		return false;
	}
}
