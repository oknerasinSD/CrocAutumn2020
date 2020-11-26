package ru.croc.javaschool.final_task.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Класс, описывающий сущность "organization" из БД.
 */
public class Organization {

    /** ID */
    private Integer id;

    /** Наименование */
    private String title;

    /** Адрес */
    private String address;

    /** Номер телефона */
    private String phoneNumber;

    /** Тип организации (муниципальная или досуговая) */
    private String type;

    /** Время открытия */
    private LocalTime openTime;

    /** Время закрытия */
    private LocalTime closeTime;

    /** Время начала перерыва */
    private LocalTime breakStart;

    /** Время окончания перерыва */
    private LocalTime breakEnd;

    /** Дата основания */
    private LocalDate foundationDate;

    /** Дата упразднения */
    private LocalDate closeDate;

    public Organization(Integer id, String title, String address, String phoneNumber, String type, LocalTime openTime,
                        LocalTime closeTime, LocalTime breakStart, LocalTime breakEnd, LocalDate foundationDate
    ) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.breakStart = breakStart;
        this.breakEnd = breakEnd;
        this.foundationDate = foundationDate;
        closeDate = null;
    }

    public Organization(int id, String title, String address, String phoneNumber, String type, LocalTime openTime,
                        LocalTime closeTime, LocalTime breakStart, LocalTime breakEnd, LocalDate foundationDate,
                        LocalDate closeDate
    ) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.type = type;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.breakStart = breakStart;
        this.breakEnd = breakEnd;
        this.foundationDate = foundationDate;
        this.closeDate = closeDate;
    }



    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getType() {
        return type;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public LocalTime getBreakStart() {
        return breakStart;
    }

    public LocalTime getBreakEnd() {
        return breakEnd;
    }

    public LocalDate getFoundationDate() {
        return foundationDate;
    }

    public LocalDate getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(LocalDate closeDate) {
        this.closeDate = closeDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Organization organization = (Organization) obj;
        return Objects.equals(id, organization.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
