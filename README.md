## MessageFrameDecoder

FrameDecoder is a tool to decode protocol byte streams into messages.

## Usage
The FrameDecoder is a library that consumes byte streams and processes them into a specified message.  To use, include the dependency and in your modules and import in the the source files you would like to use.  

To use the message frame decoder, simply instantiate one and start passing byte arrays to the readBytes method.  As output, you'll receive the corresponding messages.  

```java
byte[] byteStream = new byte[] { 0x04, 0x01, 'f', 'o', 'o' }

FrameDecoder decoder = new MessageFrameDecoder();

Collection<Message> messages = decoder.readBytes(byteStream);
```

I packaged the app using IntelliJ IDEA and referenced the following docs: https://www.jetbrains.com/help/idea/packaging-a-module-into-a-jar-file.html

In the referenced instructions, the Main Class was set to main.java.FrameDecoderTest and the CLASSPATH was left to the default value.

After you have created a jar, you can either run from a context menu within IntelliJ or run 
```
>   java -jar FrameDecoder.jar
```
from the command line.

Simply running the FrameDecoder.jar will run the test scenarios created in the instructions with the addition of a couple of tests I added.  

If you want to add additional tests or experiment with the decoder, you may load the source in your IDE of choice and run from there.  The file in which the main method lives is the FrameDecoderTest file.    

Ensure that you have Java 1.8 installed to run the program.

## Running Tests

There are 19 tests written in 2 different test classes for this project.  Testing was done with "junit:junit:4.12"

To run the test, load the source code into your corresponding IDE.  Ensure that JUnit is available and tests can be run from the src/test directory.  

## Design Decisions
1. Message Frame Decoder
-------------------------
The frame decoder can be broken into two key functions: (1.) reading a stream of bytes into a buffer, and (2.) decoding a buffer once it has been filled.  

The design behind reading bytes into the  buffer is directly related to the specification.  Once a FrameLength is designated, the decoder initializes a buffer with the exact size requirement.  As the following MessageType and Body bytes are streamed as input, they are processed into the buffer.  Once a buffer is full, this indicates the message is complete and can be decoded.  

To decode a message, a full buffer is needed.  The buffer should only contain the message type and body of the message if available.  Message types are decoded differently based type and if an unknown message type is processed then a corresponding invalid message is processed.  

2. Buffer
-------------------------
The buffer is lightweight data structure responsible for storing a stream of bytes at a particular moment.  The implementation provides List like append functionality while being able to wrap a primitive byte.    

The choice to build the buffer on top of an array was to avoid unnecessary operations when storing the primitive byte.  A list consumes more memory as pointers are needed for each additional element.  In contrast, our requirements limit our need to a single pointer of where to insert the next byte within the buffer.  

The protocol specifies that the first frame will indicate the size of the incoming message.  The nature of this specification makes a fixed size array a suitable solution as we consume no more or less memory than needed. 

In addition, this implementation avoids unnecessary processes as it relates to boxing as Lists rely on object wrappers.

3. Error Handling
-------------------------
Validation is performed on null input.  Given the nature specification, partial frames and empty frames should be expected flow and the frame decoder has been designed accordingly.  

The buffer reports an exception if attempting to insert a byte while the buffer is at capacity.  

## Opportunities for Improvement
With this specification there are few areas I would further explore to improve.

1. Message Frame Decoder
-------------------------
The current design relies on frames being received in an expected order that aligns to the specification.  If frames didn't not align to the specification, with the current design, the decoder would not be able to recover as bytes would then be received out of order.  

Currently to extend the protocol, one would have to implement the message and update the decode message function in the message frame decoder.  I believe different message implementations should be able to be registered with a type and a corresponding decoding process to eliminate the need to update the message frame decoder making this library more maintainable and extendable.  

2. Testing
-------------------------
I was not able to figure out how package, build and run the tests via the command line.  I am aware this available and this can be improved by providing that functionality.  

3. Running the Decoder
-------------------------
It could be helpful to test the decoder against variable input via command line tool to.  Providing this functionality would expedite the integration process by allowing quicker testing.

4. .gitignore File
-------------------------
Adding a .gitignore file to ignore unnecessary modifications, such as build outputs, would reduce clutter.  
