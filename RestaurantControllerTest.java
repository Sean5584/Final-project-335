package finalProject;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.*;

public class RestaurantControllerTest {

    private Restaurant restaurant;
    private RestaurantController controller;

    @BeforeEach
    public void setup() throws IOException {
        restaurant = new Restaurant();
        controller = new RestaurantController(restaurant);

        Files.deleteIfExists(Paths.get("order_log.txt"));
        Files.deleteIfExists(Paths.get("tips_data.txt"));
        Files.deleteIfExists(Paths.get("order_counter.txt"));
    }

    // Tests assigning a table to an existing server
    @Test
    public void testAssignTableToServerValid() {
        Table table = new Table(1);
        Server server = new Server("Alice");
        restaurant.addTable(table);
        restaurant.addServer(server);

        controller.assignTableToServer(1, "Alice");
        assertTrue(server.getAssignedTables().contains(table));
    }

    // Tests assigning a table to a non-existent server or table
    @Test
    public void testAssignTableToServerInvalid() {
        controller.assignTableToServer(99, "Bob");
    }

    // Tests adding an order to a valid, open table
    @Test
    public void testAddOrderToTableValid() {
        Table table = new Table(2);
        restaurant.addTable(table);
        Order order = new Order();
        controller.addOrderToTable(2, order);
        assertEquals(1, table.getOrders().size());
    }

    // Tests adding an order to a closed table (should not add)
    @Test
    public void testAddOrderToClosedTable() {
        Table table = new Table(3);
        table.closeTable();
        restaurant.addTable(table);
        Order order = new Order();
        controller.addOrderToTable(3, order);
        assertEquals(0, table.getOrders().size());
    }

    // Tests closing a table with splitting the bill, and log includes split message
    @Test
    public void testCloseTableWithSplitTrue() throws Exception {
        setupOrderEnv(4, "Carol", true, 2, 20.0);
        List<String> lines = Files.readAllLines(Paths.get("order_log.txt"));
        assertTrue(lines.stream().anyMatch(line -> line.contains("Each person pays:")));
    }

    // Tests closing a table without splitting the bill
    @Test
    public void testCloseTableWithSplitFalse() throws Exception {
        setupOrderEnv(5, "David", false, 3, 30.0);
        List<String> lines = Files.readAllLines(Paths.get("order_log.txt"));
        assertFalse(lines.stream().anyMatch(line -> line.contains("Each person pays:")));
    }

    // Tests if tip stats are correctly recorded and retrieved
    @Test
    public void testGetAllTipStats() throws Exception {
        setupOrderEnv(6, "Eve", false, 1, 15.0);
        Map<String, Double> stats = controller.getAllTipStats();
        assertTrue(stats.containsKey("Eve"));
        assertEquals(15.0, stats.get("Eve"));
    }

    // Tests sales count for a menu item within a valid time range
    @Test
    public void testSalesInPeriod() {
        Table table = new Table(7);
        MenuItem item = new MenuItem("Soup", "Side", 5.0, false);
        Order order = new Order();
        order.addItem(new OrderItem(item, ""));
        table.addOrder(order);
        restaurant.addTable(table);

        LocalDateTime now = LocalDateTime.now();
        Map<String, Integer> sales = controller.getSalesInPeriod(now.minusDays(1), now.plusDays(1));
        assertEquals(1, sales.get("Soup"));
    }

    // Tests reading a valid total order count from file
    @Test
    public void testGetTotalOrderCount() throws IOException {
        Files.write(Paths.get("order_counter.txt"), "8".getBytes());
        assertEquals(8, controller.getTotalHistoricalOrderCount());
    }

    // Tests default order count when counter file is missing
    @Test
    public void testGetTotalOrderCountWhenFileMissing() {
        assertEquals(0, controller.getTotalHistoricalOrderCount());
    }

    // Tests that reading the order log does not crash
    @Test
    public void testReadOrderLogNoCrash() {
        controller.readOrderLog();
    }

    // Tests searching menu items by name
    @Test
    public void testSearchMenuByName() {
        restaurant.addMenuItem(new MenuItem("Fried Rice", "Main", 12.0, false));
        List<MenuItem> results = controller.searchMenu("Fried");
        assertFalse(results.isEmpty());
    }

    // Tests searching menu items by category
    @Test
    public void testSearchMenuByCategory() {
        restaurant.addMenuItem(new MenuItem("Soda", "Drink", 2.5, false));
        List<MenuItem> results = controller.searchMenu("Drink");
        assertFalse(results.isEmpty());
    }

    // Helper method to create full test setup: table, server, order, and close
    private void setupOrderEnv(int tableNum, String serverName, boolean split, int guests, double tip) {
        Table table = new Table(tableNum);
        table.setGuestCount(guests);

        Server server = new Server(serverName);
        MenuItem item = new MenuItem("Test", "Main", 10.0, false);
        Order order = new Order();
        order.addItem(new OrderItem(item, ""));

        restaurant.addTable(table);
        restaurant.addServer(server);
        restaurant.addMenuItem(item);

        controller.assignTableToServer(tableNum, serverName);
        controller.addOrderToTable(tableNum, order);
        controller.closeTable(tableNum, tip, split);
    }

    // Tests readOrderLog handles IOException without throwing
    @Test
    public void testReadOrderLogIOException() throws Exception {
        File file = new File("order_log.txt");
        file.createNewFile();
        file.setReadable(false);
        controller.readOrderLog();
        file.setReadable(true);
    }

    // Tests updateTipStats handles IOException gracefully
    @Test
    public void testUpdateTipStatsIOExceptionHandled() throws Exception {
        File file = new File("tips_data.txt");
        file.createNewFile();
        file.setWritable(false);
        setupOrderEnv(10, "Crash", false, 1, 5.0);
        file.setWritable(true);
    }

    // Tests closing a table without a matching server still closes the table
    @Test
    public void testCloseTableNoServerMatch() {
        Table table = new Table(8);
        table.setGuestCount(2);
        restaurant.addTable(table);

        Order order = new Order();
        order.addItem(new OrderItem(new MenuItem("Dish", "Main", 10, false), ""));
        restaurant.addMenuItem(order.getItems().get(0).getMenuItem());
        controller.addOrderToTable(8, order);

        controller.closeTable(8, 5.0, true);
        assertTrue(table.isClosed());
    }

    // Tests corrupted order_counter.txt triggers NumberFormatException
    @Test
    public void testGetTotalHistoricalOrderCountCorruptData() throws Exception {
        Files.write(Paths.get("order_counter.txt"), "bad-data".getBytes());
        assertThrows(NumberFormatException.class, () -> {
            controller.getTotalHistoricalOrderCount();
        });
    }

    // Tests corrupted counter file causes exception during setupOrderEnv
    @Test
    public void testIncrementOrderCountCorruptFile() throws Exception {
        Files.write(Paths.get("order_counter.txt"), "not-a-number".getBytes());
        assertThrows(NumberFormatException.class, () -> {
            setupOrderEnv(11, "Boom", false, 1, 1.0);
        });
    }

    // Tests handling IOException while reading total order count
    @Test
    public void testGetTotalHistoricalOrderCountIOException() throws Exception {
        File file = new File("order_counter.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        file.setReadable(false);
        int result = controller.getTotalHistoricalOrderCount();
        assertEquals(0, result);
        file.setReadable(true);
    }
}

