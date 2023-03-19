package Utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class BinaryWriter {

    private List<Byte> buffer;

    public BinaryWriter() {
        buffer = new ArrayList<Byte>();
    }

    public BinaryWriter(int size) {
        buffer = new ArrayList<Byte>(size);
    }

    public byte[] getBuffer (){
        Byte[] array = new Byte[buffer.size()];
        buffer.toArray(array);
        byte[] result = new byte[buffer.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public void write (byte data){
        buffer.add(data);
    }

    public void write (byte[] data){
        for (byte datum : data) {
            buffer.add(datum);
        }
    }

    public void write (int data){
        byte[] b = ByteBuffer.allocate(4).putInt(data).array();
        write(b);
    }


}
