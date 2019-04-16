package models;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class ElevatorJsonConfiguration implements BuildingConfigurable {
    private int elevatorSpeedInMilliseconds;
    private int elevatorCapacity;
    private int returnToFirstFloorAfter;
    private int numberOfElevators;
    private int doorOpenTime;
    private int numberOfFloors;
    private JSONObject configurationJSON;

    public ElevatorJsonConfiguration(String configurationFilePath) throws ConfigurationException {
        try {
            parseAndSetConfigurationJSON(configurationFilePath);
            elevatorSpeedInMilliseconds = getConfigurationFieldFromJSON("elevatorSpeedInMilliseconds");
            elevatorCapacity = getConfigurationFieldFromJSON("elevatorPersonCapacity");
            returnToFirstFloorAfter = getConfigurationFieldFromJSON("returnToFirstFloorAfter");
            numberOfElevators = getConfigurationFieldFromJSON("elevators");
            doorOpenTime = getConfigurationFieldFromJSON("doorOpenTime");
            numberOfFloors = getConfigurationFieldFromJSON("floors");
        } catch (IOException e) {
            throwFileNotFoundException(configurationFilePath);
        } catch (ParseException e) {
            throwInvalidJSONException();
        }
    }

    public int getElevatorSpeedInMilliseconds() {
        return elevatorSpeedInMilliseconds;
    }

    public int getElevatorCapacity() {
        return elevatorCapacity;
    }

    public int getReturnToFirstFloorAfter() {
        return returnToFirstFloorAfter;
    }

    public int getDoorOpenTime() {
        return doorOpenTime;
    }

    public int getNumberOfElevators() { return numberOfElevators; }

    public int getNumberOfFloors() { return numberOfFloors; }

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

    private int getConfigurationFieldFromJSON(String elevatorSpeedInMilliseconds) throws ConfigurationException{
        long defaultValue = 0;
        int configurationValue = (int) (long) configurationJSON.getOrDefault(elevatorSpeedInMilliseconds, defaultValue);
        if (configurationValue <= 0) {
            throwInvalidValueException(elevatorSpeedInMilliseconds, configurationValue);
        }
        return configurationValue;
    }
}