package server.commands;


import common.generatedClasses.Coordinates;
import common.generatedClasses.Location;
import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import server.receiver.collection.ICollectionManager;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс-команда execute_script со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */

public class ExecuteScriptCommand implements Command {
    /**
     * Поле имя команды
     */
    private final String name = "execute_script";
    /**
     * Поле описание команды
     */
    private final String description = "- считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь (требуется указать путь до скрипта)";

    private String arg = "FileF";

    private String result;


    @Override
    public String toString ( ) {
        return name + " " + description;
    }

    /**
     * Метод, передающий выполнение команды приемнику
     */


    @Override
    public String execute(ICollectionManager icm, String arg, Route route, Driver driver, String username) {
        try {
            result ="";
            CharArrayReader car = new CharArrayReader(arg.toCharArray());
            Scanner scanner = new Scanner(car);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = line.trim( );
                String commandname = line;
                String argue = null;
                if (line.indexOf(" ") != -1) {
                    commandname = line.substring(0, line.indexOf(" "));
                    argue = (line.substring(line.indexOf(" "))).trim( );
                }

                if (driver.getAvailable().get(commandname).endsWith("e")) {
                    route = new Route(scanner.nextLine(),
                                       new Coordinates(Long.parseLong(scanner.nextLine()), Integer.parseInt(scanner.nextLine())),
                                       new Location(scanner.nextLine(), Long.parseLong(scanner.nextLine()), Long.parseLong(scanner.nextLine())),
                                       new Location(scanner.nextLine(), Long.parseLong(scanner.nextLine()), Long.parseLong(scanner.nextLine())), Float.parseFloat(scanner.nextLine()));

                }

                result = driver.execute(icm, commandname, argue, route, driver, username) + "\n";
            }
            return result;
        } catch (NullPointerException e) {
            return result + "\nСкрипт выполнен";
        }
    }


    /**
     * Метод получения значения поля (@link ExecuteScriptCommand#description)
     *
     * @return description возвращает описание команды
     */

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Метод получения значения поля (@link ExecuteScriptCommand#name)
     *
     * @return name возвращает имя команды
     */

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getArg ( ) {
        return arg;
    }
}