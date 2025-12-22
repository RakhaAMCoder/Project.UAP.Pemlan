import javax.swing.*;
import java.awt.*;
import dashboard.CryptoDashboard;

public class Main {
    public static void main(String[] args) {
        // Set System Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Custom Dark Theme for all components.
            setupDarkTheme();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create and show dashboard
        SwingUtilities.invokeLater(() -> {
            CryptoDashboard dashboard = new CryptoDashboard();
            dashboard.setVisible(true);

            // Show welcome message
            JOptionPane.showMessageDialog(dashboard,
                    "Welcome to Crypto Dashboard!\n\n" +
                            "Features:\n" +
                            "• Real-time price updates\n" +
                            "• Interactive charts\n" +
                            "• CRUD operations\n" +
                            "• Dark theme UI\n\n" +
                            "Data is loaded from CSV files.",
                    "Crypto Dashboard v1.0",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private static void setupDarkTheme() {
        Color darkBackground = new Color(30, 30, 40);
        Color mediumBackground = new Color(40, 40, 50);
        Color lightBackground = new Color(50, 50, 60);
        Color textColor = new Color(220, 220, 220);
        Color borderColor = new Color(60, 60, 70);

        // Common components
        UIManager.put("Panel.background", darkBackground);
        UIManager.put("Label.foreground", textColor);
        UIManager.put("TextField.background", lightBackground);
        UIManager.put("TextField.foreground", textColor);
        UIManager.put("TextField.caretForeground", textColor);
        UIManager.put("TextField.border", BorderFactory.createLineBorder(borderColor));
        UIManager.put("TextArea.background", lightBackground);
        UIManager.put("TextArea.foreground", textColor);
        UIManager.put("TextArea.caretForeground", textColor);

        // Buttons
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.black);
        UIManager.put("Button.border", BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Combo Box
        UIManager.put("ComboBox.background", lightBackground);
        UIManager.put("ComboBox.foreground", textColor);
        UIManager.put("ComboBox.border", BorderFactory.createLineBorder(borderColor));

        // Table
        UIManager.put("Table.background", mediumBackground);
        UIManager.put("Table.foreground", textColor);
        UIManager.put("Table.gridColor", borderColor);
        UIManager.put("Table.selectionBackground", new Color(70, 130, 180));
        UIManager.put("Table.selectionForeground", Color.black);
        UIManager.put("TableHeader.background", new Color(50, 50, 60));
        UIManager.put("TableHeader.foreground", Color.black);

        // Scroll Pane
        UIManager.put("ScrollPane.background", darkBackground);
        UIManager.put("ScrollPane.border", BorderFactory.createLineBorder(borderColor));
        UIManager.put("Viewport.background", mediumBackground);

        // Menu and Menu Bar
        UIManager.put("MenuBar.background", mediumBackground);
        UIManager.put("MenuBar.foreground", textColor);
        UIManager.put("Menu.background", mediumBackground);
        UIManager.put("Menu.foreground", textColor);
        UIManager.put("MenuItem.background", mediumBackground);
        UIManager.put("MenuItem.foreground", textColor);

        // Option Pane
        UIManager.put("OptionPane.background", darkBackground);
        UIManager.put("OptionPane.messageForeground", textColor);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));

        // Progress Bar
        UIManager.put("ProgressBar.background", lightBackground);
        UIManager.put("ProgressBar.foreground", new Color(70, 130, 180));
        UIManager.put("ProgressBar.border", BorderFactory.createLineBorder(borderColor));

        // Tool Tip
        UIManager.put("ToolTip.background", mediumBackground);
        UIManager.put("ToolTip.foreground", textColor);
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(borderColor));

        // Tabbed Pane
        UIManager.put("TabbedPane.background", darkBackground);
        UIManager.put("TabbedPane.foreground", textColor);
        UIManager.put("TabbedPane.selected", new Color(70, 130, 180));

        // List
        UIManager.put("List.background", lightBackground);
        UIManager.put("List.foreground", textColor);
        UIManager.put("List.selectionBackground", new Color(70, 130, 180));
        UIManager.put("List.selectionForeground", Color.black);

        // Tree
        UIManager.put("Tree.background", lightBackground);
        UIManager.put("Tree.foreground", textColor);
        UIManager.put("Tree.selectionBackground", new Color(70, 130, 180));
        UIManager.put("Tree.selectionForeground", Color.black);

        // Check Box and Radio Button
        UIManager.put("CheckBox.background", darkBackground);
        UIManager.put("CheckBox.foreground", textColor);
        UIManager.put("RadioButton.background", darkBackground);
        UIManager.put("RadioButton.foreground", textColor);

        // Slider
        UIManager.put("Slider.background", darkBackground);
        UIManager.put("Slider.foreground", textColor);
        UIManager.put("Slider.trackColor", lightBackground);
        UIManager.put("Slider.thumb", new Color(70, 130, 180));

        // Separator
        UIManager.put("Separator.background", borderColor);
        UIManager.put("Separator.foreground", borderColor);

        // Titled Border
        UIManager.put("TitledBorder.titleColor", textColor);
        UIManager.put("TitledBorder.border", BorderFactory.createLineBorder(borderColor));

        // Internal Frame
        UIManager.put("InternalFrame.background", darkBackground);
        UIManager.put("InternalFrame.titleForeground", textColor);
        UIManager.put("InternalFrame.titleBackground", mediumBackground);

        // Desktop Pane
        UIManager.put("Desktop.background", darkBackground);

        // File Chooser
        UIManager.put("FileChooser.background", darkBackground);
        UIManager.put("FileChooser.foreground", textColor);

        // Color Chooser
        UIManager.put("ColorChooser.background", darkBackground);
        UIManager.put("ColorChooser.foreground", textColor);
    }
}