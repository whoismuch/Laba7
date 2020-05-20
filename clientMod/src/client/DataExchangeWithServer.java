package client;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DataExchangeWithServer {
    private SocketChannel outcomingchannel;


    public DataExchangeWithServer (SocketChannel outcomingchannel) {
        this.outcomingchannel = outcomingchannel;
    }

    public void sendToServer (Object object) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream( );
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);

        byte[] outcoming = baos.toByteArray( );

        ByteBuffer byteBuffer = ByteBuffer.wrap(outcoming);

        while ((outcomingchannel.write(byteBuffer)) > 0 ) ;


        byteBuffer.clear();
        baos.flush( );
        oos.flush();
    }


    public Object getFromServer ( ) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream( );
            ByteBuffer byteBuffer = ByteBuffer.allocate(5000);
            int n = 0;
            while ((n = outcomingchannel.read(byteBuffer)) > 0) {
                byteBuffer.flip( );
                baos.write(byteBuffer.array( ), 0, n);
            }
            ByteArrayInputStream bios = new ByteArrayInputStream(baos.toByteArray( ));
            ObjectInputStream ois = new ObjectInputStream(bios);
            Object o = ois.readObject();
            return o;
        } catch (ClassNotFoundException e) {
            System.out.println( );
            e.printStackTrace( );
            return null;
        } catch (EOFException e) {
            throw new IOException( );
        }
    }
}
