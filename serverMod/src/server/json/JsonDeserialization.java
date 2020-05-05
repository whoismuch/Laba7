package server.json;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import common.exceptions.NoPermissionsException;
import common.generatedClasses.Route;
import server.receiver.collection.ICollection;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.LinkedHashSet;

public class JsonDeserialization {
    public void loadCollectionFromFile(ICollection<Route> routeBook, String path) throws JsonSyntaxException, NullPointerException, FileNotFoundException, NoPermissionsException, IOException {
        LinkedHashSet<Route> routesFile;
        File file = new File(path);
        if (!file.exists()) {
            throw new NoPermissionsException("Упс... У нас проблемки. Файла по заданному пути нет, коллекцию не выгрузить \n");
        } else if (!file.canRead()) {
            throw new NoPermissionsException("Упс... У нас проблемки. Недостаточно прав для работы с файлом, коллекцию не выгрузить \n");
        } else {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            routesFile = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new GsonZonedDateTimeConverter()).setPrettyPrinting().create().fromJson(bufferedReader, new TypeToken<LinkedHashSet<Route>>() {
            }.getType());
            routesFile.forEach(x -> routeBook.add(x));
            bufferedReader.close();
        }
    }
}
