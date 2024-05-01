import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FightManager {
    public static void StartFight(int userId) throws IOException {
        clear();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        if (!hasPlayer(userId)) {
            System.out.println("You need to create a player character first!");
            input.readLine();
            return;
        }
        
        System.out.print("Choose Stage: ");
        int stage = Integer.parseInt(input.readLine());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/destinyclash", "root", "")) {
            ArrayList<Enemy> enemies = getEnemyData(conn, stage);
    
            if (enemies.isEmpty()) {
                System.out.println("No enemies found for the selected stage!");
                return;
            }
    
            System.out.println("Available enemies in Stage " + stage + ":");
            displayEnemiesInStage(enemies, stage);
    
            System.out.print("Choose an enemy to fight (enter the enemy ID): ");
            int enemyIndex = Integer.parseInt(input.readLine()) - 1;
    
            if (enemyIndex < 0 || enemyIndex >= enemies.size()) {
                System.out.println("Invalid enemy selection!");
                hold(1);
                return;
            }
            
            Player player = getPlayerData(conn, userId).get(0);
            Enemy enemy = enemies.get(enemyIndex);
            System.out.println("You are fighting " + enemy.getName() + " at Stage " + enemy.getStage() + "!");
            hold(1);

            while (player.getHealth() > 0 && enemy.getHealth() > 0) {
                clear();
                System.out.println("Choose your move:");
                System.out.println("[1] Rock");
                System.out.println("[2] Paper");
                System.out.println("[3] Scissors");
                System.out.print("Enter your choice -> ");
                int choice = Integer.parseInt(input.readLine());

                if (choice < 1 || choice > 3) {
                    System.out.println("Invalid choice! Please enter a valid number.");
                    hold(1);
                    continue;
                }

                int enemyChoice = new Random().nextInt(3) + 1;
                System.out.println("You chose " + getChoiceName(choice));
                System.out.println(enemy.getName() + " chose " + getChoiceName(enemyChoice));
                hold(1);

                determineWinner(player, enemy, choice, enemyChoice);
                hold(1);
            }

            if(player.getHealth() <= 0){
                System.out.println("You lost!");
                hold(1);
            } else {
                System.out.println("=".repeat(10));
                System.out.println("You won!");
                player.setXp(player.getXp() + enemy.getXp());
                System.out.println("You gained " + enemy.getXp() + " XP!");
                System.out.println("=".repeat(10));

                String sql = "UPDATE player SET xp = ? WHERE id_user = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, player.getXp());
                pstmt.setInt(2, userId);
                pstmt.executeUpdate();
                
                hold(1);
                if(player.getXp() >= 100){
                    player.setLevel(player.getLevel() + 1);
                    player.setHealth(player.getHealth() + 10);
                    player.setDamage(player.getDamage() + 5);
                    player.setXp(0);
                    sql = "UPDATE player SET level = ?, health = ?, damage = ?, xp = ? WHERE id_user = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, player.getLevel());
                    pstmt.setInt(2, player.getHealth());
                    pstmt.setInt(3, player.getDamage());
                    pstmt.setInt(4, player.getXp());
                    pstmt.setInt(5, userId);
                    pstmt.executeUpdate();

                    System.out.println("Congratulations! You leveled up!");
                    System.out.println("You are now level " + player.getLevel() + "!");
                    System.out.println("=".repeat(10));
                    hold(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Player> getPlayerData(Connection conn, int userId) throws SQLException {
        ArrayList<Player> players = new ArrayList<>();
        String query = "SELECT * FROM player WHERE id_user = '" + userId + "'";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Player player = new Player(rs.getString("player_name"), 
                                            rs.getInt("health"), 
                                            rs.getInt("damage"), 
                                            rs.getInt("xp"),
                                            rs.getInt("level"));
                players.add(player);
            }
        }
        return players;
    }

    private static ArrayList<Enemy> getEnemyData(Connection conn, int stage) throws SQLException {
        ArrayList<Enemy> enemies = new ArrayList<>();
        String query = "SELECT * FROM enemy WHERE stage = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, stage);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Enemy enemy = new Enemy(rs.getString("enemy_name"), 
                                         rs.getInt("health"), 
                                         rs.getInt("damage"), 
                                         rs.getInt("xp"), 
                                         rs.getInt("stage"));
                enemies.add(enemy);
            }
        }
        return enemies;
    }

    private static String getChoiceName(int choice) {
        switch (choice) {
            case 1:
                return "Rock";
            case 2:
                return "Paper";
            case 3:
                return "Scissors";
            default:
                return "";
        }
    }

    private static void determineWinner(Player player, Enemy enemy, int playerChoice, int enemyChoice) {
        if (playerChoice == enemyChoice) {
            System.out.println("It's a tie!");
        } else if ((playerChoice == 1 && enemyChoice == 3) || 
                    (playerChoice == 2 && enemyChoice == 1) || 
                    (playerChoice == 3 && enemyChoice == 2)) {

            System.out.println("+---------------------------+");
            System.out.println(player.getName() + " wins!");
            System.out.println("+---------------------------+");
            System.out.println();
            enemy.setHealth(enemy.getHealth() - player.getDamage());
        } else {
            System.out.println("+---------------------------+");
            System.out.println(enemy.getName() + " wins!");
            System.out.println("+---------------------------+");
            System.out.println();
            player.setHealth(player.getHealth() - enemy.getDamage());
        }

        displayHealth(player, enemy);
    }


    private static void displayHealth(Player player, Enemy enemy) {
        System.out.println();
        System.out.println("+---------------------------+");
        System.out.println(player.getName() + " - Health: " + player.getHealth());
        System.out.println(enemy.getName() + " - Health: " + enemy.getHealth());
        System.out.println("+---------------------------+");
        hold(1);
    }

    private static void displayEnemiesInStage(ArrayList<Enemy> enemies, int stage) {
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemy.getStage() == stage) {
                System.out.println((i + 1) + ". " + enemy.getName() + " - Health: " + enemy.getHealth() + " - Damage: " + enemy.getDamage());
            }
        }
    }
    public static boolean hasPlayer(int userId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/destinyclash", "root", "")) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    

    private static void hold(int time) {
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}