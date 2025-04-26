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
// the restaurant class is key part of our code,
// like LA1,2's library Model. have multiple methods like:

// Management resources: menus, tables, waiters.
// Search objects: Quick search by table number or waiter name.
// Save/load menu data.
// Statistics: the most popular dishes, the dishes with the highest income, the waiters with the most tips.
// Order history traversal: such as sales data analysis, order quantity, income statistics

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
	// I used Optional<Server> to avoid the Null pointer exception, findFirst() is 
	// Java bringed function that find the first one follow requirments.
	public Optional<Server> findServerByName(String name) {
		return servers.stream().filter(s -> s.getName().equals(name)).findFirst();
	}
	
	public Optional<Table> findTableByNumber(int number) {
		return tables.stream().filter(t -> t.getTableNumber() == number).findFirst();
	}

	public List<MenuItem> getTopItemsBySalesCount() {
		return menu.stream().sorted(Comparator.comparing(MenuItem::getTimesOrdered).reversed())
				.collect(Collectors.toList());
	}
	// Here use Comparator.comparing(...) this is just another applyments for comparator, is 
	// actually same to the implments comparator in menu.
	// if implements in menu it would be like menu.sorted(). Here is just another way to solve
	// reversed also java bringed methond, Reverse the sort order.
	public List<MenuItem> getTopItemsByRevenue() {
		return menu.stream().sorted(Comparator.comparing(MenuItem::getTotalRevenue).reversed())
				.collect(Collectors.toList());
	}
	
	public Server getTopServerByTips() {
		return servers.stream().max(Comparator.comparing(Server::getTotalTips)).orElse(null);
	}
	public boolean removeMenuItemByName(String name) {
	    return menu.removeIf(item -> item.getName().equalsIgnoreCase(name));
	}
	// save the menu in the file called menu_data
	public void saveMenuToFile() {
        	try (BufferedWriter writer = new BufferedWriter(new FileWriter("menu_data.txt"))) {
            		for (MenuItem item : menu) {
                		writer.write(item.getName() + "," + item.getCategory() + "," + item.getPrice() + "," + item.isModifiable());
                		writer.newLine();
            		}
        	} catch (IOException e) {
            		System.out.println("Failed to save menu: " + e.getMessage());
        	}
    	}

    // function to load menu from file.
    public static Restaurant loadMenuFromFile() {
    	 Restaurant r = new Restaurant();
         File file = new File(MENU_FILE);
         if (!file.exists()) {
             r.initDefaultMenu(); 
             return r;
         }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    String category = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    boolean editable = Boolean.parseBoolean(parts[3]);
                    r.menu.add(new MenuItem(name, category, price, editable));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to load menu: " + e.getMessage());
        }

        return r;
    }
	   private void initDefaultMenu() {
	    	
	        menu.add(new MenuItem("Steak", "Main", 18.99, false));
	        menu.add(new MenuItem("Burger", "Main", 11.50, true));
	        menu.add(new MenuItem("Pasta", "Main", 13.75, true));
	        menu.add(new MenuItem("Salad", "Side", 5.99, true));
	        menu.add(new MenuItem("Fries", "Side", 4.50, false));
	        menu.add(new MenuItem("Mushroom Soup", "Side", 6.25, false));
	        menu.add(new MenuItem("Ice Cream", "Dessert", 4.99, false));
	        menu.add(new MenuItem("Cheesecake", "Dessert", 6.50, false));
	        menu.add(new MenuItem("Coke", "Drink", 2.99, true));
	        menu.add(new MenuItem("Juice", "Drink", 3.50, false));
	        menu.add(new MenuItem("Coffee", "Drink", 3.25, true));
	    }
}
