package models;

import java.time.LocalDateTime;

public class PriceHistory {
    private String cryptoId;
    private double price;
    private LocalDateTime timestamp;
    private double volume;
    private double marketCap;

    public PriceHistory(String cryptoId, double price, LocalDateTime timestamp) {
        this.cryptoId = cryptoId;
        this.price = price;
        this.timestamp = timestamp;
        this.volume = 0.0;
        this.marketCap = 0.0;
    }

    public PriceHistory(String cryptoId, double price, LocalDateTime timestamp,
                        double volume, double marketCap) {
        this.cryptoId = cryptoId;
        this.price = price;
        this.timestamp = timestamp;
        this.volume = volume;
        this.marketCap = marketCap;
    }

    // Getters and Setters
    public String getCryptoId() { return cryptoId; }
    public void setCryptoId(String cryptoId) { this.cryptoId = cryptoId; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public double getVolume() { return volume; }
    public void setVolume(double volume) { this.volume = volume; }

    public double getMarketCap() { return marketCap; }
    public void setMarketCap(double marketCap) { this.marketCap = marketCap; }

    public String getFormattedPrice() {
        return String.format("$%,.2f", price);
    }

    public String getFormattedVolume() {
        if (volume >= 1000000000) {
            return String.format("$%.2fB", volume / 1000000000);
        } else if (volume >= 1000000) {
            return String.format("$%.2fM", volume / 1000000);
        } else {
            return String.format("$%,.0f", volume);
        }
    }

    public String getFormattedTimestamp() {
        return timestamp.toString();
    }
}