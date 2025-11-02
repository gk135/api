package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ApiConfig.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getApiKey() {
        String apiKey = properties.getProperty("api.key");

        if (apiKey == null || apiKey.trim().isEmpty()) {
            apiKey = System.getenv("API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                throw new IllegalStateException("API_KEY not configured in config.properties or environment variable!");
            }
        }
        return apiKey;
    }
}
