package configuration;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JsonConfiguration implements SimulationDefinable {
    private int elevatorSpeed;
    private int elevatorCapacity;
    private int returnToDefaultFloorTimeout;
    private int numberOfElevators;
    private int doorOpenTime;
    private int numberOfFloors;
    private int simulationDuration;
    private int personCreationRate;
    private JSONObject configurationJSON;

    public JsonConfiguration(String configurationFilePath) throws ConfigurationException {
        try {
            parseAndSetConfigurationJSON(configurationFilePath);
            elevatorSpeed = getConfigurationFieldFromJSON("elevatorSpeed");
            elevatorCapacity = getConfigurationFieldFromJSON("elevatorPersonCapacity");
            returnToDefaultFloorTimeout = getConfigurationFieldFromJSON("returnToDefaultFloorTimeout");
            numberOfElevators = getConfigurationFieldFromJSON("elevators");
            doorOpenTime = getConfigurationFieldFromJSON("doorOpenTime");
            numberOfFloors = getConfigurationFieldFromJSON("floors");
            simulationDuration = getConfigurationFieldFromJSON("simulationDuration");
            personCreationRate = getConfigurationFieldFromJSON("personCreationRate");
        } catch (IOException e) {
            throwFileNotFoundException(configurationFilePath);
        } catch (ParseException e) {
            throwInvalidJSONException();
        }
    }

    public int getElevatorSpeed() {
        return elevatorSpeed;
    }

    public int getElevatorCapacity() {
        return elevatorCapacity;
    }

    public int getReturnToDefaultFloorTimeout() {
        return returnToDefaultFloorTimeout;
    }

    public int getDoorOpenTime() {
        return doorOpenTime;
    }

    public int getNumberOfElevators() { return numberOfElevators; }

    public int getNumberOfFloors() { return numberOfFloors; }

    public int getSimulationDuration() { return simulationDuration; }

    public int getPersonCreationRate() { return personCreationRate; }

    private void throwFileNotFoundException(String configurationFilePath) throws ConfigurationException {
        throw new ConfigurationException("Elevator configuration could not found found: " + configurationFilePath);
    }

    private void throwInvalidJSONException() throws ConfigurationException {
        throw new ConfigurationException("Elevator configuration could not found found: ");
    }

    private void throwInvalidValueException(String fieldName, int fieldValue) throws ConfigurationException {
        throw new ConfigurationException("Field \"" + fieldName + "\" was initialized with incorrect value: \"" + fieldValue + "\"");
    }

    private void parseAndSetConfigurationJSON(String configurationFilePath) throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(configurationFilePath));
        configurationJSON = (JSONObject) obj;
    }

    private int getConfigurationFieldFromJSON(String configurationFieldName) throws ConfigurationException {
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
}