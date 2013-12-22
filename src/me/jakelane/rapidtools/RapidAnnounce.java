package me.jakelane.rapidtools;

import org.bukkit.scheduler.BukkitTask;

public class RapidAnnounce implements Runnable {

	private RapidTools plugin;
	private volatile boolean isRunning = true;

	public RapidAnnounce(RapidTools plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		while (isRunning) {
			// For every announcement, create an announcement and set its repeat
			// length
			try {
				if (null != plugin.getConfig().getConfigurationSection("Announcement").getKeys(false)) {
					for (String id : plugin.getConfig().getConfigurationSection("Announcement").getKeys(false)) {
						if (!plugin.getConfig().isSet("AnnouncementScheduled." + id)) {
							plugin.getConfig().set("AnnouncementScheduled." + id, true);
							plugin.saveConfig();
							String minsToRun = plugin.getConfig().getString("Announcement." + id + ".time");
							float secondsToRun = Math.round(Float.parseFloat(minsToRun) * 60);
							Long ticksToRun = ((long) secondsToRun) * 20;
							@SuppressWarnings("unused")
							BukkitTask task = new RapidAnnouncePusher(id, plugin).runTaskTimer(plugin, 20L, ticksToRun);
							kill();
						}
					}
				}
			} catch (NullPointerException ex) {
				kill();
			} catch (Exception ex) {
				System.out.println("Error: " + ex);
				kill();
			}
		}
	}

	public void kill() {
		isRunning = false;
	}
}
