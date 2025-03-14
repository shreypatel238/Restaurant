import java.io.File;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.FileWriter;
import java.io.BufferedWriter;

import java.io.IOException;

import java.util.ArrayList;

public class Backend {

    static ArrayList<Data> data = new ArrayList<>();
    static ArrayList<Data> altData = new ArrayList<>();
    static ArrayList<User> users = new ArrayList<>();
    static boolean loaded = false;
    String search = "";
    String path = "";

    public Backend(String path) {
        // Set backend file path on initialization
        this.path = path;
        this.readUsers("./users.csv");
    }

    private static void parse(String line) {
        String[] args = line.split(",");

        if (args.length == 4) {
            String name = args[0];
            String address = args[1];
            String pricing = args[2];
            String imagePath = args[3];
    
            // Skipping invalid data entry
            if (name == null || address == null || pricing == null || imagePath == null) {
                return;
            }
    
            // name, address, pricing
            Data resturant = new Data(name, address, pricing, imagePath);
            data.add(resturant);
        }
        
    }

    private static void read(String path) {

        // File reading
        try {
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                // File parsing
                parse(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Data> getData() {
        // Ensures the file isn't read twice
        if (!loaded) {
            read(this.path);
            loaded = true;
        }   

        return data;
    }

    private void writeData(Data resturant) {
        // File writing
        try {
            FileWriter fw = new FileWriter(this.path, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(resturant.getName() + "," + resturant.getAddress() + "," + resturant.getPricing() + "," + resturant.getImagePath() + "\n");
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addData(String name, String address, String pricing, String imagePath) {
        Data resturant = new Data(name, address, pricing, imagePath);
        data.add(resturant);
        writeData(resturant);
    }

    public void addData(Data resturant) {
        data.add(resturant);
        writeData(resturant);
    }

    public void editData(String name, boolean editAll, String newName, String newAddress, String newPricing, String newImagePath) {

        for (Data item : data) {
            if (item.getName().equals(name)) {
                item.setName(newName);
                item.setAddress(newAddress);
                item.setPricing(newPricing);
                item.setImagePath(newImagePath);
                if (!editAll) {
                    break;
                }
            }
        }
        for (Data item : altData) {
            if (item.getName().equals(name)) {
                item.setName(newName);
                item.setAddress(newAddress);
                item.setPricing(newPricing);
                item.setImagePath(newImagePath);
                if (!editAll) {
                    break;
                }
            }
        }

        try {
            FileWriter fw = new FileWriter(this.path, false);
            BufferedWriter bw = new BufferedWriter(fw);
            data.forEach(item -> {
                try {
                    bw.write(item.getName() + "," + item.getAddress() + "," + item.getPricing() + "," + item.getImagePath() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Parse users file
    private void parseUser(String line) {
        String[] args = line.split(",");

        if (args.length == 3) {
            String username = args[0];
            String password = args[1];
            int level = Integer.parseInt(args[2]);

            // Skipping invalid data entry
            if (username == null || password == null) {
                return;
            }

            // Username, password, level
            User user = new User(username, password, level);
            users.add(user);
        }
    }


    public void readUsers(String path) {
        // File reading
        try {
            File file = new File(path);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                // File parsing
                parseUser(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        try {
            FileWriter fw = new FileWriter("./users.csv", false);
            BufferedWriter bw = new BufferedWriter(fw);
            users.forEach(user -> {
                try {
                    bw.write(user.getUsername() + "," + user.getPassword() + "," + user.getLevel() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // takes in a username and password and stores it in a file to be used in account access
    public boolean register(String user, String password) {
        for (User item : users) {
            if (item.getUsername().equals(user)) {
                return false;
            }
        }

        User newUser = new User(user, password, 1);
        users.add(newUser);
        update();

        return true;
    }

    // checks if the given username and password pair are within the users file, logging in if found
    public boolean login(String user, String password) {
        
        for (User item : users) {
            if (item.getUsername().equals(user) && item.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }

    public void logout() {
        // Depends on the front end implementation
    }


    
    // removes an item of String "name" from a file
    public void removeData(String name, boolean removeAll) {

        for (Data item : data) {
            if (item.getName().equals(name)) {
                data.remove(item);
                altData.remove(item);
                if (!removeAll) {
                    break;
                }
            }
        }

        try {
            FileWriter fw = new FileWriter(this.path, false);
            BufferedWriter bw = new BufferedWriter(fw);
            data.forEach(item -> {
                try {
                    bw.write(item.getName() + "," + item.getAddress() + "," + item.getPricing() + "," + item.getImagePath() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    //Returns a list of resturants (data) containing the filter
    public void searchdata(String filter) {
        if(!filter.isEmpty()) {
            for (Data item : data) {
                if (item.getName().contains(filter)&&!altData.contains(item)) {
                    altData.add(item);

                }
            }
            search = filter;
        }
        if (filter.isEmpty()) {
            altData.clear();
            search = null;
        }
    }

    public void setData(ArrayList<Data> data) {
        Backend.data = data;
    }

    public static ArrayList<Data> getAltData() {
        return altData;
    }

    public String getSearch() {
        return search;
    }
}
