package finalProject;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private Restaurant restaurant;
    private RestaurantController controller;
    private JTextArea outputArea;
    private JTextField searchField;
    private JComboBox<String> menuBox;
    private JComboBox<String> modOptionBox;
    private JTextField tableField, serverField, modField;
    private JCheckBox splitBox;
    private List<OrderItem> cart;
    private Map<String, String[]> modificationOptions;

    public RestaurantGUI() {
        this.restaurant = Restaurant.loadMenuFromFile();
        this.controller = new RestaurantController(restaurant);
        this.cart = new ArrayList<>();
        this.modificationOptions = new HashMap<>();

        modificationOptions.put("Steak", new String[] { "Medium", "Medium-Well", "Well-Done" });
        modificationOptions.put("Burger", new String[] { "Spicy", "No Onion", "Extra Cheese" });
        modificationOptions.put("Coffee", new String[] { "No Sugar", "Light Milk", "With Ice" });

        setTitle("Restaurant Manager");
        setSize(1500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        menuBox = new JComboBox<>();
        menuBox.removeAllItems();
        List<String> addedNames = new ArrayList<>();
        for (MenuItem item : restaurant.getMenu()) {
            if (!addedNames.contains(item.getName())) {
                menuBox.addItem(item.getName());
                addedNames.add(item.getName());
            }
        }

        modOptionBox = new JComboBox<>();
        updateModOptions((String) menuBox.getSelectedItem());
        menuBox.addActionListener(e -> updateModOptions((String) menuBox.getSelectedItem()));
        modField = new JTextField(10);
        JButton tipStatsButton = new JButton("Show Tip Stats");

        tipStatsButton.addActionListener(e -> {
            Map<String, Double> tips = controller.getAllTipStats();
            outputArea.setText("-- Total Tip Stats --\n");
            for (Map.Entry<String, Double> entry : tips.entrySet()) {
                outputArea.append(entry.getKey() + ": $" + String.format("%.2f", entry.getValue()) + "\n");
            }
        });

        JButton salesStatsButton = new JButton("Top Sales");
        salesStatsButton.addActionListener(e -> {
            outputArea.setText("-- Best Sellers --\n");
            restaurant.getMenu().stream().sorted((a, b) -> Double.compare(b.getTotalRevenue(), a.getTotalRevenue()))
                    .limit(5)
                    .forEach(item -> outputArea.append(item.getName() + ": $" + item.getTotalRevenue() + "\n"));
        });
        JButton salesInPeriodButton = new JButton("Sales In Period");
        JButton addToCartButton = new JButton("Add to Cart");
        menuPanel.add(new JLabel("Item: "));
        menuPanel.add(menuBox);
        menuPanel.add(new JLabel("Option: "));
        menuPanel.add(modOptionBox);
        menuPanel.add(new JLabel("Note: "));
        menuPanel.add(modField);
        menuPanel.add(addToCartButton);

        JPanel addMenuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField newItemName = new JTextField(10);
        JTextField newItemPrice = new JTextField(5);
        String[] categories = { "Main", "Side", "Drink", "Dessert" };
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        JCheckBox editableBox = new JCheckBox("Editable");
        JButton addItemButton = new JButton("Add Item");
        addMenuPanel.add(new JLabel("New Item: "));
        addMenuPanel.add(newItemName);
        addMenuPanel.add(new JLabel("Price: "));
        addMenuPanel.add(newItemPrice);
        addMenuPanel.add(new JLabel("Category: "));
        addMenuPanel.add(categoryBox);
        addMenuPanel.add(editableBox);
        addMenuPanel.add(addItemButton);

        // 这里加上addItemButton的功能
        addItemButton.addActionListener(e -> {
            try {
                String name = newItemName.getText().trim();
                double price = Double.parseDouble(newItemPrice.getText().trim());
                String category = (String) categoryBox.getSelectedItem();
                boolean editable = editableBox.isSelected();

                if (!name.isEmpty() && price >= 0) {
                    MenuItem newItem = new MenuItem(name, category, price, editable);
                    restaurant.getMenu().add(newItem);
                    menuBox.addItem(name);
                    restaurant.saveMenuToFile();
                    outputArea.append("✅ New item added: " + newItem.toString() + "\n");
                } else {
                    outputArea.append("❌ Please enter valid name and price\n");
                }
            } catch (NumberFormatException ex) {
                outputArea.append("❌ Invalid price format, please enter number\n");
            }
        });

        leftPanel.add(menuPanel);
        leftPanel.add(addMenuPanel);
        mainPanel.add(leftPanel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        tableField = new JTextField();
        serverField = new JTextField();
        JTextField guestCountField = new JTextField();
        splitBox = new JCheckBox("Split Bill");
        JButton checkoutButton = new JButton("Checkout");
        JButton readLogButton = new JButton("View Log");
        searchField = new JTextField();
        JButton searchButton = new JButton("Search Menu");

        JButton totalOrderCountButton = new JButton("Total Orders Ever");
        totalOrderCountButton.addActionListener(e -> {
            int total = controller.getTotalHistoricalOrderCount();
            outputArea.setText("📊 Total orders ever placed: " + total + "\n");
        });
        bottomPanel.add(totalOrderCountButton);

        bottomPanel.add(new JLabel("Guests: "));
        bottomPanel.add(guestCountField);
        bottomPanel.add(new JLabel("Table: "));
        bottomPanel.add(tableField);
        bottomPanel.add(new JLabel("Server: "));
        bottomPanel.add(serverField);
        bottomPanel.add(splitBox);
        bottomPanel.add(checkoutButton);
        bottomPanel.add(new JLabel("Keyword: "));
        bottomPanel.add(searchField);
        bottomPanel.add(searchButton);
        bottomPanel.add(readLogButton);
        bottomPanel.add(tipStatsButton);
        bottomPanel.add(salesStatsButton);
        bottomPanel.add(salesInPeriodButton);

        JButton deleteFromCartButton = new JButton("Delete Item From Cart");
        menuPanel.add(deleteFromCartButton);

        deleteFromCartButton.addActionListener(e -> {
            if (cart.isEmpty()) {
                outputArea.append("🛒 Cart is empty. Nothing to delete.\n");
                return;
            }
            outputArea.append("🛒 Current Cart:\n");
            for (int i = 0; i < cart.size(); i++) {
                OrderItem item = cart.get(i);
                outputArea.append(i + ": " + item.getMenuItem().getName());
                if (!item.getModification().isEmpty()) {
                    outputArea.append(" (" + item.getModification() + ")");
                }
                outputArea.append("\n");
            }
            String input = JOptionPane.showInputDialog("Enter item index to delete (0 ~ " + (cart.size() - 1) + "):");
            try {
                int index = Integer.parseInt(input);
                if (index >= 0 && index < cart.size()) {
                    OrderItem removed = cart.remove(index);
                    outputArea.append("✅ Removed: " + removed.getMenuItem().getName() + "\n");
                } else {
                    outputArea.append("❌ Invalid index.\n");
                }
            } catch (Exception ex) {
                outputArea.append("❌ Please enter a valid number.\n");
            }
        });

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel);

        showFullMenu();

        addToCartButton.addActionListener(e -> {
            String itemName = (String) menuBox.getSelectedItem();
            String modOption = (String) modOptionBox.getSelectedItem();
            String customMod = modField.getText().trim();
            String finalMod = modOption;
            if (!customMod.isEmpty()) {
                if (!modOption.isEmpty()) {
                    finalMod = modOption + ", " + customMod;
                } else {
                    finalMod = customMod;
                }
            }
            for (MenuItem m : restaurant.getMenu()) {
                if (m.getName().equals(itemName)) {
                    cart.add(new OrderItem(m, finalMod));
                    outputArea.append("Added: " + m.getName() + (finalMod.isEmpty() ? "" : "（" + finalMod + "）") + "\n");
                    break;
                }
            }
        });

        checkoutButton.addActionListener(e -> {
            try {
                if (cart.isEmpty()) {
                    outputArea.append("❌ Please add at least one item before checkout!\n");
                    return;
                }
                if (tableField.getText().isEmpty() || serverField.getText().isEmpty() || guestCountField.getText().isEmpty()) {
                    outputArea.append("❌ Please fill all fields (table, server, guests)\n");
                    return;
                }
 
                int tableNum = Integer.parseInt(tableField.getText());
                String server = serverField.getText();
                int guestCount = Integer.parseInt(guestCountField.getText());
                boolean split = splitBox.isSelected();

                String tipInput = JOptionPane.showInputDialog(this, "Enter tip amount:", "Tip Input", JOptionPane.PLAIN_MESSAGE);
                if (tipInput == null || tipInput.trim().isEmpty()) {
                    outputArea.append("❌ Tip input is required.\n");
                    return;
                }
                double tip = Double.parseDouble(tipInput.trim());

                Order order = new Order();
                for (OrderItem item : cart) order.addItem(item);

                Table table = new Table(tableNum);
                table.setGuestCount(guestCount);
                Server s = new Server(server);
                restaurant.addTable(table);
                restaurant.addServer(s);
                controller.assignTableToServer(tableNum, server);
                controller.addOrderToTable(tableNum, order);
                controller.closeTable(tableNum, tip, split);
                outputArea.append("\n✅ Order completed and logged!\n");
                cart.clear();
            } catch (NumberFormatException ex) {
                outputArea.append("❌ Table number, guest count or tip must be valid numbers!\n");
            } catch (Exception ex) {
                outputArea.append("❌ Checkout failed, please verify input!\n");
            }
        });

        salesInPeriodButton.addActionListener(e -> {
            try {
                String startInput = JOptionPane.showInputDialog("Enter start date (yyyy-MM-dd):");
                String endInput = JOptionPane.showInputDialog("Enter end date (yyyy-MM-dd):");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate startDate = LocalDate.parse(startInput, formatter);
                LocalDate endDate = LocalDate.parse(endInput, formatter);

                LocalDateTime start = startDate.atStartOfDay();
                LocalDateTime end = endDate.plusDays(1).atStartOfDay().minusNanos(1);

                Map<String, Integer> stats = controller.getSalesInPeriod(start, end);
                outputArea.setText("-- Sales From " + startInput + " to " + endInput + " --\n");
                if (stats.isEmpty()) {
                    outputArea.append("No sales found in this period.\n");
                } else {
                    for (Map.Entry<String, Integer> entry : stats.entrySet()) {
                        outputArea.append(entry.getKey() + ": " + entry.getValue() + " orders\n");
                    }
                }
            } catch (Exception ex) {
                outputArea.setText("Invalid date format or error occurred.\n");
            }
        });

        readLogButton.addActionListener(e -> {
            outputArea.setText("");
            try {
                java.nio.file.Files.lines(java.nio.file.Paths.get("order_log.txt"))
                    .forEach(line -> outputArea.append(line + "\n"));
            } catch (Exception ex) {
                outputArea.setText("Failed to read log file.\n");
            }
        });
		searchButton.addActionListener(e -> {
			String keyword = searchField.getText();
			List<MenuItem> results = controller.searchMenu(keyword);
			outputArea.setText("Search Results:\n");
			if (results.isEmpty()) {
				outputArea.append("No items found。\n");
			} else {
				List<String> seen = new ArrayList<>();
				for (MenuItem item : results) {
					if (!seen.contains(item.getName())) {
						outputArea.append(item.toString() + "\n");
						seen.add(item.getName());
					}
				}
			}
		});

    }

    private void updateModOptions(String menuItemName) {
        modOptionBox.removeAllItems();
        if (modificationOptions.containsKey(menuItemName)) {
            for (String opt : modificationOptions.get(menuItemName)) {
                modOptionBox.addItem(opt);
            }
        } else {
            modOptionBox.addItem("");
        }
    }

    private void showFullMenu() {
        outputArea.setText("-- Menu --\n");
        List<String> seen = new ArrayList<>();
        for (MenuItem item : restaurant.getMenu()) {
            if (!seen.contains(item.getName())) {
                outputArea.append(item.toString() + "\n");
                seen.add(item.getName());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RestaurantGUI().setVisible(true));
    }

}

