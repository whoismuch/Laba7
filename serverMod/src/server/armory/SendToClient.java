package server.armory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SendToClient {

    private Socket incoming;

    public SendToClient (Socket incoming) {
        this.incoming = incoming;
    }

    public void send(Object message)  {
        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream send = new ObjectOutputStream(baos);
            send.writeObject(message);
            byte[] outcoming = baos.toByteArray();
            incoming.getOutputStream().write(outcoming);
            send.flush();
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace( );
        }
    }
}
