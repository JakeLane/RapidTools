package me.jakelane.rapidtools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RapidAnnouncePusher extends BukkitRunnable {

	private final JavaPlugin plugin;
	public String id;

	public RapidAnnouncePusher(String id, JavaPlugin plugin) {
		this.id = id;
		this.plugin = plugin;
	}

	@Override
	public void run() {
		if (plugin.getConfig().getString("Announcement." + id) != null) {
			String message = plugin.getConfig().getString("Announcement." + id + ".message");
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.sendMessage(RapidTools.wordWrap(ChatColor.GREEN + message));
			}
		} else {
			plugin.getConfig().set("AnnouncementScheduled." + id, null);
			this.cancel();
		}
	}
}
