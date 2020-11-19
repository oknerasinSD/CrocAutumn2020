package ru.croc.java.school.lesson5.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс, представляющий сущность "student" из БД.
 */
public class Student {

    /** ID */
    private Integer id;

    /** Имя */
    private String name;

    /** Фамилия */
    private String surname;

    /** Дата рождения */
    private LocalDate dateOfBirth;

    /** Курс */
    private Integer year;

    /** Получает ли стипендию */
    private Boolean scholarship;

    public Student(Integer id, String name, String surname, LocalDate dateOfBirth, Integer year, Boolean scholarship) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.year = year;
        this.scholarship = scholarship;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", year=" + year +
                ", scholarship=" + scholarship +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Integer getYear() {
        return year;
    }

    public Boolean hasScholarship() {
        return scholarship;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setScholarship(Boolean scholarship) {
        this.scholarship = scholarship;
    }

    /**
     * Сравниваем студентов по id.
     * @param obj - объект для сравнения.
     * @return - TRUE || FALSE.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Student student = (Student) obj;
        return Objects.equals(id, student.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
