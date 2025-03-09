// Restaurant class
public class Data {
    private String name;
    private String address;
    private String pricing;

    public Data(String name, String address, String pricing) {
        this.name = name;
        this.address = address;
        this.pricing = pricing;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPricing(String pricing) {
        this.pricing = pricing;
    }
}
