package ru.croc.javaschool.final_task.database.converter;

import java.sql.Time;
import java.time.LocalTime;

/**
 * Конвертер объектов представления времени.
 */
public class TimeConverter {

    /**
     * Конвертирование объекта типа LocalTime в формат для представления в БД.
     * @param localTime - конвертируемый объект.
     * @return - объект типа java.sql.Time.
     */
    public Time toDatabase(LocalTime localTime) {
        if (localTime != null) {
            return Time.valueOf(localTime);
        } else {
            return null;
        }
    }


    /**
     * Конвертирование объекта java.sql.Time в LocalTime.
     * @param sqlTime - конвертируемый объект.
     * @return - объект типа LocalTime.
     */
    public LocalTime fromDatabase(Time sqlTime) {
        if (sqlTime != null) {
            return sqlTime.toLocalTime();
        } else {
            return null;
        }
    }
}
