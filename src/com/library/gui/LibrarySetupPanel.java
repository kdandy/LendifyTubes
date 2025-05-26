package com.library.gui;

import javax.swing.*;
import java.awt.*;

public class LibrarySetupPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private LendifyGUI mainWindow;
    private JTextField nameField;
    private JTextField addressField;
    private JLabel messageLabel;

    public LibrarySetupPanel(LendifyGUI mainWindow) {
        this.mainWindow = mainWindow;
        setupUI();
    }

    private void setupUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(0xF3F6FB));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(28, 36, 28, 36),
            BorderFactory.createLineBorder(new Color(0x90A4AE), 1, true)
        ));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setMaximumSize(new Dimension(420, 320));
        cardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Judul
        JLabel titleLabel = new JLabel("Setup Perpustakaan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(0x1565C0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        // Field Nama
        JLabel nameLabel = new JLabel("Nama Perpustakaan:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(nameLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        nameField = new JTextField(32);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField.setMaximumSize(new Dimension(320, 36));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x90A4AE)),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        cardPanel.add(nameField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Field Alamat
        JLabel addressLabel = new JLabel("Alamat Perpustakaan:");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        addressLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(addressLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        addressField = new JTextField(32);
        addressField.setFont(new Font("Arial", Font.PLAIN, 16));
        addressField.setMaximumSize(new Dimension(320, 36));
        addressField.setAlignmentX(Component.CENTER_ALIGNMENT);
        addressField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x90A4AE)),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        cardPanel.add(addressField);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 16)));

        // Pesan error
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setForeground(new Color(0xD32F2F));
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(messageLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Tombol lanjut
        JButton nextButton = new JButton("Lanjut");
        nextButton.setFont(new Font("Arial", Font.BOLD, 18));
        nextButton.setBackground(new Color(0x1976D2));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.setPreferredSize(new Dimension(200, 44));
        nextButton.setMaximumSize(new Dimension(220, 44));
        nextButton.setMinimumSize(new Dimension(160, 44));
        nextButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        nextButton.addActionListener(e -> submitLibraryInfo());
        cardPanel.add(nextButton);

        add(cardPanel, gbc);
    }

    private void submitLibraryInfo() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        if (name.isEmpty() || address.isEmpty()) {
            messageLabel.setText("Nama dan alamat harus diisi!");
            return;
        }
        mainWindow.setLibraryInfo(name, address);
        mainWindow.showMainPanel();
    }
} 