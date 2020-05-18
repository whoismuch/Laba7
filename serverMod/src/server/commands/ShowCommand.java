package server.commands;

import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import server.receiver.collection.ICollectionManager;

/**
 * Класс-команда show со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
//ConcreteCommand
public class ShowCommand implements Command {
    /**
     * Поле имя команды
     */
    private final String name = "show";
    /**
     * Поле описание команды
     */
    private final String description = " - вывести в стандартный поток вывода все элементы коллекции в строковом представлении (без аргумента)";

    private String arg = "null";

    /**
     * Метод, передающий выполнение команды приемнику
     */
    @Override
    public String toString ( ) {
        return name + " " + description;
    }


    @Override
    public String execute(ICollectionManager icm, String arg, Route route, Driver driver) {
        return (icm.show());
    }

    /**
     * Метод получения значения поля (@link ShowCommand#description)
     *
     * @return description возвращает описание команды
     */

    public String getDescription() {
        return description;
    }

    /**
     * Метод получения значения поля (@link ShowCommand#name)
     *
     * @return name возвращает имя команды
     */

    public String getName() {
        return name;
    }

    public String getArg() {
        return arg;
    }
}



