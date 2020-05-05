package server.json;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import common.exceptions.NoPermissionsException;
import common.generatedClasses.Route;
import server.receiver.collection.ICollection;

import java.io.*;
import java.time.ZonedDateTime;

public class JsonSerialization {

    public void saveCollectionToFile(ICollection<Route> routeBook, String path) throws JsonSyntaxException, NullPointerException, FileNotFoundException, NoPermissionsException, IOException {
        File file = new File(path);
        if (file.canWrite()) {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new GsonZonedDateTimeConverter()).setPrettyPrinting().create().toJson(routeBook.toList(), bufferedWriter);
            bufferedWriter.close();
        } else {
            throw new NoPermissionsException("Недостаточно прав для работы с файлом");
        }
    }
}

