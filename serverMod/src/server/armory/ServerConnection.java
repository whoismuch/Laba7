package server.armory;

import common.command.CommandDescription;
import server.receiver.collection.Navigator;

import java.net.Socket;

public class ServerConnection {

    private Socket incoming;

    private Driver driver;
    private Navigator navigator;
    private String path;

    public ServerConnection (Driver driver, Navigator navigator, String path) {
        this.driver = driver;
        this.navigator = navigator;
        this.path = path;

    }


    public void serverWork ()  {

        GetFromClient getFromClient = new GetFromClient(incoming);
        SendToClient sendToClient = new SendToClient(incoming);

        if (getFromClient.get().equals("I'm ready to get available commands")) {
            sendToClient.send(driver.getAvailable( ));
        }

        CommandDescription command = (CommandDescription) getFromClient.get();
        driver.execute(sendToClient, navigator, command.getName( ), command.getArg( ), command.getRoute( ), driver);
    }


    public void theEnd() {
        driver.save(null, navigator, path );
    }

    public void setIncoming (Socket incoming) {
        this.incoming = incoming;
    }
}
