package main.java;

import javax.naming.SizeLimitExceededException;

/**
 * A data structure with the intention of providing list like abstractions
 * for the byte primitive data type and capacity metric.
 */
public class BufferList implements Buffer {

    /** Underlying array structure responsible for storing bytes. */
    private final byte[] buffer;

    /** Pointer to the address of the next available memory slot. */
    private byte currentIndex;

    /** Indication of when the buffer has reached capacity */
    private boolean isFull;

    public BufferList(byte size) {
        buffer = new byte[size];
        currentIndex = 0;
        isFull = false;
    }

    /**
     * Adds the byte to first available index indicated by the currentIndex.
     * @param b The byte to be added to the buffer.
     *
     * Throws SizeLimitExceededException if add is invoked while the buffer is full.
     */
    public void add(byte b) throws SizeLimitExceededException {
        if (!isFull) {
            buffer[currentIndex] = b;
            currentIndex++;
            isFull = currentIndex == buffer.length;
        } else {
            throw new SizeLimitExceededException("The buffer has reached capacity and can no longer add elements");
        }
    }

    /**
     * Gets a copy of the contents of the buffer.
     * @return An array representation of the bytes contained in the buffer.
     */
    public byte[] getBuffer() {
        return buffer.clone();
    }

    /**
     * Gets the status of whether the buffer is full or not.
     * @return A boolean indicating if the buffer is full or not.
     */
    public boolean isFull() {
        return isFull;
    }
}