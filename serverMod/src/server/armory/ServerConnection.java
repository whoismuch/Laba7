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
    private SendToClient sendToClient;
    private RouteBook routeBook;
    private Navigator navigator;
    private Driver driver;
    private boolean everythingIsAlright = true;
    private ExecutorService executorService;
    private Object request;
    private CommandDescription command;
    private String authenticationResult;


    public ServerConnection (Object request, Socket incoming, DataBase db, RouteBook routeBook, Navigator navigator, Driver driver, ExecutorService executorService, SendToClient sendToClient) {
        this.request = request;
        this.incoming = incoming;
        this.db = db;
        this.routeBook = routeBook;
        this.navigator = navigator;
        this.driver = driver;
        this.executorService = executorService;
        this.sendToClient = sendToClient;
    }


    @Override
    public void run ( ) {

            command = (CommandDescription) request;

            checkPassword( );

            if (!everythingIsAlright) {
                sendToClient.setMessage(authenticationResult + "\nИзвините, ваш запрос не может быть выполнен. Попробуйте еще раз");
                executorService.submit(sendToClient);

            } else executeCommand( );

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace( );
        }

        try {
            incoming.close();
        } catch (IOException e) {
            e.printStackTrace( );
        }

    }

    public void executeCommand ( ) {

        if (everythingIsAlright) {

            String result = driver.execute(navigator, command.getName( ), command.getArg( ), command.getRoute( ), driver, command.getUsername( ));

            sendToClient.setMessage(result);
            executorService.submit(sendToClient);

        }
    }


    public void checkPassword ( ) {
        try {

            authenticationResult = null;

            if (command.getChoice( ).equals("Регистрация")) {
                authenticationResult = db.registration(command.getUsername( ), command.getPassword( ));
            }
            if (command.getChoice( ).equals("Авторизация")) {
                authenticationResult = db.authorization(command.getUsername( ), command.getPassword( ));
            }

            if (authenticationResult.equals("Пользователь с таким логином не зарегистрирован") || authenticationResult.equals("Вы ввели неправильный пароль") || authenticationResult.equals("Пользователь с таким логином уже зарегистрирован. Может, вам стоит авторизоваться?")) {
                everythingIsAlright = false;
            }
        } catch (NullPointerException ex) {
            everythingIsAlright = false;
        }
    }

}
