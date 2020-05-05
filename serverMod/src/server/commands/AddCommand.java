package server.commands;

import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import server.receiver.collection.ICollectionManager;


/**
 * Класс-команда add со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
//ConcreteCommand
public class AddCommand implements Command {
    /**
     * Поле имя команды
     */
    private final String name = "add";
    /**
     * Поле описание команды
     */
    private final String description = " - добавить новый элемент в коллекцию (требуется ввод объекта коллекции)";

    private  String arg = "e";

    @Override
    public String toString ( ) {
        return name + " " + description;
    }

    /**
     * Метод, передающий выполнение команды приемнику
     */



    @Override
    public void execute(SendToClient sendToClient, ICollectionManager icm, String arg, Route route, Driver driver) {
        try {
            icm.add(route);
            sendToClient.send("Объект " + route.getName() + " успешно добавлен в коллекцию!");
        }catch (ClassCastException e){
            sendToClient.send("Ошибка при создании класса");
        }
    }

    /**
     * Метод получения значения поля (@link AddCommand#description)
     *
     * @return description возвращает описание команды
     */

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Метод получения значения поля (@link AddCommand#name)
     *
     * @return name возвращает имя команды
     */

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getArg() {return arg;}
}


