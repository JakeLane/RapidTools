package me.jakelane.rapidtools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.ChatPaginator;

/**
 * Various Tools of the RapidCraft server
 * 
 * @author jakelane
 */
public final class RapidTools extends JavaPlugin implements Listener {
	// Main Configuration Loader
	public void loadConfiguration() {
		getConfig().addDefault("ApiURL", "http://rapidcraftmc.com/api.php");
		getConfig().addDefault("JoinMessage", "Hello, and welcome to the RapidCraft server!");
		getConfig().addDefault("AnnounceUpdateTime", 600);
		getConfig().options().copyDefaults(true);
		getConfig().set("AnnouncementScheduled", null);
		getConfig().set("Announcement", null);
		saveConfig();
	}

	public void onEnable() {
		// Set Plugin Description to pdFile
		PluginDescriptionFile pdFile = this.getDescription();
		// Load config
		loadConfiguration();
		// Register events
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new RapidAfk(this), this);
		getServer().getPluginManager().registerEvents(new RapidFireworkArrowsListener(), this);
		// Executor for Ping
		getCommand("ping").setExecutor(new RapidPing(this));
		// Executor for SetSpawn
		getCommand("setspawn").setExecutor(new RapidSetSpawn(this));
		// Executor for Spawn
		getCommand("spawn").setExecutor(new RapidSpawn(this));
		// Executor for Events
		getCommand("events").setExecutor(new RapidEventsExecutor(this));
		// Executor for Timezone
		getCommand("timezone").setExecutor(new RapidTimezone(this));
		// Executor for AFK
		getCommand("afk").setExecutor(new RapidAfk(this));
		// Executor for Pranks
		getCommand("pranks").setExecutor(new RapidPranksExecutor(this));
		// Executor for FireworkArrows
		getCommand("fwa").setExecutor(new RapidFireworkArrowsExecutor(this));
		// Executor for AnnounceUpdater
		getCommand("aup").setExecutor(new RapidAnnounceExecutor(this));
		// Setup Scheduler
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		// Schedule Announcement updater for defined update time (starts at 15
		// seconds)
		long announceUpdateTime = getConfig().getInt("AnnounceUpdateTime") * 20;
		scheduler.scheduleSyncRepeatingTask(this, new RapidAnnounceUpdater(this), 20L, announceUpdateTime);
		// Echo the enable
		getLogger().info(pdFile.getName() + " version " + pdFile.getVersion() + " enabled");
	}

	public void onDisable() {
		PluginDescriptionFile pdFile = this.getDescription();
		getLogger().info(pdFile.getName() + " version " + pdFile.getVersion() + " disabled");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	// Player join event
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		String joinMessage = getConfig().getString("JoinMessage");
		event.getPlayer().sendMessage(ChatColor.GOLD + joinMessage);
		// If player has not joined before
		if (!event.getPlayer().hasPlayedBefore()) {
			String newPlayerName = event.getPlayer().getDisplayName();
			Bukkit.broadcastMessage(ChatColor.GOLD + "Welcome " + newPlayerName + " to the server!");
		}
	}

	@EventHandler
	// Player chat event
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		Player[] list = Bukkit.getOnlinePlayers();
		for (String word : ChatColor.stripColor(event.getMessage()).toLowerCase().split("[./;-`~()\\[\\]{}+ ]")) {
			for (Player p : list) {
				String user = p.getName().toLowerCase();
				String colon = user + ":";
				String comma = user + ",";
				if (colon.equals(word) || comma.equals(word) || user.equals(word)) {
					if (p.getMetadata("afk").get(0).asBoolean()) {
						event.getPlayer().sendMessage(ChatColor.YELLOW + p.getName() + " is AFK.");
					}
					Location location = p.getLocation();
					String playername = p.getName();
					Bukkit.getPlayer(playername).getWorld().playSound(location, Sound.NOTE_PLING, 1, 0);
				}
			}
		}
	}

	@EventHandler
	// Set metadata on login
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		// Set player afk metadata
		Boolean playerAfked = false;
		player.setMetadata("afk", new FixedMetadataValue(this, playerAfked));
		// Set player FireworkArrows metadata
		getConfig().addDefault(player.getDisplayName() + ".FireworkArrows", false);
		saveConfig();
		Boolean playerFireworkArrows = getConfig().getBoolean(player.getDisplayName() + ".FireworkArrows");
		player.setMetadata("FireworkArrows", new FixedMetadataValue(this, playerFireworkArrows));
	}

	// Word wrapper
	public static String[] wordWrap(String toBeWrapped) {
		return ChatPaginator.wordWrap(toBeWrapped, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH);
	}
}
