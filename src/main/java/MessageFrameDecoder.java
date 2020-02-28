package main.java;

import javax.naming.SizeLimitExceededException;
import java.util.LinkedList;

/**
 * Represents a message frame decoder that receives a stream of bytes
 * and decodes the completed byte stream into a message.
 */
public class MessageFrameDecoder implements FrameDecoder {
    /**
     * The internal storage mechanism for storing bytes over time.
     */
    private Buffer buffer = null;

    /**
     * Known types that messages can be decoded into.
     * Pick = 0x01
     * Drop = 0x02
     */
    private static final byte PICK = 0x01;
    private static final byte DROP = 0x02;

    /**
     * Reads a stream of bytes and processes into the buffer.  When
     * buffer reaches capacity, the complete buffer is decoded into
     * a message.
     *
     * @param bytes the input bytes containing zero or more frames.
     *
     * @return List of decoded messages or an empty List if no messages
     * were decoded.
     */
    public LinkedList<Message> readBytes(byte[] bytes) {
        LinkedList<Message> result = new LinkedList<>();
        if (bytes != null) {
            for (byte b : bytes) {
                if (buffer == null) {
                    initializeBuffer(b);
                } else {
                    try {
                        buffer.add(b);
                    } catch (SizeLimitExceededException e) {
                        e.printStackTrace();
                    }

                    if (buffer.isFull()) {
                        result.add(decodeBuffer(buffer.getBuffer()));
                        buffer = null;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Internal helper method that abstracts the initialization
     * of the buffer.
     *
     * @param size indicates initial size of array
     */
    private void initializeBuffer(byte size) {
        buffer = new BufferList(size);
    }

    /**
     * Helper method decodes the completed byte stream
     * into the appropriate message.
     *
     * Additional message types will need to be added to this method
     * to be decoded.
     *
     * @param bytes byte array to be decoded
     * @return decoded Message or an InvalidMessage indicating unknown message type or null if input null.
     */
    public Message decodeBuffer(byte[] bytes) {
        Message result = null;
        if (bytes != null) {
            byte messageType = bytes[0];

            switch (messageType) {
                case PICK:
                    String messageBody = new String(bytes, 1, bytes.length - 1);
                    result = new Pick(messageBody);
                    break;
                case DROP:
                    result = Drop.getInstance();
                    break;
                default:
                    result = InvalidMessage.getInstance();
                    break;
            }
        }

        return result;
    }
}