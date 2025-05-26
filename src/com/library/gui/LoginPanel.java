package com.library.gui;


import javax.swing.*;
import java.awt.*;

/**
 * Panel login untuk aplikasi Lendify
 */
public class LoginPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final String SYSTEM_PASSWORD = "lendify";
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    
    private LendifyGUI mainWindow;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    private JLabel messageLabel;
    private int loginAttempts = 0;
    
    /**
     * Constructor untuk LoginPanel
     */
    public LoginPanel(LendifyGUI mainWindow) {
        this.mainWindow = mainWindow;
        setupUI();
    }
    
    /**
     * Setup komponen UI
     */
    private void setupUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);

        // Judul
        JLabel titleLabel = new JLabel("LENDIFY", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 38));
        titleLabel.setForeground(new Color(0x1A237E));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Subjudul
        JLabel subtitleLabel = new JLabel("Sistem Manajemen Perpustakaan", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        subtitleLabel.setForeground(new Color(0x3949AB));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        // Panel anggota kelompok
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        groupPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0x7986CB), 2), "Anggota Kelompok 2 - Kelas B"),
            BorderFactory.createEmptyBorder(10, 18, 10, 18)
        ));
        groupPanel.setBackground(new Color(0xF5F5F5));
        groupPanel.add(new JLabel("Dandy Faishal Fahmi - 24060123140136"));
        groupPanel.add(new JLabel("Fauzan Hadi         - 24060123140176"));
        groupPanel.add(new JLabel("Gaza Al-Ghazali C.  - 24060123140183"));
        groupPanel.add(new JLabel("Diva Arfis Permata  - 24060123130102"));
        groupPanel.add(new JLabel("Ganendra Dzahwan Y. - 24060123140148"));
        groupPanel.setMaximumSize(new Dimension(420, 140));
        mainPanel.add(groupPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 24)));

        // Form login
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Password field
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        passwordPanel.setOpaque(false);
        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(200, 32));
        passwordField.addActionListener(e -> attemptLogin());
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        loginPanel.add(passwordPanel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Message label
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(new Color(0xD32F2F));
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(messageLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        buttonPanel.setOpaque(false);
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(110, 38));
        exitButton = new JButton("Keluar");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 16));
        exitButton.setPreferredSize(new Dimension(110, 38));
        loginButton.addActionListener(e -> attemptLogin());
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);
        loginPanel.add(buttonPanel);

        mainPanel.add(loginPanel);

        // Tambahkan mainPanel ke tengah
        add(mainPanel, gbc);
    }
    
    /**
     * Coba login dengan password yang diinput
     */
    private void attemptLogin() {
        String password = new String(passwordField.getPassword());
        
        if (password.equals(SYSTEM_PASSWORD)) {
            messageLabel.setText("");
            // Setelah login, cek apakah nama/alamat perpustakaan sudah ada
            if (mainWindow.getLibrary().getName() == null || mainWindow.getLibrary().getName().isEmpty() ||
                mainWindow.getLibrary().getAddress() == null || mainWindow.getLibrary().getAddress().isEmpty()) {
                mainWindow.showSetupPanel();
            } else {
                mainWindow.showMainPanel();
            }
        } else {
            loginAttempts++;
            int remainingAttempts = MAX_LOGIN_ATTEMPTS - loginAttempts;
            
            if (remainingAttempts > 0) {
                messageLabel.setText("Password salah. Sisa percobaan: " + remainingAttempts);
            } else {
                messageLabel.setText("Terlalu banyak percobaan gagal. Program akan ditutup.");
                loginButton.setEnabled(false);
                Timer timer = new Timer(2000, e -> System.exit(0));
                timer.setRepeats(false);
                timer.start();
            }
        }

        passwordField.setText("");
    }
    
    /**
     * Reset login attempts counter
     */
    public void resetLoginAttempts() {
        loginAttempts = 0;
        messageLabel.setText("");
        loginButton.setEnabled(true);
    }
}