import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Enemy {
    private String name;
    private int health, damage, stage;

    public Enemy(int stage, String name, int health, int damage) {
        this.stage = stage;
        this.name = name;
        this.health = health;
        this.damage = damage;
    }

    public int getStage() {
        return stage;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void display() {
        System.out.println("+-------------------+");
        System.out.println("Stage: " + stage);
        System.out.println("Name: " + name);
        System.out.println("Health: " + health);
        System.out.println("Damage: " + damage);
        System.out.println("+-------------------+");
        
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
        Enemy enemy = new Enemy(0, null, 0, 0);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter Stage: ");
        int stage = inputCheck(input.readLine());
        System.out.print("Enter Enemy name: ");
        String name = input.readLine();
        System.out.print("Enter Enemy health: ");
        int health = inputCheck(input.readLine());
        System.out.print("Enter Enemy damage: ");
        int damage = inputCheck(input.readLine());

        enemy.setStage(stage);
        enemy.setName(name);
        enemy.setHealth(health);
        enemy.setDamage(damage);

        saveEnemy(enemy);
    }

    public static void saveEnemy(Enemy enemy) throws SQLException, IOException {
        connect();
        String sql = "INSERT INTO enemy (stage, enemy_name, health, damage) VALUES (?, ?, ?, ?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, enemy.getStage());
        pstmt.setString(2, enemy.getName());
        pstmt.setInt(3, enemy.getHealth());
        pstmt.setInt(4, enemy.getDamage());
        pstmt.executeUpdate();

        System.out.println("Enemy added successfully!");
        hold(2);
        menuAdmin();
    }
    
    public static void displayAllEnemies() throws SQLException, IOException {
        connect();
        String sql = "SELECT * FROM enemy";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id_enemy");
                int stage = rs.getInt("stage");
                String name = rs.getString("enemy_name");
                int health = rs.getInt("health");
                int damage = rs.getInt("damage");
                Enemy enemy = new Enemy(stage, name, health, damage);
                System.out.println("ID: " + id);
                enemy.display();
            }
        }
    }

    public static void updateEnemy() throws SQLException, IOException {
        connect();
        displayAllEnemies();
        int id = 0;
        int choice;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the ID of the enemy you want to update: ");
        try {
            choice = Integer.parseInt(input.readLine());
            if(choice == 0){
                menuAdmin();
            }else{
                id = choice;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            hold(1);
            menuAdmin();
            return;
        }
        
        Enemy enemy = new Enemy(0, null, 0, 0);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the new name for the enemy: ");
        String newName = br.readLine();
        System.out.print("Enter the new health for the enemy: ");
        int newHealth = inputCheck(br.readLine());
        System.out.print("Enter the new damage for the enemy: ");
        int newDamage = inputCheck(br.readLine());

        enemy.setName(newName);
        enemy.setHealth(newHealth);
        enemy.setDamage(newDamage);
    
        String sql = "UPDATE enemy SET enemy_name = ?, health = ?, damage = ? WHERE id_enemy = '"+id+"'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, enemy.getName());
            pstmt.setInt(2, enemy.getHealth());
            pstmt.setInt(3, enemy.getDamage());
            pstmt.executeUpdate();
        }
        System.out.println("Enemy updated successfully!");
        hold(1);
        menuAdmin();
    }
    

    public static void deleteEnemy() throws SQLException, IOException {
        connect();
        displayAllEnemies();
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
        System.out.println("Enemy deleted successfully!");
        hold(1);
        menuAdmin();
    }

    public static void menuAdmin() throws IOException, SQLException{
        clear();
        int choice;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("+-------------------+");
        System.out.println("|  [1] Add Enemy    |");
        System.out.println("|  [2] View Enemy   |");
        System.out.println("|  [3] Update Enemy |");
        System.out.println("|  [4] Delete Enemy |");
        System.out.println("|  [0] Logout       |");
        System.out.println("+-------------------+");
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
}