import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    private PlayerManager playerManager = new PlayerManager();
    private EnemyManager enemyManager = new EnemyManager();
    private List<User> users = new ArrayList<>();
    public static void main(String[] args) throws IOException{
        clear();
        App main = new App();
        loading(3);
        clear();
        main.menuLog();

    }

    private void menu() throws IOException {
        clear();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("====================================");
        System.out.println("|       Welcome to the game!       |");
        System.out.println("====================================");
        System.out.println("| [1] Add Character                |");
        System.out.println("| [2] Display Character            |");
        System.out.println("| [3] Update Character             |");
        System.out.println("| [4] Delete Character             |");
        System.out.println("| [5] Fight                        |");
        System.out.println("| [0] Exit                         |");
        System.out.println("====================================");
        System.out.print("Enter your choice _> ");

        int choice;
        try {
            choice = Integer.parseInt(input.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            menu();
            return;
        }

        if (choice == 1) {
            clear();
            add();
        }
        else if(choice == 2){
            clear();
            display();
        }
        else if(choice == 3){
            clear();
            update();
        }
        else if(choice == 4){
            clear();
            delete();
        }
        else if(choice == 5){
            fight();
        }
        else if (choice == 0) {
            System.exit(0);
        } else {
            System.out.println("Invalid choice!");
        }
    }

    private void add() throws IOException{
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("========================");
        System.out.println("| [1] Add player       |");
        System.out.println("| [2] Add enemy        |");
        System.out.println("| [0] Back             |");
        System.out.println("========================");
        System.out.print("Enter your choice _> ");

        int choice;

        try {
            choice = Integer.parseInt(input.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            menu();
            return;
        }

        if (choice == 1) {
            System.out.print("Enter player name: ");
            String name = input.readLine();
            int health = 100;
            int damage = 10;

            playerManager.addPlayer(name, health, damage);
            System.out.println("Player added successfully!");

        } else if (choice == 2) {
            System.out.print("Enter enemy name: ");
            String name = input.readLine();
            int health = 100;
            int damage = 10;

            enemyManager.addEnemy(name, health, damage);
            System.out.println("Player added successfully!");
        }
        else if (choice == 0) {
            menu();
        }
        else {
            System.out.println("Invalid choice!");
        }
        System.out.println("Press enter to continue...");
        input.readLine();
        menu();
    }

    private void display() throws IOException{
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("======================");
        System.out.println("| [1] Display player |");
        System.out.println("| [2] Display enemy  |");
        System.out.println("| [0] Back           |");
        System.out.println("======================");
        System.out.print("Enter your choice _> ");

        int choice;

        try {
            choice = Integer.parseInt(input.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            menu();
            return;
        }

        if (choice == 1) {
            playerManager.displayAllPlayers();
        } else if (choice == 2) {
            enemyManager.displayAllEnemies();
        }
        else if (choice == 0) {
            menu();
        }
        else {
            System.out.println("Invalid choice!");
        }
        System.out.println("Press enter to continue...");
        input.readLine();
        menu();
    }

    private void update() throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("======================");
        System.out.println("| [1] Update player  |");
        System.out.println("| [2] Update enemy   |");
        System.out.println("| [0] Back           |");
        System.out.println("======================");
        System.out.print("Enter your choice _> ");

        int choice;
        try {
            choice = Integer.parseInt(input.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            menu();
            return;
        }

        switch(choice){
            case 1:
                clear();
                System.out.print("Enter Player name: ");
                String name = input.readLine();
                System.out.print("Enter new Player name: ");
                String newName = input.readLine();
                System.out.print("Enter new Player health: ");
                int newHealth = Integer.parseInt(input.readLine());
                System.out.print("Enter new Player damage: ");
                int newDamage = Integer.parseInt(input.readLine());
                playerManager.updatePlayer(name, newName, newHealth, newDamage);

                System.out.println("Player updated successfully!");
                menu();
                break;
            case 2:
                clear();
                System.out.print("Enter enemy name: ");
                String nameEnemy = input.readLine();
                System.out.print("Enter new enemy name: ");
                String newNameEnemy = input.readLine();
                System.out.print("Enter new enemy health: ");
                int newHealthEnemy = Integer.parseInt(input.readLine());
                System.out.print("Enter new enemy damage: ");
                int newDamageEnemy = Integer.parseInt(input.readLine());
                enemyManager.updateEnemy(nameEnemy, newNameEnemy, newHealthEnemy, newDamageEnemy);

                System.out.println("Enemy updated successfully!");
                menu();
                break;
            case 0:
                menu();
                break;
        }
    }

    private void delete() throws IOException{
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("======================");
        System.out.println("| [1] Delete player  |");
        System.out.println("| [2] Delete enemy   |");
        System.out.println("| [0] Back           |");
        System.out.println("======================");
        System.out.print("Enter your choice _> ");

        int choice;
        try {
            choice = Integer.parseInt(input.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            menu();
            return;
        }

        switch(choice){
            case 1:
                playerManager.displayAllPlayers();
                System.out.print("Enter enemy name: ");
                String namePlayer = input.readLine();
                playerManager.deletePlayer(namePlayer);

                System.out.println("Enemy deleted successfully!");
                menu();
                break;
            case 2:
                enemyManager.displayAllEnemies();
                System.out.print("Enter enemy name: ");
                String nameEnemy = input.readLine();
                enemyManager.deleteEnemy(nameEnemy);
                System.out.println("Enemy deleted successfully!");
                menu();
                break;
        }
    }

    private void fight() throws IOException{
        clear();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("1. Fight");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");

        int choice;

        try {
            choice = Integer.parseInt(input.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            menu();
            return;
        }

        if (choice == 1) {
            FightManager fightManager = new FightManager(playerManager, enemyManager);
            fightManager.startFight();
        }
        else if (choice == 0) {
            menu();
        }
        else {
            System.out.println("Invalid choice!");
        }
        System.out.println("Press enter to continue...");
        input.readLine();
        menu();
    }

    private void Reg() throws IOException{
        clear();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("====================================");
        System.out.println("|           Registration           |");
        System.out.println("====================================");
        System.out.print("Enter your username _> ");
        String username = input.readLine();
        if(username.equals("")){
            System.out.println("Username cannot be empty!");
            hold(1);
            Reg();
        }

        System.out.print("Enter your password _> ");
        String password = input.readLine();
        if(password.equals("")){
            System.out.println("Password cannot be empty!");
            hold(1);
            Reg();
        }
        
        loading(5);
        System.out.println("Registration success!");
        User user = new User(username, password);
        users.add(user);
        hold(1);
        Log();
    }

    private void Log() throws IOException{
        clear();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("====================================");
        System.out.println("|               Login              |");
        System.out.println("====================================");
        System.out.print("Enter your username _> ");
        String username = input.readLine();
        System.out.print("Enter your password _> ");
        String password = input.readLine();

        boolean isAdmin = username.equals("admin") && password.equals("admin");
        for(User user : users){
            if(username.equals(user.getName()) && password.equals(user.getPassword())){
                System.out.println("Login success!");
                loading(3);
                menu();
            }
            else{
                System.out.println("Login failed!");
                hold(2);
                Log();
            }
        }
        if(isAdmin){
            System.out.println("Login success!");
            loading(3);
            menu();
        }
        else{
            System.out.println("Login failed!");
            hold(2);
            Log();
        }
    }

    private void menuLog() throws IOException{
        clear();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("====================================");
        System.out.println("|       Welcome to the game!       |");
        System.out.println("====================================");
        System.out.println("| [1] Register                     |");
        System.out.println("| [0] Exit                         |");
        System.out.println("====================================");
        System.out.print("Enter your choice _> ");

        int choice;
        try {
            choice = Integer.parseInt(input.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            menu();
            return;
        }

        if (choice == 1) {
            Reg();
        }
        else if (choice == 0) {
            System.exit(0);
        } else {
            System.out.println("Invalid choice!");
        }
    }

    private static void clear(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void hold(int time){
        try {
            Thread.sleep(time * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void loading(int time){
        System.out.print("Loading ");
        for(int i = 0; i <= time ; i++){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(". ");
        }
        System.out.println();
    }
}