package common.generatedClasses;


import java.io.Serializable;

/**
 * Класс текущих координат со свойствами <b>x</b>, <b>y</b>
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */

public class Coordinates implements Serializable {
    /** Поле текущая координата x */
    private Long x;
    /** Поле текущая координата y */
    private int y;

    /**
     * Конструктор - задает текущие координаты с помощью заданных значений
     * @param x текущая координата x
     * @param y текущая координата y
     */
    public Coordinates(Long x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Метод получения строкового представления местоположения переопрелен
     *
     * @return строковое представления местоположения
     */
    @Override
    public String toString() {
        return "Coordinates{x=" + this.x + ", y=" + this.y + '}';
    }

    /**
     * Метод получения значения поля (@link Coordinates#x)
     * @return x текущую координату x
     */
    public Long getX() {
        return this.x;
    }

    /**
     * Метод получения значения поля (@link Coordinates#y)
     * @return y текущую координату y
     */
    public int getY() {
        return this.y;
    }
}
