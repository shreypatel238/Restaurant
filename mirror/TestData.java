import static org.junit.Assert.assertEquals;
import org.junit.*;

public class TestData {

    String testName = "McDonalds";
    String testAddress = "123 Sesame Street"; 
    String testPricing = "$10-$20";
    String testImagePath = "C:\\Users\\joshu\\Downloads\\mcd.png";

    @Test
    public void testCreateData() {
        Data data = new Data("name", "address", "pricing", "path");
    }

    @Test
    public void testDataInformation() {
        Data data = new Data("name", "address", "pricing", "path");
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