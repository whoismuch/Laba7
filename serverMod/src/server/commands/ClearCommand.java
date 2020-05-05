package server.commands;

import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import server.receiver.collection.ICollectionManager;



/**
 * Класс-команда clear со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
//ConcreteCommand
public class ClearCommand implements Command {
    /** Поле имя команды */
    private final String name = "clear";
    /** Поле описание команды */
    private final String description = " - очистить коллекцию(без аргумента)";

    private String arg = "null";

    @Override
    public String toString ( ) {
        return name + " " + description;
    }

    /**
     * Метод, передающий выполнение команды приемнику
     */


    @Override
    public void execute(SendToClient sendToClient, ICollectionManager icm, String arg, Route route, Driver driver) {
        icm.clear();
        sendToClient.send("Коллекция очищена!");
    }

    /**
     * Метод получения значения поля (@link ClearCommand#description)
     * @return description возвращает описание команды
     */

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Метод получения значения поля (@link ClearCommand#name)
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


