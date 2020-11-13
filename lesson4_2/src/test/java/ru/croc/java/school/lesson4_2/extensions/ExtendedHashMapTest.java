package ru.croc.java.school.lesson4_2.extensions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.croc.java.school.lesson4_2.annotated_entities.AnnotatedKey;
import ru.croc.java.school.lesson4_2.annotated_entities.AnnotatedValue;
import ru.croc.java.school.lesson4_2.exceptions.PutMapKeyFailException;
import ru.croc.java.school.lesson4_2.exceptions.PutMapValueFailException;

import java.util.Map;

public class ExtendedHashMapTest {

    @Test
    public void testPut() {

        Map<Object, Object> testMap = new ExtendedHashMap<>();

        AnnotatedKey annotatedKey = new AnnotatedKey();
        Integer simpleKey = 1;
        AnnotatedValue annotatedValue = new AnnotatedValue();
        String simpleValue = "";

        Assertions.assertThrows(PutMapKeyFailException.class, () -> {testMap.put(annotatedKey, simpleValue);});
        Assertions.assertThrows(PutMapValueFailException.class, () -> {testMap.put(simpleKey, annotatedValue);});
        Assertions.assertDoesNotThrow(() -> {testMap.put(simpleKey, simpleValue);});
    }
}
