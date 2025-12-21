package Utils;

import java.awt.Color;

public class Constants {
    // UI Colors for Dark Theme
    public static final Color BACKGROUND_DARK = new Color(30, 30, 40);
    public static final Color BACKGROUND_MEDIUM = new Color(40, 40, 50);
    public static final Color BACKGROUND_LIGHT = new Color(50, 50, 60);
    public static final Color TEXT_PRIMARY = new Color(220, 220, 220);
    public static final Color TEXT_SECONDARY = new Color(180, 180, 180);
    public static final Color TEXT_DISABLED = new Color(100, 100, 100);

    // Status Colors
    public static final Color POSITIVE_GREEN = new Color(0, 200, 0);
    public static final Color NEGATIVE_RED = new Color(220, 0, 0);
    public static final Color WARNING_ORANGE = new Color(255, 140, 0);
    public static final Color INFO_BLUE = new Color(70, 130, 180);
    public static final Color ACCENT_PURPLE = new Color(138, 43, 226);

    // Crypto Specific Colors
    public static final Color BITCOIN_ORANGE = new Color(247, 147, 26);
    public static final Color ETHEREUM_BLUE = new Color(98, 126, 234);
    public static final Color BINANCE_YELLOW = new Color(240, 185, 11);
    public static final Color CARDANO_BLUE = new Color(0, 130, 206);
    public static final Color SOLANA_GREEN = new Color(0, 255, 163);

    // Button Colors
    public static final Color BUTTON_PRIMARY = new Color(70, 130, 180);
    public static final Color BUTTON_SUCCESS = new Color(50, 205, 50);
    public static final Color BUTTON_DANGER = new Color(220, 20, 60);
    public static final Color BUTTON_WARNING = new Color(255, 140, 0);

    // Border Colors
    public static final Color BORDER_DARK = new Color(60, 60, 70);
    public static final Color BORDER_MEDIUM = new Color(70, 70, 80);
    public static final Color BORDER_LIGHT = new Color(80, 80, 90);

    // Font Sizes
    public static final int FONT_XLARGE = 24;
    public static final int FONT_LARGE = 18;
    public static final int FONT_MEDIUM = 14;
    public static final int FONT_SMALL = 12;
    public static final int FONT_XSMALL = 10;

    // Font Names
    public static final String FONT_PRIMARY = "Segoe UI";
    public static final String FONT_SECONDARY = "Arial";
    public static final String FONT_MONOSPACE = "Monospaced";

    // Dimensions
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 800;
    public static final int SIDEBAR_WIDTH = 250;
    public static final int TABLE_ROW_HEIGHT = 40;

    // API Constants
    public static final String API_BASE_URL = "https://api.coingecko.com/api/v3";
    public static final int API_TIMEOUT_SECONDS = 10;
    public static final int REFRESH_INTERVAL_MS = 10000; // 10 seconds

    // File Paths
    public static final String DATA_DIR = "data/";
    public static final String CRYPTO_FILE = DATA_DIR + "cryptocurrencies.csv";
    public static final String HISTORY_FILE = DATA_DIR + "price_history.csv";
    public static final String SETTINGS_FILE = DATA_DIR + "settings.properties";

    // Default Cryptocurrencies
    public static final String[][] DEFAULT_CRYPTOS = {
            {"BTC-001", "Bitcoin", "BTC", "Currency", "46890.50"},
            {"ETH-002", "Ethereum", "ETH", "Platform", "2485.75"},
            {"BNB-003", "Binance Coin", "BNB", "Exchange", "312.40"},
            {"ADA-004", "Cardano", "ADA", "Platform", "0.52"},
            {"SOL-005", "Solana", "SOL", "Platform", "98.60"},
            {"XRP-006", "Ripple", "XRP", "Payment", "0.62"},
            {"DOT-007", "Polkadot", "DOT", "Platform", "7.85"},
            {"DOGE-008", "Dogecoin", "DOGE", "Meme", "0.15"},
            {"AVAX-009", "Avalanche", "AVAX", "Platform", "36.40"},
            {"LINK-010", "Chainlink", "LINK", "Oracle", "14.25"}
    };

    // Time Formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // Chart Constants
    public static final int CHART_WIDTH = 800;
    public static final int CHART_HEIGHT = 400;
    public static final int MAX_DATA_POINTS = 100;
}