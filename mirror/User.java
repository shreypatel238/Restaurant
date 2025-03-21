public class User {
    private String username;
    private String password;
    private int level = 0;

    public User(String username, String password, int level){
        this.username = username;
        this.password = password;
        this.level = level;
    }
}
