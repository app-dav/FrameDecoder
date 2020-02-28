package main.java;

/**
 * A representation of the Drop Message type.  Due to the drop message
 * type containing no state, this was implemented as a singleton object.
 */
public class Drop implements Message {
    /** The singleton instance of the Drop class */
    private static Drop instance = null;

    /**
     * Creates the singleton instance of the Drop message if it
     * is not initialized yet.  Otherwise, it will return instance
     * already created.
     * @return A singleton of the Drop message
     */
    public static Drop getInstance() {
        if (instance == null) {
            instance = new Drop();
        }

        return instance;
    }

    @Override
    public String toString() {
        return "Drop";
    }
}