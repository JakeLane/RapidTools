package me.jakelane.rapidtools;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.Bukkit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RapidAnnounceUpdater implements Runnable {

	private RapidTools plugin;

	public RapidAnnounceUpdater(RapidTools plugin) {
		this.plugin = plugin;
	}

	public void run() {
		try {
			plugin.getConfig().set("Announcement", null);
			String announceUri = plugin.getConfig().getString("ApiURL") + "?announce";
			URL url = new URL(announceUri);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/xml");
			InputStream xmlAnnounceUpdate = connection.getInputStream();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document docAnnounceUpdate = db.parse(xmlAnnounceUpdate);
			docAnnounceUpdate.getDocumentElement().normalize();
			NodeList nList = docAnnounceUpdate.getElementsByTagName("announcement");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String announceId = eElement.getElementsByTagName("id").item(0).getTextContent();
					String announceName = eElement.getElementsByTagName("name").item(0).getTextContent();
					String announceTime = eElement.getElementsByTagName("time").item(0).getTextContent();
					String announceMessage = eElement.getElementsByTagName("message").item(0).getTextContent();
					plugin.getConfig().set("Announcement." + announceId + ".name", announceName);
					plugin.getConfig().set("Announcement." + announceId + ".time", announceTime);
					plugin.getConfig().set("Announcement." + announceId + ".message", announceMessage);
					plugin.saveConfig();
				}
			}
		} catch (Exception e) {
			Bukkit.broadcastMessage("Error: " + e.getMessage());
			e.printStackTrace();
		}
		// Execute the creator (DUNDUNDUN!)
		RapidAnnounce r1 = new RapidAnnounce(plugin);
		Thread t1 = new Thread(r1);
		t1.start();
	}
}
