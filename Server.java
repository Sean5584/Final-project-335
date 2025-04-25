package finalProject;

import java.util.*;

public class Server {
	private String name;
	private List<Table> assignedTables;
	private double totalTips;

	public Server(String name) {
		this.name = name;
		this.assignedTables = new ArrayList<>();
		this.totalTips = 0;
	}

	public String getName() {
		return name;
	}

	public List<Table> getAssignedTables() {
		return assignedTables;
	}

	public void assignTable(Table table) {
		assignedTables.add(table);
	}

	public void addTip(double tip) {
		totalTips += tip;
	}
 
	public double getTotalTips() {
		return totalTips;
	}
}
