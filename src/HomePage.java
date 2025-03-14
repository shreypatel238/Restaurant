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
import java.util.ArrayList;
import java.util.Objects;


import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class HomePage extends JFrame {
    private JMenuBar menu;
    private JPanel panel;
    private GridBagConstraints constraints;
    private int totalRestaurants = 0;
    private Backend backend;
    private String currentSearch;

    public HomePage() {
        this.setTitle("Restaurant Catalog");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(1500, 800);
        this.setLocationRelativeTo(null);

        this.panel = new JPanel();
        this.panel.setLayout(new GridBagLayout());

        constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        this.backend = new Backend("./data.csv");
        backend.getData().forEach(item -> {
            createCard(item.getName(), item.getAddress(), item.getPricing(), new File(item.getImagePath()));
        });

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrollPane);

        //Creates JMenuBar and sets lauout. Adds a button to add restaurants and creates a margin using horizontal glue and adds it to menu
        menu = new JMenuBar();
        menu.setLayout(new BoxLayout(menu, BoxLayout.X_AXIS));
        JMenuItem addButton = new JMenuItem("Add Restaurant");
        addButton.setMaximumSize(new Dimension(125, 30));
        menu.add(addButton);
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

        //Adds functionality to the add restaurants button. Calls addRestaurant when pressed
        addButton.addActionListener(e -> {
            try {
                addRestaurant();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        this.setVisible(true);
    }

    //Creates a card where the restaurant information will be displayed. Takes in a name, address, pricing, and image file
    public void createCard(String name, String address, String pricing, File image) {
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
                ImageIcon icon = new ImageIcon(img.getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                restImage = new JLabel(icon);
//                System.out.println(image.getAbsolutePath());
                restImage.putClientProperty("imagePath", image.getAbsolutePath());
            } catch (IOException e) {
                restImage = new JLabel("Image load failed");
            }
        } else {
            restImage = new JLabel("No Image");
        }

        //sets font of the text
        restName.setFont(new Font("", Font.BOLD, 25));
        restAddress.setFont(new Font("", Font.BOLD, 15));
        restPricing.setFont(new Font("", Font.BOLD, 20));

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
        updateButton.addActionListener(e -> editRestaurant(restaurantPanel));

        //adds it to the right side of the panel
        editPanel.add(menuBar, BorderLayout.LINE_END);

        //Creates panel for the details of the restaurant and adds the JLabels
        JPanel detailPanel = new JPanel(new GridLayout(4, 1));
        detailPanel.add(restImage);
        detailPanel.add(restName);
        detailPanel.add(restPricing);
        detailPanel.add(restAddress);
        detailPanel.setBackground(new Color(245, 224, 130));

        //Adds button for viewing details
        JButton viewDetails = new JButton("View Details");

        //adds all panels to main panel
        restaurantPanel.add(editPanel, BorderLayout.PAGE_START);
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

    /*
    //Old version of addRestaurant function, kept as reference
    public void addRestaurant() {
        String name = JOptionPane.showInputDialog(this, "Enter the restaurant name");
        if (name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "You must enter a name!");
            return;
        }
        String address = JOptionPane.showInputDialog(this, "Enter the restaurant address");
        if (address.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "You must enter an address!");
            return;
        }
        String pricing = JOptionPane.showInputDialog(this, "Enter the restaurant price range (Ex. $10-$100)");
        if (pricing.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "You must enter a price range!");
            return;
        }

        backend.addData(name, address, pricing); //Add to CSV

        createCard(name, address, pricing);
    }
    */

    //Gathers restaurant details for card creation
    public void addRestaurant() throws IOException {
        //Creates fields for the details
        JTextField nameField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField pricingField = new JTextField(20);
        String[] imagePath = {""};

        //Creates a panel for a dialog box and adds respective JLabels and textfields and buttons
        JPanel enterPanel = new JPanel(new GridLayout(4, 2));
        enterPanel.add(new JLabel("Enter Restaurant Name:"));
        enterPanel.add(nameField);
        enterPanel.add(new JLabel("Enter Restaurant Address:"));
        enterPanel.add(addressField);
        enterPanel.add(new JLabel("Enter Restaurant Price Range (Ex. $10-$100):"));
        enterPanel.add(pricingField);
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
                imagePath[0] =imageFile.getAbsolutePath();
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

            //adds data to the csv for future use
            backend.addData(name, address, pricing, imagePath[0]); //Add to CSV

            //runs createCard function to create the actual card
            createCard(name, address, pricing, new File(imagePath[0]));
        }
    }

    //function to remove a card
    public void removeRestaurant(JPanel restaurantPanel) {
        //gets the JPanel and JLabels of the card
        JPanel component = (JPanel) restaurantPanel.getComponent(1);
        JLabel nameLabel = (JLabel) component.getComponent(1);

        //removes the card and substracts 1 restaurant. Also removes the data from the database
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
    public void editRestaurant(JPanel restaurantPanel) {
        //asks what field the user wants to change and checks if its empty or not
        String type = JOptionPane.showInputDialog(this, "Which field would you like to edit? Name, Address, Pricing, or Image");
        if (type == null || type.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a field to change!");
            return;
        }

        //If the user does not want to change the image, asks the user for the new value they want to set it to
        String newField = "";
        if (!type.equals("image") && !type.equals("Image")) {
            newField = JOptionPane.showInputDialog(this, "Enter new " + type);
            if (newField.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "You must enter something!");
                return;
            }
        }

        //gets the JPanel data
        JPanel component = (JPanel) restaurantPanel.getComponent(1);
        //gets all JLabel data so the text can be extracted
        JLabel nameLabel = (JLabel) component.getComponent(1);
        JLabel addressLabel = (JLabel) component.getComponent(2);
        JLabel pricingLabel = (JLabel) component.getComponent(3);
        JLabel imageLabel = (JLabel) component.getComponent(0);
        //Stores JLabel values into variables
        String oldName = nameLabel.getText().replace("Name: ", "");
        String oldAddress = addressLabel.getText().replace("Address: ", "");
        String oldPricing = pricingLabel.getText().replace("Price Range: ", "");
        String oldPath = (String) imageLabel.getClientProperty("imagePath");

        //depending on which field the user wants to change, sets the corresponding label to the new value, and edits the database to reflect that
        if (type.equals("name") || type.equals("Name")) {
            nameLabel.setText(newField);
            backend.editData(oldName, false, newField, oldAddress, oldPricing, oldPath);
        } else if (type.equals("address") || type.equals("Address")) {
            addressLabel.setText(newField);
            backend.editData(oldName, false, oldName, newField, oldPricing, oldPath);
        } else if (type.equals("pricing") || type.equals("Pricing")) {
            pricingLabel.setText(newField);
            backend.editData(oldName, false, oldName, oldAddress, newField, oldPath);
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
                imagePath[0] = imageFile.getAbsolutePath();
            }

            if (imageFile != null) {
                try {
                    //converts file to an icon and attaches it to the JLabel. Also saves the new file path and edits database
                    BufferedImage img = ImageIO.read(imageFile);
                    ImageIcon icon = new ImageIcon(img.getScaledInstance(150, 150, Image.SCALE_SMOOTH));
                    imageLabel.setIcon(icon);
                    imageLabel.setText(null);
                    imageLabel.putClientProperty("imagePath", imagePath[0]);
                    backend.editData(oldName, false, oldName, oldAddress, oldPricing, imagePath[0]);
                } catch (IOException e) {
                    imageLabel = new JLabel("Image load failed");
                }
            } else {
                imageLabel = new JLabel("No Image");
            }
        }
        //recalculates and repaints screen
        panel.revalidate();
        panel.repaint();
    }

    //function to refresh the page so cards are positioned accordingly
    public void refreshPage() {
        //removes all panels, resets contraints, and resets total restaurant count
        panel.removeAll();
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        totalRestaurants = 0;

        //reloads all cards and displays them again
        if(!(backend.getAltData().isEmpty())&&(backend.getSearch().equals(currentSearch))) {
            backend.getAltData().forEach(item -> {
                createCard(item.getName(), item.getAddress(), item.getPricing(), new File(item.getImagePath()));
            });
        }
        else{
            backend.getData().forEach(item -> {
                createCard(item.getName(), item.getAddress(), item.getPricing(), new File(item.getImagePath()));
            });
        }
    }

    //Function to display login page
    public void showLoginPage() {
        //Creates JFrame
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(this);

        //Creates main JPanel
        JPanel page = new JPanel();

        //Creates JPanel for registering and sets gridlayout
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(3, 2));

        //Creates JLabels and JTextFields
        JLabel createName = new JLabel("Create a username: ");
        JTextField createNameField = new JTextField();
        JLabel createPass = new JLabel("Create a password: ");
        JTextField createPassField = new JTextField();
        JButton registerButton = new JButton("Register");
        registerPanel.add(createName);
        registerPanel.add(createNameField);
        registerPanel.add(createPass);
        registerPanel.add(createPassField);
        registerPanel.add(registerButton);

        //Creates loginPanel and sets GridLayout
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));

        //Creates JLabels and JTextFields
        JLabel enterName = new JLabel("Enter username: ");
        JTextField enterNameField = new JTextField();
        JLabel enterPass = new JLabel("Enter password: ");
        JTextField enterPassField = new JTextField();
        JButton loginButton = new JButton("Login");
        loginPanel.add(enterName);
        loginPanel.add(enterNameField);
        loginPanel.add(enterPass);
        loginPanel.add(enterPassField);
        loginPanel.add(loginButton);

        //Adds to main JPanel and main JFrame
        page.add(registerPanel);
        page.add(loginPanel);
        frame.add(page);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        HomePage homepage = new HomePage();
    }
}