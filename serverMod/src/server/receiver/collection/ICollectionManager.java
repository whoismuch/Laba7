package server.receiver.collection;

import com.google.gson.JsonSyntaxException;
import common.exceptions.NoPermissionsException;
import common.generatedClasses.Route;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Интерфейс для работы с коллекцией.
 */
public interface ICollectionManager {
    String info ( );

    void add (Route route);

    void clear ( );

    boolean removeById (long id);

    String show ( );

    List<Route> filterLessThanDistance (Float distance);



    void removeGreater (Route route);

    void removeLower (Route route);

    public String printAscending ( );

    boolean updateId (long id, Route route);

    Float sumOfDistance ( );

    int size ( );

    List<Route> sort (Route route);

    List<Route> sort ();

    void save (String path) throws JsonSyntaxException, NullPointerException, FileNotFoundException, NoPermissionsException, IOException;

    void load (String path) throws JsonSyntaxException, NullPointerException, FileNotFoundException, NoPermissionsException, IOException;
}
