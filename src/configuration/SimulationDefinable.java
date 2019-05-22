package configuration;

public interface SimulationDefinable {
    public int getElevatorSpeed();

    public int getElevatorCapacity();

    public int getReturnToDefaultFloorTimeout();

    public int getDoorOpenTime();

    public int getNumberOfElevators();

    public int getNumberOfFloors();

    public int getPersonCreationRate();

    public int getSimulationDuration();
}
