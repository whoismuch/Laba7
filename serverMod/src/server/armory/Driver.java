package server.armory;

import common.generatedClasses.Route;
import server.commands.*;
import common.exceptions.NoExecuteScriptInScript;
import server.receiver.collection.ICollectionManager;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Класс-invoker, вызывающий команды, со свойством <b>man</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
public class Driver {


    /**
     * Поле словарь, где ключом является название команды, а значением - объект соответствующей команды
     */

    private HashMap<String, ArrayDeque<String>> dequeHashMap;
    private HashMap<String, Command> man = new HashMap( );
    private HashMap<String, String> available = new HashMap<>( );

    public Driver () {
        this.dequeHashMap = new HashMap<>();
        registerCommand(new AddCommand( ));
        registerCommand(new ClearCommand( ));
        registerCommand(new ExecuteScriptCommand( ));
        registerCommand(new ExitCommand( ));
        registerCommand(new FilterLessThanDistanceCommand( ));
        registerCommand(new HelpCommand( ));
        registerCommand(new HistoryCommand( ));
        registerCommand(new InfoCommand( ));
        registerCommand(new PrintAscendingCommand( ));
        registerCommand(new RemoveByIdCommand( ));
        registerCommand(new RemoveGreaterCommand( ));
        registerCommand(new RemoveLowerCommand( ));
        registerCommand(new ShowCommand( ));
        registerCommand(new SumOfDistanceCommand( ));
        registerCommand(new UpdateIdCommand( ));
    }


    /**
     * Метод, регистрирующий доступные команды в словаре команд
     *
     * @param command объект команды
     */

    private void registerCommand (Command command) {
        man.put(command.getName( ), command);
        available.put(command.getName( ), command.getArg( ));
    }

    /**
     * Метод, используемый для будущего исполнения определенной команды
     *
     * @param icm
     * @param line
     * @throws NoExecuteScriptInScript ошибка возникает, если в скрипте будет команда вызова скрипта
     */
    public String execute (ICollectionManager icm, String line, String arg, Route route, Driver driver, String username) throws NoExecuteScriptInScript {
        if (!dequeHashMap.containsKey(username)) dequeHashMap.put(username, new ArrayDeque<>());
        Command command = man.get(line);
        if (command == null) {
            return "Неверное имя команды : " + line;
        } else {
            String result = command.execute(icm, arg, route, driver, username);
            addHistory(line, username);
            return result;
        }
    }

    /**
     * Возвращает все команды из реестра.
     *
     * @return Список всех команд.
     */
    public List<Command> getAllCommands ( ) {
        return man.keySet( ).stream( ).map(x -> (man.get(x))).collect(Collectors.toList( ));
    }

    /**
     * Метод добавляет команду в очердь после ее исполнения
     *
     * @param commandName имя команды
     */
    private void addHistory (String commandName, String username) {
        if (dequeHashMap.get(username).size( ) >= 7) {
            dequeHashMap.get(username).pollLast( );
        }
        dequeHashMap.get(username).addFirst(commandName);
    }

    public ArrayDeque<String> getHistory (String username) {
        return dequeHashMap.get(username);
    }

    public HashMap<String, String> getAvailable ( ) {
        return available;
    }


}