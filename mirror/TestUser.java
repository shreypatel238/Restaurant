// Test Suite for User and Admin functions
import static org.junit.Assert.assertEquals;

import org.junit.*;

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

    }
}
