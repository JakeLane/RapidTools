package me.jakelane.rapidtools;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RapidSpawn implements CommandExecutor {

	@SuppressWarnings("unused")
	private RapidTools plugin;

	public RapidSpawn(RapidTools plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Spawn command
		if (cmd.getName().equalsIgnoreCase("spawn")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				// Define player
				Player player = (Player) sender;
				// Set spawn if player has permissions
				if (player.hasPermission("rapidtools.spawn")) {
					// If world is selected
					if (args.length == 1) {
						// Throw tantrum if world doesn't exist
						if (Bukkit.getServer().getWorld(args[0]) == null) {
							player.sendMessage("Boo. This world does not exist");
						}
						// If world exists, teleport the player
						else {
							World world = Bukkit.getServer().getWorld(args[0]);
							Location loc = world.getSpawnLocation();
							loc.setWorld(world);
							if (!loc.getChunk().isLoaded()) {
								loc.getChunk().load();
							}
							player.teleport(loc);
							sender.sendMessage("Teleported to Spawn.");
						}
					}
					// If world not selected
					if (args.length == 0) {
						World world = player.getWorld();
						Location loc = world.getSpawnLocation();
						if (!loc.getChunk().isLoaded()) {
							loc.getChunk().load();
						}
						player.teleport(loc);
						sender.sendMessage("Teleported to Spawn.");
					}
				}
			}
			return true;
		}
		return false;
	}
}
