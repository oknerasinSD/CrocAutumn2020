package ru.croc.java.school.lesson5.converter;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Конвертер объектов представления даты.
 */
public class LocalDateConverter {

    /**
     * Конвертирование объекта LocalDate в формат для представления в бд.
     * @param localDate - конвертируемыый объект.
     * @return - объект типа java.sql.Date.
     */
    public Date convertToDatabaseColumn(LocalDate localDate) {
        if (localDate != null) {
            return Date.valueOf(localDate);
        } else {
            return null;
        }
    }

    /**
     * Конвертирование объекта java.sql.Date в LocalDate.
     * @param sqlDate - конвертируемый объект.
     * @return - объект типа LocalDate.
     */
    public LocalDate convertFromEntityAttribute(Date sqlDate) {
        if (sqlDate != null) {
            return sqlDate.toLocalDate();
        } else {
            return null;
        }
    }
}
