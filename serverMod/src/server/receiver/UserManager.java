//package server.receiver;
//
//import common.exceptions.NoCorrectInputException;
//import common.generatedClasses.*;
//
//import java.io.IOException;
//import java.io.Reader;
//import java.io.Writer;
//import java.util.NoSuchElementException;
//import java.util.Scanner;
//
//public class UserManager {
//    private Reader reader;
//    private Writer writer;
//    private Scanner scanner;
//    private boolean manualInput;
//
//    public UserManager(Reader reader, Writer writer, boolean manualInput) {
//        this.reader = reader;
//        this.writer = writer;
//        this.scanner = new Scanner(reader);
//        this.manualInput = manualInput;
//    }
//
//    /**
//     * Метод считывающий из потока строку
//     *
//     * @return строка
//     */
//    public String read() {
//        return scanner.nextLine();
//    }
//
//    /**
//     * Выводит в поток вывода строку.
//     *
//     * @param message строка для вывода.
//     */
//    public void write(String message) {
//        try {
//            writer.write(message);
//            writer.flush();
//        } catch (IOException e) {
//            System.out.print("Ошибка при выводе(( ");
//        }
//    }
//
//    /**
//     * Выводит в поток вывода строку с добавлением символа переноса.
//     *
//     * @param message строка для вывода.
//     */
//    public void writeln(String message) {
//        write(message + "\n");
//    }
//
//    /**
//     * Метод возрващающий есть ли что считывать из входного потока.
//     *
//     * @return Есть ли ещё что считывать.
//     */
//    public boolean hasNextLine() {
//        return scanner.hasNextLine();
//    }
//
//    public Route readRoute() throws NoSuchElementException {
//            String name;
//            do {
//                name = readString("Введите название маршрута: ", false);
//            } while (name.isEmpty());
//            Coordinates coordinates = readCoordinates();
//            Location to = readLocationTo();
//            Location from = readLocationFrom();
//            Float distance = parseFloatInputWithParameters("Введите длину маршрута больше 1: ", 1.0f, Float.POSITIVE_INFINITY);
//            return new Route(name, coordinates, from, to, distance);
//    }
//
//    public Location readLocationTo() {
//        String name = readString("Введите название места отправления: ", true);
//        Long x = parseLongInput("Введите координату места отправления x (Long): ");
//        Long y = parseLongInput("Введите координату места отправления y (Long): ");
//        return new Location(x, y, name);
//    }
//
//    public Location readLocationFrom(){
//        String name = readString("Введите название места прибытия: ", true);
//        Long x = parseLongInput("Введите координату места прибытия x (Long): ");
//        Long y = parseLongInput("Введите координату места прибытия y (Long): ");
//        return new Location(x, y, name);
//    }
//
//
//    public Coordinates readCoordinates() {
//        Long x = parseLongInput("Введите текущую координату местанахождения x (Long): ");
//        Integer y = parseIntInput("Введите текущую координату местанахождения y (int): ");
//        return new Coordinates(x, y);
//    }
//
//    /**
//     * Метод проверяет строоку на числовое значение
//     *
//     * @param input строка которую парсим
//     * @return true это значние Float
//     */
//
//    public boolean checkFloatInput(String input) {
//        try {
//            if (input == null) {
//                writeln("Это значение вам не null");
//                return false;
//            }
//            Float.parseFloat(input);
//            return true;
//        } catch (NumberFormatException e) {
//            writeln("Это значение вам не Float");
//            return false;
//        }
//    }
//
//    /**
//     * Метод проверяет строоку на числовое значение
//     *
//     * @param input строка которую парсим
//     * @return true это значние int
//     */
//    public boolean checkIntInput(String input) {
//        try {
//            if (input == null) {
//                writeln("Это значение вам не null");
//                return false;
//            }
//            Integer.parseInt(input);
//            return true;
//        } catch (NumberFormatException e) {
//            writeln("Это значение  вам не Integer");
//            return false;
//        }
//    }
//
//    /**
//     * Метод проверяет строоку на числовое значение
//     *
//     * @param input строка которую парсим
//     * @return true это значние Long
//     */
//    public boolean checkLongInput(String input) {
//        try {
//            if (input == null) {
//                writeln("Это значение вам не null");
//                return false;
//            }
//            Long.parseLong(input);
//            return true;
//        } catch (NumberFormatException e) {
//            writeln("Это значение  вам не Long");
//            return false;
//        }
//    }
//
//    /**
//     * Метод парсит в определенном диапазоне
//     */
//
//    public Float parseFloatInputWithParameters(String message, float min, float max) {
//        Float res;
//        do {
//            res = parseFloatInput(message);
//        } while (Float.sum(res, -min) < 0.001f || Float.sum(res, -max) > 0.001f);
//        return res;
//    }
//
//    /**
//     * Метод парсит строку в Int
//     */
//
//    public Float parseFloatInput(String message) {
//        String res;
//        do {
//            res = readString(message, false);
//        } while (!checkFloatInput(res));
//        return Float.parseFloat(res);
//    }
//
//    /**
//     * Метод парсит строку в Int
//     */
//
//    public Integer parseIntInput(String message) {
//        String res;
//        do {
//            res = readString(message, false);
//        } while (!checkIntInput(res));
//        return Integer.parseInt(res);
//    }
//
//    /**
//     * Метод проверяет строоку на Long
//     */
//
//    public Long parseLongInput(String message) {
//        String res;
//        do {
//            res = readString(message, false);
//        } while (!checkLongInput(res));
//        return Long.parseLong(res);
//    }
//
//
//    /**
//     * Метод проверяет строоку на null
//     *
//     * @param message  Сообщение для пользователя
//     * @param nullable Флаг. True - если мы допускаем пустой ввод от пользователя. False - если нам надо добиться от него не пустого ввода.
//     */
//
//    public String readString(String message, boolean nullable) throws NoSuchElementException {
//        String result = "";
//            do {
//                if (result == null) {
//                    writeln("Введите не пустую строку.");
//                }
//                if (manualInput) {
//                    write(message);
//                }
//                result = scanner.nextLine();
//                result = result.isEmpty() ? null : result;
//            } while (manualInput && !nullable && result == null);
//            if (!manualInput && result == null) {
//                throw new NoCorrectInputException("Это значение не может быть null.");
//            }
//        return result;
//    }
//
//}
