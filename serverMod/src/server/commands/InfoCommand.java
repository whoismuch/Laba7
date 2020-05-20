package server.commands;

import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import server.receiver.collection.ICollectionManager;

/**
 * Класс-команда info со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
//ConcreteCommand
public class InfoCommand implements Command {
    /**
     * Поле имя команды
     */
    private final String name = "info";
    /**
     * Поле описание команды
     */
    private final String description = "- вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.) (без аргумента)";

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
        return (icm.info( ));
    }


    /**
     * Метод получения значения поля (@link InfoCommand#description)
     *
     * @return description возвращает описание команды
     */

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Метод получения значения поля (@link InfoCommand#name)
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