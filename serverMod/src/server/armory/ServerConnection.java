package server.armory;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import common.command.CommandDescription;
import server.receiver.collection.Navigator;
import server.receiver.collection.RouteBook;

import java.io.IOException;
import java.net.Socket;

public class ServerConnection implements Runnable {

    private Socket incoming;

    public ServerConnection (Socket incoming) {
        this.incoming = incoming;
    }


    @Override
    public void run ( ) {

        RouteBook routeBook = new RouteBook();
        Navigator navigator = new Navigator(routeBook);

        Driver driver = new Driver();

        GetFromClient getFromClient = new GetFromClient(incoming);
        SendToClient sendToClient = new SendToClient(incoming);


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
}
