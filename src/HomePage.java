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

        menu = new JMenuBar();
        JMenu options = new JMenu("Options");
        JMenuItem addButton = new JMenuItem("Add Restaurant");
        options.add(addButton);

        JTextField searchBar = new JTextField("Search");
        searchBar.setPreferredSize(new Dimension(200, 20));
        searchBar.setMinimumSize(new Dimension(200, 20));
        searchBar.setMaximumSize(new Dimension(200, 20));
        searchBar.setBorder(BorderFactory.createLineBorder(Color.white));

        searchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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

        menu.add(options);
        menu.add(Box.createHorizontalGlue());
        menu.add(searchBar);
        menu.add(Box.createHorizontalGlue());
        this.setJMenuBar(menu);

        addButton.addActionListener(e -> {
            try {
                addRestaurant();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        this.setVisible(true);
    }


    public void createCard(String name, String address, String pricing, File image) {
        JPanel restaurantPanel = new JPanel();
        restaurantPanel.setPreferredSize(new Dimension(300, 300));
        restaurantPanel.setMinimumSize(new Dimension(300, 300));
        restaurantPanel.setMaximumSize(new Dimension(300, 300));
        restaurantPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        restaurantPanel.setLayout(new BorderLayout());
        restaurantPanel.setBackground(new Color(245, 224, 130));

        JLabel restName = new JLabel(name);
        JLabel restPricing = new JLabel(pricing);
        JLabel restAddress = new JLabel(address);
        JLabel restImage;
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

        restName.setFont(new Font("", Font.BOLD, 25));
        restAddress.setFont(new Font("", Font.BOLD, 15));
        restPricing.setFont(new Font("", Font.BOLD, 20));

        JPanel editPanel = new JPanel(new BorderLayout());
        editPanel.setBackground(new Color(245, 224, 130));
        JMenuBar menuBar = new JMenuBar();
        JMenu dots = new JMenu("...");
        JMenuItem updateButton = new JMenuItem("Update");
        JMenuItem removeButton = new JMenuItem("Remove");
        dots.add(updateButton);
        dots.add(removeButton);
        menuBar.add(dots);

        removeButton.addActionListener(e -> removeRestaurant(restaurantPanel));
        updateButton.addActionListener(e -> editRestaurant(restaurantPanel));

        editPanel.add(menuBar, BorderLayout.LINE_END);

        JPanel detailPanel = new JPanel(new GridLayout(4, 1));
        detailPanel.add(restImage);
        detailPanel.add(restName);
        detailPanel.add(restPricing);
        detailPanel.add(restAddress);
        detailPanel.setBackground(new Color(245, 224, 130));

        JButton viewDetails = new JButton("View Details");

        restaurantPanel.add(editPanel, BorderLayout.PAGE_START);
        restaurantPanel.add(detailPanel, BorderLayout.CENTER);
        restaurantPanel.add(viewDetails, BorderLayout.PAGE_END);

        constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        this.constraints.gridx = totalRestaurants % 4;
        this.constraints.gridy = totalRestaurants / 4;

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

    public void addRestaurant() throws IOException {
        JTextField nameField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField pricingField = new JTextField(20);
        String[] imagePath = {""};

        JPanel enterPanel = new JPanel(new GridLayout(4, 2));
        enterPanel.add(new JLabel("Enter Restaurant Name:"));
        enterPanel.add(nameField);
        enterPanel.add(new JLabel("Enter Restaurant Address:"));
        enterPanel.add(addressField);
        enterPanel.add(new JLabel("Enter Restaurant Price Range (Ex. $10-$100):"));
        enterPanel.add(pricingField);
        enterPanel.add(new JLabel("Select an image"));
        JButton selectButton = new JButton("Select");

        selectButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
            fileChooser.setFileFilter(filter);
            fileChooser.setCurrentDirectory(new File("."));

            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File imageFile = fileChooser.getSelectedFile();
                imagePath[0] =imageFile.getAbsolutePath();
            }
        });
        enterPanel.add(selectButton);

        int entered = JOptionPane.showConfirmDialog(this, enterPanel, "Enter Restaurant Information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

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

            backend.addData(name, address, pricing, imagePath[0]); //Add to CSV

            createCard(name, address, pricing, new File(imagePath[0]));
        }
    }

    public void removeRestaurant(JPanel restaurantPanel) {
        JPanel component = (JPanel) restaurantPanel.getComponent(1);
        JLabel nameLabel = (JLabel) component.getComponent(1);

        panel.remove(restaurantPanel);
        totalRestaurants -= 1;
        backend.removeData(nameLabel.getText().replace("Name: ", ""), false);

        refreshPage();

        panel.revalidate();
        panel.repaint();
    }

    public void editRestaurant(JPanel restaurantPanel) {
        String type = JOptionPane.showInputDialog(this, "Which field would you like to edit? Name, Address, Pricing, or Image");
        if (type == null || type.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a field to change!");
            return;
        }

        String newField = "";
        if (!type.equals("image") && !type.equals("Image")) {
            newField = JOptionPane.showInputDialog(this, "Enter new " + type);
            if (newField.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "You must enter something!");
                return;
            }
        }

        JPanel component = (JPanel) restaurantPanel.getComponent(1);
        JLabel nameLabel = (JLabel) component.getComponent(1);
        JLabel addressLabel = (JLabel) component.getComponent(2);
        JLabel pricingLabel = (JLabel) component.getComponent(3);
        JLabel imageLabel = (JLabel) component.getComponent(0);
        String oldName = nameLabel.getText().replace("Name: ", "");
        String oldAddress = addressLabel.getText().replace("Address: ", "");
        String oldPricing = pricingLabel.getText().replace("Price Range: ", "");
        String oldPath = (String) imageLabel.getClientProperty("imagePath");

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
            String[] imagePath = {""};
            JFileChooser fileChooser = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
            fileChooser.setFileFilter(filter);
            fileChooser.setCurrentDirectory(new File("."));
            File imageFile = null;

            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                imageFile = fileChooser.getSelectedFile();
                imagePath[0] = imageFile.getAbsolutePath();
            }

            if (imageFile != null) {
                try {
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
        panel.revalidate();
        panel.repaint();
    }

    public void refreshPage() {
        panel.removeAll();
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        totalRestaurants = 0;
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

    public static void main(String[] args) {
        HomePage homepage = new HomePage();
    }
}