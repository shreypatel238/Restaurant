import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TestBackend {
    
    String testPath = "./Restaurant/mirror/testBackend.csv";
    String testName = "McDonalds";
    String testAddress = "123 Sesame Street"; 
    String testPricing = "$10-$20";
    String testImagePath = "image.png";
    String testDescription = "this restauraunt is being tested";
    ArrayList<String> testTags = new ArrayList<>();
    String testUsername = "John Doe";
    String testPassword = "abc123";
    String testUsername2 = "Jane Doe";
    String testPassword2 = "123abc";
    Restaurant testData1 = new Restaurant("McDonalds","100 Mc Way","$10-$20","image.png", testDescription, testTags);
    Restaurant testData2 = new Restaurant("Subway", "100 Sub Way","$10", "image.jpg", testDescription, testTags);
    Restaurant testData3 = new Restaurant(testName, testAddress, testPricing, testImagePath, testDescription, testTags);

    
    // Tests that a "Backend" Object can be created using its parameterized constructor
    @Test
    public void testBackendCreation() {
        Backend backend = new Backend(testPath);
    }

    // Tests that adding a restaurant to the backend fits the expected format
    @Test
    public void testBackendAddData() {
        ArrayList<Restaurant> testData = new ArrayList<>();
        testData.add(testData1);
        testData.add(testData2);

        Backend backend = new Backend(testPath);
        backend.addData(testData1);
        backend.addData(testData2);
        ArrayList<Restaurant> obtainedData = Backend.data;

        assertEquals(testData, obtainedData);

        backend.removeData(testData1.getName(), false);
        backend.removeData(testData2.getName(), false);
    }

    // Tests that when the "editData()" function is called, the changed values match what is expected
    @Test
    public void testBackendEditData() {
        Restaurant expected = new Restaurant(testName, testAddress, testPricing, testImagePath, testDescription, testTags);

        Backend backend = new Backend(testPath);
        backend.addData(testData1);
        backend.editData(testData1.getName(), false, testName, testAddress, testPricing, testImagePath, testDescription, testTags);
        Restaurant actual = Backend.data.get(0);

        assertTrue(expected.getName().equals(actual.getName()));
        assertTrue(expected.getAddress().equals(actual.getAddress()));
        assertTrue(expected.getPricing().equals(actual.getPricing()));
        assertTrue(expected.getImagePath().equals(actual.getImagePath()));      

        backend.removeData(testName, false);
    }

    // Tests that the removal of restaurants from the backend works as expected
    @Test
    public void testBackendRemoveData() {
        Backend backend = new Backend(testPath);
        ArrayList<Restaurant> expected = new ArrayList<>();
        backend.addData(testData1);
        backend.removeData(testData1.getName(), false);

        assertTrue(expected.equals(Backend.data));
    }

    // Tests the that a user can register for and successfully create an account and an existing username and password will fail
    // when trying to create an account
    @Test
    public void testRegister() {
        Backend backend = new Backend(testPath);
        assertTrue(backend.register(testUsername, testPassword));
        assertFalse(backend.register(testUsername, testPassword));

        Backend.users.remove(Backend.users.size()-1);
    }

    // Tests that a user not registered cannot login to an account while a registered user can
    @Test
    public void testLogin() {
        Backend backend = new Backend(testPath);
        assertFalse(backend.login(testUsername2, testPassword2));
        backend.register(testUsername2, testPassword2);
        assertTrue(backend.login(testUsername2, testPassword2));

        Backend.users.remove(Backend.users.size()-1);
    }

    // Tests that a restaurant added to a user's favourite list contains the expected data
    @Test
    public void testAddFavourite() {
        Backend backend = new Backend(testPath);
        File testFile = new File("./Restaurant/data/mcds.jpeg");
        backend.register(testUsername, testPassword);
        backend.addData(testData3);
        ArrayList<Restaurant> expectedFavData = new ArrayList<>();
        expectedFavData.add(testData3);

        backend.addFavouriteResturant(testUsername, testName, testAddress, testPricing, testFile, testDescription, testTags);
        assertTrue(expectedFavData.equals(Backend.users.get(Backend.users.size()-1).getFavData()));

        backend.removeData(testName, false);
        Backend.users.remove(Backend.users.size()-1);
    }
}
