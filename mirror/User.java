import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private ArrayList<Restaurant> favData;
    private int level = 0;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.favData = new ArrayList<>();
    }

    public User(String username, String password, int level) {
        this.username = username;
        this.password = password;
        this.level = level;
        this.favData = new ArrayList<>();
    }   

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<Restaurant> getFavData() {
        return favData;
    }
}
