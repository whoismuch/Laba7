package server.armory;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
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

    public ServerConnection (Socket incoming, DataBase db) {
        this.incoming = incoming;
        this.db = db;
    }


    @Override
    public void run ( ) {

        RouteBook routeBook = new RouteBook();
        Navigator navigator = new Navigator(routeBook);

        Driver driver = new Driver();

        GetFromClient getFromClient = new GetFromClient(incoming);
        SendToClient sendToClient = new SendToClient(incoming);

        this.getFromClient = getFromClient;
        this.sendToClient = sendToClient;

        passwordProcessing();

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

    public void passwordProcessing() {
        String message = getFromClient.get().toString();
        String username = message.substring(0, message.indexOf(" "));
        String password = message.substring(message.indexOf(" "), message.length()-1);

        String authenticationResult = db.authentication(username, password);

        sendToClient.send(authenticationResult);
        if (authenticationResult.equals("Упс...Если вы ранее регистрировались под этим логином, то указанный вами пароль неверен:( \n Если же вы регистрируетесь впервые, вам стотит выбрать другой логин")) {
            passwordProcessing();
        }

    }

}
