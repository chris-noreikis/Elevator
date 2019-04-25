package models;

import java.util.ArrayList;

import SimulationConfiguration.BuildingConfigurable;
import SimulationConfiguration.ConfigurationException;
import SimulationConfiguration.ElevatorConfigurationFactory;
import gui.ElevatorDisplay;

public class Building {
    private ArrayList<Floor> floors;
    private static Building instance;
    private static BuildingConfigurable buildingConfiguration = Building.initialize();


    private static BuildingConfigurable initialize() {
        try {
            return ElevatorConfigurationFactory.build("json", "configuration/json/20_floors_4_elevators.json");
        } catch (ConfigurationException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    public static Building getInstance() {
        if (instance == null) {
            instance = new Building();
        }
        return instance;
    }

    private Building() {
        try {
            ElevatorDisplay.getInstance().initialize(buildingConfiguration.getNumberOfFloors());

            setupFloors();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void setupFloors() {
        int numberOfFloors = buildingConfiguration.getNumberOfFloors();
        floors = new ArrayList<>();
        for (int i = 0; i < numberOfFloors; i++) {
            floors.add(new Floor(i + 1));
        }
    }

    public int getNumberOfFloors() {
        return floors.size();
    }

    public Floor getFloor(int floorNumberZeroIndexed) {
        return floors.get(floorNumberZeroIndexed);
    }

    public int getNumElevators() {
        return buildingConfiguration.getNumberOfElevators();
    }

    public void addPerson(Person p, int floorNum) {
        getFloor(floorNum - 1).addWaitingPerson(p);
    }

    public String toString() {
        String output = "";
        output += "Building Report ...\n";

        for (Floor f : floors) {
            output += f.toString();
        }

        return output;
    }
}
