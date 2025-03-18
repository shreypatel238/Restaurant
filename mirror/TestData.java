import static org.junit.Assert.assertEquals;
import org.junit.*;

public class TestData {

    String testName = "McDonalds";
    String testAddress = "123 Sesame Street"; 
    String testPricing = "$10-$20";
    String testImagePath = "Image/path";

    //Tests that a "Data" Object can be created using its paramaterized constructor
    @Test
    public void testCreateData() {
        Restaurant data = new Restaurant("name", "address", "pricing", "path");
    }

    // Tests the getters and setters for the "Data" class
    @Test
    public void testDataInformation() {
        Restaurant data = new Restaurant("name", "address", "pricing", "path");
        data.setName(testName);
        data.setAddress(testAddress);
        data.setPricing(testPricing);
        data.setImagePath(testImagePath);
        assertEquals(data.getName(), testName);
        assertEquals(data.getAddress(), testAddress);
        assertEquals(data.getPricing(), testPricing);
        assertEquals(data.getImagePath(), testImagePath);
    }
}