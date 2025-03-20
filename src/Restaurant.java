// Restaurant class
public class Restaurant {
    private String name;
    private String address;
    private String pricing;
    private String imagePath;

    public Restaurant(String name, String address, String pricing, String imagePath) {
        this.name = name;
        this.address = address;
        this.pricing = pricing;
        this.imagePath = imagePath;
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

    public String getImagePath() {return imagePath;}

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }

    public void setImagePath(String imagePath) {this.imagePath = imagePath.replace("\\", "/");}
}
