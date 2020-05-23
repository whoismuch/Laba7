package server.armory;

import server.receiver.collection.Navigator;
import server.receiver.collection.RouteBook;

import java.io.IOException;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerApp {


    public static void main (String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            SocketAddress address = new InetSocketAddress(port);

            String portBD = args[1];
            String login = args[2];
            String password = args[3];

            DataBase db = new DataBase(portBD, login, password);
            RouteBook routeBook = new RouteBook( );
            Navigator navigator = new Navigator(routeBook, db);
            Driver driver = new Driver( );
            navigator.loadBegin( );

            System.out.print("Сервер начал слушать клиентов" + "\nПорт " + port +
                    " / Адрес " + InetAddress.getLocalHost( ) + ".\nОжидаем подключения клиента\n ");

            ExecutorService executor = Executors.newFixedThreadPool(8);
            ExecutorService executorService = Executors.newFixedThreadPool(8);

            Runtime.getRuntime( ).addShutdownHook(new Thread(( ) -> {
                executor.shutdown();
                executorService.shutdown();
                db.theEnd( );
            }));

            while (true) {
                try (ServerSocketChannel ss = ServerSocketChannel.open( )) {
                    ss.bind(address);

                    Socket incoming = ss.accept( ).socket( );
                    System.out.println(incoming + " подключился к серверу.");

                    SendToClient sendToClient = new SendToClient(incoming);

                    sendToClient.setMessage(driver.getAvailable( ));
                    sendToClient.run( );


                    GetFromClient getFromClient = new GetFromClient(incoming, db, navigator, routeBook, driver, executorService, sendToClient);
                    executor.submit(getFromClient);



                } catch (UnknownHostException | NumberFormatException ex) {
                    System.out.println("Ой, неполадочки");
                } catch (NoSuchElementException ex) {
                    System.out.println("Какое такое зло я вам сделала?");
                } catch (IOException e) {
                    e.printStackTrace( );
                }
            }
        } catch (UnknownHostException ex) {
            System.out.println("Ой, такого порта же не существует(");
            ServerApp.main(null);
        } catch (NumberFormatException | InputMismatchException ex) {
            System.out.println("Порт должен быть циферкой");
            System.out.print("Введите порт:");
            Scanner scanner = new Scanner(System.in);
            String port = scanner.nextLine( );
            String[] newArgs = new String[]{port, args[1], args[2], args[3]};
            ServerApp.main(newArgs);

        } catch (NullPointerException | ArrayIndexOutOfBoundsException ex) {
            System.out.println("Ну вы чего, все ведь просто\nПорт сервера *пробел* Порт БД *пробел* Логин *пробел* Пароль *пробел*");
        }

    }
}
