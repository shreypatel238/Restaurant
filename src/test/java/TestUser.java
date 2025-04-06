import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TestUser {
    String testUsername = "TestUser";
    String testPassword = "abc"; 
    int testLevel = 0;

    //Test if a User Object can be created
    @Test
    public void testUserCreation() {
        User testUser = new User(testUsername,testPassword,testLevel);
    }

    // Test that a User Objects properties are as expected
    @Test
    public void testUserProperties() {
        User testUser = new User(testUsername,testPassword,testLevel);
        assertEquals(testUser.getUsername(), testUsername);
        assertEquals(testUser.getPassword(), testPassword);
        assertEquals(testUser.getLevel(), testLevel);
    }
}
