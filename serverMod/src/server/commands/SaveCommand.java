//package server.commands;
//
//import com.google.gson.JsonSyntaxException;
//import common.generatedClasses.Route;
//import server.armory.Driver;
//import server.armory.SendToClient;
//import common.exceptions.NoPermissionsException;
//import server.receiver.collection.ICollectionManager;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
///**
// * Класс-команда save со свойствами <b>name</b>, <b>description</b>, <b>navigator</b>
// *
// * @author Саня Малета и Хумай Байрамова
// * @version final
// */
////ConcreteCommand
//public class SaveCommand implements Command {
//    /**
//     * Поле имя команды
//     */
//    private final String name = "save";
//    /**
//     * Поле описание команды
//     */
//    private final String description = "- сохранить коллекцию в файл";
//
//    private String arg = "path";
//
//    /**
//     * Метод, передающий выполнение команды приемнику
//     */
//
//    @Override
//    public void execute(SendToClient sendToClient, ICollectionManager icm, String arg, Route route, Driver driver) {
//        try {
//            icm.save(arg);
//            System.out.println("Мы сохранили коллекцию, хоть что-то мы смогли" );
//        } catch (NoPermissionsException e) {
//            System.out.println(e.getMessage());
//        } catch (JsonSyntaxException e) {
//            System.out.println(("Ошибка парсера"));
//        } catch (NullPointerException e) {
//            System.out.println("Файл пуст!");
//        } catch (FileNotFoundException e) {
//            System.out.println("Файла по указанному пути не существует!");
//        } catch (IOException e) {
//            System.out.println("Ошибка при рабтое с файлом");
//        }
//    }
//
//
//    /**
//     * Метод получения значения поля (@link SaveCommand#description)
//     *
//     * @return description возвращает описание команды
//     */
//
//    @Override
//    public String getDescription() {
//        return description;
//    }
//
//
//    /**
//     * Метод получения значения поля (@link SaveCommand#name)
//     *
//     * @return name возвращает имя команды
//     */
//    @Override
//    public String getName() {
//        return name;
//    }
//
//    @Override
//    public String getArg() {
//        return arg;
//    }
//}