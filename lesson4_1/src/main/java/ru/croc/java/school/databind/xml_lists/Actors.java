package ru.croc.java.school.databind.xml_lists;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import ru.croc.java.school.databind.entities.Actor;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "actors")
/**
 * Список актеров для сериализации.
 */
public class Actors {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "actor")
    private List<Actor> actors = new ArrayList<>();

    public Actors(List<Actor> actors) {
        this.actors = actors;
    }

    public Actors() {
    }

    /**
     * Добавить актера в список - актер.
     * @param actor
     */
    public void addActor(Actor actor) {
        actors.add(actor);
    }

    /**
     * Проверка на присутствие актера в списке.
     * @param actor - актер.
     * @return - TRUE || FALSE.
     */
    public boolean hasActor(Actor actor) {
        return actors.contains(actor);
    }

    /**
     * Добавление новой роли актеру из списка.
     * @param actor - актер.
     * @param filmTitle - название фильма.
     * @param role - роль.
     */
    public void addRole(Actor actor, String filmTitle, String role) {
        if (actors.contains(actor)) {
            actors.get(actors.indexOf(actor)).addRole(filmTitle, role);
        }
    }
}
