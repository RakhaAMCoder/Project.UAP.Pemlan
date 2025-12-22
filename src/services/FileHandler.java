package services;

import models.Cryptocurrency;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileHandler {
    // Gunakan absolute path untuk menghindari masalah
    private static String DATA_DIR = System.getProperty("user.dir") + File.separator + "data";
    private static String CRYPTO_FILE = DATA_DIR + File.separator + "cryptocurrencies.csv";
    private static final String HISTORY_FILE = DATA_DIR + File.separator + "price_history.csv";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FileHandler() {
        System.out.println("=== FILE HANDLER INITIALIZATION ===");
        System.out.println("Working directory: " + System.getProperty("user.dir"));
        System.out.println("Data directory: " + DATA_DIR);
        System.out.println("Crypto file: " + CRYPTO_FILE);

        // Create data directory if it doesn't exist
        createDataDirectory();

        // Create sample data files if they don't exist
        createSampleDataIfNeeded();
    }

    private void createDataDirectory() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            System.out.println("Creating data directory...");
            boolean created = dataDir.mkdirs();
            if (created) {
                System.out.println("✓ Data directory created: " + dataDir.getAbsolutePath());
            } else {
                System.err.println("✗ Failed to create data directory!");
                // Coba create di current directory
                System.out.println("Trying to create in current directory...");
                DATA_DIR = "data";
                CRYPTO_FILE = DATA_DIR + File.separator + "cryptocurrencies.csv";
            }
        } else {
            System.out.println("✓ Data directory exists: " + dataDir.getAbsolutePath());
        }
    }

    private void createSampleDataIfNeeded() {
        File cryptoFile = new File(CRYPTO_FILE);
        if (!cryptoFile.exists()) {
            System.out.println("Creating sample data file...");
            List<Cryptocurrency> sampleData = createSampleCryptocurrencies();
            boolean saved = saveCryptocurrencies(sampleData);
            if (saved) {
                System.out.println("✓ Sample data created: " + cryptoFile.getAbsolutePath());
                System.out.println("✓ File size: " + cryptoFile.length() + " bytes");
            } else {
                System.err.println("✗ Failed to create sample data!");
            }
        } else {
            System.out.println("✓ Data file exists: " + cryptoFile.getAbsolutePath());
            System.out.println("✓ File size: " + cryptoFile.length() + " bytes");
        }
    }

    private List<Cryptocurrency> createSampleCryptocurrencies() {
        List<Cryptocurrency> sampleData = new ArrayList<>();
        Random random = new Random();

        // Sample data dengan harga yang realistic
        String[][] sampleCryptos = {
                {"BTC", "Bitcoin", "Currency"},
                {"ETH", "Ethereum", "Platform"},
                {"BNB", "Binance Coin", "Exchange"},
                {"ADA", "Cardano", "Platform"},
                {"SOL", "Solana", "Platform"},
                {"XRP", "Ripple", "Payment"},
                {"DOT", "Polkadot", "Platform"},
                {"DOGE", "Dogecoin", "Meme"},
                {"LTC", "Litecoin", "Currency"},
                {"LINK", "Chainlink", "Oracle"}
        };

        for (String[] data : sampleCryptos) {
            String id = data[0] + "-" + (1000 + random.nextInt(9000));
            String name = data[1];
            String symbol = data[0];
            String category = data[2];

            Cryptocurrency crypto = new Cryptocurrency(id, name, symbol, category);

            // Set realistic prices
            double price = 0;
            switch (symbol) {
                case "BTC": price = 45000 + random.nextDouble() * 5000; break;
                case "ETH": price = 2500 + random.nextDouble() * 300; break;
                case "BNB": price = 300 + random.nextDouble() * 50; break;
                case "ADA": price = 0.5 + random.nextDouble() * 0.2; break;
                case "SOL": price = 100 + random.nextDouble() * 20; break;
                case "XRP": price = 0.6 + random.nextDouble() * 0.1; break;
                case "DOT": price = 8 + random.nextDouble() * 2; break;
                case "DOGE": price = 0.15 + random.nextDouble() * 0.05; break;
                case "LTC": price = 70 + random.nextDouble() * 10; break;
                case "LINK": price = 14 + random.nextDouble() * 3; break;
                default: price = 100 + random.nextDouble() * 100;
            }

            crypto.setCurrentPrice(price);

            // Random change between -5% to +5%
            double changePercent = (random.nextDouble() - 0.5) * 10;
            crypto.setPriceChangePercentage24h(changePercent);
            crypto.setPriceChange24h(price * (changePercent / 100));

            crypto.setFavorite(random.nextBoolean());

            sampleData.add(crypto);
        }

        System.out.println("Created " + sampleData.size() + " sample cryptocurrencies");
        return sampleData;
    }

    public List<Cryptocurrency> loadCryptocurrencies() {
        List<Cryptocurrency> cryptoList = new ArrayList<>();
        File cryptoFile = new File(CRYPTO_FILE);

        System.out.println("\n=== LOADING CRYPTOCURRENCIES ===");
        System.out.println("File path: " + cryptoFile.getAbsolutePath());
        System.out.println("File exists: " + cryptoFile.exists());

        if (!cryptoFile.exists()) {
            System.out.println("Data file not found. Creating sample data...");
            return createSampleCryptocurrencies();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(cryptoFile))) {
            String line;
            int lineNumber = 0;
            int successCount = 0;
            int errorCount = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip header or empty lines
                if (lineNumber == 1 || line.trim().isEmpty()) {
                    continue;
                }

                try {
                    // Parse CSV line
                    String[] data = line.split(",", -1); // -1 to keep trailing empty strings

                    if (data.length >= 8) {
                        String id = data[0].trim();
                        String name = data[1].trim();
                        String symbol = data[2].trim();
                        String category = data[3].trim();

                        // Create cryptocurrency
                        Cryptocurrency crypto = new Cryptocurrency(id, name, symbol, category);

                        // Parse numeric values with error handling
                        try {
                            crypto.setCurrentPrice(Double.parseDouble(data[4].trim()));
                        } catch (NumberFormatException e) {
                            crypto.setCurrentPrice(0.0);
                            System.err.println("Warning: Invalid price at line " + lineNumber + ", setting to 0.0");
                        }

                        try {
                            crypto.setPriceChange24h(Double.parseDouble(data[5].trim()));
                        } catch (NumberFormatException e) {
                            crypto.setPriceChange24h(0.0);
                        }

                        try {
                            crypto.setPriceChangePercentage24h(Double.parseDouble(data[6].trim()));
                        } catch (NumberFormatException e) {
                            crypto.setPriceChangePercentage24h(0.0);
                        }

                        try {
                            crypto.setFavorite(Boolean.parseBoolean(data[7].trim()));
                        } catch (Exception e) {
                            crypto.setFavorite(false);
                        }

                        cryptoList.add(crypto);
                        successCount++;

                    } else {
                        System.err.println("Error: Invalid data format at line " + lineNumber);
                        System.err.println("Expected 8 columns, found " + data.length);
                        System.err.println("Line: " + line);
                        errorCount++;
                    }

                } catch (Exception e) {
                    System.err.println("Error processing line " + lineNumber + ": " + e.getMessage());
                    System.err.println("Line: " + line);
                    errorCount++;
                }
            }

            System.out.println("✓ Successfully loaded: " + successCount + " cryptocurrencies");
            if (errorCount > 0) {
                System.err.println("✗ Errors: " + errorCount + " lines failed to load");
            }

        } catch (FileNotFoundException e) {
            System.err.println("✗ File not found: " + cryptoFile.getAbsolutePath());
            System.out.println("Creating sample data instead...");
            return createSampleCryptocurrencies();
        } catch (IOException e) {
            System.err.println("✗ Error reading file: " + e.getMessage());
            System.out.println("Creating sample data instead...");
            return createSampleCryptocurrencies();
        }

        // If no data was loaded, create sample data
        if (cryptoList.isEmpty()) {
            System.out.println("No data loaded, creating sample data...");
            return createSampleCryptocurrencies();
        }

        return cryptoList;
    }

    public boolean saveCryptocurrencies(List<Cryptocurrency> cryptoList) {
        System.out.println("\n=== SAVING CRYPTOCURRENCIES ===");
        System.out.println("Saving " + cryptoList.size() + " cryptocurrencies");
        System.out.println("To file: " + CRYPTO_FILE);

        File cryptoFile = new File(CRYPTO_FILE);

        // Backup existing file if it exists
        if (cryptoFile.exists()) {
            try {
                File backup = new File(CRYPTO_FILE + ".backup");
                copyFile(cryptoFile, backup);
                System.out.println("✓ Created backup: " + backup.getName());
            } catch (IOException e) {
                System.err.println("Warning: Could not create backup: " + e.getMessage());
            }
        }

        // Ensure directory exists
        File parentDir = cryptoFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            System.out.println("Creating directory: " + parentDir.getAbsolutePath());
            boolean dirCreated = parentDir.mkdirs();
            if (!dirCreated) {
                System.err.println("✗ Failed to create directory!");
                return false;
            }
        }

        // Write to temporary file first
        File tempFile = new File(CRYPTO_FILE + ".tmp");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            // Write header
            writer.write("ID,Name,Symbol,Category,Price,Change24h,ChangePercent24h,IsFavorite");
            writer.newLine();

            // Write data
            for (Cryptocurrency crypto : cryptoList) {
                String line = String.format("%s,%s,%s,%s,%.2f,%.2f,%.2f,%b",
                        escapeCSV(crypto.getId()),
                        escapeCSV(crypto.getName()),
                        escapeCSV(crypto.getSymbol()),
                        escapeCSV(crypto.getCategory()),
                        crypto.getCurrentPrice(),
                        crypto.getPriceChange24h(),
                        crypto.getPriceChangePercentage24h(),
                        crypto.isFavorite()
                );
                writer.write(line);
                writer.newLine();
            }

            writer.flush();
            System.out.println("✓ Data written to temporary file");

        } catch (IOException e) {
            System.err.println("✗ Error writing to temporary file: " + e.getMessage());
            return false;
        }

        // Replace original file with temporary file
        try {
            // Delete original file if it exists
            if (cryptoFile.exists()) {
                if (!cryptoFile.delete()) {
                    System.err.println("✗ Could not delete original file");
                    // Try to continue anyway
                }
            }

            // Rename temporary file to original
            if (tempFile.renameTo(cryptoFile)) {
                System.out.println("✓ Successfully saved to: " + cryptoFile.getAbsolutePath());
                System.out.println("✓ File size: " + cryptoFile.length() + " bytes");
                System.out.println("✓ Save completed successfully!");
                return true;
            } else {
                System.err.println("✗ Could not rename temporary file");
                return false;
            }

        } catch (SecurityException e) {
            System.err.println("✗ Security exception: " + e.getMessage());
            return false;
        }
    }

    private void copyFile(File source, File dest) throws IOException {
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        // If value contains comma, quote, or newline, escape it
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public void savePriceHistory(String cryptoId, double price, LocalDateTime timestamp) {
        File historyFile = new File(HISTORY_FILE);

        // Ensure directory exists
        File parentDir = historyFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile, true))) {
            // Write header if file is empty
            if (historyFile.length() == 0) {
                writer.write("CryptoID,Price,Timestamp");
                writer.newLine();
            }

            String line = String.format("%s,%.2f,%s",
                    escapeCSV(cryptoId),
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
        File historyFile = new File(HISTORY_FILE);

        if (!historyFile.exists()) {
            return history;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) continue; // Skip header

                String[] data = line.split(",", -1);
                if (data.length >= 3 && data[0].equals(cryptoId)) {
                    history.add(data);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading price history: " + e.getMessage());
        }

        return history;
    }

    // Helper method untuk debug
    public void printFileInfo() {
        File cryptoFile = new File(CRYPTO_FILE);
        System.out.println("\n=== FILE INFO ===");
        System.out.println("Path: " + cryptoFile.getAbsolutePath());
        System.out.println("Exists: " + cryptoFile.exists());
        System.out.println("Size: " + cryptoFile.length() + " bytes");
        System.out.println("Readable: " + cryptoFile.canRead());
        System.out.println("Writable: " + cryptoFile.canWrite());

        if (cryptoFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(cryptoFile))) {
                System.out.println("\n=== FILE CONTENT (first 5 lines) ===");
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null && count < 5) {
                    System.out.println(line);
                    count++;
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        }
    }
}