package finalProject;

public class MenuItem {
	private String name;
	private String category;
	private double price;
	private boolean canBeModified;
	private int timesOrdered;
	private double totalRevenue;
	
	public MenuItem(String name, String category, double price, boolean canBeModified) {
		this.name = name;
		this.category = category;
		this.price = price;
		this.canBeModified = canBeModified;
		this.timesOrdered = 0;
		this.totalRevenue = 0;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public double getPrice() {
		return price;
	}

	public boolean isModifiable() {
		return canBeModified;
	}

	public void recordSale() {
		timesOrdered++;
		totalRevenue += price;
	}

	public int getTimesOrdered() {
		return timesOrdered;
	}

	public double getTotalRevenue() {
		return totalRevenue;
	}

	@Override
	public String toString() {
		return name + " [" + category + "] - $" + price + (canBeModified ? " (can be modifed)" : "");
	}
}