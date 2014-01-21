package me.jakelane.rapidtools;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class RapidFireworkArrowsListener implements Listener {

	private RapidTools plugin;

	public RapidFireworkArrowsListener(RapidTools plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onProjectileHitEvent(ProjectileHitEvent event) {
		if (event.getEntity() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getEntity();
			LivingEntity shooter = arrow.getShooter();
			if ((shooter instanceof Player)) {
				Player player = (Player) shooter;
				if (player.getMetadata("FireworkArrows").get(0).asBoolean()) {
					try {
						playFirework(arrow.getLocation());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void playFirework(Location location) {
		final Firework firework = location.getWorld().spawn(location, Firework.class);
		FireworkMeta fMeta = firework.getFireworkMeta();
		fMeta.addEffect(getEffect());
		firework.setFireworkMeta(fMeta);
		new BukkitRunnable() {
			@Override
			public void run() {
				firework.detonate();
			}
		}.runTaskLater(plugin, 1);
	}

	public static FireworkEffect getEffect() {
		FireworkEffect.Type chosentype = null;
		Random generator = new Random();
		List<Color> allmccolors = Arrays.asList(new Color[] { Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN, Color.LIME,
				Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER, Color.TEAL, Color.WHITE, Color.YELLOW });
		int randnum = generator.nextInt(17);
		Color chosencolor = (Color) allmccolors.get(randnum);
		int randeff = generator.nextInt(4);
		List<Type> fireworksEffects = Arrays.asList(FireworkEffect.Type.BALL, FireworkEffect.Type.BURST, FireworkEffect.Type.BALL_LARGE,
				FireworkEffect.Type.STAR);
		chosentype = fireworksEffects.get(randeff);
		return FireworkEffect.builder().with(chosentype).withColor(chosencolor).build();
	}
}
