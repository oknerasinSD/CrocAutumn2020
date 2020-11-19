package ru.croc.java.school.lesson6_2;

import java.util.concurrent.Callable;

/**
 * Задача для отдельного потока по поиску макисмального элемента массива.
 */
public class SearchForMaxTask implements Callable<Integer> {

    /** Массив для поиска максмума */
    private int[] array;

    /** Стартовый элемент для данной конкретной задачи */
    private int start;

    /** Конечный элемент для данной конкретной задачи */
    private int end;

    /**
     * Конструктор класса, описывающего задачу для отедльного потока по поиску максимального элемента в массиве.
     * @param array - массив, в котором ищется максимумю
     * @param start - начальный элемент диапазона, за который отвечает текущая задача.
     * @param end - конечный элемент диапазона, за который отвечает текущая задача.
     */
    public SearchForMaxTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    /**
     * Поиск максимума на заданном интервале массива.
     */
    public Integer call() throws Exception {
        int max = array[start];
        for (int i = start; i < end; ++i) {
            if (max < array[i]) {
                max = array[i];
            }
        }
        return max;
    }
}
