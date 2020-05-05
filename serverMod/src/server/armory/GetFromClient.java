package server.armory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class GetFromClient {

    private Socket incoming;

    public GetFromClient (Socket incoming) {
        this.incoming = incoming;
    }

    public Object get () {
        try {
            ObjectInputStream get = new ObjectInputStream(incoming.getInputStream());
//            System.out.println(1);
            Object obj = get.readObject();
//            System.out.println(obj);
//            System.out.println(2);
            return  obj;
        } catch (EOFException e) {
            System.out.println("Клиент решил внезапно покинуть нас");
            return null;
        } catch (IOException e) {
            e.printStackTrace( );
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace( );
            return null;
        }
    }
}