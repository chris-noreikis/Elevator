package models;

public class PendingRequestProcessorFactory {
    public static PendingRequestProcessor build() {
        return new PendingRequestProcessorImpl();
    }
}
