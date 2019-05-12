package configuration;

public interface BuildingConfigurable {
    public int getElevatorSpeed();

    public int getElevatorCapacity();

    public int getReturnToDefaultFloorTimeout();

    public int getDoorOpenTime();

    public int getNumberOfElevators();

    public int getNumberOfFloors();
}
