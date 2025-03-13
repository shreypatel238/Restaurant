import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Backend b = new Backend("./data.csv");
        b.getData().forEach(item -> {
            System.out.println(item.getName() + " " + item.getAddress() + " " + item.getPricing());
        });
        ArrayList<Data> data = b.searchdata("McDonalds");

        data.forEach(item -> {
                    System.out.println(item.getName() + " " + item.getAddress() + " " + item.getPricing());
                });
        ArrayList<Data> data1 = b.searchdata("KFC");

        data1.forEach(item -> {
            System.out.println(item.getName() + " " + item.getAddress() + " " + item.getPricing());
        });
    }
}