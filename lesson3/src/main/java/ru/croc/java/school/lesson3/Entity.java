package ru.croc.java.school.lesson3;

/**
 * Интерфейс для классов, которые будут представлять сущности, с которыми работает пользователь.
 */
public interface Entity {

    /** ID сущности */
    public String getId();

    /** Короткое имя для вывода краткой информации об объекта пользователю */
    public String getShortName();

}
