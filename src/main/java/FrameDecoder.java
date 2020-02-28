package main.java;

import javax.naming.SizeLimitExceededException;
import java.util.Collection;

public interface FrameDecoder {
    /**
     * Returns zero or more fully buffered messages by reading `bytes` as they
     * arrive and decoding any complete frames.
     *
     * @param bytes the input bytes containing zero or more frames.
     *
     * @return zero or more decoded frames.
     */
    Collection<Message> readBytes(byte[] bytes) throws SizeLimitExceededException;
}