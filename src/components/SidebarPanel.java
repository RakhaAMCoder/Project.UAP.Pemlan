package components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import dashboard.CryptoDashboard;

public class SidebarPanel extends JPanel {
    private CryptoDashboard parent;

    public SidebarPanel(CryptoDashboard parent) {
        this.parent = parent;
        setBackground(new Color(40, 40, 50));
        setPreferredSize(new Dimension(250, getHeight()));
        setBorder(new EmptyBorder(20, 10, 20, 10));
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        // Logo/Title
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(40, 40, 50));
        logoPanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        JLabel logoLabel = new JLabel("CRYPTOCURRENCY");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(new Color(70, 130, 180));
        logoPanel.add(logoLabel);

        // Menu items
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(new Color(40, 40, 50));
        menuPanel.setLayout(new GridLayout(5, 1, 0, 10));

        String[] menuItems = {"Dashboard", "Detail Chart", "Input Data", "Reports", "Settings"};
        Icon[] icons = {UIManager.getIcon("FileView.directoryIcon"),
                UIManager.getIcon("FileView.fileIcon"),
                UIManager.getIcon("FileChooser.newFolderIcon"),
                UIManager.getIcon("FileView.detailsViewIcon"),
                UIManager.getIcon("OptionPane.informationIcon")};

        for (int i = 0; i < menuItems.length; i++) {
            ModernButton button = new ModernButton(menuItems[i], new Color(60, 60, 70));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setIcon(icons[i]);

            final int index = i;
            button.addActionListener(e -> handleMenuClick(index));

            menuPanel.add(button);
        }

        // Add panels to sidebar
        add(logoPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.CENTER);

        // Footer with version
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(40, 40, 50));
        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setForeground(new Color(150, 150, 150));
        footerPanel.add(versionLabel);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private void handleMenuClick(int index) {
        switch (index) {
            case 0: // Dashboard - already there
                break;
            case 1: // Detail Chart
                parent.showDetailChart();
                break;
            case 2: // Input Data
                parent.showDataInput();
                break;
            case 3: // Reports
                parent.showReport();
                break;
            case 4: // Settings
                JOptionPane.showMessageDialog(parent, "Settings feature coming soon!");
                break;
        }
    }
}