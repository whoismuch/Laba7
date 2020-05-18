package server.commands;


import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import server.receiver.collection.ICollectionManager;

/**
 * Интерфейс, используемый для паттерна команд (все команды реализуют его)
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
public interface Command {

    /**
     * Абстрактный метод для выполнения команды
     * @param icm
     */
    String execute (ICollectionManager icm, String arg, Route route, Driver driver);

    /**
     * Абстрактный метод для получения описания команды
     * @return description описание команды
     */
    String getDescription ( );

    /**
     * Абстрактный метод для получения имени команды
     * @return name имя команды
     */
    String getName ( );

    String getArg ( );


}