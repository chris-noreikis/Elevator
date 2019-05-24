package configuration;

import models.InvalidValueException;

public class SimulationConfiguration {
    private static SimulationConfiguration instance;
    private static ConfigurationReader simulationConfiguration;

    public static SimulationConfiguration getInstance() throws ConfigurationException {
        if (instance == null) {
            simulationConfiguration = ConfigurationReaderFactory.build("json", "configuration/json/20_floors_4_elevators.json");
            instance = new SimulationConfiguration();
        }
        return instance;
    }

    public int getConfigurationField(String configurationFieldName) throws ConfigurationException {
        return simulationConfiguration.getConfigurationFieldInt(configurationFieldName);
    }
}
