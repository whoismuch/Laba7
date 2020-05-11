package server.armory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class GetFromClient {

    private Socket incoming;
    Object obj;

    public GetFromClient (Socket incoming) {
        this.incoming = incoming;
    }

    public Object get () {
        try {
            ObjectInputStream get = new ObjectInputStream(incoming.getInputStream());
            obj = get.readObject();
            return  obj;
        } catch (EOFException e) {
            System.out.println("Клиент решил внезапно покинуть нас");
            return obj;
        } catch (IOException e) {
            e.printStackTrace( );
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace( );
            return null;
        }
    }

}