//package server.armory;
//
//import java.io.*;
//import java.net.Socket;
//import java.nio.ByteBuffer;
//import java.nio.charset.Charset;
//import java.util.Scanner;
//
//public class DataExchangeWithClient {
//
//    private Socket incoming;
//
//    public DataExchangeWithClient (Socket incoming) {
//       this.incoming = incoming;
//    }
//
//
//    public void sendToClient (Object message)  {
//        try {
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream send = new ObjectOutputStream(baos);
//            send.writeObject(message);
//            byte[] outcoming = baos.toByteArray();
//            incoming.getOutputStream().write(outcoming);
//            send.flush();
//            baos.flush();
//        } catch (IOException e) {
//            e.printStackTrace( );
//        }
//    }
//
//    public Object getFromClient () {
//        try {
//            ObjectInputStream get = new ObjectInputStream(incoming.getInputStream());
//            return  get.readObject();
//        } catch (IOException e) {
//            e.printStackTrace( );
//            return null;
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace( );
//            return null;
//        }
//    }
//
//
//
//}
