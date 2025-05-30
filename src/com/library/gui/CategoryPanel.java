package com.library.gui;

import com.library.enums.LibrarianPermission;
import com.library.gui.utils.GUIUtils;
import com.library.gui.utils.TableModels.CategoryTableModel;
import com.library.model.BookCategory;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Panel untuk kelola kategori buku
 */
public class CategoryPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private LendifyGUI mainWindow;
    private JTable categoryTable;
    private CategoryTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton backButton;
    private JTextField searchField;
    
    /**
     * Constructor untuk CategoryPanel
     */
    public CategoryPanel(LendifyGUI mainWindow) {
        this.mainWindow = mainWindow;
        setupUI();
    }
    
    /**
     * Setup komponen UI
     */
    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        // Main container dengan gradient background
        JPanel mainContainer = new JPanel(new BorderLayout(0, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(240, 248, 255), 0, getHeight(), new Color(230, 240, 250));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Title card dengan shadow
        JPanel titleCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 15, 15);
                
                // Main card
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 15, 15);
            }
        };
        titleCard.setOpaque(false);
        titleCard.setLayout(new BorderLayout(20, 0));
        titleCard.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        // Header panel dengan icon dan title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        headerPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("📂");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JLabel titleLabel = new JLabel("Kelola Kategori Buku");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(51, 51, 51));
        
        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);
        
        // Search panel dengan modern design
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);
        
        JLabel searchLabel = new JLabel("🔍 Cari:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchLabel.setForeground(new Color(102, 102, 102));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JButton searchButton = createModernButton("Cari", new Color(52, 152, 219), Color.WHITE);
        searchButton.addActionListener(e -> searchCategories());
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        titleCard.add(headerPanel, BorderLayout.WEST);
        titleCard.add(searchPanel, BorderLayout.EAST);
        
        mainContainer.add(titleCard, BorderLayout.NORTH);
        
        // Content card untuk tabel
        JPanel contentCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 15, 15);
                
                // Main card
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 15, 15);
            }
        };
        contentCard.setOpaque(false);
        contentCard.setLayout(new BorderLayout());
        contentCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel tabel
        tableModel = new CategoryTableModel(new ArrayList<>(mainWindow.getLibrary().getCollection().getCategories()));
        categoryTable = new JTable(tableModel);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTable.setRowHeight(30);
        categoryTable.getTableHeader().setReorderingAllowed(false);
        categoryTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Sorter untuk tabel
        TableRowSorter<CategoryTableModel> sorter = new TableRowSorter<>(tableModel);
        categoryTable.setRowSorter(sorter);
        
        // Mouse listener untuk double-click
        categoryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editCategory();
                }
            }
        });
        
        // Panel untuk tabel dengan scrolling
        JScrollPane tableScrollPane = new JScrollPane(categoryTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        contentCard.add(tableScrollPane, BorderLayout.CENTER);
        
        mainContainer.add(contentCard, BorderLayout.CENTER);
        
        // Button card dengan shadow
        JPanel buttonCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Shadow
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(3, 3, getWidth() - 3, getHeight() - 3, 15, 15);
                
                // Main card
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 15, 15);
            }
        };
        buttonCard.setOpaque(false);
        buttonCard.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonCard.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Modern buttons
        addButton = createModernButton("➕ Tambah Kategori", new Color(46, 204, 113), Color.WHITE);
        editButton = createModernButton("✏️ Edit Kategori", new Color(52, 152, 219), Color.WHITE);
        deleteButton = createModernButton("🗑️ Hapus Kategori", new Color(231, 76, 60), Color.WHITE);
        backButton = createModernButton("🏠 Kembali ke Menu Utama", new Color(149, 165, 166), Color.WHITE);
        
        addButton.addActionListener(e -> addCategory());
        editButton.addActionListener(e -> editCategory());
        deleteButton.addActionListener(e -> deleteCategory());
        backButton.addActionListener(e -> mainWindow.showMainPanel());
        
        buttonCard.add(addButton);
        buttonCard.add(editButton);
        buttonCard.add(deleteButton);
        buttonCard.add(backButton);
        
        mainContainer.add(buttonCard, BorderLayout.SOUTH);
        
        add(mainContainer, BorderLayout.CENTER);
        
        // Set initial button states
        updateButtonStates();
    }
    
    /**
     * Refresh data tabel
     */
    public void refreshData() {
        tableModel.setCategories(new ArrayList<>(mainWindow.getLibrary().getCollection().getCategories()));
        updateButtonStates();
    }
    
    /**
     * Update status tombol berdasarkan hak akses
     */
    private void updateButtonStates() {
        boolean isBasic = mainWindow.getCurrentLibrarian().getPermission() == LibrarianPermission.BASIC;
        boolean isAdmin = mainWindow.getCurrentLibrarian().getPermission() == LibrarianPermission.ADMIN;
        
        addButton.setEnabled(!isBasic);
        editButton.setEnabled(!isBasic);
        deleteButton.setEnabled(isAdmin);
    }
    
    /**
     * Cari kategori berdasarkan keyword
     */
    private void searchCategories() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            refreshData();
            return;
        }
        
        ArrayList<BookCategory> filteredList = new ArrayList<>();
        for (BookCategory category : mainWindow.getLibrary().getCollection().getCategories()) {
            if (category.getName().toLowerCase().contains(keyword) ||
                category.getDescription().toLowerCase().contains(keyword)) {
                filteredList.add(category);
            }
        }
        
        tableModel.setCategories(filteredList);
    }
    
    /**
     * Tambah kategori baru
     */
    private void addCategory() {
        if (mainWindow.getCurrentLibrarian().getPermission() == LibrarianPermission.BASIC) {
            GUIUtils.errorDialog(this, "Anda tidak memiliki hak akses untuk menambah kategori!", "Akses Ditolak");
            return;
        }
        
        // Panel form untuk input data
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        JTextField nameField = new JTextField(20);
        JTextArea descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        
        formPanel.add(new JLabel("Nama Kategori:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Deskripsi:"));
        formPanel.add(descriptionScrollPane);
        
        // Tampilkan dialog
        int result = JOptionPane.showConfirmDialog(
            this, formPanel, "Tambah Kategori", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Validasi input
                String name = nameField.getText().trim();
                String description = descriptionArea.getText().trim();
                
                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Nama kategori tidak boleh kosong!");
                }
                
                // Cek apakah kategori dengan nama sama sudah ada
                for (BookCategory category : mainWindow.getLibrary().getCollection().getCategories()) {
                    if (category.getName().equalsIgnoreCase(name)) {
                        throw new IllegalArgumentException("Kategori dengan nama " + name + " sudah ada!");
                    }
                }
                
                // Buat objek BookCategory
                BookCategory category = new BookCategory(name, description);
                
                // Tambahkan ke library
                mainWindow.addCategory(category);
                refreshData();
                
                GUIUtils.infoDialog(this, "Kategori berhasil ditambahkan!", "Sukses");
            } catch (Exception ex) {
                GUIUtils.errorDialog(this, ex.getMessage(), "Error");
            }
        }
    }
    
    /**
     * Membuat button dengan design modern
     */
    private JButton createModernButton(String text, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = backgroundColor;
                if (getModel().isPressed()) {
                    bgColor = backgroundColor.darker();
                } else if (getModel().isRollover()) {
                    bgColor = backgroundColor.brighter();
                }
                
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 40));
        
        return button;
    }
    
    /**
     * Edit kategori yang dipilih
     */
    private void editCategory() {
        if (mainWindow.getCurrentLibrarian().getPermission() == LibrarianPermission.BASIC) {
            GUIUtils.errorDialog(this, "Anda tidak memiliki hak akses untuk mengubah kategori!", "Akses Ditolak");
            return;
        }
        
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            GUIUtils.errorDialog(this, "Pilih kategori yang akan diubah!", "Tidak Ada Kategori Dipilih");
            return;
        }
        
        int modelRow = categoryTable.convertRowIndexToModel(selectedRow);
        BookCategory category = tableModel.getCategoryAt(modelRow);
        
        // Panel form untuk edit data
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        JTextField nameField = new JTextField(category.getName(), 20);
        JTextArea descriptionArea = new JTextArea(category.getDescription(), 5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        
        formPanel.add(new JLabel("Nama Kategori:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Deskripsi:"));
        formPanel.add(descriptionScrollPane);
        
        // Tampilkan dialog
        int result = JOptionPane.showConfirmDialog(
            this, formPanel, "Edit Kategori", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Validasi input
                String name = nameField.getText().trim();
                String description = descriptionArea.getText().trim();
                
                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Nama kategori tidak boleh kosong!");
                }
                
                // Cek apakah kategori dengan nama sama sudah ada (kecuali kategori yang sedang diedit)
                for (BookCategory cat : mainWindow.getLibrary().getCollection().getCategories()) {
                    if (cat.getName().equalsIgnoreCase(name) && !cat.equals(category)) {
                        throw new IllegalArgumentException("Kategori dengan nama " + name + " sudah ada!");
                    }
                }
                
                // Update dari map jika nama berubah
                if (!category.getName().equals(name)) {
                    mainWindow.getCategories().remove(category.getName());
                    category.setName(name);
                    mainWindow.getCategories().put(name, category);
                } else {
                    category.setName(name);
                }
                
                category.setDescription(description);
                
                refreshData();
                
                GUIUtils.infoDialog(this, "Kategori berhasil diubah!", "Sukses");
            } catch (Exception ex) {
                GUIUtils.errorDialog(this, ex.getMessage(), "Error");
            }
        }
    }
    
    /**
     * Hapus kategori yang dipilih
     */
    private void deleteCategory() {
        if (mainWindow.getCurrentLibrarian().getPermission() != LibrarianPermission.ADMIN) {
            GUIUtils.errorDialog(this, "Anda tidak memiliki hak akses untuk menghapus kategori!", "Akses Ditolak");
            return;
        }
        
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            GUIUtils.errorDialog(this, "Pilih kategori yang akan dihapus!", "Tidak Ada Kategori Dipilih");
            return;
        }
        
        int modelRow = categoryTable.convertRowIndexToModel(selectedRow);
        BookCategory category = tableModel.getCategoryAt(modelRow);
        
        // Cek apakah kategori memiliki buku
        if (!category.getBooks().isEmpty()) {
            boolean force = GUIUtils.confirmDialog(
                this, 
                "Kategori ini memiliki " + category.getBooks().size() + " buku terkait. Tetap hapus?", 
                "Peringatan"
            );
            
            if (!force) {
                return;
            }
        }
        
        boolean confirm = GUIUtils.confirmDialog(
            this, 
            "Apakah Anda yakin ingin menghapus kategori " + category.getName() + "?", 
            "Konfirmasi Hapus"
        );
        
        if (confirm) {
            mainWindow.removeCategory(category);
            refreshData();
            GUIUtils.infoDialog(this, "Kategori berhasil dihapus!", "Sukses");
        }
    }
}