package models;

import java.util.ArrayList;

import gui.ElevatorDisplay;

public class Building {
    private ArrayList<Floor> floors;
    private ArrayList<Elevator> elevators;
    public static boolean isUnitTest;

    public Building(String configurationFilePath) {
        try {
            BuildingConfigurable jo = new ElevatorJsonConfiguration(configurationFilePath);

            if (!isUnitTest) {
                ElevatorDisplay.getInstance().initialize(jo.getNumberOfFloors());
            }

            setupFloors(jo);
            setupElevators(jo);
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

    private void setupElevators(BuildingConfigurable buildingConfiguration) {
        elevators = new ArrayList<>();
        int numberOfElevators = buildingConfiguration.getNumberOfElevators();
        int capacity = buildingConfiguration.getElevatorCapacity();
        int elevatorSpeed = buildingConfiguration.getElevatorSpeedInMilliseconds();
        int doorOpenTime = buildingConfiguration.getDoorOpenTime();
        int returnToFirstFloor = buildingConfiguration.getReturnToFirstFloorAfter();
        for (int elevatorID = 1; elevatorID <= numberOfElevators; elevatorID++) {
            elevators.add(new Elevator(elevatorID, this, capacity, elevatorSpeed, doorOpenTime, returnToFirstFloor));
        }
    }

    public int getNumberOfFloors() {
        return floors.size();
    }

    public Floor getFloor(int floorNumberZeroIndexed) {
        return floors.get(floorNumberZeroIndexed);
    }

    public int getNumberOfElevators() {
        return elevators.size();
    }

    public Elevator getElevator(int id) {
        return elevators.get(id - 1);
    }
}
