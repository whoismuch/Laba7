package server.comparators;

import common.generatedClasses.Route;

import java.util.Comparator;

/**
 * Класс, который позволяет сравнивать объекты по критерию модуля разности текущей координаты y и координаты y начальной локации маршрута
 *
 * @author Саня Малета и Хумай Байрамова
 * @version final
 */
public class YyFromComparator implements Comparator<Route> {

    /**
     * Метод из интерфейса Comparator, предоставляющий возможность сравнивать объекты класса Route по какому-то критерию
     * @param a 1-й объект класса Route
     * @param b 2-й объект класса Route
     * @return 1, если 1-й объект больше 2-го, -1 - если меньше, 0 - если равны
     */
    @Override
    public int compare(Route a, Route b) {
        Long y1 = Math.abs(a.getCoordinates().getY() - a.getFrom().getY());
        Long y2 = Math.abs(b.getCoordinates().getY() - a.getFrom().getY());
        return y1.compareTo(y2);
    }
}