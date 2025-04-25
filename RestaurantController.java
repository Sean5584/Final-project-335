
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
// It is the central module for parsing input, organizing data structures,
// and scheduling traversal and output.
public class RestaurantController {
    private Restaurant restaurant;

    public RestaurantController(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    // addOrder to table, here use isPresent() because in before code I
    // used the Optional<Server> it can be change if use common it will need
    // something like if(table !=null). And here invoid this situation.
    public void assignTableToServer(int tableNum, String serverName) {
        var table = restaurant.findTableByNumber(tableNum);
        var server = restaurant.findServerByName(serverName);
        if (table.isPresent() && server.isPresent()) {
            server.get().assignTable(table.get());
        } else {
            System.out.println("Table or server not found.");
        }
    } 
    // similar to last one, just some detail change.
    public void addOrderToTable(int tableNum, Order order) {
        var table = restaurant.findTableByNumber(tableNum);
        if (table.isPresent() && !table.get().isClosed()) {
            table.get().addOrder(order);
        } else {
            System.out.println("Table does not exist or is already closed.");
        }
    }
    // the function use to update the tips, ask tipamound the server name
    // it will record in the tips_data.txt
    private void updateTipStats(String serverName, double tipAmount) {
        File file = new File("tips_data.txt");
        Map<String, Double> tips = new HashMap<>();

        try {
            if (file.exists()) {
                List<String> lines = java.nio.file.Files.readAllLines(file.toPath());
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        tips.put(parts[0], Double.parseDouble(parts[1]));
                    }
                }

            }

            tips.put(serverName, tips.getOrDefault(serverName, 0.0) + tipAmount);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Map.Entry<String, Double> entry : tips.entrySet()) {
                    writer.write(entry.getKey() + "," + entry.getValue());
                    writer.newLine();
                }
            }

            } catch (IOException e) {
            System.out.println("Failed to update tip stats: " + e.getMessage());
        }
    }
    // It need to use with last one, this one is read file and get the tip situation.
    public Map<String, Double> getAllTipStats() {
        Map<String, Double> tips = new HashMap<>();
        File file = new File("tips_data.txt");

        try {
            if (file.exists()) {
                List<String> lines = java.nio.file.Files.readAllLines(file.toPath());
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        tips.put(parts[0], Double.parseDouble(parts[1]));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to read tip stats: " + e.getMessage());
        }

        return tips;
    }
    // the one use to close which is after checkout, it end everything and 
    // increase total order count and updates things.
    public void closeTable(int tableNum, double tip, boolean splitEvenly) {
        var tableOpt = restaurant.findTableByNumber(tableNum);
        if (tableOpt.isEmpty())
            return;

        Table table = tableOpt.get();
        table.setTip(tip);
        table.closeTable();

        Server found = null;
        for (Server s : restaurant.getServers()) {
            if (s.getAssignedTables().contains(table)) {
                s.addTip(tip);
                found = s;
                break;
            }
        }

        logTableToFile(table, found != null ? found.getName() : "Unknown", splitEvenly);
        incrementTotalOrderCount();
        if (found != null) {
            updateTipStats(found.getName(), tip);
        }
    }
    // the one in the closeTable, this is record the informations into the file
    private void logTableToFile(Table table, String serverName, boolean splitEvenly) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        sb.append("Date: ").append(LocalDateTime.now().format(formatter)).append("\n");
        sb.append("Table: ").append(table.getTableNumber()).append("\n");
        sb.append("Guests: ").append(table.getGuestCount()).append("\n");
        sb.append("Server: ").append(serverName).append("\n");
        sb.append("Order details:\n").append(table.toString());
        sb.append("Total: $").append(String.format("%.2f", table.getTableTotal())).append("\n");
        sb.append("Split method: ").append(splitEvenly ? "Evenly Split" : "Individual Orders").append("\n");

        if (splitEvenly && table.getGuestCount() > 0) {
            double perPerson = table.getTableTotal() / table.getGuestCount();
            sb.append("Each person pays: $").append(String.format("%.2f", perPerson)).append("\n");
        }

        sb.append("Tip: $").append(String.format("%.2f", table.getTip())).append("\n");
        sb.append("-----------------------------\n");

        try (FileWriter fw = new FileWriter("order_log.txt", true)) {
            fw.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Failed to write order log: " + e.getMessage());
        }
    }
    // calculate and increase the order count into the file
    private void incrementTotalOrderCount() {
        File file = new File("order_counter.txt");
        int count = 0;
        try {
            if (file.exists()) {
                List<String> lines = java.nio.file.Files.readAllLines(file.toPath());
                if (!lines.isEmpty()) {
                    count = Integer.parseInt(lines.get(0));
                }
            }
            count++;
            java.nio.file.Files.write(file.toPath(), String.valueOf(count).getBytes());
        } catch (IOException e) {
            System.out.println("Failed to update order count: " + e.getMessage());
        }
    }
    // get the all order counts
    public int getTotalHistoricalOrderCount() {
        File file = new File("order_counter.txt");
        try {
            if (file.exists()) {
                List<String> lines = java.nio.file.Files.readAllLines(file.toPath());
                if (!lines.isEmpty()) {
                    return Integer.parseInt(lines.get(0));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to read order count: " + e.getMessage());
        }
        return 0;
    }
    // the method that read the order_log.
    public void readOrderLog() {
        File file = new File("order_log.txt");
        if (!file.exists()) {
            System.out.println("No order log found.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Failed to read order log: " + e.getMessage());
        }
    }

    public Map<String, Integer> getSalesInPeriod(LocalDateTime start, LocalDateTime end) {
        Map<String, Integer> sales = new HashMap<>();
        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();

        for (Table table : restaurant.getTables()) {
            for (Order order : table.getOrders()) {
                LocalDate orderDate = order.getTimestamp().toLocalDate();
                if (!orderDate.isBefore(startDate) && !orderDate.isAfter(endDate)) {
                    for (OrderItem item : order.getItems()) {
                        String itemName = item.getMenuItem().getName();
                        sales.put(itemName, sales.getOrDefault(itemName, 0) + 1);
                    }
                }
            }
        }

        return sales;
    }

    public List<MenuItem> searchMenu(String keyword) {
        List<MenuItem> result = new ArrayList<>();
        for (MenuItem item : restaurant.getMenu()) {
            if (item.getName().toLowerCase().contains(keyword.toLowerCase())
                    || item.getCategory().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(item);
            }
        }
        return result;
    }
}

