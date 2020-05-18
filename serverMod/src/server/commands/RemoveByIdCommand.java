package server.commands;

import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import server.receiver.collection.ICollectionManager;

/**
 * Класс-команда remove_by_id со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
//ConcreteCommand
public class RemoveByIdCommand implements Command {
    /** Поле имя команды */
    private final String name = "remove_by_id";
    /** Поле описание команды */
    private final String description = "- удалить элемент из коллекции по его id (аргумент типа Long)";

    private String arg = "Long";


    @Override
    public String toString ( ) {
        return name + " " + description;
    }
    /**
     * Метод, передающий выполнение команды приемнику
     */

    @Override
    public String execute(ICollectionManager icm, String arg, Route route, Driver driver) {
        Long id = Long.parseLong(arg);
        return (icm.removeById(id, driver.getUsername())? "Элемент с id : " + id + " удален!": "Элемент не найден и/или вам не принадлежит");
    }

    /**
     * Метод получения значения поля (@link RemoveByIdCommand#description)
     * @return description возвращает описание команды
     */

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Метод получения значения поля (@link RemoveByIdCommand#name)
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