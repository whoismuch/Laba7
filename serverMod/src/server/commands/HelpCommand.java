package server.commands;

import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import server.receiver.collection.ICollectionManager;


import java.util.stream.Collectors;

/**
 * Класс-команда help со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
//ConcreteCommand
public class HelpCommand implements Command {
    /** Поле имя команды */
    private final String name = "help";
    /** Поле описание команды */
    private final String description = "- вывести справку по доступным командам (без аргумента)";

    private String arg = "null";
    /**
     * Метод, передающий выполнение команды приемнику
     */
    @Override
    public String toString ( ) {
        return name + " " + description;
    }

    @Override
    public String execute(ICollectionManager icm, String arg, Route route, Driver driver, String username) {
        return (driver.getAllCommands().stream().map(x -> x.toString()).collect(Collectors.joining("\n")));
    }


    /**
     * Метод получения значения поля (@link HelpCommand#descriptio
     * @return description возвращает описание команды
     */

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Метод получения значения поля (@link HelpCommand#name)
     * @return name возвращает имя команды
     */

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getArg() {
        return arg;
    }


}
