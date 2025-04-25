package finalProject;

import java.util.*;

public class Table {
	private int tableNumber;
	private List<Order> orders;
	private boolean isClosed;
	private double tipAmount;
	private int guestCount;
	
	public Table(int tableNumber) {
		this.tableNumber = tableNumber;
		this.orders = new ArrayList<>();
		this.isClosed = false;
		this.tipAmount = 0;
	}

	public int getTableNumber() {
		return tableNumber;
	}
	public void setGuestCount(int count) {
	    this.guestCount = count;
	}

	public int getGuestCount() {
	    return guestCount;
	}
	public List<Order> getOrders() {
		return orders;
	}

	public void addOrder(Order order) {
		orders.add(order);
	}

	public boolean isClosed() {
		return isClosed;
	}

	public void closeTable() {
		isClosed = true;
		
	}

	public double getTableTotal() {
		double total = 0;
		for (Order o : orders) {
			total += o.getTotal();
		}
		return total;
	}

	public void setTip(double tip) {
		this.tipAmount = tip;
	}

	public double getTip() {
		return tipAmount;
	}

	public double splitBillEvenly() {
		int customerCount = orders.size();
		return customerCount == 0 ? 0 : getTableTotal() / customerCount;
	}

	public List<Double> splitBillByOrder() {
		List<Double> result = new ArrayList<>();
		for (Order o : orders) {
			result.add(o.getTotal());
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Order o : orders) {
			sb.append(o.toString());
		}
		return sb.toString();
	}
}