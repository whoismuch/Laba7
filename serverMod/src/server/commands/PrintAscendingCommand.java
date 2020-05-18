package server.commands;

import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import server.receiver.collection.ICollectionManager;

/**
 * Класс-команда print_ascending со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
//ConcreteCommand
public class PrintAscendingCommand implements Command {
    /**
     * Поле имя команды
     */
    private final String name = "print_ascending";
    /**
     * Поле описание команды
     */
    private final String description = "- вывести элементы коллекции в порядке возрастания (без аргумента)";

    private String arg = "null";

    @Override
    public String toString ( ) {
        return name + " " + description;
    }

    /**
     * Метод, передающий выполнение команды приемнику
     */

    @Override
    public String execute(ICollectionManager icm, String arg, Route route, Driver driver) {
        return (icm.printAscending());
    }


    /**
     * Метод получения значения поля (@link PrintAscendingCommand#description)
     *
     * @return description возвращает описание команды
     */

    @Override
    public String getDescription() {
        return description;
    }


    /**
     * Метод получения значения поля (@link PrintAscendingCommand#name)
     *
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