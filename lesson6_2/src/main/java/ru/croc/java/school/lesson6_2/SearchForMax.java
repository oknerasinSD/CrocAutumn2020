package ru.croc.java.school.lesson6_2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Вспомогательный класс для многопоточного поиска максимума в массиве с помощью ExecutorService.
 */
public class SearchForMax {

    /** Максимальный размер массива для одной задачи */
    private static final int MAX_SIZE_PER_TASK = 100_000;

    /** Тред-пул, в котором выполняется расчет */
    private ExecutorService executorService;

    /** Массив, в котором ищется максимум */
    private int[] array;

    /** Список задач (получается рекурсивно по значению MAX_SIZE_PER_TASK) */
    List<Callable<Integer>> tasks;

    /**
     * Конструктор спомогательного класса для многопоточного поиска максимума в массиве с помощью ExecutorService.
     * @param array - массив, в котором ищется максимум.
     * @param numberOfThreads - количетсво потоков в тред-пуле.
     */
    public SearchForMax(int[] array, int numberOfThreads) {
        executorService = Executors.newFixedThreadPool(numberOfThreads);
        this.array = array;
        tasks = new ArrayList<>();
    }

    /**
     * Запуск поиска.
     * @return - максимальный элемент массива.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public int search() throws InterruptedException, ExecutionException {
        buildTasks(array, 0, array.length);
        List<Future<Integer>> futures = executorService.invokeAll(tasks);
        return findFuturesMax(futures);
    }

    /**
     * Рекурсивное построение списка задач: размер каждой задачи не превышает MAX_SIZE_PER_TASK.
     * @param array - массив для поиска максимума.
     * @param start - начальный элемент для конкретной задачи.
     * @param end - конечный элемент для конкретной задачи.
     */
    private void buildTasks(int[] array, int start, int end) {
        if (end - start <= MAX_SIZE_PER_TASK) {
            tasks.add(new SearchForMaxTask(array, start, end));
        } else {
            int middle = start + (end - start) / 2;
            buildTasks(array, start, middle);
            buildTasks(array, middle + 1, end);
        }
    }

    /**
     * Получение максимума из объектов Future после решения всех задач.
     * @param futures - список с результатами асинхронных вычислений.
     * @return - максимальный элемент массива.
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private int findFuturesMax(List<Future<Integer>> futures) throws ExecutionException, InterruptedException {
        int max = futures.get(0).get();
        for (Future<Integer> future : futures) {
            if (future.get() > max) {
                max = future.get();
            }
        }
        return max;
    }
}
