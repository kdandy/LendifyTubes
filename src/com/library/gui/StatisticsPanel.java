package com.library.gui;

import com.library.enums.LoanStatus;
import com.library.enums.ReservationStatus;
import com.library.model.Book;
import com.library.model.BookCategory;
import com.library.model.BookLoan;
import com.library.model.Member;
import com.library.model.RegularMember;
import com.library.model.Reservation;
import com.library.model.StudentMember;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Panel untuk statistik perpustakaan
 */
public class StatisticsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private LendifyGUI mainWindow;
    private JPanel summaryPanel;
    private JPanel chartsPanel;
    private JButton refreshButton;
    private JButton backButton;
    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    
    // Labels untuk data statistik
    private JLabel totalBooksLabel;
    private JLabel totalCategoriesLabel;
    private JLabel totalMembersLabel;
    private JLabel activeLoansLabel;
    private JLabel pendingReservationsLabel;
    private JLabel totalFinesLabel;
    private JLabel studentMembersLabel;
    private JLabel regularMembersLabel;
    private JLabel availableBooksLabel;
    private JLabel borrowedBooksLabel;
    
    /**
     * Constructor untuk StatisticsPanel
     */
    public StatisticsPanel(LendifyGUI mainWindow) {
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
        
        // Modern title card dengan shadow
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
        titleCard.setLayout(new BorderLayout(20, 15));
        titleCard.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        // Header panel dengan icon dan title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        headerPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("📊");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        
        JLabel titleLabel = new JLabel("Statistik Perpustakaan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        
        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);
        
        // Info perpustakaan dengan modern styling
        JPanel libraryInfoPanel = new JPanel(new GridLayout(1, 2, 20, 5));
        libraryInfoPanel.setOpaque(false);
        
        JLabel libraryNameLabel = new JLabel("🏛️ " + mainWindow.getLibrary().getName());
        libraryNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        libraryNameLabel.setForeground(new Color(52, 73, 94));
        
        JLabel libraryAddressLabel = new JLabel("📍 " + mainWindow.getLibrary().getAddress());
        libraryAddressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        libraryAddressLabel.setForeground(new Color(52, 73, 94));
        
        libraryInfoPanel.add(libraryNameLabel);
        libraryInfoPanel.add(libraryAddressLabel);
        
        titleCard.add(headerPanel, BorderLayout.CENTER);
        titleCard.add(libraryInfoPanel, BorderLayout.SOUTH);
        
        mainContainer.add(titleCard, BorderLayout.NORTH);
        
        // Modern content area
        JPanel contentArea = new JPanel(new BorderLayout(0, 20));
        contentArea.setOpaque(false);
        
        // Panel atas untuk ringkasan statistik
        summaryPanel = createSummaryPanel();
        contentArea.add(summaryPanel, BorderLayout.NORTH);
        
        // Panel bawah untuk grafik dan chart
        chartsPanel = createChartsPanel();
        contentArea.add(chartsPanel, BorderLayout.CENTER);
        
        mainContainer.add(contentArea, BorderLayout.CENTER);
        
        // Modern button panel
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
        buttonCard.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonCard.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        refreshButton = createModernButton("🔄 Refresh Data", new Color(52, 152, 219));
        backButton = createModernButton("🏠 Kembali ke Menu Utama", new Color(149, 165, 166));
        
        refreshButton.addActionListener(e -> refreshData());
        backButton.addActionListener(e -> mainWindow.showMainPanel());
        
        buttonCard.add(refreshButton);
        buttonCard.add(backButton);
        
        mainContainer.add(buttonCard, BorderLayout.SOUTH);
        add(mainContainer, BorderLayout.CENTER);
        
        // Refresh data awal
        refreshData();
    }
    
    /**
     * Buat panel ringkasan statistik
     */
    private JPanel createSummaryPanel() {
        JPanel summaryCard = new JPanel() {
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
        summaryCard.setOpaque(false);
        summaryCard.setLayout(new BorderLayout(15, 15));
        summaryCard.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        // Modern title
        JLabel titleLabel = new JLabel("📈 Ringkasan Statistik");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        summaryCard.add(titleLabel, BorderLayout.NORTH);
        
        // Panel statistik utama dengan modern grid
        JPanel statsPanel = new JPanel(new GridLayout(5, 4, 20, 10));
        statsPanel.setOpaque(false);
        
        // Kolom 1: Statistik buku
        statsPanel.add(createModernStatsLabel("📚 Total Buku:"));
        totalBooksLabel = createModernValueLabel("0", new Color(52, 152, 219));
        statsPanel.add(totalBooksLabel);
        
        statsPanel.add(createModernStatsLabel("📂 Total Kategori:"));
        totalCategoriesLabel = createModernValueLabel("0", new Color(155, 89, 182));
        statsPanel.add(totalCategoriesLabel);
        
        statsPanel.add(createModernStatsLabel("✅ Buku Tersedia:"));
        availableBooksLabel = createModernValueLabel("0", new Color(46, 204, 113));
        statsPanel.add(availableBooksLabel);
        
        statsPanel.add(createModernStatsLabel("📤 Buku Dipinjam:"));
        borrowedBooksLabel = createModernValueLabel("0", new Color(230, 126, 34));
        statsPanel.add(borrowedBooksLabel);
        
        statsPanel.add(createModernStatsLabel("💰 Total Denda:"));
        totalFinesLabel = createModernValueLabel("Rp 0,00", new Color(231, 76, 60));
        statsPanel.add(totalFinesLabel);
        
        // Kolom 2: Statistik anggota dan transaksi
        statsPanel.add(createModernStatsLabel("👥 Total Anggota:"));
        totalMembersLabel = createModernValueLabel("0", new Color(52, 152, 219));
        statsPanel.add(totalMembersLabel);
        
        statsPanel.add(createModernStatsLabel("🎓 Anggota Mahasiswa:"));
        studentMembersLabel = createModernValueLabel("0", new Color(155, 89, 182));
        statsPanel.add(studentMembersLabel);
        
        statsPanel.add(createModernStatsLabel("👤 Anggota Reguler:"));
        regularMembersLabel = createModernValueLabel("0", new Color(46, 204, 113));
        statsPanel.add(regularMembersLabel);
        
        statsPanel.add(createModernStatsLabel("📋 Peminjaman Aktif:"));
        activeLoansLabel = createModernValueLabel("0", new Color(230, 126, 34));
        statsPanel.add(activeLoansLabel);
        
        statsPanel.add(createModernStatsLabel("⏳ Reservasi Pending:"));
        pendingReservationsLabel = createModernValueLabel("0", new Color(241, 196, 15));
        statsPanel.add(pendingReservationsLabel);
        
        summaryCard.add(statsPanel, BorderLayout.CENTER);
        
        return summaryCard;
    }
    
    /**
     * Buat panel untuk grafik dan chart
     */
    private JPanel createChartsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Panel untuk distribusi kategori
        JPanel categoryPanel = new JPanel(new BorderLayout());
        categoryPanel.setBorder(BorderFactory.createTitledBorder("Distribusi Buku per Kategori"));
        
        // Akan diisi saat refresh data
        categoryPanel.add(new JLabel("Data akan dimuat saat refresh...", SwingConstants.CENTER));
        
        // Panel untuk statistik peminjaman
        JPanel loanPanel = new JPanel(new BorderLayout());
        loanPanel.setBorder(BorderFactory.createTitledBorder("Statistik Peminjaman"));
        
        // Akan diisi saat refresh data
        loanPanel.add(new JLabel("Data akan dimuat saat refresh...", SwingConstants.CENTER));
        
        panel.add(categoryPanel);
        panel.add(loanPanel);
        
        return panel;
    }
    
    /**
     * Refresh data statistik
     */
    public void refreshData() {
        // Hitung statistik
        int totalBooks = mainWindow.getLibrary().getCollection().getTotalBooks();
        int totalCategories = mainWindow.getLibrary().getCollection().getTotalCategories();
        int totalMembers = mainWindow.getMembers().size();
        
        int studentMembers = 0;
        int regularMembers = 0;
        double totalFines = 0.0;
        
        // Hitung jumlah anggota berdasarkan tipe
        for (Member member : mainWindow.getMembers()) {
            if (member instanceof StudentMember) {
                studentMembers++;
            } else if (member instanceof RegularMember) {
                regularMembers++;
            }
            
            totalFines += member.getTotalFinesPaid();
        }
        
        // Hitung peminjaman aktif
        int activeLoans = 0;
        int borrowedBooks = 0;
        for (Member member : mainWindow.getMembers()) {
            for (BookLoan loan : member.getBookLoans()) {
                if (loan.getStatus() == LoanStatus.ACTIVE || loan.getStatus() == LoanStatus.OVERDUE) {
                    activeLoans++;
                    borrowedBooks++;
                }
            }
        }
        
        // Hitung reservasi pending
        int pendingReservations = 0;
        for (Member member : mainWindow.getMembers()) {
            for (Reservation reservation : member.getReservations()) {
                if (reservation.getStatus() == ReservationStatus.PENDING) {
                    pendingReservations++;
                }
            }
        }
        
        // Hitung buku tersedia
        int availableBooks = 0;
        for (Book book : mainWindow.getLibrary().getCollection().getBooks()) {
            availableBooks += book.getAvailableItems().size();
        }
        
        // Update labels
        totalBooksLabel.setText(String.valueOf(totalBooks));
        totalCategoriesLabel.setText(String.valueOf(totalCategories));
        totalMembersLabel.setText(String.valueOf(totalMembers));
        studentMembersLabel.setText(String.valueOf(studentMembers));
        regularMembersLabel.setText(String.valueOf(regularMembers));
        activeLoansLabel.setText(String.valueOf(activeLoans));
        pendingReservationsLabel.setText(String.valueOf(pendingReservations));
        totalFinesLabel.setText(String.format("Rp %.2f", totalFines));
        availableBooksLabel.setText(String.valueOf(availableBooks));
        borrowedBooksLabel.setText(String.valueOf(borrowedBooks));
        
        // Update panel grafik distribusi kategori
        updateCategoryChart();
        
        // Update panel statistik peminjaman
        updateLoanStatistics();
    }
    
    /**
     * Update chart distribusi kategori
     */
    private void updateCategoryChart() {
        // Ambil semua kategori
        List<BookCategory> categories = mainWindow.getLibrary().getCollection().getCategories();
        
        // Jika tidak ada kategori, tampilkan pesan
        if (categories.isEmpty()) {
            JPanel categoryPanel = (JPanel) chartsPanel.getComponent(0);
            categoryPanel.removeAll();
            categoryPanel.add(new JLabel("Tidak ada data kategori.", SwingConstants.CENTER));
            categoryPanel.revalidate();
            categoryPanel.repaint();
            return;
        }
        
        // Buat panel untuk chart
        JPanel categoryPanel = (JPanel) chartsPanel.getComponent(0);
        categoryPanel.removeAll();
        
        // Buat tabel untuk menampilkan data
        String[] columnNames = {"Kategori", "Jumlah Buku", "Persentase"};
        Object[][] data = new Object[categories.size()][3];
        
        int totalBooksInCategories = 0;
        for (BookCategory category : categories) {
            totalBooksInCategories += category.getBooks().size();
        }
        
        for (int i = 0; i < categories.size(); i++) {
            BookCategory category = categories.get(i);
            int bookCount = category.getBooks().size();
            double percentage = totalBooksInCategories > 0 ? 
                (double) bookCount / totalBooksInCategories * 100 : 0;
            
            data[i][0] = category.getName();
            data[i][1] = bookCount;
            data[i][2] = String.format("%.1f%%", percentage);
        }
        
        JTable categoryTable = new JTable(data, columnNames);
        categoryTable.setEnabled(false);
        
        categoryPanel.setLayout(new BorderLayout());
        categoryPanel.add(new JScrollPane(categoryTable), BorderLayout.CENTER);
        
        categoryPanel.revalidate();
        categoryPanel.repaint();
    }
    
    /**
     * Update statistik peminjaman
     */
    private void updateLoanStatistics() {
        // Buat panel untuk statistik peminjaman
        JPanel loanPanel = (JPanel) chartsPanel.getComponent(1);
        loanPanel.removeAll();
        
        // Kumpulkan data peminjaman
        int totalLoans = 0;
        int activeLoans = 0;
        int completedLoans = 0;
        int overdueLoans = 0;
        
        for (Member member : mainWindow.getMembers()) {
            for (BookLoan loan : member.getBookLoans()) {
                totalLoans++;
                
                switch (loan.getStatus()) {
                    case ACTIVE:
                        activeLoans++;
                        break;
                    case COMPLETED:
                        completedLoans++;
                        break;
                    case OVERDUE:
                        overdueLoans++;
                        break;
                }
            }
        }
        
        // Buat tabel untuk statistik peminjaman
        String[] loanColumnNames = {"Status", "Jumlah", "Persentase"};
        Object[][] loanData = new Object[3][3];
        
        loanData[0][0] = "Aktif";
        loanData[0][1] = activeLoans;
        loanData[0][2] = totalLoans > 0 ? String.format("%.1f%%", (double) activeLoans / totalLoans * 100) : "0.0%";
        
        loanData[1][0] = "Selesai";
        loanData[1][1] = completedLoans;
        loanData[1][2] = totalLoans > 0 ? String.format("%.1f%%", (double) completedLoans / totalLoans * 100) : "0.0%";
        
        loanData[2][0] = "Terlambat";
        loanData[2][1] = overdueLoans;
        loanData[2][2] = totalLoans > 0 ? String.format("%.1f%%", (double) overdueLoans / totalLoans * 100) : "0.0%";
        
        JTable loanTable = new JTable(loanData, loanColumnNames);
        loanTable.setEnabled(false);
        
        // Panel untuk total peminjaman
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalPanel.add(new JLabel("Total Peminjaman: " + totalLoans));
        
        loanPanel.setLayout(new BorderLayout());
        loanPanel.add(new JScrollPane(loanTable), BorderLayout.CENTER);
        loanPanel.add(totalPanel, BorderLayout.SOUTH);
        
        loanPanel.revalidate();
        loanPanel.repaint();
    }
    
    /**
     * Create modern styled button
     */
    private JButton createModernButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bgColor = backgroundColor;
                if (!isEnabled()) {
                    bgColor = new Color(189, 195, 199);
                } else if (getModel().isPressed()) {
                    bgColor = backgroundColor.darker();
                } else if (getModel().isRollover()) {
                    bgColor = backgroundColor.brighter();
                }
                
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);
                
                g2d.dispose();
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(180, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * Create modern stats label
     */
    private JLabel createModernStatsLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }
    
    /**
     * Create modern value label
     */
    private JLabel createModernValueLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(color);
        return label;
    }
    
    /**
     * Buat label judul statistik dengan style
     */
    private JLabel createStatsLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.RIGHT);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
        return label;
    }
    
    /**
     * Buat label nilai statistik dengan style
     */
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, label.getFont().getSize()));
        return label;
    }
}