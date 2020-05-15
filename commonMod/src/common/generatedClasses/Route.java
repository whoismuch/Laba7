package common.generatedClasses;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Класс маршрутов со свойствами <b>name</b>, <b>id</b>, <b>creationDate</b>, <b>from</b>, <b>to</b>, <b>distance</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
public class Route implements Comparable<Route>, Serializable {
    /**
     * Поле имя маршрута
     */
    private String name; //Поле не может быть null, Строка не может быть пустой
    /**
     * Поле уникальный номер маршрута
     */
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    /**
     * Поле координаты(текущее местоположение)
     */
    private Coordinates coordinates; //Поле не может быть null
    /**
     * Поле дата и время GMT
     */
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    /**
     * Поле начальная локация маршрута
     */
    private Location from; //Поле не может быть null
    /**
     * Поле конечная локация маршрута
     */
    private Location to; //Поле не может быть null
    /**
     * Поле длина маршрута(расстояние)
     */
    private Float distance; //Поле не может быть null, Значение поля должно быть больше 1

    /**
     * Конструктор - задает новый маршрут с заданными значениями
     *
     * @param name        имя маршрута
     * @param coordinates координаты (текущее местоположение)
     * @param from        начальная локация маршрута
     * @param to          конечная локация маршрута
     * @param distance    длина маршрута (расстояние)
     */
    public Route (String name, Coordinates coordinates, Location from, Location to, Float distance) {
        this.name = name;
        this.coordinates = coordinates;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.creationDate = ZonedDateTime.now( );
    }

    public Route (String name, Long id, Coordinates coordinates, ZonedDateTime creationDate, Location from, Location to, Float distance) {
        setId(id);
        this.name = name;
        this.coordinates = coordinates;
        this.from = from;
        this.to = to;
        this.distance = distance;
        this.creationDate = creationDate;
    }

    /**
     * Метод получения значения поля (@link Route#id)
     *
     * @return id возвращает уникальный номер маршрута
     */
    public Long getId ( ) {
        return id;
    }

    /**
     * Метод получения значения поля (@link Route#distance)
     *
     * @return distance возвращает длину маршрута (расстояние)
     */
    public Float getDistance ( ) {
        return distance;
    }

    /**
     * Метод получения строкового представления местоположения
     *
     * @return строковое представления местоположения
     */

    @Override
    public String toString ( ) {
        return "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", from=" + from +
                ", to=" + to +
                ", distance=" + distance +
                '}';
    }

    /**
     * Метод из интерфейса Comparable, предоставляющий возможность объекту быть сравнимым по какому-то критерию
     *
     * @param r объект класса Route
     * @return 1, если текущий объект больше заявленного, -1 - если меньше, 0 - если равны
     */
    @Override
    public int compareTo (Route r) {
        return distance.compareTo(r.getDistance( ));
    }


    /**
     * Метод обновления уникального номера маршрута (@link Route#id)
     */
    public void setId (Long id) {
        this.id = id;
    }

    /**
     * Метод получения значения поля (@link Route#name)
     *
     * @return name возвращает имя маршрута
     */
    public String getName ( ) {
        return name;
    }


    /**
     * Метод получения значения поля (@link Route#coordinates)
     *
     * @return coordinates возвращает объект класса Coordinate (координаты текущего местоположения)
     */
    public Coordinates getCoordinates ( ) {
        return coordinates;
    }

    /**
     * Метод получения значения поля (@link Route#from)
     *
     * @return from возвращает объект класса Location (начальная локация маршрута)
     */

    public Location getFrom ( ) {
        return from;
    }

    /**
     * Метод получения значения поля (@link Route#to)
     *
     * @return to возвращает объект класса Location (конечная локация маршрута)
     */
    public Location getTo ( ) {
        return to;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        Route route = (Route) o;
        return Objects.equals(getName( ), route.getName( )) &&
                Objects.equals(getCoordinates( ), route.getCoordinates( )) &&
                Objects.equals(getFrom( ), route.getFrom( )) &&
                Objects.equals(getTo( ), route.getTo( )) &&
                Objects.equals(getDistance( ), route.getDistance( ));
    }

    @Override
    public int hashCode ( ) {
        return Objects.hash(getFrom( ), getTo( ), getDistance( ));
    }

    public ZonedDateTime getCreationDate ( ) {
        return creationDate;
    }

}