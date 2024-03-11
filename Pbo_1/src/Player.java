import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int health, damage;

    public Player(String name, int health, int damage) {
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

class PlayerManager {
    private List<Player> players = new ArrayList<>();

    public void addPlayer(String name, int health, int damage) {
        Player player = new Player(name, health, damage);
        players.add(player);
    }

    public void displayAllPlayers() {
        List<Player> playersCopy = new ArrayList<>(players);
        for (Player player : playersCopy) {
            player.display();
            System.out.println("------------");
        }
    }

    public Player getPlayerbyIndex(int index) {
        return players.get(index);
    }

    public void updatePlayer(String oldName, String newName, int newHealth, int newDamage) {
        for (Player player : players) {
            if (player.getName().equals(oldName)) {
                player.setName(newName);
                player.setHealth(newHealth);
                player.setDamage(newDamage);
            }
        }
    }

    public void deletePlayer(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                players.remove(player);
                break;
            }
        }
    }
}