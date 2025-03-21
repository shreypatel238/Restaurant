import static org.junit.Assert.assertEquals;

import org.junit.*;

public class TestUser {
    String testUsername = "TestUser";
    String testPassword = "abc"; 
    int testLevel = 0;

    @Test
    public void testUserCreation() {
        User testUser = new User(testUsername,testPassword,testLevel);
    }
}
