

public class OrderItem {
	private MenuItem menuItem;
	private String modification;

	public OrderItem(MenuItem item, String mod) {
		this.menuItem = item;
		this.modification = mod;
		this.menuItem.recordSale();
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public String getModification() {
		return modification;
	}

	public double getItemTotal() {
		return menuItem.getPrice();
	}

	@Override
	public String toString() {
	    return menuItem.getName() + 
	        (modification != null && !modification.isEmpty() ? "（" + modification + "）" : "") + 
	        ": $" + getItemTotal();
	}
}
