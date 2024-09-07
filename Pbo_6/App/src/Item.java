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
    private static PreparedStatement pstmt;
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
    private int price, dmg_plus;

    public Item(String name, int dmg_plus, int price){
        this.name = name;
        this.dmg_plus = dmg_plus;
        this.price = price;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public int getDmg(){
        return dmg_plus;
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
    public void setDmg(int dmg){
        this.dmg_plus = dmg;
    }

    @Override
    public void setPrice(int price){
        this.price = price;
    }

    @Override
    public void addItems() throws SQLException, IOException {
        Shop shop = new Item("", 0, 0);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter Item name: ");
        String name = input.readLine();
        System.out.println("Enter damagw plus: ");
        int dmg_plus = inputCheck(input.readLine());
        System.out.print("Enter Item price: ");
        price = inputCheck(input.readLine());

        shop.setName(name);
        shop.setDmg(dmg_plus);
        shop.setPrice(price);
        saveItems(shop);
    }

    @Override
    public void saveItems(Shop shop) throws SQLException, IOException {
        connect();
        String sql = "INSERT INTO shop (item_name, dmg_plus, price) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, shop.getName());
            pstmt.setInt(2, shop.getDmg());
            pstmt.setInt(3, shop.getPrice());
            pstmt.executeUpdate();
        }

        System.out.println("Item added successfully!");
        hold(1);
        menuShop();
    }

    @Override
    public void updateItems() throws SQLException, IOException {
        connect();
        displayAllItems();
    
        if (countItems() == 0) {
            System.out.println("No enemies to update!");
            hold(1);
            menuShop();
            return;
        }
    
        int id = 0;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the ID of the enemy you want to update: ");
        try {
            id = Integer.parseInt(input.readLine());
            if (id == 0){
                menuShop();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            hold(1);
            menuShop();
            return;
        }

        if (!isItemExists(id)) {
            System.out.println("Items with ID " + id + " does not exist!");
            hold(1);
            menuShop();
            return;
        }
    
        Item item = new Item("", 0, 0);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter new Item name: ");
        String newName = br.readLine();
        System.out.println("Enter new damage plus: ");
        int newDmg_plus = inputCheck(br.readLine());
        System.out.print("Enter new Item price: ");
        int newPrice = inputCheck(br.readLine());

        item.setName(newName);
        item.setDmg(newDmg_plus);
        item.setPrice(newPrice);
    
        String sql = "UPDATE shop SET item_name = ?, dmg_plus = ? ,price = ? WHERE id_item = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getName());
            pstmt.setInt(2, item.getDmg());
            pstmt.setInt(3, item.getPrice());
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating Items: " + e.getMessage());
            hold(1);
            menuShop();
            return;
        }

        System.out.println();
        System.out.println("Items updated successfully!");
        hold(1);
        menuShop();
    }

    @Override
    public void removeItems() throws SQLException, IOException {
        connect();
        displayAllItems();

        if(countItems() == 0){
            System.out.println("No enemies to delete!");
            hold(1);
            menuShop();
            return;
        }

        int id = 0;
        int choice;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the ID of the Item you want to delete: ");
        try {
            choice = Integer.parseInt(input.readLine());
            if(choice == 0 || choice > countItems() || !isItemExists(choice)){
                menuShop();
                return;
            }else{
                id = choice;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            menuShop();
            return;
        }

        String sql = "DELETE FROM shop WHERE id_item = '"+id+"'";
        pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();

        System.out.println();
        System.out.println("Enemy deleted successfully!");
        hold(1);
        menuShop();
    }

    @Override
    public void sellItems(int userId) {
        System.out.println("Item sold!");
    }

    @Override
    public void buyItems(int userId, Shop shop) throws SQLException, IOException{
        displayAllItems();

        if(countItems() == 0){
            System.out.println("No items to buy!");
            hold(1);
            shopPlayer(userId, shop);
            return;
        }

        int id = 0;
        int choice;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the ID of the Item you want to buy: ");
        try {
            choice = Integer.parseInt(input.readLine());
            if(choice == 0 || choice > countItems() || !isItemExists(choice)){
                shopPlayer(userId, shop);
                return;
            }else{
                id = choice;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            shopPlayer(userId, shop);
            return;
        }

        
        String addInventory = "INSERT INTO inventory (item_name, dmg_plus) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(addInventory)) {
            pstmt.setString(1, shop.getName());
            pstmt.setInt(2, shop.getDmg());
            pstmt.executeUpdate();
        }
        
        String delItem = "DELETE FROM shop WHERE id_item = '"+id+"'";
        pstmt = conn.prepareStatement(delItem);
        pstmt.executeUpdate();
        
        System.out.println("Item bought successfully!");
        hold(1);
        shopPlayer(userId, shop);
    }

    @Override
    public void displayItem() {
        System.out.println("Name: " + name);
        System.out.println("Price: " + price);
    }

    public void shopPlayer(int userId, Shop shop) throws IOException, SQLException{
        clear();
        int choice;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("+-----------------------+");
        System.out.println("|  [1] Buy Items        |");
        System.out.println("|  [2] View Items       |");
        System.out.println("|  [0] Back             |");
        System.out.println("+-----------------------+");
        System.out.print("Enter your choice _> ");
        try {
            choice = Integer.parseInt(input.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            hold(1);
            shopPlayer(userId, shop);
            return;
        }
        switch(choice){
            case 1 -> buyItems(userId, shop);
            case 2 -> {
                displayAllItems();
                System.out.println("Press Enter to go back to Admin menu...");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                br.readLine();
                shopPlayer(userId, shop);
            }
            case 0 -> PlayerManager.menuUser(userId);
            default -> {
                System.out.println("Invalid choice! Please enter a valid number.");
                hold(1);
                shopPlayer(userId, shop);
            }
        }
    }

    public void menuShop() throws IOException, SQLException{
        clear();
        int choice;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("+-----------------------+");
        System.out.println("|  [1] Add Items        |");
        System.out.println("|  [2] View Items       |");
        System.out.println("|  [3] Update Items     |");
        System.out.println("|  [4] Remove Items     |");
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
            case 3 -> updateItems();
            case 4 -> removeItems();
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
            // menuShop();
            return;
        }

        String sql = "SELECT * FROM shop";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id_item");
                String name = rs.getString("item_name");
                int price = rs.getInt("price");
                Shop shop = new Item(name, dmg_plus, price);
                System.out.println("ID: " + id);
                shop.displayItem();
            }
        }
    }
}