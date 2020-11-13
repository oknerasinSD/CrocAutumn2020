package ru.croc.java.school.lesson4_2.extensions;

import ru.croc.java.school.lesson4_2.annotations.MapKeyFail;
import ru.croc.java.school.lesson4_2.annotations.MapValueFail;
import ru.croc.java.school.lesson4_2.exceptions.PutMapKeyFailException;
import ru.croc.java.school.lesson4_2.exceptions.PutMapValueFailException;

import java.lang.annotation.Annotation;
import java.util.HashMap;

public class ExtendedHashMap<T, K> extends HashMap<T, K> {

    @Override
    public K put(T key, K value) {
        checkForbiddenAnnotations(key);
        checkForbiddenAnnotations(value);
        return super.put(key, value);
    }

    /**
     * Проверяем, является ли класс объекта, поданного в мапу, аннотированным запрещенной аннотацией.
     * @param parameter - проверяемый объект.
     */
    private void checkForbiddenAnnotations(Object parameter) {
        Class clazz = parameter.getClass();
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == MapKeyFail.class) {
                throw new PutMapKeyFailException("PutMapKeyFailException");
            } else if (annotation.annotationType() == MapValueFail.class) {
                throw new PutMapValueFailException("PutMapValueFailException");
            }
        }
    }
}
