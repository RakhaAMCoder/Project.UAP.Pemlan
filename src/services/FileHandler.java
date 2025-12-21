package services;

import models.Cryptocurrency;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileHandler {
    private static final String CRYPTO_FILE = "data/cryptocurrencies.csv";
    private static final String HISTORY_FILE = "data/price_history.csv";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public FileHandler() {
        // Create data directory if it doesn't exist
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        // Create sample data files if they don't exist
        createSampleDataIfNeeded();
    }

    private void createSampleDataIfNeeded() {
        File cryptoFile = new File(CRYPTO_FILE);
        if (!cryptoFile.exists()) {
            try {
                List<Cryptocurrency> sampleData = createSampleCryptocurrencies();
                saveCryptocurrencies(sampleData);
                System.out.println("Sample data file created: " + CRYPTO_FILE);
            } catch (Exception e) {
                System.err.println("Error creating sample data: " + e.getMessage());
            }
        }
    }

    private List<Cryptocurrency> createSampleCryptocurrencies() {
        List<Cryptocurrency> sampleData = new ArrayList<>();
        Random random = new Random();

        // Sample cryptocurrencies data
        String[][] sampleCryptos = {
                {"BTC-001", "Bitcoin", "BTC", "Currency"},
                {"ETH-002", "Ethereum", "ETH", "Platform"},
                {"BNB-003", "Binance Coin", "BNB", "Exchange"},
                {"ADA-004", "Cardano", "ADA", "Platform"},
                {"SOL-005", "Solana", "SOL", "Platform"},
                {"XRP-006", "Ripple", "XRP", "Payment"},
                {"DOT-007", "Polkadot", "DOT", "Platform"},
                {"DOGE-008", "Dogecoin", "DOGE", "Meme"},
                {"AVAX-009", "Avalanche", "AVAX", "Platform"},
                {"LINK-010", "Chainlink", "LINK", "Oracle"}
        };

        APIService apiService = new APIService();

        for (String[] data : sampleCryptos) {
            Cryptocurrency crypto = new Cryptocurrency(data[0], data[1], data[2], data[3]);
            crypto.setCurrentPrice(apiService.getCryptoPrice(data[1]));
            crypto.setPriceChange24h((random.nextDouble() - 0.5) * 1000);
            crypto.setPriceChangePercentage24h(apiService.getPriceChange24h(data[1]));
            sampleData.add(crypto);
        }

        return sampleData;
    }

    public List<Cryptocurrency> loadCryptocurrencies() {
        List<Cryptocurrency> cryptoList = new ArrayList<>();

        File cryptoFile = new File(CRYPTO_FILE);
        if (!cryptoFile.exists()) {
            System.out.println("Data file not found, creating sample data...");
            return createSampleCryptocurrencies();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CRYPTO_FILE))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }

                String[] data = line.split(",");
                if (data.length >= 8) {
                    Cryptocurrency crypto = new Cryptocurrency(data[0], data[1], data[2], data[3]);
                    crypto.setCurrentPrice(Double.parseDouble(data[4]));
                    crypto.setPriceChange24h(Double.parseDouble(data[5]));
                    crypto.setPriceChangePercentage24h(Double.parseDouble(data[6]));

                    // Handle isFavorite with default false if parsing fails
                    try {
                        crypto.setFavorite(Boolean.parseBoolean(data[7]));
                    } catch (Exception e) {
                        crypto.setFavorite(false);
                    }

                    cryptoList.add(crypto);
                }
            }
            System.out.println("Loaded " + cryptoList.size() + " cryptocurrencies from file.");
        } catch (IOException e) {
            System.err.println("Error loading cryptocurrencies: " + e.getMessage());
            System.out.println("Creating sample data instead...");
            // Return sample data if file doesn't exist or has errors
            return createSampleCryptocurrencies();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number from file: " + e.getMessage());
            return createSampleCryptocurrencies();
        }

        return cryptoList;
    }


    public void saveCryptocurrencies(List<Cryptocurrency> cryptoList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CRYPTO_FILE))) {
            // Write header
            writer.write("ID,Name,Symbol,Category,Price,Change24h,ChangePercent24h,IsFavorite");
            writer.newLine();

            // Write data
            for (Cryptocurrency crypto : cryptoList) {
                String line = String.format("%s,%s,%s,%s,%.2f,%.2f,%.2f,%b",
                        crypto.getId(),
                        crypto.getName(),
                        crypto.getSymbol(),
                        crypto.getCategory(),
                        crypto.getCurrentPrice(),
                        crypto.getPriceChange24h(),
                        crypto.getPriceChangePercentage24h(),
                        crypto.isFavorite()
                );
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Saved " + cryptoList.size() + " cryptocurrencies to file.");
        } catch (IOException e) {
            System.err.println("Error saving cryptocurrencies: " + e.getMessage());
            // Create backup directory if needed
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdir();
                // Try saving again
                saveCryptocurrencies(cryptoList);
            }
        }
    }

    public void savePriceHistory(String cryptoId, double price, LocalDateTime timestamp) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE, true))) {
            File file = new File(HISTORY_FILE);
            if (file.length() == 0) {
                // Write header if file is empty
                writer.write("CryptoID,Price,Timestamp");
                writer.newLine();
            }

            String line = String.format("%s,%.2f,%s",
                    cryptoId,
                    price,
                    timestamp.format(formatter)
            );
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving price history: " + e.getMessage());
        }
    }

    public List<String[]> loadPriceHistory(String cryptoId) {
        List<String[]> history = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }

                String[] data = line.split(",");
                if (data.length >= 3 && data[0].equals(cryptoId)) {
                    history.add(data);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading price history: " + e.getMessage());
        }

        return history;
    }
}