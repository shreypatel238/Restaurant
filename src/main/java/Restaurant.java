import java.util.ArrayList;

// Restaurant class
public class Restaurant {
    private String name;
    private String address;
    private String pricing;
    private String imagePath;
    private String description;
    private ArrayList<String> tags;

    public Restaurant(String name, String address, String pricing, String imagePath) {
        this.name = name;
        this.address = address;
        this.pricing = pricing;
        this.imagePath = imagePath;
        this.tags = new ArrayList<>();
        this.description = "";
    }

    public Restaurant(String name, String address, String pricing, String imagePath, ArrayList<String> tags) {
        this.name = name;
        this.address = address;
        this.pricing = pricing;
        this.imagePath = imagePath;
        this.tags = tags;
    }

    public Restaurant(String name, String address, String pricing, String imagePath, String description) {
        this.name = name;
        this.address = address;
        this.pricing = pricing;
        this.imagePath = imagePath;
        this.description = description;
    }

    public Restaurant(String name, String address, String pricing, String imagePath, String description, ArrayList<String> tags) {
        this.name = name;
        this.address = address;
        this.pricing = pricing;
        this.imagePath = imagePath;
        this.tags = tags;
        this.description = description;
    }



    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPricing() {
        return pricing;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath.replace("\\", "/");
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTags(ArrayList<String> tags) {this.tags = tags;}
}
