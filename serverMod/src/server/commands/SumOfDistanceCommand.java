package server.commands;

import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import server.receiver.collection.ICollectionManager;

/**
 * Класс-команда sum_of_distance со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
//ConcreteCommand
public class SumOfDistanceCommand implements Command {
    /**
     * Поле имя команды
     */
    private final String name = "sum_of_distance";
    /**
     * Поле описание команды
     */
    private final String description = "- ввывести сумму значений поля distance для всех элементов коллекции (без аргумента)";

    private String arg = "null";

    /**
     * Метод, передающий выполнение команды приемнику
     */

    @Override
    public String toString ( ) {
        return name + " " + description;
    }

    @Override
    public void execute(SendToClient sendToClient, ICollectionManager icm, String arg, Route route, Driver driver) {
        if (icm.size() == 0) {
            sendToClient.send("Коллекция пуста");
        } else {
            sendToClient.send("Суммарная длина всех маршрутов : " + icm.sumOfDistance());
        }
    }

    /**
     * Метод получения значения поля (@link SumOfDistanceCommand#description)
     *
     * @return description возвращает описание команды
     */
    @Override
    public String getDescription() {
        return description;
    }


    /**
     * Метод получения значения поля (@link SumOfDistanceCommand#name)
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