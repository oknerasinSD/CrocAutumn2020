package ru.croc.java.school.databind.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;
import java.util.Objects;

@JacksonXmlRootElement(localName = "film")
public class Film {

    @JacksonXmlProperty(localName = "title")
    String title;

    @JacksonXmlProperty(localName = "description")
    String description;

    @JacksonXmlElementWrapper(localName = "actors")
    @JacksonXmlProperty(localName = "actor")
    List<Actor> actors;

    public Film(String title, String description, List<Actor> actor) {
        this.title = title;
        this.description = description;
        this.actors = actor;
    }

    public Film() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Actor> getActors() {
        return actors;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Film film = (Film) obj;
        return Objects.equals(title, film.getTitle()) && Objects.equals(description, film.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description);
    }

    @Override
    public String toString() {
        return "Film{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", actor=" + actors +
                '}';
    }
}
