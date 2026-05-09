# Fast Food Ordering System 
### Java + JDBC + MySQL based Food Management System for streamlined customer orders and inventory tracking.

---

## ➜ Project Description

The **Fast Food Ordering System** is a professional, console-based Java application designed to modernize and automate the order-taking process for fast-food outlets. By integrating **JDBC (Java Database Connectivity)** with a **MySQL** backend, the system ensures data persistence and structural integrity for all transactions. The application manages the end-to-end lifecycle of an order—from capturing customer details and browsing a dynamic menu to real-time total calculations and digital receipt generation. This solution is built to minimize manual errors and provide a structured database-driven approach to food service management.

---

## ➜ Group Members & Contribution

| Name | CMS / ID | Section |
| :--- | :--- | :--- |
| **Eman Gul** | 023-25-0207 | B |

---

## ➜ Purpose & Scope

The system is engineered to demonstrate the practical application of Object-Oriented Programming and Database Management.

* **Database Persistence:** Unlike basic array-based systems, this project utilizes MySQL via JDBC to ensure that all customer records and order histories are permanently stored.
* **Relational Integrity:** Implements complex SQL relationships between `Customers`, `Orders`, `Products`, and `OrderDetails` to maintain data consistency.
* **Dynamic Calculations:** Automatically calculates subtotals and final bills using SQL aggregation functions (`SUM`), ensuring 100% accuracy in billing.
* **Exception Handling:** Uses robust `try-catch` blocks to manage `SQLException` and `ClassNotFoundException`, preventing crashes during database connection failures.
* **Professional Formatting:** Utilizes Java's `System.out.printf` to generate clean, tabular reports and receipts for the end-user.

---

## ➜ Main Modules

### 1. Customer Enrollment
Captures essential information (Name, Phone, Address) and assigns a unique ID to every customer. This data is instantly pushed to the `Customer` table for CRM purposes.

### 2. Interactive Menu & Selection
Fetches current product listings directly from the database. It allows users to view IDs, prices, and categories, ensuring the menu is always up-to-date with current stock/pricing.

### 3. Transaction Management
Handles the creation of unique Order IDs and manages the `OrderDetails` table, which links specific products to specific orders, supporting multiple items per transaction.

### 4. Payment & Billing
Provides a dedicated module for recording payment methods (Cash/Card) and generates a detailed, professional receipt including a summary of all items and total amount due.

---

## ➜ How to Run

###  Database Setup (MySQL)
1.  **Create the Database:** Execute the following in your MySQL Workbench:
    ```sql
    CREATE DATABASE fastfood_db;
    ```
2.  **Schema Setup:** Run your `.sql` script to create the necessary tables.
3.  **Credential Check:** Ensure the `URL`, `USER`, and `PASSWORD` constants in `FastFoodSystem.java` match your local MySQL configuration.

###  Compilation & Execution
1.  **Clone the Repository:**
    ```bash
    git clone (https://docs.github.com/en/repositories/creating-and-managing-repositories/about-repositories)
    ```
2.  **Add Connector:** Ensure `mysql-connector-java.jar` is added to your project's build path/libraries.
3.  **Compile:**
    ```bash
    javac FastFoodSystem.java
    ```
4.  **Run:**
    ```bash
    java -cp ".;mysql-connector-java.jar" FastFoodSystem
    ```
    *(Note: Use `:` instead of `;` in the classpath if you are on macOS/Linux.)*

---

## ➜ Project Links

* **GitHub Repository:** https://github.com/Eman-Khanum/OnlineFastFoodOrderingSystem
* **Demo Video:** [Insert your YouTube or Drive Video Link here]

---

## ➜ Project Structure

```text
├── database/
│   └── fastfood_schema.sql    # Database schema and initial menu data
├── src/
│   └── FastFoodSystem.java    # Main Java source code
├── README.md                  # Project documentation
└── assets/                    # Screenshots and project assets
