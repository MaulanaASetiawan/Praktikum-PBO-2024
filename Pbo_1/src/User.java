import java.util.*;

public class User {
    private String name, password;
    private List<User> users = new ArrayList<>();

    User(String name, String password){
        this.name = name;
        this.password = password;
    }

    public String getName(){
        return this.name;
    }

    public String getPassword(){
        return this.password;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void addUser(String name, String password){
        User user = new User(name, password);
        users.add(user);
    }
}