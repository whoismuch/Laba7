package server.commands;

import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import server.receiver.collection.ICollectionManager;

/**
 * Класс-команда exit со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
//ConcreteCommand
public class ExitCommand implements Command {
    /** Поле имя команды */
    private final String name = "exit";
    /** Поле описание команды */
    private final String description = " - завершить программу(без аргумента)";

    private String arg = "null";

    /**
     * Конструктор - привязывает команду к приемнику
    // * @param navigator объект класса-receiver Navigator
     */

    @Override
    public String toString ( ) {
        return name + " " + description;
    }

    /**
     * Метод, передающий выполнение команды приемнику
     */

    @Override
    public String execute(ICollectionManager icm, String arg, Route route, Driver driver) {
        return "Завершаю работу...";
    }


    /**
     * Метод получения значения поля (@link ExitCommand#description)
     * @return description возвращает описание команды
     */

    public String getDescription() {
        return description;
    }

    /**
     * Метод получения значения поля (@link ExitCommand#name)
     * @return name возвращает имя команды
     */

    public String getName() {
        return name;
    }

    public String getArg() {
        return arg;
    }
}


