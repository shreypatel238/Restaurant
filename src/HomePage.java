import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomePage extends JFrame {
    private JMenuBar menu;
    private JPanel panel;
    private GridBagConstraints constraints;
    private int totalRestaurants = 0;
    private Backend backend;

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
            createCard(item.getName(), item.getAddress(), item.getPricing());
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
                searchBar.setText("");
            }
        });

        menu.add(options);
        menu.add(Box.createHorizontalGlue());
        menu.add(searchBar);
        menu.add(Box.createHorizontalGlue());
        this.setJMenuBar(menu);

        addButton.addActionListener(e -> addRestaurant());

        this.setVisible(true);
    }


    public void createCard(String name, String address, String pricing) {
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

        JPanel detailPanel = new JPanel(new GridLayout(3, 1));
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

    public void addRestaurant() {
        JTextField nameField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField pricingField = new JTextField(20);

        JPanel enterPanel = new JPanel(new GridLayout(3, 2));
        enterPanel.add(new JLabel("Enter Restaurant Name:"));
        enterPanel.add(nameField);
        enterPanel.add(new JLabel("Enter Restaurant Address:"));
        enterPanel.add(addressField);
        enterPanel.add(new JLabel("Enter Restaurant Price Range (Ex. $10-$100):"));
        enterPanel.add(pricingField);

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

            backend.addData(name, address, pricing); //Add to CSV

            createCard(name, address, pricing);
        }
    }

    public void removeRestaurant(JPanel restaurantPanel) {
        JPanel component = (JPanel) restaurantPanel.getComponent(1);
        JLabel nameLabel = (JLabel) component.getComponent(0);

        panel.remove(restaurantPanel);
        totalRestaurants -= 1;
        backend.removeData(nameLabel.getText().replace("Name: ", ""), false);

        refreshPage();

        panel.revalidate();
        panel.repaint();
    }

    public void editRestaurant(JPanel restaurantPanel) {
        String type = JOptionPane.showInputDialog(this, "Which field would you like to edit? Name, Address or Pricing");
        if (type.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a field to change!");
            return;
        }

        String newField = JOptionPane.showInputDialog(this, "Enter new " + type);
        if (newField.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "You must enter something!");
            return;
        }

        JPanel component = (JPanel) restaurantPanel.getComponent(1);
        JLabel nameLabel = (JLabel) component.getComponent(0);
        JLabel addressLabel = (JLabel) component.getComponent(1);
        JLabel pricingLabel = (JLabel) component.getComponent(2);
        String oldName = nameLabel.getText().replace("Name: ", "");
        String oldAddress = addressLabel.getText().replace("Address: ", "");
        String oldPricing = pricingLabel.getText().replace("Price Range: ", "");

        if (type.equals("name") || type.equals("Name")) {
            nameLabel.setText("Name: " + newField);
            backend.editData(oldName, false, newField, addressLabel.getText().replace("Address: ", ""), pricingLabel.getText().replace("Price Range: ", ""));
        } else if (type.equals("address") || type.equals("Address")) {
            addressLabel.setText("Address: " + newField);
            backend.editData(oldName, false, oldName, newField, oldPricing);
        } else if (type.equals("pricing") || type.equals("Pricing")) {
            pricingLabel.setText("Price Range: " + newField);
            backend.editData(oldName, false, oldName, oldAddress, newField);
        }
        System.out.println(nameLabel.getText());
        panel.revalidate();
        panel.repaint();
    }

    public void refreshPage() {
        panel.removeAll();
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        totalRestaurants = 0;
        backend.getData().forEach(item -> {
            createCard(item.getName(), item.getAddress(), item.getPricing());
        });
    }

    public static void main(String[] args) {
        HomePage homepage = new HomePage();
    }
}