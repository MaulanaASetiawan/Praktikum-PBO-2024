import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Item implements Shop{
    private static Connection conn;
    public static void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/destinyclash", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String name;
    private int price;

    public Item(String name, int price){
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public int getPrice(){
        return price;
    }

    @Override
    public void setName(String name){
        this.name = name;
    }

    @Override
    public void setPrice(int price){
        this.price = price;
    }

    @Override
    public void addItems() throws SQLException, IOException {
        Shop shop = new Item("", 0);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter Item name: ");
        String name = input.readLine();
        System.out.print("Enter Item price: ");
        price = inputCheck(input.readLine());

        shop.setName(name);
        shop.setPrice(price);
        saveItems(shop);
    }

    @Override
    public void saveItems(Shop shop) throws SQLException, IOException {
        connect();
        String sql = "INSERT INTO shop (item_name, price) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, shop.getName());
            pstmt.setInt(2, shop.getPrice());
            pstmt.executeUpdate();
        }

        System.out.println("Item added successfully!");
        hold(1);
        menuShop();
    }

    @Override
    public void removeItems() {
        System.out.println("Item removed!");
    }

    @Override
    public void sellItems() {
        System.out.println("Item sold!");
    }

    @Override
    public void buyItems() {
        System.out.println("Item bought!");
    }

    @Override
    public void displayItem() {
        System.out.println("Name: " + name);
        System.out.println("Price: " + price);
    }

    public void menuShop() throws IOException, SQLException{
        clear();
        int choice;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("+-----------------------+");
        System.out.println("|  [1] Add Items        |");
        System.out.println("|  [2] View Items       |");
        System.out.println("|  [3] Update Items     |");
        System.out.println("|  [4] Delete Items     |");
        System.out.println("|  [0] Back             |");
        System.out.println("+-----------------------+");
        System.out.print("Enter your choice _> ");
        try {
            choice = Integer.parseInt(input.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            hold(1);
            menuShop();
            return;
        }
        switch(choice){
            case 1 -> addItems();
            case 2 -> {
                displayAllItems();
                System.out.println("Press Enter to go back to Admin menu...");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                br.readLine();
                menuShop();
            }
            // case 3 -> updateItems();
            // case 4 -> deleteItems();
            case 5 -> menuShop();
            case 0 -> EnemyManager.menuAdmin();
            default -> {
                System.out.println("Invalid choice! Please enter a valid number.");
                hold(1);
                menuShop();
            }
        }
    }

    private static void hold(int time){
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void clear(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static int inputCheck(String input) throws IOException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            return inputCheck(br.readLine());
        }
    }

    public static int countItems() throws SQLException {
        connect();
        String sql = "SELECT COUNT(*) FROM shop";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private static boolean isItemExists(int id) throws SQLException {
        String sql = "SELECT * FROM shop WHERE id_item = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void displayAllItems() throws SQLException, IOException {
        connect();

        if(countItems() == 0){
            System.out.println("No items to display!");
            hold(1);
            menuShop();
            return;
        }

        String sql = "SELECT * FROM shop";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id_item");
                String name = rs.getString("item_name");
                int price = rs.getInt("price");
                Shop shop = new Item(name, price);
                System.out.println("ID: " + id);
                shop.displayItem();
            }
        }
    }
}