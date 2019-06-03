package configuration;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JsonReader implements ConfigurationReader {
    private JSONObject configurationJSON;

    public JsonReader(String configurationFilePath) throws ConfigurationException {
        if (configurationFilePath == null) {
            throw new ConfigurationException("Configuration File cannot be null");
        }
        try {
            parseAndSetConfigurationJSON(configurationFilePath);
        } catch (IOException e) {
            throwFileNotFoundException(configurationFilePath);
        } catch (ParseException e) {
            throwInvalidJSONException();
        }
    }

    public int getConfigurationFieldInt(String configurationFieldName) throws ConfigurationException {
        int configurationValue;
        try {
            configurationValue = (int) (long) configurationJSON.get(configurationFieldName);
        } catch (Exception e) {
            throw new ConfigurationException("Could not parse configuration field: " + configurationFieldName);
        }

        if (configurationValue <= 0) {
            throwInvalidValueException(configurationFieldName, configurationValue);
        }

        return configurationValue;
    }

    private void throwFileNotFoundException(String configurationFilePath) throws ConfigurationException {
        throw new ConfigurationException("Elevator configuration file could not be found: " + configurationFilePath);
    }

    private void throwInvalidJSONException() throws ConfigurationException {
        throw new ConfigurationException("Elevator configuration could not be parsed: ");
    }

    private void throwInvalidValueException(String fieldName, int fieldValue) throws ConfigurationException {
        throw new ConfigurationException("Field \"" + fieldName + "\" was initialized with incorrect value: \"" + fieldValue + "\"");
    }

    private void parseAndSetConfigurationJSON(String configurationFilePath) throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(configurationFilePath));
        configurationJSON = (JSONObject) obj;
    }
}