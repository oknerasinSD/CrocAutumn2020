package ru.croc.java.school.lesson2_1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class WordCountTest {

    private String testString1 = "Раз, два, три, 4, пять, раз, два, 3, четыре, пять!";
    private String testString2 = "";

    @Test
    @DisplayName("Проверка разбиения строки")
    public void testStringParsing() {

        String[] array1 = WordCount.formatString(testString1);
        Assertions.assertEquals(8, array1.length);
        for (String element : array1) {
            Assertions.assertTrue(element.matches("[а-яА-Яa-zA-Z]+"));
        }

        String[] array2 = WordCount.formatString(testString2);
        Assertions.assertEquals(1, array2.length);
    }

    @Test
    @DisplayName("Проверка подсчета слов")
    public void countingWordsTest() {

        Map<String, Integer> words1 = WordCount.countWords(testString1);
        Assertions.assertEquals(2, words1.get("раз"));

        Map<String, Integer> words2 = WordCount.countWords(testString2);
        Assertions.assertNull(words2.get(""));
    }
}
