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

    // Test that a User can Login to an account previously registered
    @Test
    public void testUserLogin() {

    }

    //Test that a User can add a restaurant to a favourites list
    @Test
    public void testUserAddFavourites() {

    }

    //Test that a User can remove a restaurant to a favourites list
    @Test
    public void testUserRemoveFavourites() {

    }

    // Test that a User can leave a review for a restaurant
    @Test
    public void testUserAddReview(){

    }

    // Test that a User's access level to see if they can use admin level functions
    @Test
    public void testUserAccessLevel() {

    }

    // Test an Admin user's ability to add a restaurant to the page
    @Test
    public void testAdminAddRestaurant() {

    }

    // Test an Admin user's ability to remove a restaurant from the page
    @Test
    public void testAdminRemoveRestaurant() {

    }

    // Test an Admin user's ability to update/edit a restaurant on the page
    @Test
    public void testAdminUpdateRestaurant() {

    }
}
