package server.armory;

import common.command.CommandDescription;
import server.receiver.collection.Navigator;
import server.receiver.collection.RouteBook;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;

public class ServerConnection implements Runnable {

    private Socket incoming;
    private DataBase db;
    private GetFromClient getFromClient;
    private SendToClient sendToClient;
    private String username;
    private RouteBook routeBook;
    private Navigator navigator;
    private boolean everythingIsAlright = true;
    private Driver driver;
    private Future<Object> future;
    private ExecutorService executorService;
    private ExecutorService executor;

    public ServerConnection (Socket incoming, DataBase db, RouteBook routeBook, Navigator navigator) {
        this.incoming = incoming;
        this.db = db;
        this.routeBook = routeBook;
        this.navigator = navigator;
    }


    @Override
    public void run ( ) {

        GetFromClient getFromClient = new GetFromClient(incoming);
        SendToClient sendToClient = new SendToClient(incoming);

        executor = Executors.newFixedThreadPool(1);
        executorService = Executors.newFixedThreadPool(1);

        this.getFromClient = getFromClient;
        this.sendToClient = sendToClient;


        checkPassword( );


        Driver driver = new Driver(username);
        this.driver = driver;


        sendToClient.setMessage(driver.getAvailable( ));
        executorService.submit(sendToClient);

        try {
            executorService.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace( );
        }

        executeCommands( );

        close( );
    }

    public void executeCommands ( ) {
        try {
            while (true) {
                if (!everythingIsAlright) break;
                future = executor.submit(getFromClient);
                CommandDescription command = (CommandDescription) future.get();
                String result =  driver.execute(navigator, command.getName( ), command.getArg( ), command.getRoute( ), driver);

                sendToClient.setMessage(result);
                executorService.submit(sendToClient);

                try {
                    executorService.awaitTermination(2, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace( );
                }

                if (command.getName( ).equals("exit")) break;
            }
        } catch (ClassCastException | NullPointerException ex) {
            everythingIsAlright = false;
            executeCommands( );
        } catch (InterruptedException e) {
            e.printStackTrace( );
        } catch (ExecutionException e) {
            e.printStackTrace( );
        }
    }

    public void close ( ) {
        try {
            incoming.close( );
        } catch (IOException e) {
            e.printStackTrace( );
        }
    }

    public void checkPassword ( ) {
        try {
            future = executor.submit(getFromClient);
            String message = future.get().toString();
            String[] mas = message.split(" ");
            String choice = mas[0];
            String username = mas[1];
            String password = mas[2];

            this.username = username;

            String authenticationResult = null;

            if (choice.equals("Регистрация")) {
                authenticationResult = db.registration(username, password);
            }
            if (choice.equals("Авторизация")) {
                authenticationResult = db.authorization(username, password);
            }

            sendToClient.setMessage(authenticationResult);
            executorService.submit(sendToClient);

            try {
                executorService.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace( );
            }

            if (authenticationResult.equals("Пользователь с таким логином не зарегистрирован") || authenticationResult.equals("Вы ввели неправильный пароль") || authenticationResult.equals("Пользователь с таким логином уже зарегистрирован. Может, вам стоит авторизоваться?")) {
                checkPassword( );
            }
        } catch (NullPointerException ex) {
            everythingIsAlright = false;
        } catch (InterruptedException e) {
            e.printStackTrace( );
        } catch (ExecutionException e) {
            e.printStackTrace( );
        }
    }

}
