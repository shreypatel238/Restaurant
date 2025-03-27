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


    // Override equals for getting values instead of references in testing
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if(!(o instanceof Restaurant))
            return false;
        Restaurant other = (Restaurant) o;
        boolean nameEquals = (this.name == null && other.name == null)
          || (this.name != null && this.name.equals(other.name));
        boolean addressEquals = (this.address == null && other.address == null)
          || (this.address != null && this.address.equals(other.address));
        boolean pricingEquals = (this.pricing == null && other.pricing == null)
          || (this.pricing != null && this.pricing.equals(other.pricing));
        boolean imagePathEquals = (this.imagePath == null && other.imagePath == null)
          || (this.imagePath != null && this.imagePath.equals(other.imagePath));
        return nameEquals && addressEquals && pricingEquals && imagePathEquals;
    }

    @Override
    public final int hashCode() {
        return Objects.hash(name, address, pricing, imagePath);
    }

}
