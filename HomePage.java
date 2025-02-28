import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

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
        JMenuItem removeButton = new JMenuItem("Remove Restaurant");
        JMenuItem editButton = new JMenuItem("Edit Restaurant");

        options.add(addButton);
        options.add(removeButton);
        options.add(editButton);

        menu.add(options);
        this.setJMenuBar(menu);

        addButton.addActionListener(e -> addRestaurant());
        removeButton.addActionListener(e -> removeRestaurant());
        editButton.addActionListener(e -> editRestaurant());

        this.setVisible(true);
    }


    public void createCard(String name, String address, String pricing) {
        JPanel restaurantPanel = new JPanel();
        restaurantPanel.setPreferredSize(new Dimension(200, 200));
        restaurantPanel.setMinimumSize(new Dimension(200, 200));
        restaurantPanel.setMaximumSize(new Dimension(200, 200));
        restaurantPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        restaurantPanel.setLayout(new GridLayout(3, 1));

        JLabel restName = new JLabel("Name: " + name);
        JLabel restAddress = new JLabel("Address: " + address);
        JLabel restPricing = new JLabel("Price Range: " + pricing);

        restaurantPanel.add(restName);
        restaurantPanel.add(restAddress);
        restaurantPanel.add(restPricing);

        this.constraints.gridx = totalRestaurants % 6;
        this.constraints.gridy = totalRestaurants / 6;

        this.panel.add(restaurantPanel, this.constraints);
        totalRestaurants += 1;
        panel.revalidate();
        panel.repaint();
    }

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

    public void removeRestaurant() {
        if (totalRestaurants == 0) {
            JOptionPane.showMessageDialog(this, "No restaurants to remove!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String toRemove = JOptionPane.showInputDialog(this, "Enter restaurant name to remove (MUST BE EXACT)");
        if (toRemove.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "You must enter a name!");
            return;
        }

        this.backend.removeData(toRemove, false);

        Component components[] = panel.getComponents();
        for (Component x : components) {
            if (x instanceof JPanel) {
                JPanel tempPanel = (JPanel) x;
                Component[] names = tempPanel.getComponents();

                for (Component y : names) {
                    if (y instanceof JLabel) {
                        JLabel tempLabel = (JLabel) y;
                        if (tempLabel.getText().equals("Name: " + toRemove)) {
                            panel.remove(tempPanel);
                            totalRestaurants -= 1;

                            Component remainComponents[] = panel.getComponents();

                            panel.removeAll();

                            for (int i = 0; i < remainComponents.length; i++) {
                                constraints.gridx = i % 6;
                                constraints.gridy = i / 6;
                                panel.add(remainComponents[i], constraints);
                            }

                            panel.revalidate();
                            panel.repaint();
                            return;
                        }
                    }
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Restaurant not found");
    }

    public void editRestaurant() {
        if (totalRestaurants == 0) {
            JOptionPane.showMessageDialog(this, "No restaurants to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String toEdit = JOptionPane.showInputDialog(this, "Enter restaurant name to edit (MUST BE EXACT)");
        String type = JOptionPane.showInputDialog(this, "Which field would you like to edit? Name, Address or Pricing");
        String newField = JOptionPane.showInputDialog(this, "Enter new detail");

        Component components[] = panel.getComponents();
        for (Component x : components) {
            if (x instanceof JPanel) {
                JPanel tempPanel = (JPanel) x;
                Component[] labels = tempPanel.getComponents();

                JLabel nameLabel = null;
                JLabel addressLabel = null;
                JLabel priceLabel = null;

                for (Component y : labels) {
                    if (y instanceof JLabel) {
                        JLabel tempLabel = (JLabel) y;
                        if (tempLabel.getText().startsWith("Name: ")) {
                            nameLabel = tempLabel;
                        } else if (tempLabel.getText().startsWith("Address: ")) {
                            addressLabel = tempLabel;
                        } else if (tempLabel.getText().startsWith("Price Range: ")) {
                            priceLabel = tempLabel;
                        }
                    }
                }

                if (nameLabel != null && nameLabel.getText().equals("Name: " + toEdit)) {
                    if (type.equals("Name") || type.equals("name")) {
                        nameLabel.setText("Name: " + newField);
                        backend.editData(toEdit, false, newField, addressLabel.getText().substring(addressLabel.getText().indexOf(":") + 2), priceLabel.getText().substring(priceLabel.getText().indexOf(":") + 2));
                    } else if (type.equals("Address") || type.equals("address")) {
                        if (addressLabel != null) addressLabel.setText("Address: " + newField);
                        backend.editData(toEdit, false, nameLabel.getText().substring(nameLabel.getText().indexOf(":") + 2), newField, priceLabel.getText().substring(priceLabel.getText().indexOf(":") + 2));
                    } else if (type.equals("Pricing") || type.equals("pricing")) {
                        if (priceLabel != null) priceLabel.setText("Price Range: " + newField);
                        backend.editData(toEdit, false, nameLabel.getText().substring(nameLabel.getText().indexOf(":") + 2), addressLabel.getText().substring(addressLabel.getText().indexOf(":") + 2), newField);
                    }

                    panel.revalidate();
                    panel.repaint();
                    return;
                }
            }
        }

        JOptionPane.showMessageDialog(this, "Restaurant not found");
    }

    public static void main(String[] args) {
        HomePage homepage = new HomePage();
    }
}