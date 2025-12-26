package services;

import java.util.Random;

public class APIService {
    private Random random;

    public APIService() {
        this.random = new Random();
    }

    // Simulasi mendapatkan harga crypto tanpa dependency external
    public double getCryptoPrice(String cryptoId) {
        double basePrice;

        switch (cryptoId.toLowerCase()) {
            case "bitcoin":
            case "btc":
                basePrice = 40000 + random.nextDouble() * 10000;
                break;
            case "ethereum":
            case "eth":
                basePrice = 11000 + random.nextDouble() * 500;
                break;
            case "binancecoin":
            case "bnb":
                basePrice = 300 + random.nextDouble() * 50;
                break;
            case "cardano":
            case "ada":
                basePrice = 0.5 + random.nextDouble() * 0.1;
                break;
            case "solana":
            case "sol":
                basePrice = 2000 + random.nextDouble() * 20;
                break;
            case "ripple":
            case "xrp":
                basePrice = 0.6 + random.nextDouble() * 0.1;
                break;
            case "polkadot":
            case "dot":
                basePrice = 8 + random.nextDouble() * 2;
                break;
            case "dogecoin":
            case "doge":
                basePrice = 0.15 + random.nextDouble() * 0.05;
                break;
            case "avalanche":
            case "avax":
                basePrice = 35 + random.nextDouble() * 10;
                break;
            case "chainlink":
            case "link":
                basePrice = 14 + random.nextDouble() * 3;
                break;
            default:
                basePrice = 1000 + random.nextDouble() * 100;
        }

        // Tambahkan fluktuasi kecil untuk simulasi real-time
        double fluctuation = (random.nextDouble() - 0.5) * 0.02; // ±1%
        return basePrice * (1 + fluctuation);
    }
}