package server.commands;


import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import server.receiver.collection.ICollectionManager;

import java.util.stream.Collectors;

/**
 * Класс-команда filter_less_than_distance со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
//ConcreteCommand
public class FilterLessThanDistanceCommand implements Command {
    /** Поле имя команды */
    private final String name = "filter_less_than_distance";
    /** Поле описание команды */
    private final String description = "- вывести элементы, значение поля distance которых меньше заданного (аргумент типа Float)";

    private String arg = "Float";

    @Override
    public String toString ( ) {
        return name + " " + description;
    }



    /**
     * Метод, передающий выполнение команды приемнику
     */

    @Override
    public void execute(SendToClient sendToClient, ICollectionManager icm, String arg, Route route, Driver driver) {
        Float distance = Float.parseFloat(arg);
        sendToClient.send(distance < 1 ? "Аргумент должен быть больше 1, мы разве не предупредили?" : icm.filterLessThanDistance(distance).isEmpty( ) ? "ой, а таких элементов-то и нет :(" : icm.filterLessThanDistance(distance).stream( ).map(x -> x.toString( )).collect(Collectors.joining("\n")));
    }


    /**
     * Метод получения значения поля (@link FilterLessThanDistanceCommand#description)
     * @return description возвращает описание команды
     */

    @Override
    public String getDescription() {
        return description;
    }


    /**
     * Метод получения значения поля (@link FilterLessThanDistanceCommand#name)
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