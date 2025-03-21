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

    @Test
    public void testUserProperties() {

    }

    @Test
    public void testUserRegister(){
        
    }

    @Test
    public void testUserLogin() {

    }

    @Test
    public void testUserAddFavourites() {

    }

    @Test
    public void testUserRemoveFavourites() {

    }

    @Test
    public void testUserAddReview(){
        
    }

    @Test
    public void testUserAccessLevel() {

    }

    @Test
    public void testAdminAddRestaurant() {

    }

    @Test
    public void testAdminRemoveRestaurant() {

    }

    @Test
    public void testAdminUpdateRestaurant() {

    }
}
