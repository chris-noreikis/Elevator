package models;

public class FloorRequestAssignerFactory {
    public static FloorRequestAssigner build() {
        return new FloorRequestAssignerImpl();
    }
}
