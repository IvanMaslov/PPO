package config;

import java.io.*;
import java.util.Properties;

public class Config {
    private final String applicationPropertyFile = "application.properties";

    private final String accessKey;
    private final String apiVersion;

    public Config() throws IOException {
        try (InputStream input = new FileInputStream(applicationPropertyFile)) {
            Properties prop = new Properties();
            prop.load(input);
            accessKey = prop.getProperty("access_token");
            apiVersion = prop.getProperty("api_version");
        } catch (IOException e) {
            throw new IOException("Can't read properties file", e);
        }
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getApiVersion() {
        return apiVersion;
    }
}
