package finalProject;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

public class OrderTest {

    private MenuItem item1, item2;
    private OrderItem orderItem1, orderItem2;
    private Order order;

    @BeforeEach
    public void setUp() {
        item1 = new MenuItem("Fries", "Side", 3.5, false);
        item2 = new MenuItem("Cola", "Drink", 2.0, false);
        orderItem1 = new OrderItem(item1, "");
        orderItem2 = new OrderItem(item2, "No Ice");
        order = new Order();
    }

    // Test that the timestamp is not null when order is created
    @Test
    public void testGetTimestampNotNull() {
        assertNotNull(order.getTimestamp());
    }

    // Test that the timestamp is within a reasonable range of current time
    @Test
    public void testGetTimestampIsNowOrEarlier() {
        LocalDateTime before = LocalDateTime.now();
        Order newOrder = new Order();
        LocalDateTime after = LocalDateTime.now();
        assertTrue(!newOrder.getTimestamp().isBefore(before));
        assertTrue(!newOrder.getTimestamp().isAfter(after));
    }

    // Test adding items to the order and retrieving them
    @Test
    public void testAddItemAndGetItems() {
        order.addItem(orderItem1);
        order.addItem(orderItem2);

        List<OrderItem> items = order.getItems();
        assertEquals(2, items.size());
        assertEquals(orderItem1, items.get(0));
        assertEquals(orderItem2, items.get(1));
    }

    // Test total calculation with multiple items
    @Test
    public void testGetTotal() {
        order.addItem(orderItem1); // 3.5
        order.addItem(orderItem2); // 2.0
        assertEquals(5.5, order.getTotal());
    }

    // Test total calculation with no items in the order
    @Test
    public void testGetTotalEmptyOrder() {
        Order emptyOrder = new Order();
        assertEquals(0.0, emptyOrder.getTotal());
    }

    // Test toString output includes all order items and modifications
    @Test
    public void testToStringFormat() {
        order.addItem(orderItem1);
        order.addItem(orderItem2);
        String result = order.toString();
        assertTrue(result.contains("Fries"));
        assertTrue(result.contains("Cola"));
        assertTrue(result.contains("No Ice"));
    }
}

