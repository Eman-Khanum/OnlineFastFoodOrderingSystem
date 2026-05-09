import java.sql.*;
import java.util.Scanner;

public class FastFoodSystem {

    static final String URL = "jdbc:mysql://localhost:3306/fastfood_db";
    static final String USER = "root";
    static final String PASSWORD = "@@eman1";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found: " + e.getMessage());
            return;
        }
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("===========================================");
            System.out.println("  WELCOME TO FAST FOOD ORDERING SYSTEM   ");
            System.out.println("===========================================");
            Scanner sc = new Scanner(System.in);
            int customerId = addCustomer(conn, sc);
            int orderId = createOrder(conn, customerId);
            addOrderItems(conn, sc, orderId);
            updateOrderTotal(conn, orderId);
            processPayment(conn, sc, orderId);
            printReceipt(conn, orderId);
        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    static int addCustomer(Connection conn, Scanner sc) throws SQLException {
        System.out.println("\n--- CUSTOMER INFORMATION ---");
        System.out.print("Enter Your Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Phone Number: ");
        String phone = sc.nextLine();
        System.out.print("Enter Address: ");
        String address = sc.nextLine();
        String sql = "INSERT INTO Customer (name, phone, address) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, name);
        ps.setString(2, phone);
        ps.setString(3, address);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    static int createOrder(Connection conn, int customerId) throws SQLException {
        String sql = "INSERT INTO Orders (customer_id, total_amount) VALUES (?, 0)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, customerId);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        return rs.getInt(1);
    }

    static void addOrderItems(Connection conn, Scanner sc, int orderId) throws SQLException {
        System.out.println("\n--- OUR MENU ---");
        Statement st = conn.createStatement();
        ResultSet menu = st.executeQuery("SELECT * FROM Product");
        System.out.println("--------------------------------------------");
        System.out.printf("%-5s %-20s %-10s %s%n", "ID", "Item", "Price", "Category");
        System.out.println("--------------------------------------------");
        while (menu.next()) {
            System.out.printf("%-5d %-20s Rs.%-7.2f %s%n",
                menu.getInt("product_id"),
                menu.getString("product_name"),
                menu.getDouble("price"),
                menu.getString("category"));
        }
        System.out.println("--------------------------------------------");
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO OrderDetails (order_id, product_id, quantity, subtotal) VALUES (?, ?, ?, ?)");
        while (true) {
            System.out.print("\nEnter Product ID to order (0 to finish): ");
            int productId = sc.nextInt();
            if (productId == 0) break;
            System.out.print("Enter Quantity: ");
            int qty = sc.nextInt();
            PreparedStatement pricePs = conn.prepareStatement(
                "SELECT price FROM Product WHERE product_id=?");
            pricePs.setInt(1, productId);
            ResultSet priceRs = pricePs.executeQuery();
            if (priceRs.next()) {
                double price = priceRs.getDouble("price");
                double subtotal = price * qty;
                ps.setInt(1, orderId);
                ps.setInt(2, productId);
                ps.setInt(3, qty);
                ps.setDouble(4, subtotal);
                ps.executeUpdate();
                System.out.println("Item added successfully!");
            } else {
                System.out.println("Invalid Product ID, try again.");
            }
        }
    }

    static void updateOrderTotal(Connection conn, int orderId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE Orders SET total_amount = " +
            "(SELECT SUM(subtotal) FROM OrderDetails WHERE order_id = ?) " +
            "WHERE order_id = ?");
        ps.setInt(1, orderId);
        ps.setInt(2, orderId);
        ps.executeUpdate();
    }

    static void processPayment(Connection conn, Scanner sc, int orderId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT total_amount FROM Orders WHERE order_id=?");
        ps.setInt(1, orderId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        double total = rs.getDouble("total_amount");
        System.out.println("\n--- PAYMENT ---");
        System.out.println("Total Amount: Rs." + total);
        sc.nextLine();
        System.out.print("Payment Method (Cash/Card): ");
        String method = sc.nextLine();
        PreparedStatement payPs = conn.prepareStatement(
            "INSERT INTO Payment (order_id, amount, payment_method) VALUES (?, ?, ?)");
        payPs.setInt(1, orderId);
        payPs.setDouble(2, total);
        payPs.setString(3, method);
        payPs.executeUpdate();
        System.out.println("Payment Successful!");
    }

    static void printReceipt(Connection conn, int orderId) throws SQLException {
        System.out.println("\n===========================================");
        System.out.println("             *** RECEIPT ***               ");
        System.out.println("===========================================");
        PreparedStatement ps = conn.prepareStatement(
            "SELECT c.name, c.phone, c.address, o.order_id, o.order_date " +
            "FROM Customer c JOIN Orders o ON c.customer_id = o.customer_id " +
            "WHERE o.order_id = ?");
        ps.setInt(1, orderId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        System.out.println("Order ID   : " + rs.getInt("order_id"));
        System.out.println("Customer   : " + rs.getString("name"));
        System.out.println("Phone      : " + rs.getString("phone"));
        System.out.println("Address    : " + rs.getString("address"));
        System.out.println("Date       : " + rs.getString("order_date"));
        System.out.println("-------------------------------------------");
        System.out.printf("%-20s %-5s %-10s %s%n", "Item", "Qty", "Price", "Subtotal");
        System.out.println("-------------------------------------------");
        PreparedStatement itemPs = conn.prepareStatement(
            "SELECT p.product_name, p.price, od.quantity, od.subtotal " +
            "FROM OrderDetails od JOIN Product p ON od.product_id = p.product_id " +
            "WHERE od.order_id = ?");
        itemPs.setInt(1, orderId);
        ResultSet items = itemPs.executeQuery();
        while (items.next()) {
            System.out.printf("%-20s %-5d Rs.%-7.2f Rs.%.2f%n",
                items.getString("product_name"),
                items.getInt("quantity"),
                items.getDouble("price"),
                items.getDouble("subtotal"));
        }
        PreparedStatement totalPs = conn.prepareStatement(
            "SELECT o.total_amount, py.payment_method " +
            "FROM Orders o JOIN Payment py ON o.order_id = py.order_id " +
            "WHERE o.order_id = ?");
        totalPs.setInt(1, orderId);
        ResultSet totals = totalPs.executeQuery();
        totals.next();
        System.out.println("-------------------------------------------");
        System.out.println("TOTAL      : Rs." + totals.getDouble("total_amount"));
        System.out.println("Payment By : " + totals.getString("payment_method"));
        System.out.println("===========================================");
        System.out.println("      Thank you for your order!            ");
        System.out.println("===========================================");
    }
}