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
    private Future a;
    private Future b;
    private Future c;
    private Future d;


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
        try {

            command = (CommandDescription) request;

            everythingIsAlright = true;

            checkPassword( );

            if (command.getName( ) == null) {
                sendToClient.setMessage(authenticationResult);
                a = executorService.submit(sendToClient);
                a.get( );
            } else {

                if (!everythingIsAlright) {
                    sendToClient.setMessage(authenticationResult + "\nИзвините, ваш запрос не может быть выполнен. Попробуйте еще раз");
                    b = executorService.submit(sendToClient);
                    b.get( );
                } else executeCommand( );
            }


            try {
                incoming.close( );
            } catch (IOException e) {
                e.printStackTrace( );
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace( );
        } catch (NullPointerException ex) {
            System.out.println("Мы дико извиняемся, но у нас неполадочки со связью" );
            sendToClient.setMessage("Упс...у сервера маленькие неполадОчки");
            d = executorService.submit(sendToClient);
            try {
                d.get();
            } catch (InterruptedException e) {
                e.printStackTrace( );
            } catch (ExecutionException e) {
                e.printStackTrace( );
            }
            try {
                incoming.close();
            } catch (IOException e) {
                e.printStackTrace( );
            }
        }

    }

    public void executeCommand ( ) throws ExecutionException, InterruptedException {

        if (everythingIsAlright) {

            String result = driver.execute(navigator, command.getName( ), command.getArg( ), command.getRoute( ), driver, command.getUsername( ));

            sendToClient.setMessage(result);
            c = executorService.submit(sendToClient);

            c.get( );
        }
    }


    public void checkPassword ( ) {
        try {

            authenticationResult = null;

            if (command.getChoice( ).equals("R")) {
                authenticationResult = db.registration(command.getUsername( ), command.getPassword( ));
            }
            if (command.getChoice( ).equals("A")) {
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
