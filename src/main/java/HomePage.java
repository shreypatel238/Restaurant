import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.swing.event.DocumentEvent;

public class HomePage extends JFrame {
    private JMenuBar menu;
    private JPanel panel;
    private GridBagConstraints constraints;
    private int totalRestaurants = 0;
    private Backend backend = new Backend("./data.csv");
    private String currentSearch;
    private boolean loggedIn = false;
    private User user = null;

    public HomePage() {
        this.setTitle("Restaurant Catalog");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1500, 800);
        this.setLocationRelativeTo(null);

        if (!loggedIn) {
            LoginPage loginPage = new LoginPage(backend, this);
            loginPage.setVisible(true);
        }
    }

    //Creates the home page
    public void createHomePage() {
        this.getContentPane().removeAll();
        this.repaint();

        this.panel = new JPanel();
        this.panel.setLayout(new GridBagLayout());

        constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        panel.setBackground(new Color(210, 180, 140));

        //Creates a card for each restaurant in data
        backend.getData().forEach(item -> {
            createCard(item.getName(), item.getAddress(), item.getPricing(), new File(item.getImagePath()), item.getDescription(), item.getTags());
        });

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrollPane);

        //Creates JMenuBar and sets layout. Adds a button to add restaurants and creates a margin using horizontal glue and adds it to menu
        menu = new JMenuBar();
        menu.setLayout(new BoxLayout(menu, BoxLayout.X_AXIS));

        //If user is admin, adds add restaurant button
        if (user.getLevel() == 0) {
            JMenuItem addButton = new JMenuItem("Add Restaurant");
            addButton.setMaximumSize(new Dimension(125, 30));
            menu.add(addButton);

            //Adds functionality to the add restaurants button. Calls addRestaurant when pressed
            addButton.addActionListener(e -> {
                try {
                    addRestaurant();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }

        JMenuItem logOutButton = new JMenuItem("Log Out");
        logOutButton.setMaximumSize(new Dimension(75, 30));

        //Logs user out
        logOutButton.addActionListener(e -> {
            loggedIn = false;
            user = null;
            totalRestaurants = 0;
            LoginPage loginPage = new LoginPage(backend, this);
            loginPage.setVisible(true);
            this.dispose();
        });

        menu.add(logOutButton);
        menu.add(Box.createHorizontalGlue());

        //Creates a search bar, sets size, and adds to menu
        JTextField searchBar = new JTextField("Search");
        searchBar.setPreferredSize(new Dimension(200, 30));
        searchBar.setMinimumSize(new Dimension(200, 30));
        searchBar.setMaximumSize(new Dimension(200, 30));
        searchBar.setBorder(BorderFactory.createLineBorder(Color.black));
        menu.add(searchBar);
        menu.add(Box.createHorizontalGlue());

        searchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //At first, the default text in the search bar is "search". If user clicks on it, the search text will disappear
                String search = searchBar.getText();
                if((searchBar.getText().equals("Search"))) {
                    searchBar.setText("");
                }else {
                    if (!search.isEmpty()) {
                        backend.searchdata(search);
                        refreshPage();
                    }else{
                        backend.searchdata(search);
                        refreshPage();
                    }
                    currentSearch = search;
                    panel.revalidate();
                    panel.repaint();
                }
            }
        });

        searchBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onTextChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onTextChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onTextChange();
            }

            private void onTextChange() {
                String search = searchBar.getText();
                if((searchBar.getText().equals("Search"))) {
                    searchBar.setText("");
                }else {
                    currentSearch = search;
                    if (!search.isEmpty()) {
                        backend.searchdata(search);
                        refreshPage();
                    }else{
                        backend.searchdata(search);
                        refreshPage();
                    }
                    panel.revalidate();
                    panel.repaint();
                }
            }
        });

        this.setJMenuBar(menu);
        this.setVisible(true);
    }

    public void setUser(User user) {
        this.user = user;
        this.loggedIn = true;
        createHomePage();
        setVisible(true);
    }

    //Creates a card where the restaurant information will be displayed. Takes in a name, address, pricing, and image file
    public void createCard(String name, String address, String pricing, File image, String description, ArrayList<String> tags) {
        //Creates main JPanel for the card. Sets size, border, layout and background colour
        JPanel restaurantPanel = new JPanel();
        restaurantPanel.setPreferredSize(new Dimension(300, 300));
        restaurantPanel.setMinimumSize(new Dimension(300, 300));
        restaurantPanel.setMaximumSize(new Dimension(300, 300));
        restaurantPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        restaurantPanel.setLayout(new BorderLayout());
        restaurantPanel.setBackground(new Color(245, 224, 130));

        //Creates JLabels and assigns them their respective names from function parameters
        JLabel restName = new JLabel(name);
        JLabel restPricing = new JLabel(pricing);
        JLabel restAddress = new JLabel(address);
        JLabel restImage;

        //Creates an ImageIcon from the image file, and assigns it to the restImage JLabel. Also attaches the path to the JLabel as well
        if (image != null) {
            try {
                BufferedImage img = ImageIO.read(image);
                ImageIcon icon = new ImageIcon(img.getScaledInstance(300, 200, Image.SCALE_SMOOTH));
                restImage = new JLabel(icon);
                restImage.putClientProperty("imagePath", image.getPath());
            } catch (IOException e) {
                //if fails, sets placeholder image
                JOptionPane.showMessageDialog(this, "Image load failed", "Error", JOptionPane.ERROR_MESSAGE);
                ImageIcon icon = new ImageIcon("data/default-placeholder.png");
                restImage = new JLabel(icon);
                restImage.putClientProperty("imagePath", "data/default-placeholder.png");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Image load failed", "Error", JOptionPane.ERROR_MESSAGE);
            ImageIcon icon = new ImageIcon("data/default-placeholder.png");
            restImage = new JLabel(icon);
            restImage.putClientProperty("imagePath", "data/default-placeholder.png");
        }

        //sets font of the text
        restName.setFont(new Font("", Font.BOLD, 25));
        restAddress.setFont(new Font("", Font.BOLD, 15));
        restPricing.setFont(new Font("", Font.BOLD, 20));

        //If user is admin, adds edit and remove buttons
        if (user.getLevel() == 0) {
            //Creates a panel dedicated to the edit button
            JPanel editPanel = new JPanel(new BorderLayout());
            editPanel.setBackground(new Color(245, 224, 130));
            JMenuBar menuBar = new JMenuBar();
            //Creates a JMenu item and assigns to JMenuItem's to it: update and remove
            JMenu dots = new JMenu("...");
            JMenuItem updateButton = new JMenuItem("Update");
            JMenuItem removeButton = new JMenuItem("Remove");
            dots.add(updateButton);
            dots.add(removeButton);
            menuBar.add(dots);

            //When clicked, the remove and update buttons will run their respective functions
            removeButton.addActionListener(e -> removeRestaurant(restaurantPanel));
            updateButton.addActionListener(e -> editRestaurant(restaurantPanel, description));

            //adds it to the right side of the panel
            editPanel.add(menuBar, BorderLayout.LINE_END);
            restaurantPanel.add(editPanel, BorderLayout.PAGE_START);
        }

        //Creates panel for the details of the restaurant and adds the JLabels
        JPanel detailPanel = new JPanel(new GridLayout(4, 1));
        detailPanel.add(restImage);
        detailPanel.add(restName);
        detailPanel.add(restPricing);
        detailPanel.add(restAddress);
        detailPanel.add(restAddress);
        detailPanel.setBackground(new Color(245, 224, 130));

        //Adds button for viewing details
        JButton viewDetails = new JButton("View Details");
        viewDetails.addActionListener(e -> {
            Restaurant restaurant = null;
            for (Restaurant x : backend.getData()) {
                if (x.getName().equals(name)) {
                    restaurant = new Restaurant(x.getName(), x.getAddress(), x.getPricing(), x.getImagePath(), x.getDescription(), x.getTags());
                }
            }
            assert restaurant != null;
            viewResDetails(restaurant, image);
        });

        //adds all panels to main panel
        restaurantPanel.add(detailPanel, BorderLayout.CENTER);
        restaurantPanel.add(viewDetails, BorderLayout.PAGE_END);

        //Refreshes constraints for the cards so everything works fine
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        //makes it so four cards max will fit in a row, after that they will move to a new row
        this.constraints.gridx = totalRestaurants % 4;
        this.constraints.gridy = totalRestaurants / 4;

        //adds to main JFrame with constraints. Adds one to restaurant counter, and refreshes and repaints the panel
        this.panel.add(restaurantPanel, this.constraints);
        totalRestaurants += 1;
        panel.revalidate();
        panel.repaint();
    }

    //Gathers restaurant details for card creation
    public void addRestaurant() throws IOException {
        //Creates fields for the details
        JTextField nameField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField pricingField = new JTextField(20);
        JTextField descriptionField = new JTextField();
        JTextField tagsField = new JTextField();
        String[] imagePath = {""};

        //Creates a panel for a dialog box and adds respective JLabels and textfields and buttons
        JPanel enterPanel = new JPanel(new GridLayout(6, 2));
        enterPanel.add(new JLabel("Enter Restaurant Name:"));
        enterPanel.add(nameField);
        enterPanel.add(new JLabel("Enter Restaurant Address:"));
        enterPanel.add(addressField);
        enterPanel.add(new JLabel("Enter Restaurant Price Range (Ex. $10-$100):"));
        enterPanel.add(pricingField);
        enterPanel.add(new JLabel("Enter Restaurant Description:"));
        enterPanel.add(descriptionField);
        enterPanel.add(new JLabel("Enter Tags (Ex. chinese,takeout,outdoors):"));
        enterPanel.add(tagsField);
        enterPanel.add(new JLabel("Select an image"));
        JButton selectButton = new JButton("Select");

        //Adds functionality to select button for image adding
        selectButton.addActionListener(e -> {
            //Asks user to select an image file to enter
            JFileChooser fileChooser = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
            fileChooser.setFileFilter(filter);
            fileChooser.setCurrentDirectory(new File("."));

            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                //Gets file and file path of the image the user selected
                File imageFile = fileChooser.getSelectedFile();

                File folder = new File("data");
                File file = new File(folder, imageFile.getName());
                if (!file.exists()) {
                    try {
                        //Copies the file to the folder so it can be displayed on any machine
                        Files.copy(imageFile.toPath(), file.toPath());
                        imagePath[0] = file.getPath();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Image load failed", "Error", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(ex);
                    }
                }
                if (imagePath[0].isEmpty()) {
                    imagePath[0] = "data/default-placeholder.png"; // A placeholder image in case of failure
                }
            }

        });
        enterPanel.add(selectButton);

        //Creates JOptionPane containing the enterPanel
        int entered = JOptionPane.showConfirmDialog(this, enterPanel, "Enter Restaurant Information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        //gets the values of the textfields and checks if they are empty or not
        if (entered == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You must enter a name!", "Restaurant Name", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String address = addressField.getText().trim();
            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You must enter an address!", "Restaurant Address", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String pricing = pricingField.getText().trim();
            if (pricing.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You must enter a price range!" ,"Restaurant Price", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String description = descriptionField.getText().trim();
            if (description.isEmpty()) {
                description = "No description provided";
            }

            String[] tags = tagsField.getText().trim().split(",");
            ArrayList<String> tagsList = new ArrayList<>();
            if (tags.length == 0) {
                tagsList.add("No tags provided");
            } else {
                for (String x : tags) {
                    tagsList.add(x);
                }
            }

            //adds data to the csv for future use
            backend.addData(name, address, pricing, imagePath[0], description, tagsList); //Add to CSV

            //runs createCard function to create the actual card
            createCard(name, address, pricing, new File(imagePath[0]), description, tagsList);
        }
    }

    //function to remove a card
    public void removeRestaurant(JPanel restaurantPanel) {
        //gets the JPanel and JLabels of the card
        JPanel component = (JPanel) restaurantPanel.getComponent(1);
        JLabel nameLabel = (JLabel) component.getComponent(1);
        JLabel imageLabel = (JLabel) component.getComponent(0);

        //Deletes file when card is removed
        String path = (String) imageLabel.getClientProperty("imagePath");
        File filePath = new File(path);
        System.out.println(filePath.getPath());
        if (!filePath.getAbsolutePath().endsWith("data" + File.separator + "default-placeholder.png")) {
            filePath.delete();
        }

        //removes the card and subtracts 1 restaurant. Also removes the data from the database
        panel.remove(restaurantPanel);
        totalRestaurants -= 1;
        backend.removeData(nameLabel.getText().replace("Name: ", ""), false);

        //runs refreshPage function
        refreshPage();

        //recalculates and repaints the screen
        panel.revalidate();
        panel.repaint();
    }

    //edits restaurant data
    public void editRestaurant(JPanel restaurantPanel, String description) {
        //asks what field the user wants to change and checks if it's empty or not
        String[] options = {"Name", "Address", "Pricing", "Image", "Description", "Tags"};
        JComboBox<String> comboBox = new JComboBox<>(options);
        int result = JOptionPane.showConfirmDialog(this, comboBox, "Which field would you like to edit?", JOptionPane.OK_CANCEL_OPTION);
        String type = (String) comboBox.getSelectedItem();
        System.out.println(result);
        if (result == JOptionPane.OK_OPTION && type == null || type.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a field to change!");
            return;
        } else if (result == JOptionPane.CANCEL_OPTION) {
            return;
        }

        //If the user does not want to change the image, asks the user for the new value they want to set it to
        String newField = "";
        if (!type.equals("image") && !type.equals("Image")) {
            newField = JOptionPane.showInputDialog(this, "Enter new " + type);
            if (newField.equals("") || newField.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "You must enter something!");
                return;
            }
        }

        //gets the JPanel data
        JPanel component = (JPanel) restaurantPanel.getComponent(1);
        //gets all JLabel data so the text can be extracted
        JLabel nameLabel = (JLabel) component.getComponent(1);
        JLabel addressLabel = (JLabel) component.getComponent(3);
        JLabel pricingLabel = (JLabel) component.getComponent(2);
        JLabel imageLabel = (JLabel) component.getComponent(0);
        //Stores JLabel values into variables
        String oldName = nameLabel.getText().replace("Name: ", "");
        String oldAddress = addressLabel.getText().replace("Address: ", "");
        String oldPricing = pricingLabel.getText().replace("Price Range: ", "");
        String oldPath = (String) imageLabel.getClientProperty("imagePath");
        System.out.println(description);

        //depending on which field the user wants to change, sets the corresponding label to the new value, and edits the database to reflect that
        if (type.equals("name") || type.equals("Name")) {
            nameLabel.setText(newField);
            backend.editData(oldName, false, newField, oldAddress, oldPricing, oldPath, description);
        } else if (type.equals("Address")) {
            addressLabel.setText(newField);
            backend.editData(oldName, false, oldName, newField, oldPricing, oldPath, description);
        } else if (type.equals("pricing") || type.equals("Pricing")) {
            pricingLabel.setText(newField);
            backend.editData(oldName, false, oldName, oldAddress, newField, oldPath, description);
        } else if (type.equals("image") || type.equals("Image")) {
            //if user wants to change image, will ask for a new image file and assign it to the JLabel
            String[] imagePath = {""};
            JFileChooser fileChooser = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
            fileChooser.setFileFilter(filter);
            fileChooser.setCurrentDirectory(new File("."));
            File imageFile = null;

            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                //assigns image file and path
                imageFile = fileChooser.getSelectedFile();

                File folder = new File("data");
                File file = new File(folder, imageFile.getName());
                if (!file.exists()) {
                    try {
                        //Copies the file to the folder so it can be displayed on any machine
                        Files.copy(imageFile.toPath(), file.toPath());
                        imagePath[0] = file.getPath();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Image loading failed", "Error", JOptionPane.ERROR_MESSAGE);
                        throw new RuntimeException(ex);
                    }
                }
                if (imagePath[0].isEmpty()) {
                    imagePath[0] = "data/default-placeholder.png"; // A placeholder image in case of failure
                }
                //Deletes old image file
                File path = new File(oldPath);
                if (!path.getAbsolutePath().endsWith("data" + File.separator + "default-placeholder.png")) {
                    path.delete();
                }
            }

            if (imageFile != null) {
                try {
                    //converts file to an icon and attaches it to the JLabel. Also saves the new file path and edits database
                    BufferedImage img = ImageIO.read(imageFile);
                    ImageIcon icon = new ImageIcon(img.getScaledInstance(300, 200, Image.SCALE_SMOOTH));
                    imageLabel.setIcon(icon);
                    imageLabel.setText(null);
                    imageLabel.putClientProperty("imagePath", imagePath[0]);
                    backend.editData(oldName, false, oldName, oldAddress, oldPricing, imagePath[0], description);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Image load failed", "Error", JOptionPane.ERROR_MESSAGE);
                    ImageIcon icon = new ImageIcon("data/default-placeholder.png");
                    imageLabel = new JLabel(icon);
                    imageLabel.putClientProperty("imagePath", "data/default-placeholder.png");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Image load failed", "Error", JOptionPane.ERROR_MESSAGE);
                ImageIcon icon = new ImageIcon("data/default-placeholder.png");
                imageLabel = new JLabel(icon);
                imageLabel.putClientProperty("imagePath", "data/default-placeholder.png");
            }
        } else if (type.equals("description") || type.equals("Description")) {
            backend.editData(oldName, false, oldName, oldAddress, oldPricing, oldPath, newField);
        }
        //recalculates and repaints screen
        panel.revalidate();
        panel.repaint();
    }

    //function to refresh the page so cards are positioned accordingly
    public void refreshPage() {
        //removes all panels, resets constraints, and resets total restaurant count
        panel.removeAll();
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        totalRestaurants = 0;

        //reloads all cards and displays them again
        if(!(backend.getAltData().isEmpty())&&(backend.getSearch().equals(currentSearch))) {
            backend.getAltData().forEach(item -> {
                createCard(item.getName(), item.getAddress(), item.getPricing(), new File(item.getImagePath()), item.getDescription(), item.getTags());
            });
        }
        else{
            backend.getData().forEach(item -> {
                createCard(item.getName(), item.getAddress(), item.getPricing(), new File(item.getImagePath()), item.getDescription(), item.getTags());
            });
        }
    }

    public void viewResDetails(Restaurant restaurant, File image) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setTitle("Details");

        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);

        JLabel nameLabel = new JLabel(restaurant.getName());
        JLabel pricingLabel = new JLabel(restaurant.getPricing());
        JLabel addressLabel = new JLabel(restaurant.getAddress());
        JLabel descriptionLabel = new JLabel("<html>" + restaurant.getDescription() + "</html>");
        String tagString = "";
        for (String x : restaurant.getTags()) {
            System.out.println(x);
            tagString = x + ", ";
        }
        JLabel tagsLabel = new JLabel(tagString);

        nameLabel.setFont(new Font("Arial", Font.BOLD, 40));
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        pricingLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pricingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel imageLabel;
        if (image != null) {
            try {
                BufferedImage img = ImageIO.read(image);
                ImageIcon icon = new ImageIcon(img.getScaledInstance(300, 200, Image.SCALE_SMOOTH));
                imageLabel = new JLabel(icon);
                imageLabel.putClientProperty("imagePath", image.getPath());
            } catch (IOException e) {
                //if fails, sets placeholder image
                JOptionPane.showMessageDialog(this, "Image load failed", "Error", JOptionPane.ERROR_MESSAGE);
                ImageIcon icon = new ImageIcon("data/default-placeholder.png");
                imageLabel = new JLabel(icon);
                imageLabel.putClientProperty("imagePath", "data/default-placeholder.png");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Image load failed", "Error", JOptionPane.ERROR_MESSAGE);
            ImageIcon icon = new ImageIcon("data/default-placeholder.png");
            imageLabel = new JLabel(icon);
            imageLabel.putClientProperty("imagePath", "data/default-placeholder.png");
        }

        JPanel imagePanel = new JPanel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel);

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.add(nameLabel);
        detailPanel.add(pricingLabel);
        detailPanel.add(addressLabel);
        detailPanel.add(descriptionLabel);
        detailPanel.add(tagsLabel);
        detailPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        panel.add(imagePanel, BorderLayout.PAGE_START);
        panel.add(detailPanel, BorderLayout.CENTER);
        frame.add(panel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        HomePage homepage = new HomePage();
    }
}