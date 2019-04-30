package  espresso.classfile.util;




import  java.io.*;




/**
 * Utility class that implements a sequence of bytes which can be read
 * via the `readByte()' method. This is used to implement a wrapper for the 
 * Java byte code stream to gain some more readability.
 *
 * @version 980120
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ByteSequence
    extends DataInputStream
{

    private ByteArrayStream byte_stream;


    public ByteSequence (byte[] bytes) {
        super(new ByteArrayStream(bytes));
        byte_stream =  (ByteArrayStream)in;
    }


    byte getByte ()
        throws IOException
    {
        return  readByte();
    } // Deprecated


    public final int getIndex () {
        return  byte_stream.getPosition();
    }

}




/**
 * Can't be implemented as an inner class, unfortunately.
 */
class ByteArrayStream
    extends ByteArrayInputStream
{

    ByteArrayStream (byte[] bytes) {
        super(bytes);
    }


    final int getPosition () {
        return  pos;
    } // is protected in ByteArrayInputStream

}

