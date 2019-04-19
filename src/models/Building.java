package models;

import java.util.ArrayList;

import gui.ElevatorDisplay;
import reports.ActionReport;

public class Building {
    private ArrayList<Floor> floors;
    private int numElevators;
    private static Building instance;

    public static Building getInstance()
    {
        if (instance == null) {
            String configurationFilePath = "./configuration/json/20_floors_4_elevators.json";
            return new Building(configurationFilePath);
        }
        return instance;
    }


    private Building(String configurationFilePath) {
        try {
            BuildingConfigurable jo = new ElevatorJsonConfiguration(configurationFilePath);

            ElevatorDisplay.getInstance().initialize(jo.getNumberOfFloors());

            setupFloors(jo);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void setupFloors(BuildingConfigurable buildingConfiguration) {
        int numberOfFloors = buildingConfiguration.getNumberOfFloors();
        floors = new ArrayList<>();
        for (int i = 0; i < numberOfFloors; i++) {
            floors.add(new Floor(i + 1));
        }
    }

    private void setNumElevators(int numElevators) { this.numElevators = numElevators; }

    public int getNumberOfFloors() {
        return floors.size();
    }

    public Floor getFloor(int floorNumberZeroIndexed) {
        return floors.get(floorNumberZeroIndexed);
    }

    public int getNumberOfElevators() { return numElevators; }
}
