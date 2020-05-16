package server.commands;

import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import server.receiver.collection.ICollectionManager;

/**
 * Класс-команда update_id со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
public class UpdateIdCommand implements Command {
    /**
     * Поле имя команды
     */
    private final String name = "update_id";
    /**
     * Поле описание команды
     */
    private final String description = " - обновить значение элемента коллекции, id которого равен заданному (аргумент типа Long, требует ввод объекта коллекции)";

    private String arg = "Longe";

    @Override
    public String toString ( ) {
        return name + " " + description;
    }

    /**
     * Метод, передающий выполнение команды приемнику
     */

    @Override
    public void execute(SendToClient sendToClient, ICollectionManager icm, String arg, Route route, Driver driver) {
        Long id = Long.parseLong(arg);
        sendToClient.send(icm.updateId(id,route, driver.getUsername())? "Объект с id: " + id + " обновлен" : "Элемент с введенным id не найден и/или вам не принадлежит");
    }

    /**
     * Метод получения значения поля (@link UpdateIdCommand#description)
     *
     * @return description возвращает описание команды
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Метод получения значения поля (@link UpdateIdCommand#name)
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