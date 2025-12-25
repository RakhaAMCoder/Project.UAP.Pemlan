package dashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import org.jfree.chart.*;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import models.Cryptocurrency;
import services.FileHandler;

public class ReportFrame extends JFrame {
    private JTable historyTable;
    private DefaultTableModel tableModel;
    private JTextArea summaryArea;
    private FileHandler fileHandler;
    private List<Cryptocurrency> cryptoList;
    private JButton refreshBtn;
    private ChartPanel pieChartPanel;

    public ReportFrame() {
        this.fileHandler = new FileHandler();
        this.cryptoList = fileHandler.loadCryptocurrencies();

        setTitle("Cryptocurrency Report & History");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadReportData();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 40));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header
        JPanel headerPanel = createHeaderPanel();

        // Content panel with two sections
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setDividerSize(3);

        // Left panel - Charts and Summary
        JPanel leftPanel = createLeftPanel();

        // Right panel - History Table
        JPanel rightPanel = createRightPanel();

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 50));
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Title
        JLabel titleLabel = new JLabel("Cryptocurrency Reports & History");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(220, 220, 220));

        // Refresh button only
        refreshBtn = new JButton("Refresh");
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.BLACK);
        refreshBtn.addActionListener(e -> refreshData());

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(refreshBtn, BorderLayout.EAST);

        return panel;
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 50));
        panel.setBorder(new EmptyBorder(0, 0, 0, 10));

        // Summary panel
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(new Color(40, 40, 50));
        summaryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80)),
                "Market Summary",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(220, 220, 220)
        ));

        summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setBackground(new Color(50, 50, 60));
        summaryArea.setForeground(new Color(220, 220, 220));
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        summaryArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane summaryScroll = new JScrollPane(summaryArea);
        summaryScroll.setBorder(null);

        summaryPanel.add(summaryScroll, BorderLayout.CENTER);

        // Chart panel
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(new Color(40, 40, 50));
        chartPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80)),
                "Market Distribution",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(220, 220, 220)
        ));

        // Create pie chart
        pieChartPanel = createPieChart();
        chartPanel.add(pieChartPanel, BorderLayout.CENTER);

        // Add to left panel with split
        JSplitPane leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftSplit.setDividerLocation(250);
        leftSplit.setDividerSize(3);
        leftSplit.setTopComponent(summaryPanel);
        leftSplit.setBottomComponent(chartPanel);

        panel.add(leftSplit, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 50));

        // Table - TAMPILKAN SEMUA COIN
        String[] columns = {"No", "Name", "Symbol", "Price (USD)", "24h Change", "Category", "Last Updated"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = new JTable(tableModel);
        historyTable.setBackground(new Color(40, 40, 50));
        historyTable.setForeground(new Color(220, 220, 220));
        historyTable.setRowHeight(35);
        historyTable.getTableHeader().setBackground(new Color(50, 50, 60));
        historyTable.getTableHeader().setForeground(Color.BLACK);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(new Color(40, 40, 50));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 80)),
                "All Cryptocurrencies",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(220, 220, 220)
        ));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private ChartPanel createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Add initial data
        dataset.setValue("Loading...", 100);

        JFreeChart chart = ChartFactory.createPieChart(
                "Market Distribution",
                dataset,
                true,
                true,
                false
        );

        // Apply dark theme
        chart.setBackgroundPaint(new Color(40, 40, 50));
        chart.getTitle().setPaint(new Color(220, 220, 220));

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(40, 40, 50));
        plot.setLabelBackgroundPaint(new Color(40, 40, 50));
        plot.setLabelPaint(new Color(220, 220, 220));
        plot.setOutlinePaint(new Color(60, 60, 70));

        // Set colors for each slice
        plot.setSectionPaint("Loading...", new Color(128, 128, 128));

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(new Color(40, 40, 50));

        return chartPanel;
    }

    private void loadReportData() {
        // Load fresh data
        cryptoList = fileHandler.loadCryptocurrencies();

        // Generate summary
        generateSummary();

        // Load ALL coins to table
        loadAllCoinsToTable();

        // Update pie chart with real data
        updatePieChart();
    }

    private void generateSummary() {
        StringBuilder summary = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Calculate totals
        double totalMarketCap = 0;
        double totalVolume = 0;
        int totalCryptos = cryptoList.size();
        int gainers = 0;
        int losers = 0;

        for (Cryptocurrency crypto : cryptoList) {
            totalMarketCap += crypto.getCurrentPrice() * 1000000; // Simplified
            totalVolume += Math.abs(crypto.getPriceChange24h()) * 100000;

            if (crypto.getPriceChangePercentage24h() >= 0) {
                gainers++;
            } else {
                losers++;
            }
        }

        summary.append("=== CRYPTOCURRENCY MARKET REPORT ===\n\n");
        summary.append(String.format("Report Date: %s\n", LocalDateTime.now().format(dateFormatter)));
        summary.append(String.format("Total Cryptocurrencies: %d\n", totalCryptos));
        summary.append(String.format("Market Cap: $%,.2fB\n", totalMarketCap / 1000000000));
        summary.append(String.format("24h Volume: $%,.2fB\n", totalVolume / 1000000000));
        summary.append(String.format("Gainers: %d | Losers: %d\n\n", gainers, losers));
        summary.append("Top Performers (24h):\n");
        summary.append("----------------------\n");

        // Sort by performance
        cryptoList.sort((c1, c2) ->
                Double.compare(c2.getPriceChangePercentage24h(), c1.getPriceChangePercentage24h()));

        for (int i = 0; i < Math.min(5, cryptoList.size()); i++) {
            Cryptocurrency crypto = cryptoList.get(i);
            summary.append(String.format("%s: %+.2f%% ($%,.2f)\n",
                    crypto.getSymbol(),
                    crypto.getPriceChangePercentage24h(),
                    crypto.getCurrentPrice()));
        }

        summaryArea.setText(summary.toString());
    }

    private void loadAllCoinsToTable() {
        tableModel.setRowCount(0);

        // Tampilkan SEMUA coin yang ada
        for (int i = 0; i < cryptoList.size(); i++) {
            Cryptocurrency crypto = cryptoList.get(i);

            String change = crypto.getFormattedChange();
            String changeDisplay = change;
            if (change.contains("+")) {
                changeDisplay = "▲ " + change;
            } else if (change.contains("-")) {
                changeDisplay = "▼ " + change;
            }

            Object[] row = {
                    i + 1,
                    crypto.getName(),
                    crypto.getSymbol(),
                    crypto.getFormattedPrice(),
                    changeDisplay,
                    crypto.getCategory(),
                    crypto.getLastUpdated().toString().substring(0, 19)
            };
            tableModel.addRow(row);
        }
    }

    private void updatePieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        if (cryptoList.isEmpty()) {
            dataset.setValue("No Data", 100);
        } else {
            // Tampilkan top 5 coins di pie chart
            List<Cryptocurrency> sortedList = new ArrayList<>(cryptoList);
            sortedList.sort((c1, c2) -> Double.compare(c2.getCurrentPrice(), c1.getCurrentPrice()));

            int count = Math.min(5, sortedList.size());
            for (int i = 0; i < count; i++) {
                Cryptocurrency crypto = sortedList.get(i);
                dataset.setValue(crypto.getSymbol(), crypto.getCurrentPrice());
            }

            // Sisanya sebagai "Others"
            double othersTotal = 0;
            for (int i = count; i < sortedList.size(); i++) {
                othersTotal += sortedList.get(i).getCurrentPrice();
            }
            if (othersTotal > 0) {
                dataset.setValue("Others", othersTotal);
            }
        }

        // Update chart
        PiePlot plot = (PiePlot) pieChartPanel.getChart().getPlot();
        plot.setDataset(dataset);

        // Update chart title
        pieChartPanel.getChart().setTitle("Market Distribution (Top " + Math.min(5, cryptoList.size()) + ")");

        // Set colors
        String[] colors = {"#F7931A", "#627EEA", "#F0B90B", "#0082CE", "#00FFA3", "#808080"};
        int colorIndex = 0;
        for (Object key : dataset.getKeys()) {
            plot.setSectionPaint(key.toString(), Color.decode(colors[colorIndex % colors.length]));
            colorIndex++;
        }
    }

    private void refreshData() {
        refreshBtn.setEnabled(false);
        refreshBtn.setText("Refreshing...");

        new Thread(() -> {
            try {
                loadReportData();

                SwingUtilities.invokeLater(() -> {
                    refreshBtn.setEnabled(true);
                    refreshBtn.setText("Refresh");
                    setTitle("Cryptocurrency Report - Updated: " +
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                });

            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    refreshBtn.setEnabled(true);
                    refreshBtn.setText("Refresh");
                });
            }
        }).start();
    }
}