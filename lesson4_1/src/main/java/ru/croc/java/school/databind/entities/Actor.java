package ru.croc.java.school.databind.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.*;

@JacksonXmlRootElement(localName = "actor")
@JsonPropertyOrder({"name", "age", "role", "films"})
public class Actor {

    @JacksonXmlProperty(localName = "name")
    private String name;

    @JacksonXmlProperty(localName = "age")
    private int age;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JacksonXmlProperty(localName = "role")
    /**
     * Роль актера в конкретном фильме (присваивается при десериализации объекта, игнорируется при сериализации).
     */
    private String role;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JacksonXmlElementWrapper(localName = "films")
    @JacksonXmlProperty(localName = "film")
    /**
     * Список всех ролей актера (игнорируется при десериализации, учитывается при сериализации).
     */
    private List<Role> roles = new ArrayList<>();

    public Actor(String name, int age, String role) {
        this.name = name;
        this.age = age;
        this.role = role;
    }

    public Actor() {
    }

    /**
     * Добавление новой роли актера.
     * @param filmTitle - название фильма.
     * @param role - роль.
     */
    public void addRole(String filmTitle, String role) {
        roles.add(new Role(filmTitle, role));
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Actor actor = (Actor) obj;
        return Objects.equals(name, actor.getName()) && Objects.equals(age, actor.getAge());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
