package com.library.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ModeSelectionPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private LendifyGUI mainWindow;

    public ModeSelectionPanel(LendifyGUI mainWindow) {
        this.mainWindow = mainWindow;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80));

        JLabel label = new JLabel("Pilih Mode Aplikasi", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);
        add(Box.createRigidArea(new Dimension(0, 30)));

        JButton guiButton = new JButton("Lanjut ke GUI");
        guiButton.setFont(new Font("Arial", Font.PLAIN, 18));
        guiButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        guiButton.addActionListener((ActionEvent e) -> mainWindow.showMainPanel());
        add(guiButton);
        add(Box.createRigidArea(new Dimension(0, 20)));

        JButton terminalButton = new JButton("Pakai Mode Terminal");
        terminalButton.setFont(new Font("Arial", Font.PLAIN, 18));
        terminalButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        terminalButton.addActionListener((ActionEvent e) -> {
            // Tutup GUI, jalankan mode terminal
            SwingUtilities.invokeLater(() -> {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (topFrame != null) {
                    topFrame.dispose();
                }
                com.library.Main.runTerminalModeStatic();
            });
        });
        add(terminalButton);
    }
} 