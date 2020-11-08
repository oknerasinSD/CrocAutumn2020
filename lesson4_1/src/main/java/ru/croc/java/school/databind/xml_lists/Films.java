package ru.croc.java.school.databind.xml_lists;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import ru.croc.java.school.databind.entities.Actor;
import ru.croc.java.school.databind.entities.Film;

import java.util.List;

@JacksonXmlRootElement(localName = "films")
/**
 * Список фильмов для десериализации.
 */
public class Films {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "film")
    private List<Film> films;

    public Films(List<Film> films) {
        this.films = films;
    }

    public Films() {

    }

    /**
     * Формирование списка уникальных актеров из имеющихся фильмов.
     */
    public Actors getActorsFromFilms() {

        Actors actors = new Actors();
        for (Film film : films) {
            for (Actor actor : film.getActors()) {
                if (!actors.hasActor(actor)) {
                    actors.addActor(actor);
                }
                actors.addRole(actor, film.getTitle(), actor.getRole());
            }
        }
        return actors;
    }

    public List<Film> getFilms() {
        return films;
    }
}
