package models;

import java.time.LocalDateTime;

public class Cryptocurrency {
    private String id;
    private String name;
    private String symbol;
    private String category;
    private double currentPrice;
    private double priceChange24h;
    private double priceChangePercentage24h;
    private LocalDateTime lastUpdated;
    private boolean isFavorite;

    public Cryptocurrency(String id, String name, String symbol, String category) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.category = category;
        this.currentPrice = 0.0;
        this.priceChange24h = 0.0;
        this.priceChangePercentage24h = 0.0;
        this.lastUpdated = LocalDateTime.now();
        this.isFavorite = false;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
        this.lastUpdated = LocalDateTime.now();
    }

    public double getPriceChange24h() { return priceChange24h; }
    public void setPriceChange24h(double priceChange24h) {
        this.priceChange24h = priceChange24h;
    }

    public double getPriceChangePercentage24h() { return priceChangePercentage24h; }
    public void setPriceChangePercentage24h(double priceChangePercentage24h) {
        this.priceChangePercentage24h = priceChangePercentage24h;
    }

    public LocalDateTime getLastUpdated() { return lastUpdated; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public String getStatus() {
        return priceChangePercentage24h >= 0 ? "Naik" : "Turun";
    }

    public String getFormattedPrice() {
        return String.format("$%,.2f", currentPrice);
    }

    public String getFormattedChange() {
        String sign = priceChangePercentage24h >= 0 ? "+" : "";
        return String.format("%s%.2f%%", sign, priceChangePercentage24h);
    }
}