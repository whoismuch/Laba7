package server.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс конвертер ZonedDateTime
 */
public class GsonZonedDateTimeConverter implements JsonDeserializer<ZonedDateTime>, JsonSerializer<ZonedDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    @Override
    public ZonedDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        return formatter.parse(jsonElement.getAsString(), ZonedDateTime::from);
    }

    @Override
    public JsonElement serialize(ZonedDateTime zonedDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(formatter.format(zonedDateTime));
    }
}
