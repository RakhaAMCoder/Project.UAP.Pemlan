package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModernButton extends JButton {
    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;

    public ModernButton(String text, Color bgColor) {
        super(text);
        this.backgroundColor = bgColor;
        this.hoverColor = bgColor.brighter();
        this.pressedColor = bgColor.darker();

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(backgroundColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColor);
            }
        });

        setBackground(backgroundColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded rectangle background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        // Draw border
        g2.setColor(getBackground().darker());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        backgroundColor = bg;
        hoverColor = bg.brighter();
        pressedColor = bg.darker();
    }
}