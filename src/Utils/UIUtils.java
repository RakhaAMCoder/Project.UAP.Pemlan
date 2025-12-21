package Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UIUtils {

    // Create a styled button with hover effects
    public static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw rounded rectangle
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Draw border
                g2.setColor(getBackground().darker());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font(Constants.FONT_PRIMARY, Font.BOLD, Constants.FONT_MEDIUM));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
        });

        return button;
    }

    // Create a styled text field
    public static JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setBackground(Constants.BACKGROUND_LIGHT);
        field.setForeground(Constants.TEXT_PRIMARY);
        field.setCaretColor(Constants.TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.BORDER_MEDIUM),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setFont(new Font(Constants.FONT_PRIMARY, Font.PLAIN, Constants.FONT_MEDIUM));
        return field;
    }

    // Create a styled combo box
    public static JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBackground(Constants.BACKGROUND_LIGHT);
        comboBox.setForeground(Constants.TEXT_PRIMARY);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.BORDER_MEDIUM),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBackground(isSelected ? Constants.BUTTON_PRIMARY : Constants.BACKGROUND_LIGHT);
                c.setForeground(isSelected ? Color.WHITE : Constants.TEXT_PRIMARY);
                return c;
            }
        });
        return comboBox;
    }

    // Create a stat card (for dashboard)
    public static JPanel createStatCard(String title, String value, Color valueColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Constants.BACKGROUND_MEDIUM);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.BORDER_DARK),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Constants.TEXT_SECONDARY);
        titleLabel.setFont(new Font(Constants.FONT_PRIMARY, Font.PLAIN, Constants.FONT_SMALL));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(valueColor);
        valueLabel.setFont(new Font(Constants.FONT_PRIMARY, Font.BOLD, Constants.FONT_LARGE));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    // Create a section header
    public static JPanel createSectionHeader(String title) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Constants.BACKGROUND_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Constants.TEXT_PRIMARY);
        titleLabel.setFont(new Font(Constants.FONT_PRIMARY, Font.BOLD, Constants.FONT_LARGE));

        JSeparator separator = new JSeparator();
        separator.setForeground(Constants.BORDER_MEDIUM);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(separator, BorderLayout.SOUTH);

        return header;
    }

    // Apply dark theme to a component
    public static void applyDarkTheme(Component component) {
        if (component instanceof JPanel) {
            component.setBackground(Constants.BACKGROUND_DARK);
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyDarkTheme(child);
            }
        }
    }

    // Create a loading spinner
    public static JPanel createLoadingSpinner(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.BACKGROUND_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel label = new JLabel(message + "...");
        label.setForeground(Constants.TEXT_PRIMARY);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font(Constants.FONT_PRIMARY, Font.PLAIN, Constants.FONT_MEDIUM));

        // Simple animated dots (would be better with proper spinner)
        Timer timer = new Timer(500, e -> {
            String text = label.getText();
            if (text.endsWith("...")) {
                label.setText(message);
            } else {
                label.setText(text + ".");
            }
        });
        timer.start();

        panel.add(label, BorderLayout.CENTER);

        // Stop timer when panel is removed
        panel.addHierarchyListener(e -> {
            if (e.getChangeFlags() == HierarchyEvent.DISPLAYABILITY_CHANGED && !panel.isDisplayable()) {
                timer.stop();
            }
        });

        return panel;
    }

    // Create a tooltip with custom styling
    public static void setStyledTooltip(JComponent component, String text) {
        component.setToolTipText(text);

        // Customize tooltip appearance
        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(10000);

        UIManager.put("ToolTip.background", Constants.BACKGROUND_MEDIUM);
        UIManager.put("ToolTip.foreground", Constants.TEXT_PRIMARY);
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(Constants.BORDER_MEDIUM));
    }
}