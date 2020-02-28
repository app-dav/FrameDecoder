package main.java;

import javax.naming.SizeLimitExceededException;

public interface Buffer {
    void add(byte b) throws SizeLimitExceededException;
    byte[] getBuffer();
    boolean isFull();
}
