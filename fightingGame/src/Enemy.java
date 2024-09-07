import java.util.ArrayList;
import java.util.List;

public class Enemy {
    private String name;
    private int health, damage;

    public Enemy(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.damage = damage;
    }

    public void display() {
        System.out.println("Name: " + this.name);
        System.out.println("Health: " + this.health);
        System.out.println("Damage: " + this.damage);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}

class EnemyManager {
    private List<Enemy> enemies = new ArrayList<>();

    public void addEnemy(String name, int health, int damage) {
        Enemy enemy = new Enemy(name, health, damage);
        enemies.add(enemy);
    }

    public void displayAllEnemies() {
        List<Enemy> enemiesCopy = new ArrayList<>(enemies);
        for (Enemy enemy : enemiesCopy) {
            enemy.display();
            System.out.println("------------");
        }
    }

    public Enemy getEnemyByName(String name) {
        for (Enemy enemy : enemies) {
            if (enemy.getName().equals(name)) {
                return enemy;
            }
        }
        return null;
    }

    public void updateEnemy(String oldName, String newName, int newHealth, int newDamage) {
        for (Enemy enemy : enemies) {
            if (enemy.getName().equals(oldName)) {
                enemy.setName(newName);
                enemy.setHealth(newHealth);
                enemy.setDamage(newDamage);
                break;
            }
        }
    }

    public void deleteEnemy(String name) {
        enemies.removeIf(enemy -> enemy.getName().equals(name));
    }
}