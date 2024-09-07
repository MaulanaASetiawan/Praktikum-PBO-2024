import java.util.*;

public class FightManager {
    private PlayerManager playerManager;
    private EnemyManager enemyManager;

    FightManager(PlayerManager playerManager, EnemyManager enemyManager) {
        this.playerManager = playerManager;
        this.enemyManager = enemyManager;
    }

    public void startFight() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter player name:");
        String playerName = scanner.nextLine();

        System.out.println("Enter enemy name:");
        String enemyName = scanner.nextLine();

        Player player = playerManager.getPlayerByName(playerName);
        Enemy enemy = enemyManager.getEnemyByName(enemyName);

        if (player == null || enemy == null) {
            System.out.println("Player or enemy not found!");
            return;
        }

        System.out.println(" [1] " + player.getName());
        System.out.println(" [2] " + enemy.getName());
        System.out.println("Choose who attacks first _> ");

        int firstAttacker = scanner.nextInt();

        System.out.println("Let the fight begin!");

        while (player.getHealth() > 0 && enemy.getHealth() > 0) {
            if (firstAttacker == 1) {
                playerAttack(player, enemy);
                if (enemy.getHealth() <= 0) {
                    System.out.println(player.getName() + " wins!");
                    break;
                }
                enemyAttack(player, enemy);
                if (player.getHealth() <= 0) {
                    System.out.println(enemy.getName() + " wins!");
                    break;
                }
            } else if (firstAttacker == 2) {
                enemyAttack(player, enemy);
                if (player.getHealth() <= 0) {
                    System.out.println(enemy.getName() + " wins!");
                    break;
                }
                playerAttack(player, enemy);
                if (enemy.getHealth() <= 0) {
                    System.out.println(player.getName() + " wins!");
                    break;
                }
            } else {
                System.out.println("Invalid choice for the first attacker. Fight aborted.");
                break;
            }
        }
    }

    private void playerAttack(Player player, Enemy enemy) {
        int damage = player.getDamage();
        enemy.setHealth(enemy.getHealth() - damage);
        System.out.println(player.getName() + " attacks " + enemy.getName() + " for " + damage + " damage.");
        displayHealth(player, enemy);
    }

    private void enemyAttack(Player player, Enemy enemy) {
        int damage = enemy.getDamage();
        player.setHealth(player.getHealth() - damage);
        System.out.println(enemy.getName() + " attacks " + player.getName() + " for " + damage + " damage.");
        displayHealth(player, enemy);
    }

    private void displayHealth(Player player, Enemy enemy) {
        System.out.println(player.getName() + " - Health: " + player.getHealth());
        System.out.println(enemy.getName() + " - Health: " + enemy.getHealth());
        System.out.println("---------------");
        hold(1);
    }

    private static void hold(int time){
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
