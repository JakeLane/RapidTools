package me.jakelane.rapidtools;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RapidHorseOwner implements Listener {
	public RapidHorseOwner(RapidTools rapidTools) {
	}

	public static ItemStack getOwnerSaddleItemStack() {
		// Create the item
		ItemStack is = new ItemStack(Material.SADDLE, 1);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("Personal Saddle");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Right click to bind this saddle to you!");
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	@EventHandler
	public void playerMountHorse(VehicleEnterEvent e) {
		// Get on a horse
		if ((e.getEntered() instanceof Player)) {
			Player p = (Player) e.getEntered();
			if (e.getVehicle().getType().equals(EntityType.HORSE)) {
				Horse h = (Horse) e.getVehicle();
				if (isHorseOwner((Horse) e.getVehicle(), p)) {
					try {
						if (!h.getOwner().getName().equalsIgnoreCase(p.getName())) {
							h.setOwner(p);
						}
					} catch (Exception localException) {
					}
					return;
				}
				if (p.hasPermission("rapidtools.playersaddle")) {
					return;
				}
				e.setCancelled(true);
				p.sendMessage(ChatColor.RED + "This is " + getSaddleOwnername(p, h.getInventory().getSaddle()) + "'s horse.");
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayeruseItem(PlayerInteractEvent e) {
		// Use an item
		if ((e.getPlayer() instanceof Player)) {
			Player p = e.getPlayer();

			if (e.getPlayer().getItemInHand() != null) {
				if (e.getPlayer().getItemInHand().getType().equals(Material.SADDLE)) {
					if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
						if (checkOwnerSaddle(e.getPlayer().getItemInHand())) {
							ArrayList<String> lore = new ArrayList<String>();
							lore.add(p.getName());
							ItemMeta im = p.getItemInHand().getItemMeta();
							im.setLore(lore);
							p.getItemInHand().setItemMeta(im);
							p.updateInventory();
							p.sendMessage(ChatColor.GREEN + "This saddle is now owned by " + p.getName() + "!");
							return;
						}
					}
				}
			}
		}
	}

	public static boolean checkOwnerSaddle(ItemStack is) {
		// Check if saddle is owner
		if (is == null) {
			return false;
		}
		ItemMeta im = is.getItemMeta();
		String display = im.getDisplayName();
		if (display == null) {
			return false;
		}
		if ((display.equalsIgnoreCase("Personal Saddle")) || (display.contains("Personal Saddle"))) {
			return true;
		}
		return false;
	}

	@EventHandler
	public void onPlayerOpenHorseInv(InventoryOpenEvent e) {
		Player p = (Player) e.getPlayer();

		if ((e.getInventory() instanceof HorseInventory)) {
			HorseInventory hi = (HorseInventory) e.getInventory();
			ItemStack is = hi.getSaddle();
			if (p.hasPermission("rapidtools.playersaddle")) {
				return;
			}
			if (checkOwnerSaddle(is)) {
				if (!isSaddleOwner(p, is)) {
					e.setCancelled(true);
					p.sendMessage(ChatColor.RED + "This is " + getSaddleOwnername(p, is) + "'s horse.");
				}
			}
			return;
		}
	}

	public boolean isSaddleOwner(Player p, ItemStack is) {
		if (is == null) {
			return true;
		}
		ItemMeta im = is.getItemMeta();

		if (im.getLore().get(0) == null) {
			return true;
		}
		String owner = (String) im.getLore().get(0);

		if (p.getName().equalsIgnoreCase(owner)) {
			return true;
		}
		if (owner.contains("!")) {
			return true;
		}
		return false;
	}

	public boolean isHorseOwner(Horse h, Player p) {
		HorseInventory hi = h.getInventory();
		ItemStack is = hi.getSaddle();

		if (checkOwnerSaddle(is)) {
			if (isSaddleOwner(p, is)) {
				return true;
			}
			return false;
		}
		return true;
	}

	public String getSaddleOwnername(Player p, ItemStack is) {
		ItemMeta im = is.getItemMeta();

		if (im.getLore().get(0) == null) {
			return null;
		}
		String owner = (String) im.getLore().get(0);

		return owner;
	}
}
