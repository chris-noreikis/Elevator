package configuration;

public interface ConfigurationReader {
    public int getConfigurationFieldInt(String configurationFieldName) throws ConfigurationException;
}
