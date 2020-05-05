package server.receiver.collection;

import java.util.Date;
import java.util.List;

/**
 * Интерфейс для коллекции.
 *
 * @param <T> Тип элеметов коллекции
 */
public interface ICollection <T> {
    void add (T obj);
    void add (long id, T obj);

    Long giveMeId ( );

    void remove (T obj);

    int size ( );

    void clear ( );

    List<T> toList ( );

    Date getInitializationTime ( );

    Class<?> getCollectionClass ( );

}
