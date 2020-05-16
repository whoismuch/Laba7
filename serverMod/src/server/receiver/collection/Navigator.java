package server.receiver.collection;


import server.armory.DataBase;
import server.comparators.*;
import common.generatedClasses.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс для работы с коллекцией
 */
public class Navigator implements ICollectionManager {

    private ICollection<Route> routeBook;
    private ReadWriteLock lock;
    private DataBase db;

    public Navigator(ICollection<Route> routeBook, DataBase db) {
        this.routeBook = routeBook;
        lock = new ReentrantReadWriteLock();
        this.db = db;
    }

    /**
     * Метод выводит информацию о коллекции
     *
     * @return строка с информацией о коллекции
     */
    @Override
    public String info() {
        lock.readLock().lock();
        try {
            StringBuilder stringBuilder = new StringBuilder( );
            stringBuilder.append("Время инициализации коллекции: ").append(routeBook.getInitializationTime( ).toString( )).append('\n');
            stringBuilder.append("Количество элементов в коллекции: ").append(routeBook.size( )).append('\n');
            stringBuilder.append("Тип коллекции: ").append(routeBook.getCollectionClass( ).getName( ));
            return stringBuilder.toString( );
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Добавляет маршрут в коллекцию.
     *
     * @param route маршрут, которая будет добавлена.
     */
    @Override
    public void add(Route route, String username) {
        lock.writeLock().lock();
        try {
            if (db.add(route, username)) routeBook.add(route);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Очищает коллекцию.
     */
    @Override
    public void clear(String username) {
        lock.writeLock().lock();
        try {
            db.deleteRoutes(username);
            routeBook.clear();
            loadBegin();
            System.out.println(routeBook.getId());
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Метод удаляет элемент коллекции, id которого равен заданному
     * @param id элемента который удаляем
     * @return true - если элемент существовал и false - в ином случае
     */
    @Override
    public boolean removeById(long id, String username) {
        lock.writeLock().lock();
        try {
            if (db.removeById(id, username) > 0) {
                routeBook.clear();
                load();
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
    /**
     * Возвращает все элементы коллекции.
     * @return Строку со всеми элементами коллекции.
     */
    @Override
    public String show() {
        lock.readLock().lock();
        try {
            return Stream.builder( ).add("Элементов в коллекции: " + routeBook.size( )).add(routeBook.toList( ).stream( ).map(x -> x.toString( )).collect(Collectors.joining("\n"))).build( ).map(x -> x.toString( )).collect(Collectors.joining("\n"));
        } finally {
            lock.readLock().unlock();
        }
    }
    /**
     * Генерирует новый id
     * @param id   идентификатор объекта
     * @param route
     * @return true - если объект был найден и обновлён, false -  в ином случае
     */
    @Override
    public boolean updateId(long id, Route route, String username) {
        lock.writeLock().lock();
        try {
            if (db.updateId(id,route, username)) {
                routeBook.clear();
                load();
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
    /**
     * Метод выводит сумму значений поля distance для всех элементов коллекции
     */
    @Override
    public Float sumOfDistance() {
        lock.readLock().lock();
        try {
            return routeBook.toList( ).stream( ).reduce(0f, (x, y) -> x + y.getDistance( ), (x, y) -> x + y);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Возвращает размер коллекции.
     *
     * @return кол-во элементов в коллекции.
     */
    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return routeBook.size( );
        } finally {
            lock.readLock().unlock();
        }
    }

//    @Override
//    public void save(String path) throws JsonSyntaxException, NullPointerException, FileNotFoundException, NoPermissionsException, IOException {
//        JsonSerialization jsonSerialization = new JsonSerialization();
//        jsonSerialization.saveCollectionToFile(routeBook, path);
//    }

//    @Override
//    public void load(String path) throws JsonSyntaxException, NullPointerException, FileNotFoundException, NoPermissionsException, IOException {
//        JsonDeserialization jsonDeserialization = new JsonDeserialization();
//        jsonDeserialization.loadCollectionFromFile(routeBook, path);
//    }

    /**
     * Метод сортирующий коллекцию обьъектов класса Route(маршрутов)
     *
    // * @param коллекция объектов класса Route(маршрутов)
     * @return routes2 возвращает отсортированную коллекцию объектов класса Route(маршрутов)
     */
    @Override
    public List<Route> sort(Route route) {
        List<Route> list = new ArrayList<>(routeBook.toList());
        list.add(route);
        return (list.stream().sorted(new NameComparator().thenComparing(new DistanceComparator()).thenComparing(new XxFromComparator()).thenComparing(new YyFromComparator()).thenComparing(new XFromXToComparator()).thenComparing(new YFromYToComparator())).collect(Collectors.toList()));
    }

    @Override
    public List<Route> sort() {
        return routeBook.toList().stream().sorted(new NameComparator().thenComparing(new DistanceComparator()).thenComparing(new XxFromComparator()).thenComparing(new YyFromComparator()).thenComparing(new XFromXToComparator()).thenComparing(new YFromYToComparator())).collect(Collectors.toList());
    }


    /**
     * Метод выводит элементы, значение поля distance которых меньше заданного
     *
     * @param distance значение для сравнения
     */
    @Override
    public List<Route> filterLessThanDistance(Float distance) {
        lock.readLock().lock();
        try {
            return routeBook.toList( ).stream( ).filter(x -> x.getDistance( ) < distance).collect(Collectors.toList( ));
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Метод удаляет из коллекции все элементы, превышающие заданный
     *
     * @param route эемент для сравнения
     */
    @Override
    public void removeGreater(Route route, String username) {
        lock.writeLock().lock();
        try {
            routeBook.toList( ).stream( ).filter(x -> sort(route).indexOf(x) > sort(route).indexOf(route)).forEach(x -> db.removeById(x.getId(), username));
            routeBook.clear();
            load();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Метод удаляет из коллекции все элементы, меньше, чем заданный
     *
     * @param route эемент для сравнения
     */
    @Override
    public void removeLower(Route route, String username) {
        lock.writeLock().lock();
        try {
            routeBook.toList( ).stream( ).filter(x -> sort(route).indexOf(x) < sort(route).indexOf(route)).forEach(x -> db.removeById(x.getId(), username));
            routeBook.clear();
            load();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Метод выводит элементы коллекции в порядке возрастания
     */
    @Override
    public String printAscending() {
        lock.readLock().lock();
        try {
            if (sort( ).size( ) == 0) {
                return "Коллекция пуста";
            } else {
                return sort( ).stream( ).map(x -> x.toString( )).collect(Collectors.joining("\n"));
            }
        } finally {
            lock.readLock().unlock();
        }

    }

    @Override
    public void load() {
        if (db.load(routeBook.getCollection()).equals(0)) {
            routeBook.setId(0L);
        }
    }

    @Override
    public void loadBegin() {
        routeBook.setId(db.load(routeBook.getCollection()));
    }
}
