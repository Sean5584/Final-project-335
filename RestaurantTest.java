package finalProject;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.*;
import java.util.*;

public class RestaurantTest {

	private Restaurant restaurant;

	@BeforeEach
	public void setUp() {
		restaurant = new Restaurant();
	}

	// Test adding a menu item to the restaurant
	@Test
	public void testAddMenuItemAndGetMenu() {
		MenuItem item = new MenuItem("Sushi", "Main", 15.0, true);
		restaurant.addMenuItem(item);
		assertTrue(restaurant.getMenu().contains(item));
	}

	// Test finding a server by name
	@Test
	public void testFindServerByName() {
		Server server = new Server("Alice");
		restaurant.addServer(server);
		assertTrue(restaurant.findServerByName("Alice").isPresent());
	}

	// Test finding a table by number
	@Test
	public void testFindTableByNumber() {
		Table table = new Table(1);
		restaurant.addTable(table);
		assertTrue(restaurant.findTableByNumber(1).isPresent());
	}

	// Test getting top-selling menu item by order count
	@Test
	public void testTopItemsBySalesCount() {
		MenuItem i1 = new MenuItem("Burger", "Main", 10.0, true);
		MenuItem i2 = new MenuItem("Salad", "Side", 5.0, true);
		i1.recordSale();
		i1.recordSale();
		i2.recordSale();
		restaurant.addMenuItem(i1);
		restaurant.addMenuItem(i2);

		List<MenuItem> top = restaurant.getTopItemsBySalesCount();
		assertEquals("Burger", top.get(0).getName());
	}

	// Test getting top-selling menu item by total revenue
	@Test
	public void testTopItemsByRevenue() {
		MenuItem i1 = new MenuItem("Steak", "Main", 20.0, false);
		MenuItem i2 = new MenuItem("Cake", "Dessert", 6.0, false);
		i1.recordSale(); // 20
		i2.recordSale();
		i2.recordSale(); // 12
		restaurant.addMenuItem(i1);
		restaurant.addMenuItem(i2);

		List<MenuItem> top = restaurant.getTopItemsByRevenue();
		assertEquals("Steak", top.get(0).getName());
	}

	// Test identifying the top-earning server by tip amount
	@Test
	public void testTopServerByTips() {
		Server s1 = new Server("A");
		Server s2 = new Server("B");
		s1.addTip(15.0);
		s2.addTip(30.0);
		restaurant.addServer(s1);
		restaurant.addServer(s2);
		Server top = restaurant.getTopServerByTips();
		assertEquals("B", top.getName());
	}

	// Test saving and loading menu data to and from file
	@Test
	public void testSaveAndLoadMenuFromFile() throws Exception {
		MenuItem item = new MenuItem("Ramen", "Main", 9.99, true);
		restaurant.addMenuItem(item);
		restaurant.saveMenuToFile();

		Restaurant loaded = Restaurant.loadMenuFromFile();
		boolean found = loaded.getMenu().stream()
				.anyMatch(i -> i.getName().equals("Ramen") && i.getCategory().equals("Main"));
		assertTrue(found);

		Files.deleteIfExists(Paths.get("menu_data.txt"));
	}

	// Test that the default menu is initialized with data
	@Test
	public void testInitDefaultMenuHasItems() {
		assertFalse(restaurant.getMenu().isEmpty());
	}
}
