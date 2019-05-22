package models;

import configuration.SimulationConfiguration;
import gui.ElevatorDisplay.Direction;

public class FloorRequestAssignerImpl implements FloorRequestAssigner {
    public void addElevatorRequest(ElevatorRequest elevatorRequest, Person person) throws InvalidValueException {
        ElevatorLogger.getInstance().logAction("Person " + person.getId() + " presses " + elevatorRequest.getDirection() + " button on Floor " + elevatorRequest.getFloorNumber());

        int requestFloor = elevatorRequest.getFloorNumber();
        Direction requestDirection = elevatorRequest.getDirection();

        int numElevators = SimulationConfiguration.getInstance().getNumberOfElevators();

        for (int i = 1; i <= numElevators; i++) {
            Elevator e = ElevatorController.getInstance().getElevator(i);
            if (e.getCurrentFloor() == requestFloor && (e.getElevatorDirection() == requestDirection || e.getElevatorDirection() == Direction.IDLE)) {
                e.addFloorRequest(elevatorRequest);
                return;
            }
        }

        for (int i = 1; i <= numElevators; i++) {
            Elevator e = ElevatorController.getInstance().getElevator(i);
            if (e.getElevatorDirection() != Direction.IDLE && e.isInDesiredDirection(requestFloor, requestDirection)) {
                e.addFloorRequest(elevatorRequest);
                return;
            }
        }

        for (int i = 1; i <= numElevators; i++) {
            Elevator e = ElevatorController.getInstance().getElevator(i);
            if (e.getElevatorDirection() == Direction.IDLE) {
                e.addFloorRequest(elevatorRequest);
                return;
            }
        }

        ElevatorController.getInstance().addPendingRequest(elevatorRequest);
    }
}
