import java.util.*;

public class App {
    private PlayerManager playerManager = new PlayerManager();
    private EnemyManager enemyManager = new EnemyManager();
    private List<User> users = new ArrayList<>();
    public static void main(String[] args) {
        clear();
        App main = new App();
        loading(3);
        clear();
        main.menuLog();

    }

    private void menu() {
        clear();
        Scanner scanner = new Scanner(System.in);
        System.out.println("====================================");
        System.out.println("|       Welcome to the game!       |");
        System.out.println("====================================");
        System.out.println("| [1] Add Character                |");
        System.out.println("| [2] Display Character            |");
        System.out.println("| [3] Fight                        |");
        System.out.println("| [0] Exit                         |");
        System.out.println("====================================");
        System.out.print("Enter your choice _> ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
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
           fight();
       }
        else if (choice == 0) {
            System.exit(0);
        } else {
            System.out.println("Invalid choice!");
        }
    }

    private void add() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("========================");
        System.out.println("| [1] Add player       |");
        System.out.println("| [2] Add enemy        |");
        System.out.println("| [0] Back             |");
        System.out.println("========================");
        System.out.print("Enter your choice _> ");

        int choice;

        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            menu();
            return;
        }

        if (choice == 1) {
            System.out.print("Enter player name: ");
            String name = scanner.nextLine();
            System.out.print("Enter player health: ");
            int health = Integer.parseInt(scanner.nextLine());

            if(health < 0){
                System.out.println("Invalid health!");
                hold(1);
                menu();
            } else if(health > 100){
                System.out.println("Health To High, Health should be less than 100!");
                hold(1);
                menu();
            }

            System.out.print("Enter player damage: ");
            int damage = Integer.parseInt(scanner.nextLine());

            if(damage < 0){
                System.out.println("Invalid damage!");
                hold(1);
                menu();
            } else if(damage > 20){
                System.out.println("Damage To High, Damage should be less than 20!");
                hold(1);
                menu();
            }

            playerManager.addPlayer(name, health, damage);
            System.out.println("Player added successfully!");

        } else if (choice == 2) {
            System.out.print("Enter enemy name: ");
            String name = scanner.nextLine();
            System.out.print("Enter enemy health: ");
            int health = Integer.parseInt(scanner.nextLine());

            if(health < 0){
                System.out.println("Invalid health!");
                hold(1);
                menu();
            } else if(health > 100){
                System.out.println("Health To High, Health should be less than 100!");
                hold(1);
                menu();
            }

            System.out.print("Enter enemy damage: ");
            int damage = Integer.parseInt(scanner.nextLine());

            if(damage < 0){
                System.out.println("Invalid damage!");
                hold(1);
                menu();
            } else if(damage > 20){
                System.out.println("Damage To High, Damage should be less than 20!");
                hold(1);
                menu();
            }

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
        scanner.nextLine();
        menu();
    }

    private void display(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("======================");
        System.out.println("| [1] Display player |");
        System.out.println("| [2] Display enemy  |");
        System.out.println("| [0] Back           |");
        System.out.println("======================");
        System.out.print("Enter your choice _> ");

        int choice;

        try {
            choice = Integer.parseInt(scanner.nextLine());
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
        scanner.nextLine();
        menu();
    }

    private void fight(){
        clear();
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Fight");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");

        int choice;

        try {
            choice = Integer.parseInt(scanner.nextLine());
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
        scanner.nextLine();
        menu();
    }

    private void Reg(){
        clear();
        Scanner scanner = new Scanner(System.in);
        System.out.println("====================================");
        System.out.println("|       Welcome to the game!       |");
        System.out.println("====================================");
        System.out.print("Enter your username _> ");
        String username = scanner.nextLine();
        if(username.equals("")){
            System.out.println("Username cannot be empty!");
            hold(1);
            Reg();
        }

        System.out.print("Enter your password _> ");
        String password = scanner.nextLine();
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
        menuLog();
    }

    private void Log(){
        clear();
        Scanner scanner = new Scanner(System.in);
        System.out.println("====================================");
        System.out.println("|       Welcome to the game!       |");
        System.out.println("====================================");
        System.out.print("Enter your username _> ");
        String username = scanner.nextLine();
        System.out.print("Enter your password _> ");
        String password = scanner.nextLine();
        for(User user : users){
            if(username.equals(user.getName()) && password.equals(user.getPassword())){
                loading(3);
                System.out.println("Login success!");
                menu();
            }//nanti tambahin menu admin dan user khusus
            else{
                System.out.println("Login failed!");
                hold(2);
                Log();
            }
        }
    }

    private void menuLog(){
        clear();
        Scanner scanner = new Scanner(System.in);
        System.out.println("====================================");
        System.out.println("|       Welcome to the game!       |");
        System.out.println("====================================");
        System.out.println("| [1] Login                        |");
        System.out.println("| [2] Register                     |");
        System.out.println("| [0] Exit                         |");
        System.out.println("====================================");
        System.out.print("Enter your choice _> ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            menu();
            return;
        }

        if (choice == 1) {
            Log();
        }
        else if(choice == 2){
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

    // public static void print(){
    //     String text = "Loading";
    //     String Alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    //     for (int i = 0; i < text.length(); i++) {
    //         for (int j = 0; j < Alphabet.length(); j++) {
    //             String prefix = Alphabet.substring(j, j + 1);
    //             String cur = prefix + text.substring(0, i + 1);
                
    //             if (cur.equals(text)) {
    //                 break;
    //             }
    //             clear();
    //             System.out.println(cur);
    //             try {
    //                 Thread.sleep(50);
    //             } catch (InterruptedException e) {
    //                 e.printStackTrace();
    //             }
    //         }
    //     }
    // }
}