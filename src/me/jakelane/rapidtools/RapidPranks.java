package me.jakelane.rapidtools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RapidPranks implements Runnable {

	private RapidTools plugin;
	private volatile boolean isRunning = true;

	public RapidPranks(RapidTools plugin) {
		this.plugin = plugin;
	}

	public String listAsCSV(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for (String str : list) {
			if (sb.length() != 0) {
				sb.append(", ");
			}
			sb.append(str);
		}
		return sb.toString();
	}

	public void run() {
		while (isRunning) {
			Player player = (Player) RapidPranksExecutor.getSender();
			if (RapidPranksExecutor.getArgs().length == 1 && RapidPranksExecutor.getArgs()[0].equalsIgnoreCase("enable")) {
				try {
					// Set the URL and parameters
					String uri = plugin.getConfig().getString("ApiURL") + "?key=" + plugin.getConfig().getString("Key");
					String urlParameters = "prank=" + player.getDisplayName() + "&set=1";
					URL url = new URL(uri);
					// Do the connection stuffs
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
					writer.write(urlParameters);
					writer.flush();
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					writer.close();
					reader.close();
					RapidPranksExecutor.getSender().sendMessage(
							ChatColor.YELLOW + "You are now on the prank list! Make sure sure you read the rules.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (RapidPranksExecutor.getArgs().length == 1 && RapidPranksExecutor.getArgs()[0].equalsIgnoreCase("disable")) {
				try {
					// Set the URL and parameters
					String uri = plugin.getConfig().getString("ApiURL") + "?key=" + plugin.getConfig().getString("Key");
					String urlParameters = "prank=" + player.getDisplayName() + "&set=0";
					URL url = new URL(uri);

					// Do the connection stuffs
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
					writer.write(urlParameters);
					writer.flush();
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					writer.close();
					reader.close();
					RapidPranksExecutor.getSender().sendMessage(ChatColor.YELLOW + "You are no longer on the prank list.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (RapidPranksExecutor.getArgs().length == 1 && RapidPranksExecutor.getArgs()[0].equalsIgnoreCase("list")) {
				try {
					// Set the URL and parameters
					String uri = plugin.getConfig().getString("ApiURL") + "?prank";
					URL url = new URL(uri);
					// Do the connection stuffs
					URLConnection conn = url.openConnection();
					conn.setRequestProperty("Accept", "application/xml");
					conn.setDoOutput(false);
					// Do the XML stuffs
					InputStream xml = conn.getInputStream();
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					Document doc = db.parse(xml);
					doc.getDocumentElement().normalize();

					// define the root main element (you fucking idiot)
					NodeList nList = doc.getElementsByTagName("prankmembers");

					// Check if there are prankers
					if (nList.getLength() == 0) {
						RapidEventsExecutor.getSender().sendMessage(ChatColor.RED + "There are no prankers!");
					}

					// Add the prankers to the list
					List<String> prankMembers = new ArrayList<String>();
					for (int temp = 0; temp < nList.getLength(); temp++) {
						Node nNode = nList.item(temp);
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) nNode;
							if (eElement.getElementsByTagName("status").item(0).getTextContent().equals("1")) {
								prankMembers.add(eElement.getElementsByTagName("username").item(0).getTextContent());
							}
						}
					}

					// Print that shit
					RapidPranksExecutor.getSender().sendMessage(RapidTools.wordWrap(listAsCSV(prankMembers)));
					kill();
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
					kill();
				}
			}
			kill();
		}
	}

	public void kill() {
		isRunning = false;
	}
}
