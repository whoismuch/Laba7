package server.comparators;

import common.generatedClasses.Route;

import java.util.Comparator;

/**
 * Класс, который позволяет сравнивать объекты по критерию модуля разности координаты y начальной локации маршрута и координаты y конечной локации маршрута
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
public class YFromYToComparator implements Comparator<Route> {

    /**
     * Метод из интерфейса Comparator, предоставляющий возможность сравнивать объекты класса Route по какому-то критерию
     * @param a 1-й объект класса Route
     * @param b 2-й объект класса Route
     * @return 1, если 1-й объект больше 2-го, -1 - если меньше, 0 - если равны
     */
    @Override
    public int compare(Route a, Route b) {
        Long y1 = Math.abs(a.getFrom().getY() - a.getTo().getY());
        Long y2 = Math.abs(b.getFrom().getY() - a.getTo().getY());
        return y1.compareTo(y2);
    }
}