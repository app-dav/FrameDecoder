package test;

import main.java.*;
import org.junit.Before;
import org.junit.Test;
import sun.awt.image.ImageWatched;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class MessageFrameDecoderTest {
    private MessageFrameDecoder decoder;

    @Before
    public void before() throws Exception {
        decoder = new MessageFrameDecoder();
    }

    @Test
    public void readBytes_ReturnsEmptyList_NullInput() {
        byte[] bytes = null;

        Collection<Message> messages = decoder.readBytes(bytes);
        Collection<Message> expected = new LinkedList<>();

        assertEquals(expected, messages);
    }

    @Test
    public void readBytes_ReturnsSinglePickMessage_SingleCompleteFrame() {
        byte[] bytes = new byte[] { 0x04, 0x01, 'f', 'o', 'o' };

        Collection<Message> messages = decoder.readBytes(bytes);
        Message expectedMessage = new Pick("foo");
        Collection<Message> expected = Collections.singletonList(expectedMessage);

        assertEquals(messages, expected);
    }

    @Test
    public void readBytes_ReturnsSinglePickMessage_PartialFramesCompleteFrame() {
        byte[] byteSequence1 = new byte[] { 0x04, 0x01 };
        byte[] byteSequence2 = new byte[] { 'f', 'o', 'o' };

        decoder.readBytes(byteSequence1);
        Collection<Message> messages = decoder.readBytes(byteSequence2);
        Message expectedMessage = new Pick("foo");
        Collection<Message> expected = Collections.singletonList(expectedMessage);

        assertEquals(messages, expected);
    }

    @Test
    public void readBytes_ReturnsSinglePickMessage_CompleteFrameWithOverflow() {
        byte[] bytes = new byte[] { 0x04, 0x01, 'f', 'o', 'o', 0x04, 0x01 };

        Collection<Message> messages = decoder.readBytes(bytes);
        Message expectedMessage = new Pick("foo");
        Collection<Message> expected = Collections.singletonList(expectedMessage);

        assertEquals(messages, expected);
    }

    @Test
    public void readBytes_ReturnsEmptyList_PartialFrameIncompleteFrame() {
        byte[] bytes = new byte[] { 0x04, 0x01 };

        Collection<Message> messages = decoder.readBytes(bytes);
        Collection<Message> expected = new LinkedList<>();

        assertEquals(messages, expected);
    }

    @Test
    public void readBytes_ReturnsListPickMessages_MultipleCompleteFrames() {
        byte[] bytes = new byte[] { 0x04, 0x01, 'f', 'o', 'o' , 0x05, 0x01, 't', 'e', 's', 't' };

        Collection<Message> messages = decoder.readBytes(bytes);
        Message expectedMessage1 = new Pick("foo");
        Message expectedMessage2 = new Pick("test");
        Collection<Message> expected = Arrays.asList(expectedMessage1, expectedMessage2);

        assertEquals(messages, expected);
    }

    @Test
    public void readBytes_ReturnsSingleDropMessage_SingleCompleteFrame() {
        byte[] bytes = new byte[] { 0x01, 0x02 };

        Collection<Message> messages = decoder.readBytes(bytes);
        Message expectedMessage = Drop.getInstance();
        Collection<Message> expected = Collections.singletonList(expectedMessage);

        assertEquals(messages, expected);
    }

    @Test
    public void readBytes_ReturnsSingleDropMessage_PartialFramesCompleteFrame() {
        byte[] byteSequence1 = new byte[] { 0x01 };
        byte[] byteSequence2 = new byte[] { 0x02 };

        decoder.readBytes(byteSequence1);
        Collection<Message> messages = decoder.readBytes(byteSequence2);
        Message expectedMessage = Drop.getInstance();
        Collection<Message> expected = Collections.singletonList(expectedMessage);

        assertEquals(messages, expected);
    }

    @Test
    public void readBytes_ReturnsListDropMessages_MultipleCompleteFrames() {
        byte[] bytes = new byte[] { 0x01, 0x02, 0x01, 0x02 };

        Collection<Message> messages = decoder.readBytes(bytes);
        Message expectedMessage1 = Drop.getInstance();
        Message expectedMessage2 = Drop.getInstance();
        Collection<Message> expected = Arrays.asList(expectedMessage1, expectedMessage2);

        assertEquals(messages, expected);
    }

    @Test
    public void readBytes_ReturnsInvalidMessage_UnknownMessageTypeFrames() {
        byte[] bytes = new byte[] { 0x04, 0x09, 'f', 'o', 'o' };

        Collection<Message> messages = decoder.readBytes(bytes);
        Message expectedMessage = InvalidMessage.getInstance();
        Collection<Message> expected = Collections.singletonList(expectedMessage);

        assertEquals(messages, expected);
    }

    @Test
    public void decodeBuffer_ReturnsPickMessageWithMessage() {
        byte[] buffer = { 0x01, 'y', 'o' };

        Message message = decoder.decodeBuffer(buffer);
        Message expectedMessage = new Pick("yo");

        assertEquals(expectedMessage, message);
    }

    @Test
    public void decodeBuffer_ReturnsDropMessage() {
        byte[] buffer = { 0x02 };

        Message message = decoder.decodeBuffer(buffer);
        Message expectedMessage = Drop.getInstance();

        assertEquals(expectedMessage, message);
    }

    @Test
    public void decodeBuffer_DecodesInvalidMessage_UnknownMessageType() {
        byte[] buffer = { 0x09 };

        Message message = decoder.decodeBuffer(buffer);
        Message expectedMessage = InvalidMessage.getInstance();

        assertEquals(expectedMessage, message);
    }

    @Test
    public void decodeBuffer_ReturnsNull_NullInput() {
        byte[] buffer = null;

        Message message = decoder.decodeBuffer(buffer);
        Message expectedMessage = null;

        assertEquals(expectedMessage, message);
    }
}