package configuration;

public interface ConfigurationReader {
    int getConfigurationFieldInt(String configurationFieldName) throws ConfigurationException;
}
