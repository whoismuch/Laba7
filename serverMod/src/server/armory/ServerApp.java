package server.armory;

import server.receiver.collection.Navigator;
import server.receiver.collection.RouteBook;

import java.io.IOException;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp {

    static ExecutorService executeIt = Executors.newFixedThreadPool(2);

    public static void main (String[] args) {

        Runtime.getRuntime( ).addShutdownHook(new Thread(( ) -> {
            executeIt.shutdown( );
        }));

        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите порт: ");

            int port = Integer.parseInt(scanner.nextLine( ));
            SocketAddress address = new InetSocketAddress(port);

            DataBase db = new DataBase();

            System.out.print("Сервер начал слушать клиента " + "\nПорт " + port +
                    " / Адрес " + InetAddress.getLocalHost( ) + ".\nОжидаем подключения клиента\n ");
            while (true) {
                try (ServerSocketChannel ss = ServerSocketChannel.open( )) {
                    ss.bind(address);

                    Socket incoming = ss.accept( ).socket( );
                    System.out.println(incoming + " подключился к серверу.");
                    executeIt.execute(new ServerConnection(incoming, db));


                } catch (UnknownHostException | NumberFormatException ex) {
                    System.out.println("Ой, неполадочки");
                } catch (IOException e) {
                    e.printStackTrace( );
                }
            }
        } catch (UnknownHostException | NumberFormatException ex) {
            System.out.println("Ой, такого порта же не существует(");
            ServerApp.main(null);

        }

    }
}
