package SimulationConfiguration;

public interface BuildingConfigurable {
    public int getElevatorSpeedInMilliseconds();

    public int getElevatorCapacity();

    public int getReturnToDefaultFloorTimeout();

    public int getDoorOpenTime();

    public int getNumberOfElevators();

    public int getNumberOfFloors();
}
