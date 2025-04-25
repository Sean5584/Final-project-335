package finalProject;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ServerTest {

    private Server server;

    @BeforeEach
    public void setUp() {
        server = new Server("Bob");
    }

    // Test that the server's name is stored and returned correctly
    @Test
    public void testGetName() {
        assertEquals("Bob", server.getName());
    }

    // Test assigning a table to a server and retrieving it
    @Test
    public void testAssignTable() {
        Table t1 = new Table(1);
        server.assignTable(t1);
        List<Table> assigned = server.getAssignedTables();
        assertEquals(1, assigned.size());
        assertTrue(assigned.contains(t1));
    }

    // Test that tips are correctly accumulated
    @Test
    public void testAddTip() {
        server.addTip(10.0);
        server.addTip(5.5);
        assertEquals(15.5, server.getTotalTips());
    }

    // Test that a server has no tips initially
    @Test
    public void testInitialTipAmount() {
        assertEquals(0.0, server.getTotalTips());
    }

    // Test that a server has no assigned tables initially
    @Test
    public void testInitialAssignedTablesEmpty() {
        assertTrue(server.getAssignedTables().isEmpty());
    }
}
