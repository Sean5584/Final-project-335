package finalProject;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class OrderItemTest {

    private MenuItem menuItem;

    @BeforeEach
    public void setUp() {
        menuItem = new MenuItem("Pasta", "Main", 12.75, true);
    }

    // Test constructor and that getters return correct values
    @Test
    public void testConstructorAndGetters() {
        OrderItem orderItem = new OrderItem(menuItem, "No Cheese");

        assertEquals(menuItem, orderItem.getMenuItem());
        assertEquals("No Cheese", orderItem.getModification());
    }

    // Test getItemTotal returns price of the menu item
    @Test
    public void testGetItemTotal() {
        OrderItem orderItem = new OrderItem(menuItem, "");
        assertEquals(12.75, orderItem.getItemTotal());
    }

    // Test toString includes modification info
    @Test
    public void testToStringWithModification() {
        OrderItem orderItem = new OrderItem(menuItem, "No Garlic");
        String output = orderItem.toString();
        assertTrue(output.contains("Pasta"));
        assertTrue(output.contains("No Garlic"));
        assertTrue(output.contains("12.75"));
    }

    // Test toString when no modification is provided
    @Test
    public void testToStringWithoutModification() {
        OrderItem orderItem = new OrderItem(menuItem, "");
        String output = orderItem.toString();
        assertTrue(output.contains("Pasta"));
        assertFalse(output.contains("()"));  // Should not show empty parentheses
    }

    // Test that the menu item's sale count is updated when OrderItem is created
    @Test
    public void testRecordSaleCalled() {
        int before = menuItem.getTimesOrdered();
        new OrderItem(menuItem, "Extra Sauce");
        int after = menuItem.getTimesOrdered();
        assertEquals(before + 1, after);
    }
}
