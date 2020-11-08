package ru.croc.java.school.databind.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Роль в фильме.
 */
public class Role {

    @JacksonXmlProperty(isAttribute = true, localName = "title")
    /** Название фильма */
    private String filmTitle;

    @JacksonXmlProperty(isAttribute = true, localName = "role")
    /** Роль */
    private String role;

    public Role(String filmTitle, String role) {
        this.filmTitle = filmTitle;
        this.role = role;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public String getRole() {
        return role;
    }
}
