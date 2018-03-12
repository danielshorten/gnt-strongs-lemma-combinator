package combinator;

import combinator.util.PropertiesLoader;

import java.util.Properties;

public class Config {

    private static Properties config;

    public static void init() throws Exception {
        if (config == null) {
            config = PropertiesLoader.loadProperties("config.properties");
        }
        else {
            throw new Exception("Config is already initialized.");
        }
    }

    public static Object get(String key) {
        return config.get(key);
    }
}
