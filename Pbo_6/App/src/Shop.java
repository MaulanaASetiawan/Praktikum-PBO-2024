import java.io.IOException;
import java.sql.SQLException;

public interface Shop {
    public void addItems() throws SQLException, IOException;
    public void removeItems() throws SQLException, IOException;
    public void sellItems() throws SQLException, IOException;
    public void buyItems() throws SQLException, IOException;
    public void saveItems(Shop shop) throws SQLException, IOException;
    public void displayItem();
    String getName();
    int getPrice();
    void setName(String name);
    void setPrice(int price);
}
