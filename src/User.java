public class User {
    private String username;
    private String password;
    private int level = 0;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, int level) {
        this.username = username;
        this.password = password;
        this.level = level;
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
}
