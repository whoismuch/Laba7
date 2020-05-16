package client;

import common.exceptions.NoCorrectInputException;
import common.exceptions.NoPermissionsException;
import common.generatedClasses.*;

import java.io.*;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserManager {
    private static Writer writer;
    private static Scanner scanner;
    private boolean manualInput;
    private String finalScript;

    private HashMap<String,String> available = new HashMap<>();

    public UserManager(Scanner scanner, Writer writer, boolean manualInput) {
        this.writer = writer;
        this.scanner = scanner;
        this.manualInput = manualInput;
    }

    /**
     * Метод считывающий из потока строку
     *
     * @return строка
     */
    public String read() {
        return scanner.nextLine();
    }

    /**
     * Выводит в поток вывода строку.
     *
     * @param message строка для вывода.
     */
    public void write(String message) {
        try {
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            System.out.print("Ошибка при выводе(( ");
        }
    }

    /**
     * Выводит в поток вывода строку с добавлением символа переноса.
     *
     * @param message строка для вывода.
     */
    public void writeln(String message) {
        write(message + "\n");
    }

    /**
     * Метод возрващающий есть ли что считывать из входного потока.
     *
     * @return Есть ли ещё что считывать.
     */

    public Route readRoute() throws NoSuchElementException {
        String name;
        do {
            name = readString("Введите название маршрута: ", false);
        } while (name.isEmpty());
        Coordinates coordinates = readCoordinates();
        Location from = readLocationFrom();
        Location to = readLocationTo();
        Float distance = parseFloatInputWithParameters("Введите длину маршрута больше 1: ", 1.0f, Float.POSITIVE_INFINITY);
        return new Route(name, coordinates, from, to, distance);
    }

    public Location readLocationFrom() {
        String name = readString("Введите название места отправления: ", true);
        Long x = parseLongInput("Введите координату места отправления x (Long): ");
        Long y = parseLongInput("Введите координату места отправления y (Long): ");
        return new Location(name, x, y);
    }

    public Location readLocationTo(){
        String name = readString("Введите название места прибытия: ", true);
        Long x = parseLongInput("Введите координату места прибытия x (Long): ");
        Long y = parseLongInput("Введите координату места прибытия y (Long): ");
        return new Location(name, x, y);
    }


    public Coordinates readCoordinates() {
        Long x = parseLongInput("Введите текущую координату местанахождения x (Long): ");
        int y = parseIntInput("Введите текущую координату местанахождения y (int): ");
        return new Coordinates(x, y);
    }

    /**
     * Метод проверяет строоку на числовое значение
     *
     * @param input строка которую парсим
     * @return true это значние Float
     */

    public boolean checkFloatInput(String input) {
        try {
            if (input == null) {
                writeln("Это значение вам не null");
                return false;
            }
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            writeln("Это значение вам не Float");
            return false;
        }
    }

    /**
     * Метод проверяет строоку на числовое значение
     *
     * @param input строка которую парсим
     * @return true это значние int
     */
    public boolean checkIntInput(String input) {
        try {
            if (input == null) {
                writeln("Это значение вам не null");
                return false;
            }
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            writeln("Это значение  вам не Integer");
            return false;
        }
    }

    /**
     * Метод проверяет строоку на числовое значение
     *
     * @param input строка которую парсим
     * @return true это значние Long
     */
    public boolean checkLongInput(String input) {
        try {
            if (input == null) {
                writeln("Это значение вам не null");
                return false;
            }
            Long.parseLong(input);
            return true;
        } catch (NumberFormatException e) {
            writeln("Это значение  вам не Long");
            return false;
        }
    }

    public boolean checkCommandName(String command){
        if ((available.get(command) == null)) {
            writeln("Неверное имя команды: " + command);
            writeln("Введите 'help', чтобы получить список доступных команд");
            return false;
        }
        return true;
    }

    public boolean checkCommandNameForScript(String command) {
        if ((available.get(command) == null)) {
            return false;
        }
        return true;
    }

    public boolean checkElement(String command) {
        return (available.get(command).endsWith("e"));
    }

    public boolean checkFieldsForScript(Scanner scanner) {
        try {
            String name = scanner.nextLine( );
            Long x = Long.parseLong(scanner.nextLine( ));
            int y = Integer.parseInt(scanner.nextLine( ));
            String nameFrom = scanner.nextLine( );
            Long xFrom = Long.parseLong(scanner.nextLine( ));
            Long yFrom = Long.parseLong(scanner.nextLine( ));
            String nameTo = scanner.nextLine( );
            Long xTo = Long.parseLong(scanner.nextLine( ));
            Long yTo = Long.parseLong(scanner.nextLine( ));
            Float distance = Float.parseFloat(scanner.nextLine( ));
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean checkFile(String command) { return available.get(command).startsWith("File");}

    public boolean checkArgForScript (String command, String arg) {
        String trueArg = available.get(command);
        if (!trueArg.equals("null") && !trueArg.equals("e") && arg == null) {
            return false;
        }
        if ((trueArg.equals("null") || trueArg.equals("e")) && arg != null) {
            return false;
        }
        if (trueArg.startsWith("Float")) {
            try {
                Float.parseFloat(arg);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        if (trueArg.startsWith("Long")) {
            try {
                Long.parseLong(arg);
                return true;
            } catch (NumberFormatException e) {
            }
        }

        return true;
    }


    public boolean checkArg(String command, String arg) {
        String trueArg = available.get(command);
        if (!trueArg.equals("null") && !trueArg.equals("e") && arg == null) {
            writeln("А где аргумент? \n Попробуйте, позязя, еще раз");
            return false;
        }
        if ((trueArg.equals("null") || trueArg.equals("e")) && arg != null) {
                writeln("И зачем аргумент? Вы понимаете, что наделали?");
                return false;
        }
        if (trueArg.startsWith("Float")) {
                try {
                    Float.parseFloat(arg);
                    return true;
                } catch (NumberFormatException e) {
                    writeln("Мне жаль, но аргумент должен быть Float");
                    return false;
                }
        }
        if (trueArg.startsWith("Long")) {
            try {
                Long.parseLong(arg);
                return true;
            } catch (NumberFormatException e) {
                writeln("Мне жаль, но аргумент должен быть Long");
            }
        }
        
        return true;
    }

    public  int  checkContentOfFile (String arg, int commandN) {
        try {
            if (arg.equals("")) throw new NullPointerException( );
            CharArrayReader car = new CharArrayReader(arg.toCharArray( ));
            Scanner scanner = new Scanner(car);
            int commandNumber = commandN;
            while (scanner.hasNextLine( )) {
                commandNumber += 1;
                String justLine = scanner.nextLine( );
                String line = justLine.trim( );
                String commandname = line;
                String argue = null;
                if (line.contains(" ")) {
                    commandname = line.substring(0, line.indexOf(" "));
                    argue = (line.substring(line.indexOf(" "))).trim( );
                }


                if (!checkCommandNameForScript(commandname) || !checkArgForScript(commandname, argue)) {
                    writeln("В строке № " + commandNumber + " ошибка");
                    writeln("Если у вас присутсвуют вложенные скрипты, то нумерация строк выполняется согласно вложенности");
                    return 0;
                }

                if (checkElement(commandname)) {
                    if (!checkFieldsForScript(scanner)) {
                        writeln("В строке № " + commandNumber + " ошибка (неправильный ввод объекта в дальнейшем)");
                        writeln("Если у вас присутсвуют вложенные скрипты, то нумерация строк выполняется согласно вложенности");
                        return 0;
                    }
                }

                if (checkFile(commandname)) {
                    String previousX = arg;
                    finalScript = contentOfFile(argue);
                    int innerCommandNumber = checkContentOfFile(finalScript, commandNumber);
                    if (innerCommandNumber == 0) return 0;
                    commandNumber = innerCommandNumber;
                    commandNumber -= 1;
                    finalContentOfFile(previousX, justLine, finalScript);
                }
            }
            return commandNumber;
        } catch (NullPointerException e) {
            writeln("Эмм, а чоита скрипт пустой?");
            return 0;
        }
    }

    public void finalContentOfFile(String previousArg, String lineWithExScript, String presentArg ) {
        finalScript = previousArg.replace(lineWithExScript, presentArg);
    }

    public String contentOfFile (String arg) {
        try {
            CharArrayWriter caw = new CharArrayWriter( );
            CharBuffer charBuffer = CharBuffer.allocate(10);
            File file = new File(arg);

            if (!file.exists( )) throw new FileNotFoundException();
            else if (!file.canRead( )) throw new NoPermissionsException("бе");

            FileReader fileReader = new FileReader(file);
            int n = 0;
            while ((n = fileReader.read(charBuffer)) > 0) {
                charBuffer.flip( );
                caw.write(charBuffer.array( ), 0, n);
            }

            return caw.toString( );


        } catch (NoPermissionsException e) {
            writeln("Недостаточно прав для чтения скрипта");
            return null;
        } catch (FileNotFoundException e) {
            writeln("Файла со скриптом по указанному пути не существует");
            return null;
        } catch (NullPointerException e) {
            writeln("Файл пуст!");
            return null;
        } catch (IOException e) {
            writeln("Ой, временные неполадки");
            return null;
        }
    }

        /**
     * Метод парсит в определенном диапазоне
     */

    public Float parseFloatInputWithParameters(String message, float min, float max) {
        Float res;
        do {
            res = parseFloatInput(message);
        } while (Float.sum(res, -min) < 0.001f || Float.sum(res, -max) > 0.001f);
        return res;
    }

    /**
     * Метод парсит строку в Int
     */

    public Float parseFloatInput(String message) {
        String res;
        do {
            res = readString(message, false);
        } while (!checkFloatInput(res));
        return Float.parseFloat(res);
    }

    /**
     * Метод парсит строку в Int
     */

    public Integer parseIntInput(String message) {
        String res;
        do {
            res = readString(message, false);
        } while (!checkIntInput(res));
        return Integer.parseInt(res);
    }

    /**
     * Метод проверяет строоку на Long
     */

    public Long parseLongInput(String message) {
        String res;
        do {
            res = readString(message, false);
        } while (!checkLongInput(res));
        return Long.parseLong(res);
    }


    public static void setScanner (Scanner scanner) {
        UserManager.scanner = scanner;
    }

    /**
     * Метод проверяет строоку на null
     *
     * @param message  Сообщение для пользователя
     * @param nullable Флаг. True - если мы допускаем пустой ввод от пользователя. False - если нам надо добиться от него не пустого ввода.
     */

    public String readString(String message, boolean nullable) throws NoSuchElementException {
        String result = "";
        do {
            if (result == null) {
                writeln("Введите не пустую строку.");
            }
            if (manualInput) {
                write(message);
            }
            result = scanner.nextLine();
            result = result.trim();
            result = result.isEmpty() ? null : result;
        } while (manualInput && !nullable && result == null);
        if (!manualInput && result == null) {
            throw new NoCorrectInputException("Это значение не может быть null.");
        }
        return result;
    }

    public String readChoice(String message, boolean nullable) throws NoSuchElementException {
        String result = null;
        while (manualInput && !nullable && result == null) {
            if (manualInput) {
                write(message);
            }
            result = scanner.nextLine();
            result= result.trim();
            if (result == null) {
                writeln("Введите не пустую строку");
            }
            if (!result.equals("Регистрация") && !result.equals("Авторизация")) {
                writeln("Введите коректный ответ");
                readChoice(message, nullable);
            }
        }
        if (!manualInput && result == null) {
            throw new NoCorrectInputException("Это значение не может быть null.");
        }
        return result;
    }

    public HashMap<String, String> getAvailable ( ) {
        return available;
    }

    public void setAvailable (HashMap<String, String> available) {
        this.available = available;
    }

    public void setFinalScript (String finalScript) {
        this.finalScript = finalScript;
    }

    public String getFinalScript ( ) {
        return finalScript;
    }
}
