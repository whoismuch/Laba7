package server.armory;

import common.command.CommandDescription;
import server.receiver.collection.Navigator;
import server.receiver.collection.RouteBook;

import java.io.IOException;
import java.net.Socket;

public class ServerConnection implements Runnable {

    private Socket incoming;
    private DataBase db;
    private GetFromClient getFromClient;
    private SendToClient sendToClient;
    private String username;
    private RouteBook routeBook;
    private Navigator navigator;

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

        this.getFromClient = getFromClient;
        this.sendToClient = sendToClient;

        checkPassword();

        Driver driver = new Driver(username);

        sendToClient.send(driver.getAvailable( ));

        while (true) {
            CommandDescription command = (CommandDescription) getFromClient.get( );
            driver.execute(sendToClient, navigator, command.getName( ), command.getArg( ), command.getRoute( ), driver);
            if (command.getName().equals("exit")) break;
        }

        try {
            incoming.close();
        } catch (IOException e) {
            e.printStackTrace( );
        }
    }

    public void checkPassword() {
        String message = getFromClient.get().toString();
        String username = message.substring(0, message.indexOf(" "));
        String password = message.substring(message.indexOf(" ") + 1);

        this.username = username;

        String authenticationResult = db.authentication(username, password);

        sendToClient.send(authenticationResult);
        if (authenticationResult.equals("Упс...Если вы ранее регистрировались под этим логином, то указанный вами пароль неверен:( \n Если же вы регистрируетесь впервые, вам стотит выбрать другой логин")) {
            checkPassword();
        }
    }

}
