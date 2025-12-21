package services;

import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.XYDataset;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class ChartGenerator {

    public static JFreeChart createLineChart(String title, String xAxisLabel, String yAxisLabel,
                                             Map<LocalDateTime, Double> data, Color lineColor) {

        TimeSeries series = new TimeSeries(title);

        // Convert LocalDateTime to Date and add to series
        for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
            Date date = Date.from(entry.getKey().atZone(ZoneId.systemDefault()).toInstant());
            series.add(new Millisecond(date), entry.getValue());
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset,
                true,
                true,
                false
        );

        // Apply dark theme
        applyDarkTheme(chart);

        // Customize line
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, lineColor);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        return chart;
    }

    public static JFreeChart createCandleStickChart(String title, List<CandleData> candleData) {
        // Create dataset for candlestick
        DefaultHighLowDataset dataset = createCandleDataset(candleData);

        JFreeChart chart = ChartFactory.createCandlestickChart(
                title,
                "Time",
                "Price",
                dataset,
                true
        );

        // Apply dark theme
        applyDarkTheme(chart);

        // Customize candlestick renderer
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(30, 30, 40));

        // Set axis colors
        plot.getDomainAxis().setLabelPaint(new Color(220, 220, 220));
        plot.getDomainAxis().setTickLabelPaint(new Color(180, 180, 180));
        plot.getRangeAxis().setLabelPaint(new Color(220, 220, 220));
        plot.getRangeAxis().setTickLabelPaint(new Color(180, 180, 180));

        return chart;
    }

    public static JFreeChart createSplineChart(String title, String xAxisLabel, String yAxisLabel,
                                               Map<LocalDateTime, Double> data, Color lineColor) {

        TimeSeries series = new TimeSeries(title);

        for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
            Date date = Date.from(entry.getKey().atZone(ZoneId.systemDefault()).toInstant());
            series.add(new Millisecond(date), entry.getValue());
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title,
                xAxisLabel,
                yAxisLabel,
                dataset,
                true,
                true,
                false
        );

        applyDarkTheme(chart);

        // Use spline renderer for smoother lines
        XYPlot plot = chart.getXYPlot();
        XYSplineRenderer renderer = new XYSplineRenderer();
        renderer.setSeriesPaint(0, lineColor);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        return chart;
    }

    public static JFreeChart createMultiLineChart(String title,
                                                  Map<String, Map<LocalDateTime, Double>> datasets) {

        TimeSeriesCollection collection = new TimeSeriesCollection();

        Color[] colors = {
                new Color(70, 130, 180),   // Blue
                new Color(50, 205, 50),    // Green
                new Color(255, 140, 0),    // Orange
                new Color(220, 20, 60),    // Red
                new Color(138, 43, 226)    // Purple
        };

        int colorIndex = 0;
        for (Map.Entry<String, Map<LocalDateTime, Double>> entry : datasets.entrySet()) {
            TimeSeries series = new TimeSeries(entry.getKey());

            for (Map.Entry<LocalDateTime, Double> dataPoint : entry.getValue().entrySet()) {
                Date date = Date.from(dataPoint.getKey().atZone(ZoneId.systemDefault()).toInstant());
                series.add(new Millisecond(date), dataPoint.getValue());
            }

            collection.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title,
                "Time",
                "Price (USD)",
                collection,
                true,
                true,
                false
        );

        applyDarkTheme(chart);

        // Set different colors for each series
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        for (int i = 0; i < datasets.size(); i++) {
            renderer.setSeriesPaint(i, colors[i % colors.length]);
            renderer.setSeriesShapesVisible(i, false);
            renderer.setSeriesStroke(i, new BasicStroke(1.5f));
        }

        plot.setRenderer(renderer);

        return chart;
    }

    private static DefaultHighLowDataset createCandleDataset(List<CandleData> candleData) {
        int n = candleData.size();
        Date[] dates = new Date[n];
        double[] highs = new double[n];
        double[] lows = new double[n];
        double[] opens = new double[n];
        double[] closes = new double[n];
        double[] volumes = new double[n];

        for (int i = 0; i < n; i++) {
            CandleData data = candleData.get(i);
            dates[i] = Date.from(data.getTimestamp().atZone(ZoneId.systemDefault()).toInstant());
            highs[i] = data.getHigh();
            lows[i] = data.getLow();
            opens[i] = data.getOpen();
            closes[i] = data.getClose();
            volumes[i] = data.getVolume();
        }

        return new DefaultHighLowDataset("Crypto", dates, highs, lows, opens, closes, volumes);
    }

    private static void applyDarkTheme(JFreeChart chart) {
        chart.setBackgroundPaint(new Color(40, 40, 50));
        chart.getTitle().setPaint(new Color(220, 220, 220));

        if (chart.getPlot() instanceof XYPlot) {
            XYPlot plot = (XYPlot) chart.getPlot();
            plot.setBackgroundPaint(new Color(30, 30, 40));
            plot.setDomainGridlinePaint(new Color(60, 60, 70));
            plot.setRangeGridlinePaint(new Color(60, 60, 70));

            // Set axis colors
            plot.getDomainAxis().setLabelPaint(new Color(220, 220, 220));
            plot.getDomainAxis().setTickLabelPaint(new Color(180, 180, 180));
            plot.getRangeAxis().setLabelPaint(new Color(220, 220, 220));
            plot.getRangeAxis().setTickLabelPaint(new Color(180, 180, 180));
        }
    }

    // Inner class for candlestick data
    public static class CandleData {
        private LocalDateTime timestamp;
        private double open;
        private double high;
        private double low;
        private double close;
        private double volume;

        public CandleData(LocalDateTime timestamp, double open, double high,
                          double low, double close, double volume) {
            this.timestamp = timestamp;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
        }

        // Getters
        public LocalDateTime getTimestamp() { return timestamp; }
        public double getOpen() { return open; }
        public double getHigh() { return high; }
        public double getLow() { return low; }
        public double getClose() { return close; }
        public double getVolume() { return volume; }
    }
}