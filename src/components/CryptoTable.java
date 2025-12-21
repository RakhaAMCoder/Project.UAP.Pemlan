package components;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class CryptoTable extends JTable {
    private Color positiveColor = new Color(0, 200, 0);
    private Color negativeColor = new Color(220, 0, 0);

    public CryptoTable(TableModel model) {
        super(model);

        // Customize appearance
        setRowHeight(40);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 1));
        setFillsViewportHeight(true);
        getTableHeader().setReorderingAllowed(false);

        // Set custom cell renderer for price change column
        setDefaultRenderer(Object.class, new CryptoCellRenderer());
    }

    // Custom cell renderer for cryptocurrency table
    class CryptoCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            // Default styling
            c.setBackground(isSelected ?
                    new Color(70, 130, 180) :
                    new Color(40, 40, 50));
            c.setForeground(isSelected ?
                    Color.WHITE :
                    new Color(220, 220, 220));

            // Style specific columns
            String columnName = table.getColumnName(column);

            if ("24h Change".equals(columnName) && value != null) {
                String change = value.toString();
                if (change.contains("+")) {
                    c.setForeground(positiveColor);
                    setText("<html><b>" + change + "</b></html>");
                } else if (change.contains("-")) {
                    c.setForeground(negativeColor);
                    setText("<html><b>" + change + "</b></html>");
                }
            }

            if ("Status".equals(columnName) && value != null) {
                String status = value.toString();
                if ("Naik".equals(status)) {
                    c.setForeground(positiveColor);
                    setText("<html><b>▲ " + status + "</b></html>");
                } else {
                    c.setForeground(negativeColor);
                    setText("<html><b>▼ " + status + "</b></html>");
                }
            }

            if ("Price (USD)".equals(columnName) && value != null) {
                setText("<html><b>" + value + "</b></html>");
            }

            // Center align all cells
            setHorizontalAlignment(SwingConstants.CENTER);

            // Add border to cells
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 70)),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));

            return c;
        }
    }

    // Custom header renderer
    class CryptoHeaderRenderer extends JLabel implements TableCellRenderer {
        public CryptoHeaderRenderer() {
            setOpaque(true);
            setBackground(new Color(50, 50, 60));
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setHorizontalAlignment(SwingConstants.CENTER);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(70, 130, 180)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }
    }

    @Override
    protected JTableHeader createDefaultTableHeader() {
        JTableHeader header = super.createDefaultTableHeader();
        header.setDefaultRenderer(new CryptoHeaderRenderer());
        return header;
    }
}