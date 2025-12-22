package dashboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Random;
import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class DetailChartFrame extends JFrame {
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private JComboBox<String> timeframeCombo;
    private Timer refreshTimer;
    private String cryptoName;

    public DetailChartFrame(String cryptoName) {
        this.cryptoName = cryptoName;
        setTitle(cryptoName + " - Price Chart");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        startRealTimeChartUpdates();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 40));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(40, 40, 50));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel(cryptoName + " Price Chart");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(220, 220, 220));

        // Controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlsPanel.setBackground(new Color(40, 40, 50));

        timeframeCombo = new JComboBox<>(new String[]{"1 Minute", "5 Minutes", "15 Minutes", "1 Hour", "1 Day"});
        timeframeCombo.setBackground(new Color(50, 50, 60));
        timeframeCombo.setForeground(Color.black);
        timeframeCombo.addActionListener(e -> updateChart());

        JButton exportBtn = new JButton("Export Data");
        exportBtn.setBackground(new Color(70, 130, 180));
        exportBtn.setForeground(Color.black);
        exportBtn.addActionListener(e -> exportData());

        controlsPanel.add(new JLabel("Timeframe:"));
        controlsPanel.add(timeframeCombo);
        controlsPanel.add(Box.createHorizontalStrut(20));
        controlsPanel.add(exportBtn);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(controlsPanel, BorderLayout.EAST);

        // Create initial chart
        chart = createChart();
        chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(new Color(40, 40, 50));

        // Stats panel
        JPanel statsPanel = createStatsPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        mainPanel.add(statsPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JFreeChart createChart() {
        TimeSeries series = new TimeSeries(cryptoName + " Price");
        Random random = new Random();

        // Generate sample data for the last 24 hours
        Second current = new Second();
        for (int i = 0; i < 100; i++) {
            Second time = (Second) current.previous();
            double price = 30000 + random.nextGaussian() * 1000;
            series.add(time, price);
            current = time;
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                cryptoName + " Price Movement",
                "Time",
                "Price (USD)",
                dataset,
                true,
                true,
                false
        );

        // Apply dark theme
        chart.setBackgroundPaint(new Color(40, 40, 50));
        chart.getTitle().setPaint(new Color(220, 220, 220));

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(30, 30, 40));
        plot.setDomainGridlinePaint(new Color(60, 60, 70));
        plot.setRangeGridlinePaint(new Color(60, 60, 70));

        // Set axis colors
        plot.getDomainAxis().setLabelPaint(new Color(220, 220, 220));
        plot.getDomainAxis().setTickLabelPaint(new Color(180, 180, 180));
        plot.getRangeAxis().setLabelPaint(new Color(220, 220, 220));
        plot.getRangeAxis().setTickLabelPaint(new Color(180, 180, 180));

        // Customize renderer
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(70, 130, 180));
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);

        return chart;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.setBackground(new Color(40, 40, 50));
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Create stat cards
        String[] labels = {"Current Price", "24h High", "24h Low", "24h Volume"};
        String[] values = {"$32,456.78", "$33,123.45", "$31,890.12", "$1.2B"};
        Color[] colors = {
                new Color(50, 205, 50),  // Green
                new Color(255, 140, 0),  // Orange
                new Color(220, 20, 60),  // Red
                new Color(70, 130, 180)  // Blue
        };

        for (int i = 0; i < 4; i++) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(new Color(50, 50, 60));
            card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

            JLabel label = new JLabel(labels[i]);
            label.setForeground(new Color(180, 180, 180));
            label.setFont(new Font("Segoe UI", Font.PLAIN, 12));

            JLabel value = new JLabel(values[i]);
            value.setForeground(colors[i]);
            value.setFont(new Font("Segoe UI", Font.BOLD, 16));

            card.add(label, BorderLayout.NORTH);
            card.add(value, BorderLayout.CENTER);

            panel.add(card);
        }

        return panel;
    }

    private void updateChart() {
        // In a real application, this would fetch new data based on selected timeframe
        chartPanel.setChart(createChart());
    }

    private void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Chart Data");

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                // Implementation for exporting chart data
                JOptionPane.showMessageDialog(this, "Data exported successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error exporting data: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void startRealTimeChartUpdates() {
        refreshTimer = new Timer(5000, e -> {
            // Update chart with new data point
            TimeSeries series = ((TimeSeriesCollection) chart.getXYPlot().getDataset()).getSeries(0);
            Second latestTime = (Second) series.getTimePeriod(series.getItemCount() - 1);
            Second newTime = (Second) latestTime.next();

            Random random = new Random();
            double lastPrice = series.getValue(series.getItemCount() - 1).doubleValue();
            double newPrice = lastPrice * (1 + (random.nextGaussian() * 0.01)); // 1% change

            series.add(newTime, newPrice);

            // Remove old data points to keep chart manageable
            if (series.getItemCount() > 100) {
                series.delete(0, 0);
            }
        });
        refreshTimer.start();
    }

    @Override
    public void dispose() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
        super.dispose();
    }
}