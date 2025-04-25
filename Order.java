
import java.time.LocalDateTime;
import java.util.*;
// This is the class that for Order, we used LocalDateTime to record the Order's time
// And others are just simple methods.
public class Order {
	private List<OrderItem> items = new ArrayList<>();
    private LocalDateTime timestamp;

    public Order() {
        this.timestamp = LocalDateTime.now();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
	public void addItem(OrderItem item) {
		items.add(item);
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public double getTotal() {
		double total = 0;
		for (OrderItem item : items) {
			total += item.getItemTotal();
		}
		return total;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (OrderItem item : items) {
			sb.append(" - ").append(item.toString()).append("\n");
		}
		return sb.toString();
	}
}
