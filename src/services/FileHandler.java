package services;

import models.Cryptocurrency;
import java.io.*;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class FileHandler {
    // Simpan di project folder saja (tidak perlu src/FileData)
    private static final String CRYPTO_FILE = "cryptocurrencies.csv";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FileHandler() {
        System.out.println("=== FILE HANDLER INITIALIZED ===");
        System.out.println("File location: " + new File(CRYPTO_FILE).getAbsolutePath());

        // Create sample data if file doesn't exist
        if (!new File(CRYPTO_FILE).exists()) {
            createSampleData();
        }
    }

    private void createSampleData() {
        System.out.println("Creating sample data file...");
        List<Cryptocurrency> sampleData = createSampleCryptocurrencies();
        boolean saved = saveCryptocurrencies(sampleData);
        if (saved) {
            System.out.println("✓ Sample data created");
        } else {
            System.err.println("✗ Failed to create sample data!");
        }
    }

    private List<Cryptocurrency> createSampleCryptocurrencies() {
        List<Cryptocurrency> sampleData = new ArrayList<>();
        Random random = new Random();

        String[][] sampleCryptos = {
                {"BTC", "Bitcoin", "Currency"},
                {"ETH", "Ethereum", "Platform"},
                {"BNB", "Binance Coin", "Exchange"},
                {"ADA", "Cardano", "Platform"},
                {"SOL", "Solana", "Platform"}
        };

        for (String[] data : sampleCryptos) {
            String id = data[0] + "-" + (1000 + random.nextInt(9000));
            String name = data[1];
            String symbol = data[0];
            String category = data[2];

            Cryptocurrency crypto = new Cryptocurrency(id, name, symbol, category);

            double price = 0;
            switch (symbol) {
                case "BTC": price = 45000 + random.nextDouble() * 5000; break;
                case "ETH": price = 2500 + random.nextDouble() * 300; break;
                case "BNB": price = 300 + random.nextDouble() * 50; break;
                case "ADA": price = 0.5 + random.nextDouble() * 0.2; break;
                case "SOL": price = 100 + random.nextDouble() * 20; break;
            }

            crypto.setCurrentPrice(price);
            double changePercent = (random.nextDouble() - 0.5) * 10;
            crypto.setPriceChangePercentage24h(changePercent);
            crypto.setPriceChange24h(price * (changePercent / 100));

            sampleData.add(crypto);
        }

        System.out.println("Created " + sampleData.size() + " sample cryptocurrencies");
        return sampleData;
    }

    public List<Cryptocurrency> loadCryptocurrencies() {
        System.out.println("\n=== LOADING CRYPTOCURRENCIES ===");

        File cryptoFile = new File(CRYPTO_FILE);
        System.out.println("File: " + cryptoFile.getAbsolutePath());
        System.out.println("Exists: " + cryptoFile.exists());

        if (!cryptoFile.exists()) {
            System.out.println("File not found. Creating sample data...");
            return createSampleCryptocurrencies();
        }

        List<Cryptocurrency> cryptoList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(cryptoFile))) {
            String line;
            int lineNumber = 0;
            int loadedCount = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) continue; // Skip header

                String[] data = line.split(",", -1);

                if (data.length >= 7) {
                    try {
                        Cryptocurrency crypto = new Cryptocurrency(
                                data[0].trim(),
                                data[1].trim(),
                                data[2].trim(),
                                data[3].trim()
                        );

                        crypto.setCurrentPrice(Double.parseDouble(data[4].trim()));
                        crypto.setPriceChange24h(Double.parseDouble(data[5].trim()));
                        crypto.setPriceChangePercentage24h(Double.parseDouble(data[6].trim()));

                        cryptoList.add(crypto);
                        loadedCount++;

                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing line " + lineNumber);
                    }
                }
            }

            System.out.println("✓ Loaded " + loadedCount + " cryptocurrencies");

        } catch (IOException e) {
            System.err.println("✗ Error reading file: " + e.getMessage());
            return createSampleCryptocurrencies();
        }

        return cryptoList;
    }

    public boolean saveCryptocurrencies(List<Cryptocurrency> cryptoList) {
        System.out.println("\n=== SAVING CRYPTOCURRENCIES ===");
        System.out.println("Saving " + cryptoList.size() + " cryptocurrencies");

        File cryptoFile = new File(CRYPTO_FILE);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cryptoFile))) {
            // Write header
            writer.write("ID,Name,Symbol,Category,Price,Change24h,ChangePercent24h");
            writer.newLine();

            // Write data
            for (Cryptocurrency crypto : cryptoList) {
                String line = String.format("%s,%s,%s,%s,%.2f,%.2f,%.2f",
                        escapeCSV(crypto.getId()),
                        escapeCSV(crypto.getName()),
                        escapeCSV(crypto.getSymbol()),
                        escapeCSV(crypto.getCategory()),
                        crypto.getCurrentPrice(),
                        crypto.getPriceChange24h(),
                        crypto.getPriceChangePercentage24h()
                );
                writer.write(line);
                writer.newLine();
            }

            writer.flush();
            System.out.println("✓ Saved to: " + cryptoFile.getAbsolutePath());
            return true;

        } catch (IOException e) {
            System.err.println("✗ Save error: " + e.getMessage());
            return false;
        }
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    // ========== CRUD HELPER METHODS (OPSIONAL) ==========

    public boolean updateCryptocurrency(Cryptocurrency updatedCrypto) {
        List<Cryptocurrency> cryptoList = loadCryptocurrencies();

        for (int i = 0; i < cryptoList.size(); i++) {
            if (cryptoList.get(i).getId().equals(updatedCrypto.getId())) {
                cryptoList.set(i, updatedCrypto);
                return saveCryptocurrencies(cryptoList);
            }
        }

        return false;
    }

    public boolean deleteCryptocurrency(String cryptoId) {
        List<Cryptocurrency> cryptoList = loadCryptocurrencies();
        int initialSize = cryptoList.size();

        cryptoList.removeIf(crypto -> crypto.getId().equals(cryptoId));

        if (cryptoList.size() < initialSize) {
            return saveCryptocurrencies(cryptoList);
        }

        return false;
    }
}