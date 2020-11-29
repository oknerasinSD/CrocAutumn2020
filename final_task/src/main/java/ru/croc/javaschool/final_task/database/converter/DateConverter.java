package ru.croc.javaschool.final_task.database.converter;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Конвертер объектов представления даты.
 */
public class DateConverter {

    /**
     * Конвертирование объекта LocalDate в формат для представления в БД.
     * @param localDate - конвертируемыый объект.
     * @return - объект типа java.sql.Date.
     */
    public Date toDatabase(LocalDate localDate) {
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
    public LocalDate fromDatabase(Date sqlDate) {
        if (sqlDate != null) {
            return sqlDate.toLocalDate();
        } else {
            return null;
        }
    }
}
