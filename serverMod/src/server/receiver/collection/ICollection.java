package server.receiver.collection;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Интерфейс для коллекции.
 *
 * @param <T> Тип элеметов коллекции
 */
public interface ICollection <T> {
    void add (T obj);
    void add (Long id, T obj);

    Long giveMeId ( );

    void remove (T obj);

    int size ( );

    void clear ( );

    List<T> toList ( );

    Date getInitializationTime ( );

    Class<?> getCollectionClass ( );

    LinkedHashSet<T> getCollection();

    void setId(Long id);

    Long getId();

}
