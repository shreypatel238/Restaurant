// Restaurant class

import java.util.Objects;

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

    public void setImagePath(String imagePath) {this.imagePath = imagePath;}


}
