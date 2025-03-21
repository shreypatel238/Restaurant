import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {
    private Backend backend;
    private HomePage homePage;

    public LoginPage(Backend backend, HomePage homePage) {
        this.backend = backend;
        this.homePage = homePage;

        //Creates JFrame
        setTitle("Register or Log In");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        createLoginPage();
    }

    private void createLoginPage() {
        //Creates main JPanel
        JPanel loginWindow = new JPanel(new GridLayout(2, 1));

        //Register Panel
        //Creates JPanel for registering and sets gridlayout
        JPanel registerPanel = new JPanel(new GridLayout(3, 2));

        //Creates JLabels and JTextFields
        JLabel regName = new JLabel("Create a username: ");
        JTextField regNameField = new JTextField();
        JLabel regPass = new JLabel("Create a password: ");
        JPasswordField regPassField = new JPasswordField();
        JButton registerButton = new JButton("Register");

        registerButton.addActionListener(e -> {
            //Checks if all fields are filled
            if (regNameField.getText().trim().isEmpty() || regPassField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "You must enter a username and password!", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else if (backend.register(regNameField.getText().trim(), new String(regPassField.getPassword()).trim())) {
                //Registers and displays home page if username is valid
                homePage.setUser(backend.getUser(regNameField.getText().trim()));
                dispose();
            } else {
                //If username is taken, displays error message
                JOptionPane.showMessageDialog(this, "Username is already taken", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerPanel.add(regName);
        registerPanel.add(regNameField);
        registerPanel.add(regPass);
        registerPanel.add(regPassField);
        registerPanel.add(registerButton);

        //Login Panel
        //Creates loginPanel and sets GridLayout
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));

        //Creates JLabels and JTextFields
        JLabel loginName = new JLabel("Enter username: ");
        JTextField loginNameField = new JTextField();
        JLabel loginPass = new JLabel("Enter password: ");
        JPasswordField loginPassField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton guestButton = new JButton("Continue as Guest");


        loginButton.addActionListener(e -> {
            //Checks if all fields are valid
            if (loginNameField.getText().trim().isEmpty() || loginPassField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "You must enter a username and password!", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else if (backend.login(loginNameField.getText().trim(), new String(loginPassField.getPassword()).trim())) {
                //If login is valid, logs in and displays home page
                homePage.setUser(backend.getUser(loginNameField.getText().trim()));
                dispose();
            } else {
                //If login is not valid, displays error message
                JOptionPane.showMessageDialog(this, "Incorrect username or password", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        guestButton.addActionListener(e -> {
            homePage.setUser(backend.getGuest());
            dispose();
        });

        loginPanel.add(loginName);
        loginPanel.add(loginNameField);
        loginPanel.add(loginPass);
        loginPanel.add(loginPassField);
        loginPanel.add(loginButton);
        loginPanel.add(guestButton);
        
    
        
        //Adds to main JPanel and main JFrame of Login Page
        loginWindow.add(registerPanel);
        loginWindow.add(loginPanel);
        add(loginWindow);
    }
}