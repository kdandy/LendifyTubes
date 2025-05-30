package com.library.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class LibrarySetupPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private LendifyGUI mainWindow;
    private JTextField nameField;
    private JTextField addressField;
    private JLabel messageLabel;
    private JButton nextButton;

    public LibrarySetupPanel(LendifyGUI mainWindow) {
        this.mainWindow = mainWindow;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        
        // Background dengan gradient
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(0x667eea),
                    0, getHeight(), new Color(0x764ba2)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        
        // Main card panel dengan shadow effect
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow effect
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(8, 8, getWidth() - 8, getHeight() - 8, 20, 20);
                
                // Main card background
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, 20, 20);
            }
        };
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(500, 600));
        
        // Header section
        createHeaderSection(cardPanel);
        
        // Form section
        createFormSection(cardPanel);
        
        // Button section
        createButtonSection(cardPanel);
        
        // Center the card panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(cardPanel, gbc);
        
        add(backgroundPanel, BorderLayout.CENTER);
    }
    
    private void createHeaderSection(JPanel parent) {
        // Icon
        JLabel iconLabel = new JLabel("\ud83c\udfe2"); // Library building emoji
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(iconLabel);
        parent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Title
        JLabel titleLabel = new JLabel("Setup Perpustakaan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(0x2c3e50));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(titleLabel);
        parent.add(Box.createRigidArea(new Dimension(0, 8)));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Konfigurasikan informasi perpustakaan Anda", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(0x7f8c8d));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(subtitleLabel);
        parent.add(Box.createRigidArea(new Dimension(0, 40)));
    }
    
    private void createFormSection(JPanel parent) {
        // Name field
        createInputField(parent, "Nama Perpustakaan", "Masukkan nama perpustakaan...", true);
        parent.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Address field
        createInputField(parent, "Alamat Perpustakaan", "Masukkan alamat lengkap...", false);
        parent.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Message label
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(messageLabel);
        parent.add(Box.createRigidArea(new Dimension(0, 30)));
    }
    
    private void createInputField(JPanel parent, String labelText, String placeholder, boolean isNameField) {
        // Label
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(0x34495e));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(label);
        parent.add(Box.createRigidArea(new Dimension(0, 8)));
        
        // Input field with modern styling
        JTextField field = new JTextField(25) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Border
                if (hasFocus()) {
                    g2d.setColor(new Color(0x3498db));
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(new Color(0xbdc3c7));
                    g2d.setStroke(new BasicStroke(1));
                }
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                
                super.paintComponent(g);
            }
        };
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(380, 45));
        field.setMaximumSize(new Dimension(380, 45));
        field.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        field.setBackground(new Color(0xf8f9fa));
        field.setOpaque(false);
        
        // Placeholder effect
        field.setForeground(new Color(0xadb5bd));
        field.setText(placeholder);
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(new Color(0x2c3e50));
                }
                field.repaint();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(new Color(0xadb5bd));
                    field.setText(placeholder);
                }
                field.repaint();
            }
        });
        
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        if (isNameField) {
            nameField = field;
        } else {
            addressField = field;
        }
        
        parent.add(field);
    }
    
    private void createButtonSection(JPanel parent) {
        nextButton = new JButton("Mulai Menggunakan Lendify") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient;
                if (getModel().isPressed()) {
                    gradient = new GradientPaint(
                        0, 0, new Color(0x2980b9),
                        0, getHeight(), new Color(0x3498db)
                    );
                } else if (getModel().isRollover()) {
                    gradient = new GradientPaint(
                        0, 0, new Color(0x3498db),
                        0, getHeight(), new Color(0x5dade2)
                    );
                } else {
                    gradient = new GradientPaint(
                        0, 0, new Color(0x667eea),
                        0, getHeight(), new Color(0x764ba2)
                    );
                }
                
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                
                // Text
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() + textHeight) / 2 - 2;
                g2d.drawString(getText(), x, y);
            }
        };
        
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nextButton.setPreferredSize(new Dimension(300, 50));
        nextButton.setMaximumSize(new Dimension(300, 50));
        nextButton.setFocusPainted(false);
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.addActionListener(e -> {
            submitLibraryInfo();
        });
        
        parent.add(nextButton);
    }

    private void submitLibraryInfo() {
        String name = getFieldText(nameField, "Masukkan nama perpustakaan...");
        String address = getFieldText(addressField, "Masukkan alamat lengkap...");
        
        // Validasi input
        if (name.isEmpty() || address.isEmpty()) {
            showMessage("Semua field harus diisi!", false);
            return;
        }
        
        // Validasi panjang minimum
        if (name.length() < 3) {
            showMessage("Nama perpustakaan minimal 3 karakter!", false);
            return;
        }
        
        if (address.length() < 5) {
            showMessage("Alamat perpustakaan minimal 5 karakter!", false);
            return;
        }
        
        // Tampilkan pesan sukses
        showMessage("✓ Setup berhasil! Menuju halaman utama...", true);
        nextButton.setEnabled(false);
        
        // Set informasi perpustakaan
        mainWindow.setLibraryInfo(name, address);
        
        // Delay sebentar untuk menampilkan pesan sukses, lalu pindah ke main panel
        Timer timer = new Timer(2000, e -> {
            SwingUtilities.invokeLater(() -> {
                mainWindow.showMainPanel();
                nextButton.setEnabled(true);
            });
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private String getFieldText(JTextField field, String placeholder) {
        String text = field.getText();
        return (text == null || text.equals(placeholder)) ? "" : text.trim();
    }
    
    private void showMessage(String message, boolean isSuccess) {
        try {
            if (messageLabel == null) {
                System.err.println("ERROR: messageLabel is null!");
                return;
            }
            
            SwingUtilities.invokeLater(() -> {
                messageLabel.setText(message);
                if (isSuccess) {
                    messageLabel.setForeground(new Color(0x27ae60));
                } else {
                    messageLabel.setForeground(new Color(0xe74c3c));
                }
                messageLabel.repaint();
            });
        } catch (Exception e) {
            System.err.println("Error showing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}