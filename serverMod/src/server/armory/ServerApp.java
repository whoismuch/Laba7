package server.armory;

import server.receiver.collection.Navigator;
import server.receiver.collection.RouteBook;

import java.io.IOException;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerApp {


    public static void main (String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
//            System.out.print("Сколько клиентов вы собираетесь обслуживать одновременно?: ");
//            String a = scanner.nextLine().trim();
//            if (a.equals("")) throw new NumberFormatException();
//
//            ExecutorService executeIt = Executors.newFixedThreadPool(Integer.parseInt(a));

            System.out.print("Введите порт: ");

            int port = Integer.parseInt(scanner.nextLine( ));
            SocketAddress address = new InetSocketAddress(port);

            DataBase db = new DataBase( );
            RouteBook routeBook = new RouteBook( );
            Navigator navigator = new Navigator(routeBook, db);
            Driver driver = new Driver();
            navigator.loadBegin( );


            Runtime.getRuntime( ).addShutdownHook(new Thread(( ) -> {
//                executeIt.shutdown( );
                db.theEnd();
            }));

            System.out.print("Сервер начал слушать клиентов" + "\nПорт " + port +
                    " / Адрес " + InetAddress.getLocalHost( ) + ".\nОжидаем подключения клиента\n ");
            while (true) {
                try (ServerSocketChannel ss = ServerSocketChannel.open( )) {
                    ss.bind(address);

                    Socket incoming = ss.accept( ).socket( );
                    System.out.println(incoming + " подключился к серверу.");

                    SendToClient sendToClient = new SendToClient(incoming);

                    sendToClient.setMessage(driver.getAvailable( ));
                    sendToClient.run();



                    ExecutorService executor = Executors.newFixedThreadPool(8);
                    ExecutorService executorService = Executors.newFixedThreadPool(8);

                    GetFromClient getFromClient = new GetFromClient(incoming, db, navigator, routeBook, driver, executorService, sendToClient  );
                    executor.submit(getFromClient);


                } catch (UnknownHostException | NumberFormatException ex) {
                    System.out.println("Ой, неполадочки");
                } catch (IOException e) {
                    e.printStackTrace( );
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("Ой, такого порта же не существует(");
            ServerApp.main(null);
        } catch (NumberFormatException | InputMismatchException ex) {
            System.out.println("Введите циферку, позязя");
            ServerApp.main(null);
        }

    }
}
