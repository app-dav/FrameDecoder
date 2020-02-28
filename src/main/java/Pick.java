package main.java;

/**
 * A representation of the Pick Message type.
 */
public class Pick implements Message {

    /** A string representing the associated message */
    private String message;

    public Pick(String msg) {
        message = msg;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Pick)) {
            return false;
        }

        Pick p = (Pick) o;

        return this.message.equals(p.message);
    }

    @Override
    public String toString() {
        return "Pick(" + message + ")";
    }
}