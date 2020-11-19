package ru.croc.java.school.lesson6_2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class SearchForMaxTest {

    @Test
    public void testSearch() throws ExecutionException, InterruptedException {
        int[] array = buildArray(10_000_000);
        SearchForMax searchForMax = new SearchForMax(array, 4);
        Assertions.assertEquals(search(array), searchForMax.search());
    }

    @Test
    public void testIllegalArraySize() {
        int[] array = buildArray(10_000_001);
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {new SearchForMax(buildArray(10_000_001), 4);}
                );
    }

    /**
     * Однопоточный метод поиска максимума для проверки корректности работы многопоточного алгоритма.
     * @param array - массив.
     * @return - максимальный элемент массива.
     */
    private int search(int[] array) {
        int max = array[0];
        for (int i = 0; i < array.length; ++i) {
            if (max < array[i]) {
                max = array[i];
            }
        }
        return max;
    }

    /**
     * Формируем целочисленный массив псевдослучайных чисел.
     * @param size - размер массива.
     * @return - массив размера size.
     */
    private int[] buildArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; ++i) {
            array[i] = (int) (Math.random() * 2_000_000_000 - 1_000_000_000);
        }
        return array;
    }
}
