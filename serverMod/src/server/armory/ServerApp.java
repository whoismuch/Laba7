package server.armory;

import server.receiver.collection.Navigator;
import server.receiver.collection.RouteBook;

import java.io.IOException;
import java.net.*;
import java.nio.channels.ServerSocketChannel;

public class ServerApp {

    /**
     * Точка входа в программу. Управляет подключением к клиентам и созданием потоков для каждого из них.
     *
     * @param args массив по умолчанию в основном методе. Не используется здесь.
     */
    public static void main (String[] args) throws IOException {

        RouteBook routeBook = new RouteBook( );
        Navigator navigator = new Navigator(routeBook);
        SocketAddress address = new InetSocketAddress( 8080);

        try (ServerSocketChannel ss = ServerSocketChannel.open()) {
            ss.bind(address);
            System.out.print("Сервер начал слушать клиента " + "\nПорт " + ss.getLocalAddress( ) +
                    " / Адрес " + InetAddress.getLocalHost( ) + ".\nОжидаем подключения клиента\n ");
//            String path = args[0];
            String path = "serverMod/routes.json";
            Driver driver = new Driver( );
            ServerConnection sc = new ServerConnection(driver, navigator, path);

            Runtime.getRuntime().addShutdownHook(new Thread(( ) -> {
                System.out.println("\n Воу, чем я вам не угодил? Ну ладно, сохраню коллекцию...");
                sc.theEnd();
            }));

            driver.load(null, navigator, path);

            while (true) {
                Socket incoming = (ss.accept( )).socket();
                System.out.println(incoming + " подключился к серверу.");
                sc.setIncoming(incoming);
                sc.serverWork( );
                //incoming.shutdownOutput();
                incoming.close();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace( );
        } catch (IOException e) {
            e.printStackTrace( );
        }
    }

}
