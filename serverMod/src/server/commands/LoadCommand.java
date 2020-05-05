package server.commands;

import com.google.gson.JsonSyntaxException;
import common.generatedClasses.Route;
import server.armory.Driver;
import server.armory.SendToClient;
import common.exceptions.NoPermissionsException;
import server.receiver.collection.ICollectionManager;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Класс-команда load со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
public class LoadCommand implements Command {
    /**
     * Поле имя команды
     */
    private final String name = "load";
    /**
     * Поле описание команды
     */
    private final String description = "- загрузка колекции из файла";

    private String arg = "path";

    /**
     * Метод, передающий выполнение команды приемнику
     */

    @Override
    public void execute(SendToClient sendToClient, ICollectionManager icm, String arg, Route route, Driver driver) {
        try {
            icm.load(arg);
        } catch (NoPermissionsException e) {
            sendToClient.send(e.getMessage());
        } catch (JsonSyntaxException e) {
            sendToClient.send("Ошибка парсера, сохраненная на сервере коллекция не доступна");
        } catch (NullPointerException e) {
            sendToClient.send("Файл пуст! Сохраненная на сервере коллекция не доступна");
        } catch (FileNotFoundException e) {
            sendToClient.send("Упс... у нас неполадки. Сохраненная на сервере коллекция не доступна");
        } catch (IOException e) {
            sendToClient.send("Ошибка при рабтое с файлом. Сохраненная на сервере коллекция не достуна");
        }
    }

    /**
     * Метод получения значения поля (@link LoadCommand#description)
     *
     * @return description возвращает описание команды
     */

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Метод получения значения поля (@link LoadCommand#name)
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