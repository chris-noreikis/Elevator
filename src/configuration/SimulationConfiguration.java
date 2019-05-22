package configuration;

import models.InvalidValueException;

public class SimulationConfiguration {
    private static SimulationConfiguration instance;
    private static SimulationDefinable simulationConfiguration = SimulationConfiguration.initialize();

    private static SimulationDefinable initialize() {
        try {
            return ElevatorConfigurationFactory.build("json", "configuration/json/20_floors_4_elevators.json");
        } catch (ConfigurationException ex) {
            System.out.println("Building created with invalid configuration, exiting program");
            ex.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    public static SimulationConfiguration getInstance() throws InvalidValueException {
        if (instance == null) {
            instance = new SimulationConfiguration();
        }
        return instance;
    }

    public int getNumberOfElevators() {
        return simulationConfiguration.getNumberOfElevators();
    }

    public int getNumberOfFloors() {
        return simulationConfiguration.getNumberOfFloors();
    }

    public int getElevatorCapacity() {
        return simulationConfiguration.getElevatorCapacity();
    }

    public int getElevatorSpeed() {
        return simulationConfiguration.getElevatorSpeed();
    }

    public int getDoorOpenTime() {
        return simulationConfiguration.getDoorOpenTime();
    }

    public int getReturnToDefaultFloorTimeout() {
        return simulationConfiguration.getReturnToDefaultFloorTimeout();
    }

    public int getPersonCreationRate() { return simulationConfiguration.getPersonCreationRate(); }

    public int getSimulationDuration() { return simulationConfiguration.getSimulationDuration(); }
}
