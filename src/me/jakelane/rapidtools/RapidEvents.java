package me.jakelane.rapidtools;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RapidEvents implements Runnable {

	private RapidTools plugin;
	private volatile boolean isRunning = true;

	public RapidEvents(RapidTools plugin) {
		this.plugin = plugin;
	}

	public void run() {
		while (isRunning) {
			try {
				Player player = (Player) RapidPranksExecutor.getSender();
				// Parse the events
				String apiUrl = plugin.getConfig().getString("ApiURL");
				String uri = apiUrl + "?events";
				URL url = new URL(uri);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", "application/xml");
				InputStream xml = connection.getInputStream();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(xml);
				doc.getDocumentElement().normalize();

				NodeList nList = doc.getElementsByTagName("event");
				if (nList.getLength() == 0) {
					RapidEventsExecutor.getSender().sendMessage(ChatColor.YELLOW + "No events scheduled.");
				}

				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						RapidEventsExecutor.getSender()
								.sendMessage(ChatColor.YELLOW + eElement.getElementsByTagName("name").item(0).getTextContent());
						// Define timestamp to string dd/MM/yyyy hh:mm a
						String timestampString = eElement.getElementsByTagName("timestamp").item(0).getTextContent();
						long unixTimestamp = Long.parseLong(timestampString);
						Date date = new Date(unixTimestamp * 1000L);
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
						String playerTimezonePath = player.getName() + ".Timezone";
						String playersTimezone = plugin.getConfig().getString(playerTimezonePath);
						if (playersTimezone != null) {
							sdf.setTimeZone(TimeZone.getTimeZone(playersTimezone));
							String formattedDate = sdf.format(date);
							RapidEventsExecutor.getSender().sendMessage("Time: " + formattedDate + " (" + playersTimezone + ")");
						} else {
							sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
							String formattedDate = sdf.format(date);
							RapidEventsExecutor.getSender().sendMessage("Time: " + formattedDate + " (UTC)");
							RapidEventsExecutor.getSender().sendMessage(
									ChatColor.RED + "" + ChatColor.ITALIC + "You can change your timezone with /timezone");
						}
						RapidEventsExecutor.getSender().sendMessage(
								"Location of Event: " + eElement.getElementsByTagName("location").item(0).getTextContent());
						RapidEventsExecutor.getSender().sendMessage(
								"Description: " + eElement.getElementsByTagName("description").item(0).getTextContent());
					}
				}
				kill();
			} catch (Exception e) {
				RapidEventsExecutor.getSender().sendMessage("Error: " + e.getMessage());
				kill();
			}
		}
	}

	public void kill() {
		isRunning = false;
	}
}
