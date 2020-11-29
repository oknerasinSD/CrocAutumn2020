package ru.croc.javaschool.final_task.database.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import javax.xml.bind.annotation.XmlAttribute;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Класс, описывающий сущность "organization" из БД.
 */
@JacksonXmlRootElement(localName = "organization")
public class Organization {

    /** ID */
    @JacksonXmlProperty(localName = "id")
    private Integer id;

    /** Наименование */
    @JacksonXmlProperty(localName = "title")
    private String title;

    /** Адрес */
    @JacksonXmlProperty(localName = "address")
    private String address;

    /** Номер телефона */
    @JacksonXmlProperty(localName = "phoneNumber")
    private String phoneNumber;

    /** Тип организации (муниципальная или досуговая) */
    @JacksonXmlProperty(localName = "type")
    private String type;

    /** Время открытия */
    @JacksonXmlProperty(localName = "openTime")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime openTime;

    /** Время закрытия */
    @JacksonXmlProperty(localName = "closeTime")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closeTime;

    /** Время начала перерыва */
    @JacksonXmlProperty(localName = "breakStart")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime breakStart;

    /** Время окончания перерыва */
    @JacksonXmlProperty(localName = "breakEnd")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime breakEnd;

    /** Дата основания */
    @JacksonXmlProperty(localName = "foundationDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate foundationDate;

    /** Дата упразднения */
    @JacksonXmlProperty(localName = "closeDate")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
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

    public Organization(Integer id, String title, String address, String phoneNumber, String type, LocalTime openTime,
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

    /**
     * Проверка того, работает ли организация в указанное время суток.
     * @param time - проверяемый момент времени.
     * @return - TRUE, если работает; FALSE, если не работает.
     */
    public boolean isWorking(LocalTime time) {
        return isAppropriatedTime(time) && !isOnBreak(time);
    }

    /**
     * Вспомогательный метод, проверяющий, являюся ли оба объекта типа LocalTime null.
     * @param time1 - проверяемый объект 1.
     * @param time2 - проверяемый объект 2.
     * @return - TRUE, если оба == null; FALSE - если хотя бы один != null.
     */
    private boolean checkNullTimeFrames(LocalTime time1, LocalTime time2) {
        return time1 == null && time2 == null;
    }

    /**
     * Работает ли организация круглосуточно.
     * @return - TRUE, если да; FALSE, если нет.
     */
    private boolean worksRoundTheClock() {
        if (checkNullTimeFrames(openTime, closeTime)) {
            return true;
        } else {
            return openTime.equals(closeTime);
        }
    }

    /**
     * Проверка того, вписывается ли указанный момент времени в рабочие часы организации без учета перерыва.
     *
     * - Если рабочий день организации заканчивается в те же сутки, что и начался, то необходимо,
     * чтобы проверяемое время находилось между значениями openTime и closeTime.
     *
     * - Если рабочий день организации заканчивается на следующие сутки после старта работы, то необходимо,
     * чтобы проверяемое время не принадлежало диапазону [closeTime, openTime]
     * (например, если рабочий день начался в 22:00, а закончился в 6:00).
     *
     * @param time - проверямое время суток.
     * @return - TRUE, если да; FALSE, если нет.
     */
    private boolean isAppropriatedTime(LocalTime time) {
        if (worksRoundTheClock()) {
            return true;
        } else if (openTime.isBefore(closeTime)) {
            return isBetween(time, openTime, closeTime);
        } else {
            return !isBetween(time, closeTime, openTime);
        }
    }

    /**
     * Находится ли предприятие на перерыве в указанный момент времени.
     *
     * - Если перерыв заканчивается в те же сутки, что и начался, то необходимо,
     * чтобы проверяемое время находилось между значениями openTime и closeTime.
     *
     * - Если перерыв заканчивается на следующие сутки после старта работы, то необходимо,
     * чтобы проверяемое время не принадлежало диапазону [breakEnd, breakStart]
     * (например, если перерыв начался в 23:00, а закончился в 01:00).
     *
     * @param time - проверяемое время суток.
     * @return - TRUE, если да; FALSE, если нет.
     */
    private boolean isOnBreak(LocalTime time) {
        if (checkNullTimeFrames(breakStart, breakEnd)) {
            return false;
        } else if (breakStart.isBefore(breakEnd)) {
            return isBetween(time, breakStart, breakEnd);
        } else {
            return !isBetween(time, breakEnd, breakStart);
        }
    }

    /**
     * Проверка, лежит ли опредленное временное значение в установленном диапазоне.
     * @param checkedTime - проверяемое время.
     * @param lowerValue - левая граница диапазона.
     * @param higherValue - правая граница диапазонаю
     * @return - TRUE, если принадлежит; FALSE, если не принадлежит.
     */
    private boolean isBetween(LocalTime checkedTime, LocalTime lowerValue, LocalTime higherValue) {
        return checkedTime.isAfter(lowerValue) && checkedTime.isBefore(higherValue);
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

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", type='" + type + '\'' +
                ", openTime=" + openTime +
                ", closeTime=" + closeTime +
                ", breakStart=" + breakStart +
                ", breakEnd=" + breakEnd +
                ", foundationDate=" + foundationDate +
                ", closeDate=" + closeDate +
                '}';
    }
}
