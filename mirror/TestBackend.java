import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.*;

public class TestBackend {
    
    String testPath = "./Restaurant/mirror/testBackend.csv";
    Restaurant testData1 = new Restaurant("McDonalds","100 Mc Way","$10-$20","image.png");
    Restaurant testData2 = new Restaurant("Subway", "100 Sub Way","$10", "image.jpg");
    String testName = "McDonalds";
    String testAddress = "123 Sesame Street"; 
    String testPricing = "$10-$20";
    String testImagePath = "image.png";
    
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
        Restaurant expected = new Restaurant(testName, testAddress, testPricing, testImagePath);

        Backend backend = new Backend(testPath);
        backend.addData(testData1);
        backend.editData(testData1.getName(), false, testName, testAddress, testPricing, testImagePath);
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
}
