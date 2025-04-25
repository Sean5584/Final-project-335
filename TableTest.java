package finalProject;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TableTest {

    private Table table;
    private Order order1, order2;

    @BeforeEach
    public void setUp() {
        table = new Table(1);

        MenuItem item1 = new MenuItem("Pizza", "Main", 12.0, true);
        MenuItem item2 = new MenuItem("Water", "Drink", 0.0, false);

        OrderItem orderItem1 = new OrderItem(item1, "Extra cheese");
        OrderItem orderItem2 = new OrderItem(item2, "");

        order1 = new Order();
        order1.addItem(orderItem1);

        order2 = new Order();
        order2.addItem(orderItem2);
    }

    // Test getting the table number
    @Test
    public void testGetTableNumber() {
        assertEquals(1, table.getTableNumber());
    }

    // Test setting and getting guest count
    @Test
    public void testSetAndGetGuestCount() {
        table.setGuestCount(4);
        assertEquals(4, table.getGuestCount());
    }

    // Test adding orders and retrieving them
    @Test
    public void testAddAndGetOrders() {
        table.addOrder(order1);
        table.addOrder(order2);
        List<Order> orders = table.getOrders();
        assertEquals(2, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
    }

    // Test initial closed state is false
    @Test
    public void testIsInitiallyOpen() {
        assertFalse(table.isClosed());
    }

    // Test closing the table
    @Test
    public void testCloseTable() {
        table.closeTable();
        assertTrue(table.isClosed());
    }

    // Test total amount for all orders
    @Test
    public void testGetTableTotal() {
        table.addOrder(order1); // 12.0
        table.addOrder(order2); // 0.0
        assertEquals(12.0, table.getTableTotal());
    }

    // Test setting and getting tip amount
    @Test
    public void testSetAndGetTip() {
        table.setTip(5.5);
        assertEquals(5.5, table.getTip());
    }

    // Test splitting bill evenly based on order count
    @Test
    public void testSplitBillEvenly() {
        table.addOrder(order1);
        table.addOrder(order2); // total = 12.0, orders = 2
        assertEquals(6.0, table.splitBillEvenly());
    }

    // Test splitting bill by order
    @Test
    public void testSplitBillByOrder() {
        table.addOrder(order1); // 12.0
        table.addOrder(order2); // 0.0
        List<Double> splits = table.splitBillByOrder();
        assertEquals(2, splits.size());
        assertEquals(12.0, splits.get(0));
        assertEquals(0.0, splits.get(1));
    }

    // Test string output includes order item details
    @Test
    public void testToStringIncludesOrder() {
        table.addOrder(order1);
        String output = table.toString();
        assertTrue(output.contains("Pizza"));
        assertTrue(output.contains("Extra cheese"));
    }
}
