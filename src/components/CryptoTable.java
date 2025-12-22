package components;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class CryptoTable extends JTable {
    private Color positiveColor = new Color(0, 200, 0);    // HIJAU
    private Color negativeColor = new Color(220, 0, 0);    // MERAH

    public CryptoTable(TableModel model) {
        super(model);

        // Customize appearance
        setRowHeight(40);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 1));
        setFillsViewportHeight(true);
        getTableHeader().setReorderingAllowed(false);

        // Set custom cell renderer untuk SEMUA kolom
        setDefaultRenderer(Object.class, new CryptoCellRenderer());
    }

    // Custom cell renderer - SAMA dengan DataInputFrame
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

            // KOLOM 24h Change - SAMA PERSIS dengan DataInputFrame
            if ("24h Change".equals(columnName) && value != null) {
                String change = value.toString();
                if (change.contains("+")) {
                    c.setForeground(positiveColor); // HIJAU untuk positif
                    setText("<html><b>" + change + "</b></html>");
                } else if (change.contains("-")) {
                    c.setForeground(negativeColor); // MERAH untuk negatif
                    setText("<html><b>" + change + "</b></html>");
                }
            }

            // KOLOM Status - SAMA PERSIS dengan DataInputFrame
            if ("Status".equals(columnName) && value != null) {
                String status = value.toString();
                if ("Naik".equals(status)) {
                    c.setForeground(positiveColor); // HIJAU
                    setText("<html><b>▲ " + status + "</b></html>");
                } else {
                    c.setForeground(negativeColor); // MERAH
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