package models;

import gui.ElevatorDisplay;
import org.junit.jupiter.api.*;
import gui.ElevatorDisplay.Direction;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {
    private int personCounter = 0;
    private ArrayList<Person> people;
    private Elevator elevator;

    @BeforeAll
    static void setupBuilding() {
        Building.getInstance();
        ElevatorController.getInstance();
    }

    @BeforeEach
    void resetState() {
        people = new ArrayList<>();
        elevator = ElevatorController.getInstance().getElevator(1);
        elevator.resetState();
    }


    @Test
    @DisplayName("Test case 1 - Doors open")
    void opensDoorsForPassengerOnFirstFloor() {
        addPerson(1, 10, 1);
        assertEquals(elevator.getFloorRequests().get(0), new ElevatorRequest(Direction.UP, 1));
        moveElevatorNumTimes(1);
        assertEquals(true, elevator.getIsDoorOpen());
        moveElevatorNumTimes(1);
        assertEquals(true, elevator.getIsDoorOpen());
        moveElevatorNumTimes(1);
        assertEquals(false, elevator.getIsDoorOpen());
        assertEquals(2, elevator.getCurrentFloor());
        moveElevatorNumTimes(1);
        ElevatorController.getInstance().moveElevators(2000);
        assertEquals(false, elevator.getIsDoorOpen());
    }

    @Test
    @DisplayName("Test case 1 - Rider can enter request")
    void enterRiderRequestForPassenger() {
        addPerson(1, 10, 1);
        ElevatorController.getInstance().moveElevators(1000);
        assertEquals(new ElevatorRequest(Direction.UP, 10), elevator.getRiderRequests().get(0));
    }

    @Test
    @DisplayName("Test case 1 - Rider can be moved to 10th floor")
    void takesPassengerToTenthFloor() {
        addPerson(1, 10, 1);

        // Trigger person pickup
        ElevatorController.getInstance().moveElevators(1000);

        // Triger elevator door close
        ElevatorController.getInstance().moveElevators(2000);

        // Move elevator up to tenth floor
        for (int i = 0; i < 11; i++) {
            ElevatorController.getInstance().moveElevators(1000);
        }
        assertEquals(true, elevator.getIsDoorOpen());
    }

    @Test
    @DisplayName("Test case 1 - Elevator goes back to first floor after reset period")
    void idleTest1() {
        addPerson(1, 10, 1);
        // Doors open, passenger enters
        moveElevatorNumTimes(1);

        // Doors close
        moveElevatorNumTimes(2);

        // Up to Floor 10
        moveElevatorNumTimes(10);

        // Doors close
        moveElevatorNumTimes(2);

        // Wait for idle time
        moveElevatorNumTimes(5);
        assertEquals(9, elevator.getCurrentFloor());
        assertEquals(Direction.DOWN, elevator.getElevatorDirection());
        moveElevatorNumTimes(12);
        assertEquals(1, elevator.getCurrentFloor());
        assertEquals(Direction.IDLE, elevator.getElevatorDirection());
    }

    private void moveElevatorNumTimes(int numTimes) {
        for (int i = 0; i < numTimes; i++) {
            ElevatorController.getInstance().moveElevators(1000);
        }
    }

    private void addPerson(int start, int end, int elevId) {
        ElevatorDisplay.Direction d = end > start ? ElevatorDisplay.Direction.UP : ElevatorDisplay.Direction.DOWN;
        Person p = new Person(start, end,"P" + personCounter++);
        people.add(p);
        Building.getInstance().addPerson(p, start);
        ElevatorController.getInstance().getElevator(elevId).addFloorRequest(new ElevatorRequest(d, start));
        System.out.println("Person " + p.getId() + " pressed " + d + " on Floor " + start);
    }
}