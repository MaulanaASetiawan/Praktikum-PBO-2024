import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Enemy extends Chara{
    private int stage;

    public Enemy(String name, int health, int damage,int xp, int stage) {
        super(name, health, damage, xp);
        this.stage = stage;
    }

    @Override
    public void setXp(String xp){
        try{
            this.xp = Integer.parseInt(xp);
        }catch (NumberFormatException e){
            System.out.println("Invalid input! Please enter a valid number.");
        }
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    @Override
    public void display() {
        System.out.println("+-------------------+");
        System.out.println("Stage: " + stage);
        System.out.println("+-------------------+");
        System.out.println("Name: " + name);
        System.out.println("Health: " + health);
        System.out.println("Damage: " + damage);
        System.out.println("Exp Given: " + xp);
        System.out.println("+-------------------+");
        System.out.println();
    }
}

class EnemyManager {
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

    public static void addEnemy() throws IOException, SQLException {
        Enemy enemy = new Enemy(null, 0, 0, 0, 0);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter Stage: ");
        int stage = inputCheck(input.readLine());
        System.out.print("Enter Enemy name: ");
        String name = input.readLine();
        System.out.print("Enter Enemy health: ");
        int health = inputCheck(input.readLine());
        System.out.print("Enter Enemy damage: ");
        int damage = inputCheck(input.readLine());
        System.out.print("Enter Exp given: ");
        String xp = input.readLine();

        enemy.setStage(stage);
        enemy.setName(name);
        enemy.setHealth(health);
        enemy.setDamage(damage);
        enemy.setXp(xp);

        saveEnemy(enemy);
    }

    public static void saveEnemy(Enemy enemy) throws SQLException, IOException {
        connect();
        String sql = "INSERT INTO enemy (stage, enemy_name, health, damage, xp) VALUES (?, ?, ?, ?, ?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, enemy.getStage());
        pstmt.setString(2, enemy.getName());
        pstmt.setInt(3, enemy.getHealth());
        pstmt.setInt(4, enemy.getDamage());
        pstmt.setInt(5, enemy.getXp());
        pstmt.executeUpdate();

        System.out.println();
        System.out.println("Enemy added successfully!");
        hold(2);
        menuAdmin();
    }
    
    public static void displayAllEnemies() throws SQLException, IOException {
        connect();

        if(countEnemies() == 0){
            System.out.println("No enemies to display!");
            hold(1);
            menuAdmin();
            return;
        }

        String sql = "SELECT * FROM enemy";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id_enemy");
                int stage = rs.getInt("stage");
                String name = rs.getString("enemy_name");
                int health = rs.getInt("health");
                int damage = rs.getInt("damage");
                int xp = rs.getInt("xp");
                Enemy enemy = new Enemy(name, health, damage, xp, stage);
                System.out.println("ID: " + id);
                enemy.display();
            }
        }
    }

    public static void updateEnemy() throws SQLException, IOException {
        connect();
        displayAllEnemies();
    
        if (countEnemies() == 0) {
            System.out.println("No enemies to update!");
            hold(1);
            menuAdmin();
            return;
        }
    
        int id = 0;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the ID of the enemy you want to update: ");
        try {
            id = Integer.parseInt(input.readLine());
            if (id == 0) {
                menuAdmin();
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            hold(1);
            menuAdmin();
            return;
        }

        if (!isEnemyExists(id)) {
            System.out.println("Enemy with ID " + id + " does not exist!");
            hold(1);
            menuAdmin();
            return;
        }
    
        Enemy enemy = new Enemy(null, 0, 0, 0, 0);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the new name for the enemy: ");
        String newName = br.readLine();
        System.out.print("Enter the new health for the enemy: ");
        int newHealth = inputCheck(br.readLine());
        System.out.print("Enter the new damage for the enemy: ");
        int newDamage = inputCheck(br.readLine());
        System.out.print("Enter the new xp given: ");
        int newXp = inputCheck(br.readLine());
    
        enemy.setName(newName);
        enemy.setHealth(newHealth);
        enemy.setDamage(newDamage);
        enemy.setXp(newXp);
    
        String sql = "UPDATE enemy SET enemy_name = ?, health = ?, damage = ?, xp = ? WHERE id_enemy = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, enemy.getName());
            pstmt.setInt(2, enemy.getHealth());
            pstmt.setInt(3, enemy.getDamage());
            pstmt.setInt(4, enemy.getXp());
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating enemy: " + e.getMessage());
            hold(1);
            menuAdmin();
            return;
        }

        System.out.println();
        System.out.println("Enemy updated successfully!");
        hold(1);
        menuAdmin();
    }
    

    public static void deleteEnemy() throws SQLException, IOException {
        connect();
        displayAllEnemies();

        if(countEnemies() == 0){
            System.out.println("No enemies to delete!");
            hold(1);
            menuAdmin();
            return;
        }

        int id = 0;
        int choice;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the ID of the enemy you want to delete: ");
        try {
            choice = Integer.parseInt(input.readLine());
            if(choice == 0){
                menuAdmin();
                return;
            }else{
                id = choice;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            menuAdmin();
            return;
        }

        String sql = "DELETE FROM enemy WHERE id_enemy = '"+id+"'";
        pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();

        System.out.println();
        System.out.println("Enemy deleted successfully!");
        hold(1);
        menuAdmin();
    }

    public static void menuAdmin() throws IOException, SQLException{
        clear();
        int choice;
        Item item = new Item(null, 0);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("+-----------------------+");
        System.out.println("|  [1] Add Enemy        |");
        System.out.println("|  [2] View Enemy       |");
        System.out.println("|  [3] Update Enemy     |");
        System.out.println("|  [4] Delete Enemy     |");
        System.out.println("|  [5] Shop Managements |");
        System.out.println("|  [0] Logout           |");
        System.out.println("+-----------------------+");
        System.out.print("Enter your choice _> ");
        try {
            choice = Integer.parseInt(input.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            hold(1);
            menuAdmin();
            return;
        }
        switch(choice){
            case 1 -> addEnemy();
            case 2 -> {
                displayAllEnemies();
                System.out.println("Press Enter to go back to Admin menu...");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                br.readLine();
                menuAdmin();
            }
            case 3 -> updateEnemy();
            case 4 -> deleteEnemy();
            case 5 -> item.menuShop();
            case 0 -> System.exit(0);
            default -> {
                System.out.println("Invalid choice! Please enter a valid number.");
                hold(1);
                menuAdmin();
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

    public static int countEnemies() throws SQLException {
        connect();
        String sql = "SELECT COUNT(*) FROM enemy";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private static boolean isEnemyExists(int id) throws SQLException {
        String sql = "SELECT * FROM enemy WHERE id_enemy = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}