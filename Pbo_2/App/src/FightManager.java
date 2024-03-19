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
        if (!hasPlayer(userId)) {
            System.out.println("You need to create a player character first!");
            hold(1);
            return;
        }

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
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
                return;
            }
            
            
            Player player = getPlayerData(conn).get(0);
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
                System.out.println("You won!");
                hold(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Player> getPlayerData(Connection conn) throws SQLException {
        ArrayList<Player> players = new ArrayList<>();
        String query = "SELECT * FROM player";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Player player = new Player(rs.getString("player_name"), 
                                           rs.getInt("health"), 
                                           rs.getInt("damage"));
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
                Enemy enemy = new Enemy(rs.getInt("stage"), 
                                        rs.getString("enemy_name"), 
                                        rs.getInt("health"), 
                                        rs.getInt("damage"));
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
        System.out.println(player.getName() + " - Health: " + player.getHealth());
        System.out.println(enemy.getName() + " - Health: " + enemy.getHealth());
        System.out.println("---------------");
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
