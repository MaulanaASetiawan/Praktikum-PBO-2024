import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Player extends Chara{
    private int level;
    public Player(String name, int health, int damage, int xp, int level) {
        super(name, health, damage, xp);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void display() {
        System.out.println("=".repeat(15));
        System.out.println("Player Info");
        System.out.println("=".repeat(15));
        System.out.println("Level: " + level);
        super.display();
        System.out.println("=".repeat(15));
        System.out.println();
    }
}

class PlayerManager {
    private static Connection conn;
    private static PreparedStatement pstmt;

    public static void connect(){
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/destinyclash", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        if(conn != null){
            try{
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addPlayer(int userId) throws IOException, SQLException {
        Player player = new Player(null, 0, 0, 0, 0);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        if(hasPlayer(userId)){
            System.out.println("You already have a player!");
            hold(2);
            menuUser(userId);
        }

        System.out.print("Enter Player name: ");
        String name = input.readLine();
        int health = 100;
        int damage = 10;
        int xp = 0;
        int level = 1;
        
        player.setName(name);
        player.setHealth(health);
        player.setDamage(damage);
        player.setXp(xp);
        player.setLevel(level);

        savePlayer(userId, player);
    }
    public static void savePlayer(int userId, Player player) throws SQLException, IOException {
        connect();
        String sql = "INSERT INTO player (id_player,id_user,player_name, health, damage, xp, level) VALUES ('"+userId+"',?, ?, ?, ?, ?, ?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);
        pstmt.setString(2, player.getName());
        pstmt.setInt(3, player.getHealth());
        pstmt.setInt(4, player.getDamage());
        pstmt.setInt(5, player.getXp());
        pstmt.setInt(6, player.getLevel());
        pstmt.executeUpdate();

        System.out.println();
        System.out.println("Player added successfully!");
        hold(2);
        menuUser(userId);
    }

    public static void displayAllPlayers(int userId) throws SQLException, IOException {
        connect();
        String sql = "SELECT * FROM player WHERE id_user = '"+userId+"'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("player_name");
                int health = rs.getInt("health");
                int damage = rs.getInt("damage");
                int xp = rs.getInt("xp");
                int level = rs.getInt("level");

                Player player = new Player(name, health, damage, xp, level);
                player.display();
            }
        }
    }

    public static void updatePlayer(int userId) throws SQLException, IOException {
        connect();
        displayAllPlayers(userId);

        if(!hasPlayer(userId)){
            System.out.println("You don't have any player!");
            hold(1);
            menuUser(userId);
        }

        Player player = new Player(null, 0, 0, 0, 0);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter New Player name: ");
        String name = input.readLine();

        System.out.print("Enter new health: ");
        int health = inputCheck(input.readLine());
        if(health < 0 || health > 100){
            System.out.println("Invalid, please enter a valid number between 0 and 100.");
            hold(1);
            updatePlayer(userId);
        }

        System.out.print("Enter new damage: ");
        int damage = inputCheck(input.readLine());
        if(damage < 0 || damage > 100){
            System.out.println("Invalid, please enter a valid number between 0 and 100.");
            hold(1);
            updatePlayer(userId);
        }

        player.setName(name);
        player.setHealth(health);
        player.setDamage(damage);

        String sql = "UPDATE player SET player_name = ?, health = ?, damage = ? WHERE id_player = '"+userId+"'";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, player.getName());
        pstmt.setInt(2, player.getHealth());
        pstmt.setInt(3, player.getDamage());
        pstmt.executeUpdate();

        System.out.println();
        System.out.println("Player updated successfully!");
        hold(2);
        menuUser(userId);
    }

    public static void deletePlayer(int userId) throws SQLException, IOException {
        connect();
        displayAllPlayers(userId);

        if(!hasPlayer(userId)){
            System.out.println("You don't have any player!");
            hold(1);
            menuUser(userId);
        }
        
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter Player name: ");
        String name = input.readLine();

        String sql = "DELETE FROM player WHERE player_name = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.executeUpdate();

        System.out.println();
        System.out.println("Player deleted successfully!");
        hold(2);
        menuUser(userId);
    }

    public static void menuUser(int userId) throws IOException, SQLException {
        clear();
        int choice;
        try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("+-------------------+");
            System.out.println("|  [1] Fight        |");
            System.out.println("|  [2] Add Player   |");
            System.out.println("|  [3] Update Player|");
            System.out.println("|  [4] Delete Player|");
            System.out.println("|  [5] Player Stat  |");
            System.out.println("+-------------------+");
            System.out.println("|    Coming Soon    |");
            System.out.println("|        Shop       |");
            System.out.println("|     Inventory     |");
            System.out.println("+-------------------+");
            System.out.println("|  [0] Exit         |");
            System.out.println("+-------------------+");
            System.out.print("Enter your choice _> ");

            try{
                choice = Integer.parseInt(input.readLine());
            }catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                hold(1);
                menuUser(userId);
                return;
            }

            switch (choice) {
                case 1 -> {
                    FightManager.StartFight(userId);
                    menuUser(userId);
                }
                case 2 -> addPlayer(userId);
                case 3 -> updatePlayer(userId);
                case 4 -> deletePlayer(userId);
                case 5 -> {
                    clear();
                    displayAllPlayers(userId); 
                    input.readLine();
                    menuUser(userId);
                }
                case 0 -> System.exit(0);
                default -> {
                    System.out.println("Invalid choice! Please enter a valid number.");
                    hold(1);
                    menuUser(userId);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
            menuUser(userId);
        }
    }

    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void hold(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static int inputCheck(String input) throws IOException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
            System.out.println();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            return inputCheck(br.readLine());
        }
    }

    public static boolean hasPlayer(int userId) throws SQLException {
        connect();
        String sql = "SELECT COUNT(*) AS count FROM player WHERE id_user = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }
}