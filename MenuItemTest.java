package finalProject;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MenuItemTest {

    private MenuItem item;

    @BeforeEach
    public void setUp() {
        item = new MenuItem("Burger", "Main", 10.5, true);
    }

    // Test getting the name of the menu item
    @Test
    public void testGetName() {
        assertEquals("Burger", item.getName());
    }

    // Test getting the category of the menu item
    @Test
    public void testGetCategory() {
        assertEquals("Main", item.getCategory());
    }

    // Test getting the price of the menu item
    @Test
    public void testGetPrice() {
        assertEquals(10.5, item.getPrice());
    }

    // Test whether the menu item is modifiable
    @Test
    public void testIsModifiable() {
        assertTrue(item.isModifiable());
    }

    // Test recording a single sale
    @Test
    public void testRecordSaleOnce() {
        item.recordSale();
        assertEquals(1, item.getTimesOrdered());
        assertEquals(10.5, item.getTotalRevenue());
    }

    // Test recording multiple sales and checking cumulative revenue
    @Test
    public void testRecordSaleMultipleTimes() {
        item.recordSale();
        item.recordSale();
        item.recordSale();
        assertEquals(3, item.getTimesOrdered());
        assertEquals(31.5, item.getTotalRevenue());
    }

    // Test string output for a modifiable item
    @Test
    public void testToStringModifiable() {
        String output = item.toString();
        assertTrue(output.contains("Burger"));
        assertTrue(output.contains("Main"));
        assertTrue(output.contains("10.5"));
        assertTrue(output.contains("can be modifed"));
    }

    // Test string output for a non-modifiable item
    @Test
    public void testToStringNonModifiable() {
        MenuItem nonMod = new MenuItem("Steak", "Main", 20.0, false);
        String output = nonMod.toString();
        assertTrue(output.contains("Steak"));
        assertFalse(output.contains("can be modifed"));
    }
}

