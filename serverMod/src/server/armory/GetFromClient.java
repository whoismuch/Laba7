package server.armory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Callable;

public class GetFromClient implements Callable {

    private Socket incoming;
    Object obj;

    public GetFromClient (Socket incoming) {
        this.incoming = incoming;
    }

    public Object call () {
        try {
            ObjectInputStream get = new ObjectInputStream(incoming.getInputStream());
            obj = get.readObject();
            return  obj;
        } catch (EOFException e) {
            System.out.println("Клиент решил внезапно покинуть нас");
            return obj;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace( );
            return null;
        }
    }

}