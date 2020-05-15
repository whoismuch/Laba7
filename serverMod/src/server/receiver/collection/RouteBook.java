package server.receiver.collection;

import common.generatedClasses.Route;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация CollectionManager. Хранит маршруты во множестве.
 */
public class RouteBook implements ICollection<Route> {

    /**
     * Поле коллекция объектов класса Route
     */
    public LinkedHashSet<Route> routes = new LinkedHashSet<>();
    /**
     * Поле уникальный номер маршрута
     */
    private Long id = new Long(0);
    private Date creationDate;

    /**
     * Конструктор, который инициализирует время создания коллекции.
     */
    public RouteBook() {
        this.creationDate = new Date();
    }

    /**
     * Метод, добавляющий маршрут в коллекцию и генерирует уникальный ID.
     *
     * @param obj маршрут
     */

    @Override
    public void add(Route obj) {
        obj.setId(this.giveMeId());
        routes.add(obj);
    }

    @Override
    public Long getId ( ) {
        return id;
    }

    /**
     * Добавляет объект с определенным id
     *
     * @param id позиция
     * @param obj объект
     */
    @Override
    public void add(Long id, Route obj) {
        obj.setId(id);
        routes.add(obj);
    }

    @Override
    public Long giveMeId() {
        id++;
        return id;
    }

    @Override
    public void remove(Route obj) {
        routes.remove(obj);
    }

    /**
     * Возарщает кол-во элементоов в коллекции.
     *
     * @return кол-во элементов в коллекции.
     */
    @Override
    public int size() {
        return routes.size();
    }

    /**
     * Очищает коллекцию.
     */
    @Override
    public void clear() {
        routes.clear();
    }

    /**
     * Преобразовывает коллекцию в ArrayList.
     *
     * @return List, с элементами из стэка
     */
    @Override
    public List<Route> toList() {
        return routes.stream().collect(Collectors.toList());
    }

    @Override
    public Date getInitializationTime() {
        return creationDate;
    }

    @Override
    public Class<?> getCollectionClass() {
        return routes.getClass();
    }

    @Override
    public LinkedHashSet<Route> getCollection ( ) {
        return routes;
    }

    @Override
    public void setId (Long id) {
        this.id = id;
    }
}
