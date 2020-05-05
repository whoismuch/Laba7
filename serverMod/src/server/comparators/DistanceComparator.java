package server.comparators;

import common.generatedClasses.Route;

import java.util.Comparator;

/**
 * Класс, который позволяет сравнивать объекты по критерию длины маршрута (расстояния)
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
public class DistanceComparator implements Comparator<Route> {

    /**
     * Метод из интерфейса Comparator, предоставляющий возможность сравнивать объекты класса Route по какому-то критерию
     * @param a 1-й объект класса Route
     * @param b 2-й объект класса Route
     * @return 1, если 1-й объект больше 2-го, -1 - если меньше, 0 - если равны
     */
    @Override
    public int compare(Route a, Route b) {
        return a.getDistance().compareTo(b.getDistance());
    }
}