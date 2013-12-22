package me.jakelane.rapidtools;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RapidSetSpawn implements CommandExecutor {

	@SuppressWarnings("unused")
	private RapidTools plugin;

	public RapidSetSpawn(RapidTools plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// SetSpawn command
		if (cmd.getName().equalsIgnoreCase("setspawn")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				// Define player
				Player player = (Player) sender;
				// Set spawn if player has permissions
				if (player.hasPermission("rapidtools.setspawn")) {
					World world = player.getWorld();
					Location loc = player.getLocation();
					world.setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
					sender.sendMessage("Teleported to spawn.");
				}
			}
			return true;
		}
		return false;
	}
}
