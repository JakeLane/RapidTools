package me.jakelane.rapidtools;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class RapidSetSpawn implements CommandExecutor, Listener {

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
					Location loc = player.getLocation();
			        int x = loc.getBlockX();
			        int y = loc.getBlockY();
			        int z = loc.getBlockZ();
			        player.getWorld().setSpawnLocation(x, y, z);
					sender.sendMessage("Spawn location set.");
				}
			}
			return true;
		}
		return false;
	}
}
