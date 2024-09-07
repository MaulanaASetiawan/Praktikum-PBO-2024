import java.io.IOException;
import java.sql.SQLException;

public interface Shop {
    public void addItems() throws SQLException, IOException;
    public void updateItems() throws SQLException, IOException;
    public void removeItems() throws SQLException, IOException;
    public void sellItems(int userId) throws SQLException, IOException;
    public void buyItems(int userId, Shop shop) throws SQLException, IOException;
    public void saveItems(Shop shop) throws SQLException, IOException;
    public void displayItem();
    String getName();
    int getDmg();
    int getPrice();
    void setName(String name);
    void setDmg(int dmg);
    void setPrice(int price);
}
