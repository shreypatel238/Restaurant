import java.io.File;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.FileWriter;
import java.io.BufferedWriter;

import java.io.IOException;

import java.util.ArrayList;

public class Backend {

    static ArrayList<Restaurant> data = new ArrayList<>();
    static ArrayList<Restaurant> altData = new ArrayList<>();
    static ArrayList<User> users = new ArrayList<>();
    static boolean loaded = false;
    String search = "";
    String path = "";

    public Backend(String path) {
        // Set backend file path on initialization
        this.path = path;
        this.readUsers("./users.csv");
    }


    // Parses the resturant data from the file
    private static void parseRestaurant(String line) {
        String[] args = line.split(",");

        if (args.length >= 4) {
            String name = args[0];
            String address = args[1];
            String pricing = args[2];
            String imagePath = args[3].replace("\\", "/");
            String tagData = "";
            String description = "";
            if (args.length == 5) {
                description = args[4];
            }
            if (args.length == 6) {
                description = args[4].replace("~",",");
                tagData = args[5];
            }

    
            // Skipping invalid data entry
            if (name == null || address == null || pricing == null || imagePath == null) {
                return;
            }
    
            // name, address, pricing, imagePath, tags
            if (tagData != null) {
                ArrayList<String> tagsArray = parseTags(tagData);
                Restaurant resturant = new Restaurant(name, address, pricing, imagePath, description, tagsArray);
                data.add(resturant);
                return;
            }
            Restaurant resturant = new Restaurant(name, address, pricing, imagePath);
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
                parseRestaurant(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Restaurant> getData() {
        // Ensures the file isn't read twice
        if (!loaded) {
            read(this.path);
            loaded = true;
        }   

        return data;
    }

    private void writeData(Restaurant resturant) {
        // File writing
        try {
            FileWriter fw = new FileWriter(this.path, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(resturant.getName() + "," + resturant.getAddress() + "," + resturant.getPricing() + "," + resturant.getImagePath() + "," + resturant.getDescription().replace(",","~") + "," +  reformmatedTags(resturant.getTags())+ "\n");
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Adds a new resturant
    public void addData(String name, String address, String pricing, String imagePath, String description, ArrayList<String> tags) {
        Restaurant restaurant = new Restaurant(name, address, pricing, imagePath.replace("\\", "/"), description, tags);
        data.add(restaurant);
        writeData(restaurant);
    }

    // Overloaded addData
    public void addData(Restaurant restaurant) {
        data.add(restaurant);
        writeData(restaurant);
    }

    public void setDescription(Restaurant restaurant, String description) {
        restaurant.setDescription(description);
        writeData(restaurant);
    }

    // Edits restaurant data for tag edits refer to addTag and removeTag
    public void editData(String name, boolean editAll, String newName, String newAddress, String newPricing, String newImagePath, String newDescription) {
        for (Restaurant item : data) {
            if (item.getName().equals(name)) {
                item.setName(newName);
                item.setAddress(newAddress);
                item.setPricing(newPricing);
                item.setImagePath(newImagePath);
                item.setDescription(newDescription);
                if (!editAll) {
                    break;
                }
            }
        }
        for (Restaurant item : altData) {
            if (item.getName().equals(name)) {
                item.setName(newName);
                item.setAddress(newAddress);
                item.setPricing(newPricing);
                item.setImagePath(newImagePath);
                item.setDescription(newDescription);
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
                    bw.write(item.getName() + "," + item.getAddress() + "," + item.getPricing() + "," + item.getImagePath() + "," + item.getDescription().replace(",","~") + "," + parseTagsToString(item.getTags()) + "\n");
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
    public void editData(String name, boolean editAll, String newName, String newAddress, String newPricing, String newImagePath, String newDescription, ArrayList<String> tags) {
        for (Restaurant item : data) {
            if (item.getName().equals(name)) {
                item.setName(newName);
                item.setAddress(newAddress);
                item.setPricing(newPricing);
                item.setImagePath(newImagePath);
                item.setDescription(newDescription);
                item.setTags(tags);
                if (!editAll) {
                    break;
                }
            }
        }
        for (Restaurant item : altData) {
            if (item.getName().equals(name)) {
                item.setName(newName);
                item.setAddress(newAddress);
                item.setPricing(newPricing);
                item.setImagePath(newImagePath);
                item.setDescription(newDescription);
                item.setTags(tags);
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
                    bw.write(item.getName() + "," + item.getAddress() + "," + item.getPricing() + "," + item.getImagePath() + "," + item.getDescription().replace(",","~") + "," + reformmatedTags(item.getTags()) + "\n");
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

    // Parse tag lines from csv
    private static ArrayList<String> parseTags(String tags) {
        String[] tagData = tags.split(" ");
        ArrayList<String> tagList = new ArrayList<>();
        for (String tag : tagData) {
            tag = tag.trim();
            if (tag.length() == 0) {
                continue;
            }
            tagList.add(tag);
        }
        return tagList;
    }

    private static String parseTagsToString(ArrayList<String> tags) {
        String tagString = "";
        for (String tag : tags) {
            tagString += tag + ";";
        }
        return tagString;
    }

    // Add tag
    public void addTag(String name, String tag) {
        for (Restaurant item : data) {
            if (item.getName().equals(name)) {
                item.getTags().add(tag);
                writeData(item);
            }
        };
    }

    // Remove tag
    public void removeTag(String name, String tag) {
        for (Restaurant item : data) {
            if (item.getName().equals(name)) {
                item.getTags().remove(tag);
                writeData(item);
            }
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


    // Read user files
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

    // Updates the users file
    private void updateUser() {
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
        updateUser();

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

    // removes an item of String "name" from a file
    public void removeData(String name, boolean removeAll) {
        for (Restaurant item : data) {
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
                    bw.write(item.getName() + "," + item.getAddress() + "," + item.getPricing() + "," + item.getImagePath() + "," + item.getDescription().replace(",", "~") + "," + parseTagsToString(item.getTags()) + "\n");
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

            boolean pricingcheck = filter.matches("[$][0-9]+ - [$][0-9]+");
            if(pricingcheck) {
                altData.clear();
            }
            for (Restaurant item : data) {
                if (!pricingcheck&&item.getName().contains(filter)&&!altData.contains(item)) {
                    altData.add(item);

                }

                else if(pricingcheck&&item.getPricing().contains(filter) && !altData.contains(item)) {
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

    //Gets user object based on username
    public User getUser(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    // Returns guest user
    public User getGuest() {
        return new User("Guest", "", 2);
    }

    public void setData(ArrayList<Restaurant> data) {
        Backend.data = data;
    }

    public static ArrayList<Restaurant> getAltData() {
        return altData;
    }

    public String getSearch() {
        return search;
    }

    // public static void main(String[] args) {
    //     // Tag testing
    //     ArrayList<String> tags = new ArrayList<String>();
    //     tags.add("tag1");
    //     tags.add("tag2");
    //     tags.add("tag3");
    //     tags.add("tag4");
    //     tags.add("tag5");

    //     // Restaurant testing
    //     Restaurant resturant = new Restaurant("name", "address", "pricing", "imagePath", "Description", tags);
    //     Backend backend = new Backend("./data.csv");
    //     backend.addData(resturant);

    //     // Tag retrieving
    //     for (Restaurant item : backend.getData()) {
    //         System.out.println(item.getName());
    //         for (String tag : item.getTags()) {
    //             System.out.print(tag + ";");
    //         }
    //     }
    // }
    public String reformmatedTags(ArrayList<String> tags) {
        if(tags==null||tags.isEmpty()) {
            return "";
        }else{
            String text = "";
            for (String tag : tags) {
                text += tag + " ";

            }
            return text;
        }
    }
}
