package finalProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Restaurant {
	private List<MenuItem> menu;
	private List<Table> tables;
	private List<Server> servers;

	private static final String MENU_FILE = "menu_data.txt";

	public Restaurant() {
		this.menu = new ArrayList<>();
		this.tables = new ArrayList<>();
		this.servers = new ArrayList<>();
		initDefaultMenu();
	}

	public void addMenuItem(MenuItem item) {
		menu.add(item);
	}

	public void addTable(Table table) {
		tables.add(table);
	}

	public void addServer(Server server) {
		servers.add(server);
	}

	public List<Table> getTables() {
		return tables;
	}

	public List<Server> getServers() {
		return servers;
	}

	public List<MenuItem> getMenu() {
		return menu;
	}

}