package main.java;

/**
 * A representation of an Invalid Message type.  Due to the drop message
 * type containing no state, this was implemented as a singleton object.
 */
public class InvalidMessage implements Message {
    /** The singleton instance of the InvalidMessage class */
    private static InvalidMessage instance = null;

    /**
     * Creates the singleton instance of the Drop message if it
     * is not initialized yet.  Otherwise, it will return instance
     * already created.
     * @return A singleton of the Drop message
     */
    public static InvalidMessage getInstance() {
        if (instance == null) {
            instance = new InvalidMessage();
        }

        return instance;
    }

    @Override
    public String toString() {
        return "Invalid Message Type";
    }
}