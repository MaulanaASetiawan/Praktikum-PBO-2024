import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    static Connection conn;
    static Statement stmt;
    static ResultSet rs;
    private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private static EnemyManager enemyManager = new EnemyManager();
    private static PlayerManager playerManager = new PlayerManager();
    private static int userId;

    public static void main(String[] args) throws SQLException, IOException {
        try{
            conn = DbConnection.getConnection();
            stmt = conn.createStatement();

            while(!conn.isClosed()){
                menuLog();
            }
            stmt.close();
            DbConnection.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    private static void menuLog() throws IOException, SQLException {
        clear();
        int choice;
        System.out.println("+------------------+");
        System.out.println("|  [1] Login       |");
        System.out.println("|  [2] Register    |");
        System.out.println("|  [0] Exit        |");
        System.out.println("+------------------+");
        System.out.print("Enter your choice _> ");
        try {
            choice = Integer.parseInt(input.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice! Please enter a valid number.");
            menuLog();
            return;
        }
        switch(choice){
            case 1 -> login();
            case 2 -> Reg();
            case 0 -> System.exit(0);
        }
    }

    @SuppressWarnings("static-access") // ni kalo diliat dari stack overflow, ini buat nge-disable warning static dari IDE bang
    private static void login() throws IOException, SQLException {
        clear();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("+-------------------+");
        System.out.println("|   DESTINY CLASH   |");
        System.out.println("+-------------------+");
        System.out.print("Enter your username _> ");
        String username = input.readLine();
        System.out.print("Enter your password _> ");
        String password = input.readLine();

        boolean isAdmin = username.equals("admin") && password.equals("admin");
        boolean isUser = false;

        if (!isAdmin) {
            String sql = "SELECT * FROM user WHERE username = '" + username + "' AND password = '" + password + "'";
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                isUser = true;
            }
        }

        if (!isAdmin && !isUser) {
            System.out.println("Login failed!");
            hold(2);
            login();
        } else if (isAdmin) {
            clear();
            System.out.println("Login success! Welcome, Admin!");
            hold(1);
            enemyManager.menuAdmin();
        } else {
            userId = rs.getInt("id_user");
            clear();
            System.out.println("Login success! Welcome, " + username + "!");
            hold(1);
            playerManager.menuUser(userId);
        }
    }

    private static void Reg() throws IOException, SQLException {
        clear();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("============================");
        System.out.println("|       REGISTRATION       |");
        System.out.println("============================");
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

        String sql = "INSERT INTO user (username, password) VALUES ('"+username+"', '"+password+"')";
        stmt.execute(sql);
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
}
