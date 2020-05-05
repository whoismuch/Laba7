package client;

import com.sun.javaws.IconUtil;
import common.command.CommandDescription;
import common.generatedClasses.Route;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;


public class ClientProviding {

    private DataExchangeWithServer dataExchangeWithServer;
    private UserManager userManager;
    private Selector selector;
    private String commandname = "check";
    private String arg;
    private SocketChannel outcommingchannel;

    /**
     * Устанавливает активное соединение с сервером.
     */
    public void clientWork (boolean iBegin) {
        try (Scanner scanner = new Scanner(System.in)) {
//            SocketAddress outcoming = new InetSocketAddress("localhost", 8080);
            if (iBegin) {
                userManager = new UserManager(scanner,
                        new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8)),
                        true);
            }
            while (true) {
                SocketAddress outcoming = new InetSocketAddress(userManager.readString("Введите адрес: ", false), Integer.parseInt(userManager.readString("Введите порт: ", false)));
                try (SocketChannel outcomingchannel = SocketChannel.open(outcoming)) {

                    this.outcommingchannel = outcomingchannel;

                    dataExchangeWithServer = new DataExchangeWithServer(outcomingchannel);

                    selector = Selector.open( );
                    outcomingchannel.configureBlocking(false);
                    outcomingchannel.register(selector, SelectionKey.OP_READ);

                    String beginMessage = "I've already got everything :(";
                    if (iBegin) beginMessage = "I'm ready to get available commands";
                    dataExchangeWithServer.sendToServer(beginMessage);
                    if (iBegin) {
                        selector.select( );
                        userManager.setAvailable((HashMap) dataExchangeWithServer.getFromServer( ));
                        iBegin = false;
                    }

                    clientLaunch( );

                    exit( );
                } catch (UnresolvedAddressException e) {
                    userManager.writeln("Ойойой, такого адреса ведь не существует");
                    continue;
                } catch (IOException e) {
                    if (!commandname.equals("exit")) {
                        userManager.writeln("Нет связи с сервером. Подключиться ещё раз (введите {да} или {нет})?");
                        String answer;
                        while (!(answer = userManager.read( )).equals("да")) {
                            switch (answer) {
                                case "":
                                    break;
                                case "нет":
                                    exit( );
                                    break;
                                default:
                                    userManager.write("Введите корректный ответ.");
                            }
                        }
                        userManager.writeln("Подключение ...");
                        continue;
                    } else exit( );
                }
            }
        }
    }


    public void clientLaunch ( ) throws IOException {

        String line = "check";
        while (!line.equals("exit")) {
            userManager.write("Введите команду: ");
            line = userManager.read( );
            line = line.trim( );
            commandname = line;
            arg = null;
            if (line.indexOf(" ") != -1) {
                commandname = line.substring(0, line.indexOf(" "));
                arg = (line.substring(line.indexOf(" "))).trim( );
            }

            if (!userManager.checkCommandName(commandname)) {
                continue;
            }

            if (!userManager.checkArg(commandname, arg)) {
                continue;
            }

            if (userManager.checkFile(commandname)) {
                arg = userManager.contentOfFile(arg);
                userManager.setFinalScript(arg);
                if (arg == null) continue;
                else {
                    int commandNumber = userManager.checkContentOfFile(arg, 0);
                    if (commandNumber == 0) {
                        System.out.println("Бе, скрипт с ошибочками, такой скрипт мы обработать не сможем\nПожалуй, исправьте скрипт и введите следующую команду");
                        continue;
                    }
                    arg = userManager.getFinalScript( );
                    sendCommand( );
                    getScriptResult(commandNumber);
                }
            } else {
                sendCommand( );
                getResult( );
            }

        }
    }

    public void exit ( ) {
        userManager.write("Завершение программы.");
        System.exit(0);
    }

    public void sendCommand ( ) throws IOException {
        CommandDescription command;
        if (userManager.checkElement(commandname)) {
            Route route = userManager.readRoute( );
            command = new CommandDescription(commandname, arg, route);
        } else {
            command = new CommandDescription(commandname, arg, null);
        }

        dataExchangeWithServer.sendToServer(command);
    }

    public void getResult ( ) throws IOException {
        selector.select( );
        userManager.writeln(dataExchangeWithServer.getFromServer( ).toString( ));

    }

    public void getScriptResult (int commandNumber) throws IOException {
        for (int i = 0; i <= commandNumber; i++) {
            try {
                getResult( );
            } catch (IOException e) {
                lostConnection( );
                clientWork(false);
            }
            System.out.println("\n");
        }
    }

    public void lostConnection ( ) {
        userManager.writeln("Нет связи с сервером. Подключиться ещё раз (введите {да} или {нет})?");
        String answer;
        while (!(answer = userManager.read( )).equals("да")) {
            switch (answer) {
                case "":
                    break;
                case "нет":
                    exit( );
                    break;
                default:
                    userManager.write("Введите корректный ответ.");
            }
        }
    }
}

