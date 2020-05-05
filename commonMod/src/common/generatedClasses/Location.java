package common.generatedClasses;


import java.io.Serializable;
import java.util.Objects;

/**
 * Класс местоположения со свойствами <b>x</b>, <b>y</b>, <b>name</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */


public class Location implements Serializable {
    /** Поле координата x местоположения */
    private long x;
    /** Поле координата y местоположения */
    private Long y; //Поле не может быть null
    /** Поле название местоположения */
    private String name; //Поле может быть null

    /**
     * Конструктор - задает новое местоположение с заданными значениями
     * @param x координата x местоположения
     * @param y координата y местоположения
     * @param name имя местоположения
     */
    public Location(String name, long x, Long y) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    /**
     * Метод получения строкового представления местоположения
     * @return строковое представления местоположения
     */
    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Метод получения значения поля (@link Location#x)
     * @return x координату x местоположения
     */

    public long getX() {
        return x;
    }

    /**
     * Метод получения значения поля (@link Location#y)
     * @return y координату y местоположения
     */

    public Long getY() {
        return y;
    }

    /**
     * Метод получения значения поля (@link Location#name)
     * @return name название местоположения
     */

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return getX() == location.getX() &&
                Objects.equals(getY(), location.getY()) &&
                Objects.equals(getName(), location.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}