# Final-project-335

RESTAURANT MANAGEMENT SYSTEM

**CSc 335 – FINAL PROJECT**  
**SPRING 2025**  
**TEAM MEMBERS**: Haobin Yan, 
--------------------------------------------
## **PROJECT OVERVIEW**

This is a Java-based Restaurant Management System built using **MVC architecture**. The application allows restaurant staff to:

- Manage tables and assign servers
- Create and track orders
- Handle menu items and modifications
- Calculate bills (individually or split evenly)
- Record tips and analyze sales performance

A **Java GUI** is included to support real-time interactions.

-------------------------------------------

## ** KEY FEATURES**

- Graphical User Interface
- MVC Separation: Model, View, Controller
- File Persistence for orders, tips, and menu
- Sales statistics by count and revenue
- Tip tracking and top server by tips
- Full support for order log with split bill logic
- Searchable menu system

HOW TO USE THE GUI (INTERFACE WALKTHROUGH)

The GUI of the Restaurant Manager system provides a complete point-of-sale interface. Here's what each section does:

1. TOP PANEL – Order Selection

Item: Drop-down list of all menu items. Select the item you want to add to the cart.

Option: Shows pre-defined modification options based on the selected item (e.g., "Medium", "No Onion").

Note: Text field where users can add additional notes or customization (e.g., "extra spicy").

Add to Cart: Adds the selected item and any modifications to the current cart.

Delete Item From Cart: Opens a prompt to remove an item from the current cart by its index.


2.ADD NEW ITEM PANEL

New Item / Price / Category: Enter details to create a new custom menu item.

Editable: Checkbox indicating whether the new item can have modifications.

Add Item: Adds the new item to the menu list.

3. CENTER – MENU DISPLAY
Displays the full current menu. Each item is shown with:

Name

Category

Price

Modification availability (e.g., “can be modified”)


4. BOTTOM PANEL – Order Checkout & Operations

Total Orders Ever: Shows the cumulative number of orders recorded (stored in file).

Guests: Number of diners at the table.

Table: Table number for this order.

Server: Server’s name who is taking this order.

Split Bill: Checkbox to toggle between split-evenly billing or individual order billing.

Checkout: Finalizes the order, logs it to file, and resets the cart.

Tip: Prompted via dialog box during checkout; used to update server tip stats.


5. SEARCH AND REPORTING

Keyword: Search field to look up menu items by name or category.

Search Menu: Filters the menu using the keyword entered.

View Log: Displays all historical order logs from file.

Show Tip Stats: Displays total tips earned by each server.

Top Sales: Lists top-performing menu items based on total revenue.

Sales In Period: Prompts for start/end date and shows total sales of each item during that range.


6. SPECIAL FEATURES

Cart logic supports editing and removal before checkout.

Menu dynamically updates when new items are added.

Supports real-time stats for servers and sales analysis.

Logs are persistently saved to files like order_log.txt, tips_data.txt.














