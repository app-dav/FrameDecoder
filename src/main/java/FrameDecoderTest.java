package main.java;

// Run with: $ javac *.java && java FrameDecoderTest

import javax.naming.SizeLimitExceededException;
import java.util.*;

public class FrameDecoderTest {

    public static class TestData {
        public byte[] bytes;
        public Collection<Message> expectedOutput;

        public TestData(byte[] bytes, Collection<Message> output) {
            this.bytes = bytes;
            this.expectedOutput = output;
        }
    };

    public static TestData[] Tests = new TestData[] {
            // 1) Both messages arrive in one single chunk
            new TestData(new byte[] { 0x04, 0x01, 'f', 'o', 'o' }, Collections.singletonList(new Pick("foo"))/* Single Pick message, target = foo */),
            new TestData(new byte[] { 0x01, 0x02 }, Collections.singletonList(Drop.getInstance())/* Single Drop message */),

            // 2) A Pick messages arrives in two chunks
            new TestData(new byte[] { 0x03, 0x01 }, new LinkedList<>()/* Nothing */),
            new TestData(new byte[] { 'm', 'e' }, Collections.singletonList(new Pick("me"))/* Single pick message, target = me */),

            // 3) An empty byte array
            new TestData(new byte[] {}, new LinkedList<>()/* Nothing */),

            // 4) Two Drop messages arrive at once
            new TestData(new byte[] { 0x01, 0x02, 0x01, 0x02 }, new LinkedList<>(Arrays.asList(Drop.getInstance(), Drop.getInstance()))/* Two Drop messages */),

            // 5) Additional tests:
            //a) An invalid message type arrives
            new TestData(new byte[] { 0x04, 0x09, 'm', 'o', 'o' }, Collections.singletonList(InvalidMessage.getInstance())),
            //b) Null input
            new TestData(null, new LinkedList<>())
    };

    public static void main(String[] args) {

        FrameDecoder decoder = new MessageFrameDecoder();
        int numFailures = 0;

        System.out.println("Running tests");

        for (TestData data : Tests) {
            Collection<Message> result = null;
            try {
                result = decoder.readBytes(data.bytes);
            } catch (SizeLimitExceededException e) {
                System.out.println(
                        "Test failed. Exception: " + e.getMessage());
                numFailures++;
            }
            if (!(result).equals(data.expectedOutput)) {
                System.out.println(
                        "Test failed. Expected " + data.expectedOutput +
                                " but found " + result + ".");
                numFailures++;
            }
        }

        if (numFailures > 0) {
            System.out.println(numFailures + " tests failed.");
            System.exit(1);
        } else {
            System.out.println("Tests ran successfully");
        }
    }
}