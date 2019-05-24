package configuration;

public class ConfigurationReaderFactory {
    public static ConfigurationReader build(String type, String filepath) throws ConfigurationException {
        switch (type) {
            case "json":
                return new JsonReader(filepath);
            default:
                throw new ConfigurationException("Unknown reader type: " + type);
        }
    }
}
