package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ModernButton extends JButton {
    private Color backgroundColor;
    private Color hoverColor;
    private Color pressedColor;
    private boolean isHovered = false;
    private boolean isPressed = false;

    public ModernButton(String text, Color bgColor) {
        super(text);
        this.backgroundColor = bgColor;
        this.hoverColor = bgColor.brighter();
        this.pressedColor = bgColor.darker();

        // KUNCI PERBAIKAN: Biarkan JButton handle painting
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(true); // HARUS TRUE agar background terisi
        setOpaque(true);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Set warna awal
        setBackground(backgroundColor);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    updateButtonColor();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = false;
                    isPressed = false;
                    updateButtonColor();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled() && SwingUtilities.isLeftMouseButton(e)) {
                    isPressed = true;
                    updateButtonColor();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled() && SwingUtilities.isLeftMouseButton(e)) {
                    isPressed = false;
                    // Cek apakah mouse masih di atas button
                    if (contains(e.getPoint())) {
                        isHovered = true;
                    } else {
                        isHovered = false;
                    }
                    updateButtonColor();
                }
            }
        });
    }

    private void updateButtonColor() {
        Color currentColor;
        if (!isEnabled()) {
            currentColor = backgroundColor.darker().darker();
        } else if (isPressed) {
            currentColor = pressedColor;
        } else if (isHovered) {
            currentColor = hoverColor;
        } else {
            currentColor = backgroundColor;
        }

        // Set background menggunakan parent method
        super.setBackground(currentColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.dispose();
    }

    @Override
    public void setBackground(Color bg) {
        this.backgroundColor = bg;
        this.hoverColor = bg.brighter();
        this.pressedColor = bg.darker();

        if (!isHovered && !isPressed) {
            super.setBackground(bg);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        updateButtonColor();
    }
}