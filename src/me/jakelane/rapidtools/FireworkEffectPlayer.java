package me.jakelane.rapidtools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * 
 * FireworkEffectPlayer provides a thread-safe and (reasonably) version
 * independant way to instantly explode a FireworkEffect at a given location.
 * You are welcome to use, redistribute, modify and destroy your own copies of
 * this source with the following conditions:
 * 
 * 1. No warranty is given or implied. 2. All damage is your own responsibility.
 * 3. You provide credit publicly to the original source should you release the
 * plugin.
 * 
 * @author codename_B
 */
public class FireworkEffectPlayer {

	/*
	 * Example use:
	 * 
	 * public class FireWorkPlugin implements Listener {
	 * 
	 * FireworkEffectPlayer fplayer = new FireworkEffectPlayer();
	 * 
	 * @EventHandler public void onPlayerLogin(PlayerLoginEvent event) {
	 * fplayer.playFirework(event.getPlayer().getWorld(),
	 * event.getPlayer.getLocation(), Util.getRandomFireworkEffect()); }
	 * 
	 * }
	 */

	// internal references, performance improvements
	private Method firework_getHandle = null;

	/**
	 * Play a pretty firework at the location with the FireworkEffect when
	 * called
	 * 
	 * @param world
	 * @param loc
	 * @param fe
	 * @throws Exception
	 */
	public void playFirework(World world, Location loc, FireworkEffect fe) throws Exception {

		final Firework fw = (Firework) world.spawn(loc, Firework.class);

		if (firework_getHandle == null) {
			firework_getHandle = getMethod(fw.getClass(), "getHandle");
		}

		final Object nms_firework = firework_getHandle.invoke(fw, (Object[]) null);

		FireworkMeta data = (FireworkMeta) fw.getFireworkMeta();
		data.clearEffects();
		data.setPower(1);
		data.addEffect(fe);
		fw.setFireworkMeta(data);

		Field field = nms_firework.getClass().getDeclaredField("ticksFlown");
		field.setAccessible(true);
		field.set(nms_firework, 123);
	}

	/**
	 * Internal method, used as shorthand to grab our method in a nice friendly
	 * manner
	 * 
	 * @param cl
	 * @param method
	 * @return Method (or null)
	 */
	private static Method getMethod(Class<?> cl, String method) {
		for (Method m : cl.getMethods()) {
			if (m.getName().equals(method)) {
				return m;
			}
		}
		return null;
	}

}