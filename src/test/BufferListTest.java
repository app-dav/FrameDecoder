package test;

import main.java.BufferList;
import org.junit.Test;

import javax.naming.SizeLimitExceededException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class BufferListTest {

    @Test
    public void add_AppendsExpectedElements() {
        byte[] bytes = {0x01, 0x02, 0x03 };
        BufferList buffer = new BufferList((byte) 3);

        for (byte b : bytes) {
            try {
                buffer.add(b);
            } catch (SizeLimitExceededException e) {
                e.printStackTrace();
            }
        }

        byte expected1 = 0x01;
        byte expected2 = 0x02;
        byte expected3 = 0x03;

        assertEquals(expected1, buffer.getBuffer()[0]);
        assertEquals(expected2, buffer.getBuffer()[1]);
        assertEquals(expected3, buffer.getBuffer()[2]);
    }

    @Test(expected = SizeLimitExceededException.class)
    public void add_ThrowsSizeLimitExceededException_IfBufferFull() throws SizeLimitExceededException {
        byte[] bytes = { 0x01, 0x02, 0x03 };
        BufferList buffer = new BufferList((byte) 2);

        for (byte b : bytes) {
            buffer.add(b);
        }
    }

    @Test
    public void isFull_ReturnsTrue_BufferIsFull() {
        byte[] bytes = { 0x01, 0x01, 0x01 };
        BufferList buffer = new BufferList((byte) 3);

        for (byte b : bytes) {
            try {
                buffer.add(b);
            } catch (SizeLimitExceededException e) {
                e.printStackTrace();
            }
        }

        assertTrue(buffer.isFull());
    }

    @Test
    public void isFull_ReturnsFalse_BufferIsNotFull() {
        byte[] bytes = { 0x01, 0x01 };
        BufferList buffer = new BufferList((byte) 3);

        for (byte b : bytes) {
            try {
                buffer.add(b);
            } catch (SizeLimitExceededException e) {
                e.printStackTrace();
            }
        }

        assertFalse(buffer.isFull());
    }

    @Test
    public void getBuffer_ReturnsCopy() {
        byte [] bytes = { 0x01, 0x01 };
        BufferList buffer = new BufferList((byte) 2);

        for (byte b : bytes) {
            try {
                buffer.add(b);
            } catch (SizeLimitExceededException e) {
                e.printStackTrace();
            }
        }

        buffer.getBuffer()[0] = 0x02;
        byte[] expected = bytes;

        assertArrayEquals(expected, buffer.getBuffer());
    }
}