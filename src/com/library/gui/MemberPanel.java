package com.library.gui;

import com.library.enums.LibrarianPermission;
import com.library.enums.MemberStatus;
import com.library.gui.utils.DialogUtils;
import com.library.gui.utils.GUIUtils;
import com.library.gui.utils.TableModels.MemberTableModel;
import com.library.model.Member;
import com.library.model.Person;
import com.library.model.RegularMember;
import com.library.model.StudentMember;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Panel untuk kelola anggota perpustakaan
 */
public class MemberPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private LendifyGUI mainWindow;
    private JTable memberTable;
    private MemberTableModel tableModel;
    private JTextField searchField;
    private JButton addButton;
    private JButton editButton;
    private JButton detailsButton;
    private JButton renewButton;
    private JButton toggleStatusButton;
    private JButton backButton;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    
    /**
     * Constructor untuk MemberPanel
     */
    public MemberPanel(LendifyGUI mainWindow) {
        this.mainWindow = mainWindow;
        setupUI();
    }
    
    /**
     * Setup komponen UI
     */
    private void setupUI() {
        setLayout(new BorderLayout());
        setOpaque(false);
        
        // Create main container with gradient background
        JPanel mainContainer = new JPanel(new BorderLayout(20, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(74, 144, 226),
                    0, getHeight(), new Color(80, 170, 200)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        mainContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Modern title panel with card design
        JPanel titleCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(5, 5, getWidth() - 5, getHeight() - 5, 25, 25);
                
                // Draw main card
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 25, 25);
                g2d.dispose();
            }
        };
        titleCard.setLayout(new BorderLayout(20, 0));
        titleCard.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        titleCard.setOpaque(false);
        
        // Header with icon and title
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("👥");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        headerPanel.add(iconLabel, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel("Kelola Anggota");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Modern search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);
        
        JLabel searchLabel = new JLabel("🔍 Cari:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchLabel.setForeground(new Color(44, 62, 80));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        JButton searchButton = new JButton("Cari") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(52, 152, 219).darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(52, 152, 219).brighter());
                } else {
                    g2d.setColor(new Color(52, 152, 219));
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.dispose();
                
                super.paintComponent(g);
            }
        };
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchButton.setForeground(Color.WHITE);
        searchButton.setPreferredSize(new Dimension(80, 35));
        searchButton.setBorderPainted(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> searchMembers());
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        titleCard.add(headerPanel, BorderLayout.CENTER);
        titleCard.add(searchPanel, BorderLayout.EAST);
        mainContainer.add(titleCard, BorderLayout.NORTH);
        
        // Create content card with modern design
        JPanel contentCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(5, 5, getWidth() - 5, getHeight() - 5, 25, 25);
                
                // Draw main card
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 25, 25);
                g2d.dispose();
            }
        };
        contentCard.setLayout(new BorderLayout(15, 15));
        contentCard.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        contentCard.setOpaque(false);
        
        // Panel tabel
        tableModel = new MemberTableModel(new ArrayList<>(mainWindow.getMembers()));
        memberTable = new JTable(tableModel);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.setRowHeight(30);
        memberTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        memberTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        memberTable.getTableHeader().setReorderingAllowed(false);
        memberTable.setGridColor(new Color(230, 230, 230));
        memberTable.setSelectionBackground(new Color(52, 152, 219, 50));
        
        // Sorter untuk tabel
        TableRowSorter<MemberTableModel> sorter = new TableRowSorter<>(tableModel);
        memberTable.setRowSorter(sorter);
        
        // Mouse listener untuk double-click
        memberTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewMemberDetails();
                }
            }
        });
        
        // Panel untuk tabel dengan scrolling
        JScrollPane tableScrollPane = new JScrollPane(memberTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableScrollPane.getViewport().setBackground(Color.WHITE);
        
        // Modern filter panel
        JPanel filterCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(248, 249, 250));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
            }
        };
        filterCard.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterCard.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        filterCard.setOpaque(false);
        
        JLabel filterLabel = new JLabel("🔧 Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterLabel.setForeground(new Color(44, 62, 80));
        filterCard.add(filterLabel);
        
        JRadioButton allRadio = new JRadioButton("Semua", true);
        JRadioButton studentRadio = new JRadioButton("Mahasiswa");
        JRadioButton regularRadio = new JRadioButton("Reguler");
        JRadioButton activeRadio = new JRadioButton("Aktif");
        JRadioButton inactiveRadio = new JRadioButton("Tidak Aktif");
        
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(allRadio);
        typeGroup.add(studentRadio);
        typeGroup.add(regularRadio);
        
        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(allRadio);
        statusGroup.add(activeRadio);
        statusGroup.add(inactiveRadio);
        
        // Style radio buttons
        Font radioFont = new Font("Segoe UI", Font.PLAIN, 13);
        Color radioColor = new Color(44, 62, 80);
        
        allRadio.setFont(radioFont);
        allRadio.setForeground(radioColor);
        allRadio.setOpaque(false);
        studentRadio.setFont(radioFont);
        studentRadio.setForeground(radioColor);
        studentRadio.setOpaque(false);
        regularRadio.setFont(radioFont);
        regularRadio.setForeground(radioColor);
        regularRadio.setOpaque(false);
        activeRadio.setFont(radioFont);
        activeRadio.setForeground(radioColor);
        activeRadio.setOpaque(false);
        inactiveRadio.setFont(radioFont);
        inactiveRadio.setForeground(radioColor);
        inactiveRadio.setOpaque(false);
        
        allRadio.addActionListener(e -> filterMembers(null, null));
        studentRadio.addActionListener(e -> filterMembers("student", null));
        regularRadio.addActionListener(e -> filterMembers("regular", null));
        activeRadio.addActionListener(e -> filterMembers(null, true));
        inactiveRadio.addActionListener(e -> filterMembers(null, false));
        
        JLabel typeLabel = new JLabel("Tipe:");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        typeLabel.setForeground(radioColor);
        
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(radioColor);
        
        filterCard.add(typeLabel);
        filterCard.add(allRadio);
        filterCard.add(studentRadio);
        filterCard.add(regularRadio);
        filterCard.add(Box.createHorizontalStrut(20));
        filterCard.add(statusLabel);
        filterCard.add(activeRadio);
        filterCard.add(inactiveRadio);
        
        // Add components to content card
        contentCard.add(filterCard, BorderLayout.NORTH);
        contentCard.add(tableScrollPane, BorderLayout.CENTER);
        mainContainer.add(contentCard, BorderLayout.CENTER);
        
        // Modern button panel
        JPanel buttonCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(5, 5, getWidth() - 5, getHeight() - 5, 25, 25);
                
                // Draw main card
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 25, 25);
                g2d.dispose();
            }
        };
        buttonCard.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonCard.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        buttonCard.setOpaque(false);
        
        // Create modern buttons
        addButton = createModernButton("➕ Tambah Anggota", new Color(46, 204, 113));
        editButton = createModernButton("✏️ Edit Anggota", new Color(52, 152, 219));
        detailsButton = createModernButton("👁️ Lihat Detail", new Color(155, 89, 182));
        renewButton = createModernButton("🔄 Perpanjang", new Color(230, 126, 34));
        toggleStatusButton = createModernButton("🔄 Status", new Color(241, 196, 15));
        backButton = createModernButton("🏠 Kembali", new Color(149, 165, 166));

        // Add tooltips for better usability
        addButton.setToolTipText("Tambah anggota baru ke perpustakaan");
        editButton.setToolTipText("Edit data anggota yang dipilih");
        detailsButton.setToolTipText("Tampilkan detail lengkap anggota yang dipilih");
        renewButton.setToolTipText("Perpanjang masa keanggotaan anggota yang dipilih");
        toggleStatusButton.setToolTipText("Aktifkan atau nonaktifkan status anggota yang dipilih");
        backButton.setToolTipText("Kembali ke menu utama");
        
        // Add action listeners
        addButton.addActionListener(e -> addMember());
        editButton.addActionListener(e -> editMember());
        detailsButton.addActionListener(e -> viewMemberDetails());
        renewButton.addActionListener(e -> renewMembership());
        toggleStatusButton.addActionListener(e -> toggleMemberStatus());
        backButton.addActionListener(e -> mainWindow.showMainPanel());
        
        // Add buttons to button card
        buttonCard.add(addButton);
        buttonCard.add(editButton);
        buttonCard.add(detailsButton);
        buttonCard.add(renewButton);
        buttonCard.add(toggleStatusButton);
        buttonCard.add(backButton);
        
        mainContainer.add(buttonCard, BorderLayout.SOUTH);
        add(mainContainer, BorderLayout.CENTER);
        
        // Tambahkan selection listener untuk update button states
        memberTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });
        
        // Set initial button states
        updateButtonStates();
    }
    
    /**
     * Create a modern styled button
     */
    private JButton createModernButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color buttonColor;
                if (getModel().isPressed()) {
                    buttonColor = baseColor.darker();
                } else if (getModel().isRollover()) {
                    buttonColor = baseColor.brighter();
                } else {
                    buttonColor = baseColor;
                }
                
                g2d.setColor(buttonColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    /**
     * Refresh data panel
     */
    public void refreshData() {
        tableModel.setMembers(new ArrayList<>(mainWindow.getMembers()));
        updateButtonStates();
    }
    
    /**
     * Update status tombol berdasarkan hak akses dan seleksi
     */
    private void updateButtonStates() {
        boolean isBasic = mainWindow.getCurrentLibrarian().getPermission() == LibrarianPermission.BASIC;
        boolean memberSelected = memberTable.getSelectedRow() != -1;
        
        System.out.println("updateButtonStates: memberSelected = " + memberSelected);
        
        addButton.setEnabled(!isBasic);
        editButton.setEnabled(memberSelected);
        detailsButton.setEnabled(memberSelected);
        renewButton.setEnabled(!isBasic && memberSelected);
        toggleStatusButton.setEnabled(!isBasic && memberSelected);
        
        // Force repaint of buttons to ensure state is visually updated
        editButton.repaint();
        detailsButton.repaint();
        renewButton.repaint();
        toggleStatusButton.repaint();
    }
    
    /**
     * Cari anggota berdasarkan keyword
     */
    private void searchMembers() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            refreshData();
            return;
        }
        
        ArrayList<Member> filteredList = new ArrayList<>();
        for (Member member : mainWindow.getMembers()) {
            if (member.getName().toLowerCase().contains(keyword) ||
                member.getMemberId().toLowerCase().contains(keyword) ||
                member.getEmail().toLowerCase().contains(keyword) ||
                member.getPhoneNumber().toLowerCase().contains(keyword)) {
                filteredList.add(member);
            }
        }
        
        tableModel.setMembers(filteredList);
        updateButtonStates();
    }
    
    /**
     * Filter anggota berdasarkan tipe dan status
     */
    private void filterMembers(String type, Boolean active) {
        ArrayList<Member> filteredList = new ArrayList<>();
        for (Member member : mainWindow.getMembers()) {
            boolean typeMatch = true;
            boolean statusMatch = true;
            
            if (type != null) {
                if (type.equals("student") && !(member instanceof StudentMember)) {
                    typeMatch = false;
                } else if (type.equals("regular") && !(member instanceof RegularMember)) {
                    typeMatch = false;
                }
            }
            
            if (active != null && member.isActive() != active) {
                statusMatch = false;
            }
            
            if (typeMatch && statusMatch) {
                filteredList.add(member);
            }
        }
        
        tableModel.setMembers(filteredList);
        updateButtonStates();
    }
    
    /**
     * Tambah anggota baru
     */
    private void addMember() {
        if (mainWindow.getCurrentLibrarian().getPermission() == LibrarianPermission.BASIC) {
            GUIUtils.errorDialog(this, "Anda tidak memiliki hak akses untuk menambah anggota!", "Akses Ditolak");
            return;
        }
        
        // Panel untuk memilih tipe anggota
        String[] options = {"Mahasiswa", "Reguler", "Batal"};
        int choice = JOptionPane.showOptionDialog(
            this,
            "Pilih jenis anggota yang akan ditambahkan:",
            "Tambah Anggota",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (choice == 2 || choice == JOptionPane.CLOSED_OPTION) {
            return; // User canceled
        }
        
        boolean isStudent = (choice == 0);
        
        // Panel form untuk input data anggota
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        // Form data person
        JTextField personIdField = new JTextField(15);
        JTextField nameField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField phoneField = new JTextField(15);
        JTextField emailField = new JTextField(20);
        
        formPanel.add(new JLabel("ID Person:"));
        formPanel.add(personIdField);
        formPanel.add(new JLabel("Nama:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Alamat:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Nomor Telepon:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        
        // Form data spesifik berdasarkan tipe anggota
        JTextField studentIdField = null;
        JTextField facultyField = null;
        JTextField departmentField = null;
        JTextField yearOfStudyField = null;
        
        JTextField occupationField = null;
        JTextField employerField = null;
        JCheckBox premiumCheckbox = null;
        
        if (isStudent) {
            studentIdField = new JTextField(15);
            facultyField = new JTextField(20);
            departmentField = new JTextField(20);
            yearOfStudyField = new JTextField(5);
            
            formPanel.add(new JLabel("ID Mahasiswa:"));
            formPanel.add(studentIdField);
            formPanel.add(new JLabel("Fakultas:"));
            formPanel.add(facultyField);
            formPanel.add(new JLabel("Jurusan:"));
            formPanel.add(departmentField);
            formPanel.add(new JLabel("Tahun Studi:"));
            formPanel.add(yearOfStudyField);
        } else {
            occupationField = new JTextField(20);
            employerField = new JTextField(20);
            premiumCheckbox = new JCheckBox("Anggota Premium");
            
            formPanel.add(new JLabel("Pekerjaan:"));
            formPanel.add(occupationField);
            formPanel.add(new JLabel("Nama Perusahaan/Institusi:"));
            formPanel.add(employerField);
            formPanel.add(new JLabel("Status Keanggotaan:"));
            formPanel.add(premiumCheckbox);
        }
        
        // Tampilkan dialog
        int result = JOptionPane.showConfirmDialog(
            this, formPanel, "Tambah Anggota " + (isStudent ? "Mahasiswa" : "Reguler"),
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Validasi input
                if (personIdField.getText().trim().isEmpty() || 
                    nameField.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("ID Person dan Nama tidak boleh kosong!");
                }
                
                // Buat objek Person
                Person person = new Person(
                    personIdField.getText().trim(),
                    nameField.getText().trim(),
                    addressField.getText().trim(),
                    phoneField.getText().trim()
                );
                person.setEmail(emailField.getText().trim());
                
                // Tambahkan anggota sesuai tipe
                Member member = mainWindow.getCurrentLibrarian().addMember(person);
                
                if (isStudent) {
                    if (studentIdField.getText().trim().isEmpty()) {
                        throw new IllegalArgumentException("ID Mahasiswa tidak boleh kosong!");
                    }
                    
                    int yearOfStudy = 1;
                    try {
                        if (!yearOfStudyField.getText().trim().isEmpty()) {
                            yearOfStudy = Integer.parseInt(yearOfStudyField.getText().trim());
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Tahun Studi harus berupa angka!");
                    }
                    
                    StudentMember studentMember = new StudentMember(
                        member,
                        studentIdField.getText().trim(),
                        facultyField.getText().trim(),
                        departmentField.getText().trim(),
                        yearOfStudy
                    );
                    
                    mainWindow.addMember(studentMember);
                } else {
                    RegularMember regularMember = new RegularMember(
                        member,
                        occupationField.getText().trim(),
                        employerField.getText().trim(),
                        premiumCheckbox.isSelected()
                    );
                    
                    mainWindow.addMember(regularMember);
                }
                
                refreshData();
                
                GUIUtils.infoDialog(this, "Anggota berhasil ditambahkan!", "Sukses");
            } catch (Exception ex) {
                GUIUtils.errorDialog(this, ex.getMessage(), "Error");
            }
        }
    }

    /**
     * Edit anggota yang dipilih
     */
    private void editMember() {
        Member member = getSelectedMember();
        if (member == null) {
            GUIUtils.errorDialog(this, "Pilih anggota yang akan diubah!", "Tidak Ada Anggota Dipilih");
            return;
        }
        
        // Panel form untuk edit data
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        JTextField nameField = new JTextField(member.getName(), 20);
        JTextField addressField = new JTextField(member.getAddress(), 20);
        JTextField phoneField = new JTextField(member.getPhoneNumber(), 15);
        JTextField emailField = new JTextField(member.getEmail(), 20);
        
        formPanel.add(new JLabel("ID Person:"));
        formPanel.add(new JLabel(member.getId()));
        formPanel.add(new JLabel("ID Anggota:"));
        formPanel.add(new JLabel(member.getMemberId()));
        formPanel.add(new JLabel("Nama:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Alamat:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Nomor Telepon:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        
        // Form data spesifik berdasarkan tipe anggota
        if (member instanceof StudentMember) {
            StudentMember studentMember = (StudentMember) member;
            
            JTextField studentIdField = new JTextField(studentMember.getStudentId(), 15);
            JTextField facultyField = new JTextField(studentMember.getFaculty(), 20);
            JTextField departmentField = new JTextField(studentMember.getDepartment(), 20);
            JTextField yearOfStudyField = new JTextField(String.valueOf(studentMember.getYearOfStudy()), 5);
            
            formPanel.add(new JLabel("ID Mahasiswa:"));
            formPanel.add(studentIdField);
            formPanel.add(new JLabel("Fakultas:"));
            formPanel.add(facultyField);
            formPanel.add(new JLabel("Jurusan:"));
            formPanel.add(departmentField);
            formPanel.add(new JLabel("Tahun Studi:"));
            formPanel.add(yearOfStudyField);
            
            // Tampilkan dialog
            int result = JOptionPane.showConfirmDialog(
                this, formPanel, "Edit Anggota Mahasiswa", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                try {
                    // Validasi input
                    if (nameField.getText().trim().isEmpty() ||
                        studentIdField.getText().trim().isEmpty()) {
                        throw new IllegalArgumentException("Nama dan ID Mahasiswa tidak boleh kosong!");
                    }
                    
                    int yearOfStudy = 1;
                    try {
                        if (!yearOfStudyField.getText().trim().isEmpty()) {
                            yearOfStudy = Integer.parseInt(yearOfStudyField.getText().trim());
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Tahun Studi harus berupa angka!");
                    }
                    
                    // Update data
                    member.setName(nameField.getText().trim());
                    member.setAddress(addressField.getText().trim());
                    member.setPhoneNumber(phoneField.getText().trim());
                    member.setEmail(emailField.getText().trim());
                    
                    studentMember.setStudentId(studentIdField.getText().trim());
                    studentMember.setFaculty(facultyField.getText().trim());
                    studentMember.setDepartment(departmentField.getText().trim());
                    studentMember.setYearOfStudy(yearOfStudy);
                    
                    refreshData();
                    
                    GUIUtils.infoDialog(this, "Anggota berhasil diubah!", "Sukses");
                } catch (Exception ex) {
                    GUIUtils.errorDialog(this, ex.getMessage(), "Error");
                }
            }
        } else if (member instanceof RegularMember) {
            RegularMember regularMember = (RegularMember) member;
            
            JTextField occupationField = new JTextField(regularMember.getOccupation(), 20);
            JTextField employerField = new JTextField(regularMember.getEmployerName(), 20);
            JCheckBox premiumCheckbox = new JCheckBox("Anggota Premium", regularMember.isPremium());
            
            formPanel.add(new JLabel("Pekerjaan:"));
            formPanel.add(occupationField);
            formPanel.add(new JLabel("Nama Perusahaan/Institusi:"));
            formPanel.add(employerField);
            formPanel.add(new JLabel("Status Keanggotaan:"));
            formPanel.add(premiumCheckbox);
            
            // Tampilkan dialog
            int result = JOptionPane.showConfirmDialog(
                this, formPanel, "Edit Anggota Reguler", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                try {
                    // Validasi input
                    if (nameField.getText().trim().isEmpty()) {
                        throw new IllegalArgumentException("Nama tidak boleh kosong!");
                    }
                    
                    // Update data
                    member.setName(nameField.getText().trim());
                    member.setAddress(addressField.getText().trim());
                    member.setPhoneNumber(phoneField.getText().trim());
                    member.setEmail(emailField.getText().trim());
                    
                    regularMember.setOccupation(occupationField.getText().trim());
                    regularMember.setEmployerName(employerField.getText().trim());
                    regularMember.setPremium(premiumCheckbox.isSelected());
                    
                    refreshData();
                    
                    GUIUtils.infoDialog(this, "Anggota berhasil diubah!", "Sukses");
                } catch (Exception ex) {
                    GUIUtils.errorDialog(this, ex.getMessage(), "Error");
                }
            }
        }
    }
    
    /**
     * Lihat detail anggota
     */
    private void viewMemberDetails() {
        Member member = getSelectedMember();
        if (member == null) {
            GUIUtils.errorDialog(this, "Pilih anggota untuk melihat detail!", "Tidak Ada Anggota Dipilih");
            return;
        }
        
        // Add null check for expiry date before showing dialog
        if (member.getExpiryDate() == null) {
            // Set a default expiry date if it's null
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 1); // Add one year from now
            member.setExpiryDate(cal.getTime());
        }
        
        DialogUtils.showMemberDetailsDialog(this, member);
    }
    
    /**
     * Perpanjang keanggotaan anggota
     */
    private void renewMembership() {
        if (mainWindow.getCurrentLibrarian().getPermission() == LibrarianPermission.BASIC) {
            GUIUtils.errorDialog(this, "Anda tidak memiliki hak akses untuk memperpanjang keanggotaan!", "Akses Ditolak");
            return;
        }
        
        Member member = getSelectedMember();
        if (member == null) {
            GUIUtils.errorDialog(this, "Pilih anggota yang akan diperpanjang keanggotaannya!", "Tidak Ada Anggota Dipilih");
            return;
        }
        
        // Ensure expiry date is not null
        if (member.getExpiryDate() == null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 1); // Add one year from now
            member.setExpiryDate(cal.getTime());
        }
        
        // Panel untuk input jumlah bulan
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        panel.add(new JLabel("Anggota:"));
        panel.add(new JLabel(member.getName()));
        
        panel.add(new JLabel("ID Anggota:"));
        panel.add(new JLabel(member.getMemberId()));
        
        panel.add(new JLabel("Tanggal Kadaluarsa Saat Ini:"));
        panel.add(new JLabel(dateFormat.format(member.getExpiryDate())));
        
        JTextField monthsField = new JTextField("12", 5);
        panel.add(new JLabel("Jumlah Bulan Perpanjangan:"));
        panel.add(monthsField);
        
        // Tampilkan dialog
        int result = JOptionPane.showConfirmDialog(
            this, panel, "Perpanjang Keanggotaan", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                int months = Integer.parseInt(monthsField.getText().trim());
                if (months <= 0) {
                    throw new IllegalArgumentException("Jumlah bulan harus lebih dari 0!");
                }
                
                // Perpanjang keanggotaan
                member.renewMembership(months);
                
                refreshData();
                
                GUIUtils.infoDialog(
                    this, 
                    "Keanggotaan berhasil diperpanjang!\nTanggal Kadaluarsa Baru: " + dateFormat.format(member.getExpiryDate()), 
                    "Sukses"
                );
            } catch (NumberFormatException e) {
                GUIUtils.errorDialog(this, "Jumlah bulan harus berupa angka!", "Error");
            } catch (Exception ex) {
                GUIUtils.errorDialog(this, ex.getMessage(), "Error");
            }
        }
    }
    
    /**
     * Aktifkan/nonaktifkan anggota
     */
    private void toggleMemberStatus() {
        if (mainWindow.getCurrentLibrarian().getPermission() == LibrarianPermission.BASIC) {
            GUIUtils.errorDialog(this, "Anda tidak memiliki hak akses untuk mengubah status anggota!", "Akses Ditolak");
            return;
        }
        
        Member member = getSelectedMember();
        if (member == null) {
            GUIUtils.errorDialog(this, "Pilih anggota yang akan diubah statusnya!", "Tidak Ada Anggota Dipilih");
            return;
        }
        
        // Panel untuk memilih status
        String[] options = {"Aktifkan", "Nonaktifkan", "Blacklist", "Batal"};
        int choice = JOptionPane.showOptionDialog(
            this,
            "Status saat ini: " + (member.isActive() ? "Aktif" : "Tidak Aktif") + "\nPilih tindakan:",
            "Ubah Status Anggota",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (choice == 3 || choice == JOptionPane.CLOSED_OPTION) {
            return; // User canceled
        }
        
        try {
            switch (choice) {
                case 0: // Aktifkan
                    member.setActive(true);
                    member.setStatus(MemberStatus.ACTIVE);
                    break;
                case 1: // Nonaktifkan
                    member.setActive(false);
                    member.setStatus(MemberStatus.INACTIVE);
                    break;
                case 2: // Blacklist
                    if (mainWindow.getCurrentLibrarian().getPermission() != LibrarianPermission.ADMIN) {
                        GUIUtils.errorDialog(this, "Anda tidak memiliki hak akses untuk melakukan blacklist anggota!", "Akses Ditolak");
                        return;
                    }
                    
                    boolean confirm = GUIUtils.confirmDialog(
                        this, 
                        "Apakah Anda yakin ingin memasukkan anggota ini ke dalam blacklist?", 
                        "Konfirmasi Blacklist"
                    );
                    
                    if (confirm) {
                        mainWindow.getCurrentLibrarian().blacklistMember(member);
                    } else {
                        return;
                    }
                    break;
            }
            
            refreshData();
            
            GUIUtils.infoDialog(this, "Status anggota berhasil diubah!", "Sukses");
        } catch (Exception ex) {
            GUIUtils.errorDialog(this, ex.getMessage(), "Error");
        }
    }
    
    /**
     * Mendapatkan anggota yang dipilih di tabel
     */
    private Member getSelectedMember() {
        int selectedRow = memberTable.getSelectedRow();
        
        if (selectedRow == -1) return null;
        
        int modelRow = memberTable.convertRowIndexToModel(selectedRow);
        Member member = tableModel.getMemberAt(modelRow);
        
        return member;
    }
}