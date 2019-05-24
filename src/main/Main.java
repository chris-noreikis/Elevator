
package main;

import configuration.ConfigurationException;
import models.InvalidValueException;

/**
 * @author prayers
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ElevatorTestDriver d = new ElevatorTestDriver();
        try {
            d.runTests();
        } catch (InvalidValueException | ConfigurationException e) {
            e.printStackTrace();
        }
    }
}
