package server.commands;

import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import server.receiver.collection.ICollectionManager;

import java.util.stream.Collectors;

/**
 * Класс-команда history со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
//ConcreteCommand
public class HistoryCommand implements Command {
    /** Поле имя команды */
    private final String name = "history";
    /** Поле описание команды */
    private final String description = "- вывести последние 7 команд (без их аргументов) (без аргументов)";
    /**
     * Метод, передающий выполнение команды приемнику
     */
    private String arg = "null";

    @Override
    public String toString ( ) {
        return name + " " + description;
    }

    @Override
    public void execute(SendToClient sendToClient, ICollectionManager icm, String arg, Route route, Driver driver) {
        if (driver.getHistory().size() == 0) {
            sendToClient.send("Вы еще ничего не вводили");
        } else {
            sendToClient.send(driver.getHistory().stream().collect(Collectors.joining("\n")));
        }
    }


    /**
     * Метод получения значения поля (@link HistoryCommand#description)
     * @return description возвращает описание команды
     */

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Метод получения значения поля (@link HistoryCommand#name)
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