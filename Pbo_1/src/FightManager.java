import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class FightManager {
    private PlayerManager playerManager;
    private EnemyManager enemyManager;

    FightManager(PlayerManager playerManager, EnemyManager enemyManager) {
        this.playerManager = playerManager;
        this.enemyManager = enemyManager;
    }

    public void startFight() throws IOException{
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter enemy name:");
        String enemyName = input.readLine();

        Player player = playerManager.getPlayerbyIndex(0);
        Enemy enemy = enemyManager.getEnemyByName(enemyName);

        if (enemy == null) {
            System.out.println("enemy not found!");
            return;
        }
        
        clear();
        System.out.println(player.getName() + " vs " + enemy.getName() + "!");
        
        System.out.println("Let the fight begin!");
        
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
    }

    private String getChoiceName(int choice) {
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

    private void determineWinner(Player player, Enemy enemy, int playerChoice, int enemyChoice) {
        if (playerChoice == enemyChoice) {
            System.out.println("It's a tie!");
        } else if ((playerChoice == 1 && enemyChoice == 3) || (playerChoice == 2 && enemyChoice == 1) || (playerChoice == 3 && enemyChoice == 2)) {
            System.out.println(player.getName() + " wins!");
            enemy.setHealth(enemy.getHealth() - player.getDamage());
        } else {
            System.out.println(enemy.getName() + " wins!");
            player.setHealth(player.getHealth() - enemy.getDamage());
        }

        displayHealth(player, enemy);
    }


    private void displayHealth(Player player, Enemy enemy) {
        System.out.println(player.getName() + " - Health: " + player.getHealth());
        System.out.println(enemy.getName() + " - Health: " + enemy.getHealth());
        System.out.println("---------------");
        hold(1);
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
