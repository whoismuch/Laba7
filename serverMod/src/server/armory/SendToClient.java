package server.armory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class SendToClient implements Runnable {

    private Socket incoming;
    private Object message;

    public SendToClient (Socket incoming) {
        this.incoming = incoming;
    }

    public void run()  {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream send = new ObjectOutputStream(baos);
            send.writeObject(message);
            byte[] outcoming = baos.toByteArray();
            incoming.getOutputStream().write(outcoming);
            send.flush();
            baos.flush();

        } catch (IOException ex) {
            System.out.println("Клиент решил внезапно покинуть нас" );
        }
    }

    public void setMessage (Object message) {
        this.message = message;
    }
}
