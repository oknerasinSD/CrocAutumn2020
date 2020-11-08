package ru.croc.java.school.databind.jaxb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.croc.java.school.databind.converting.JacksonConverter;
import ru.croc.java.school.databind.entities.Actor;
import ru.croc.java.school.databind.entities.Film;
import ru.croc.java.school.databind.xml_lists.Actors;
import ru.croc.java.school.databind.xml_lists.Films;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JacksonConverterTest {

    String resources = "src/main/resources";

    @Test
    @DisplayName("From XML")
    public void testFromXmlConverting() throws IOException {

        JacksonConverter jacksonConverter = new JacksonConverter();
        Path path = Paths.get(resources, "films1.xml");
        String xml = Files.readString(path);
        Films films = jacksonConverter.fromXml(xml, Films.class);

        Film expectedFilm1 = new Film(
                "Фильм 1",
                "Описание 1",
                Arrays.asList(
                        new Actor("Актер 1", 30, "Роль 1"),
                        new Actor("Актер 2", 23, "Роль 2"),
                        new Actor("Актер 3", 40, "Роль 3")
                )
        );
        Film expectedFilm2 = new Film(
                "Фильм 2",
                "Описание 2",
                Arrays.asList(
                        new Actor("Актер 4", 70, "Роль 4"),
                        new Actor("Актер 2", 23, "Роль 5"),
                        new Actor("Актер 3", 40, "Роль 6")
                )
        );
        Set<Film> expectedList = new HashSet<>(Arrays.asList(expectedFilm1, expectedFilm2));

        Assertions.assertTrue(films.getFilms().containsAll(expectedList));
        Assertions.assertEquals(expectedList.size(), films.getFilms().size());
    }

    @Test
    @DisplayName("To XML")
    public void toXmlConverting() throws IOException {

        JacksonConverter jacksonConverter = new JacksonConverter();
        Path path = Paths.get(resources, "films1.xml");
        String xml = Files.readString(path);
        Films films = jacksonConverter.fromXml(xml, Films.class);
        Actors actors = films.getActorsFromFilms();
        xml = jacksonConverter.toXml(actors);

        String expected = "<actors>\r\n" +
                "  <actor>\r\n" +
                "    <name>Актер 1</name>\r\n" +
                "    <age>30</age>\r\n" +
                "    <films>\r\n" +
                "      <film title=\"Фильм 1\" role=\"Роль 1\"/>\r\n" +
                "    </films>\r\n" +
                "  </actor>\r\n" +
                "  <actor>\r\n" +
                "    <name>Актер 2</name>\r\n" +
                "    <age>23</age>\r\n" +
                "    <films>\r\n" +
                "      <film title=\"Фильм 1\" role=\"Роль 2\"/>\r\n" +
                "      <film title=\"Фильм 2\" role=\"Роль 5\"/>\r\n" +
                "    </films>\r\n" +
                "  </actor>\r\n" +
                "  <actor>\r\n" +
                "    <name>Актер 3</name>\r\n" +
                "    <age>40</age>\r\n" +
                "    <films>\r\n" +
                "      <film title=\"Фильм 1\" role=\"Роль 3\"/>\r\n" +
                "      <film title=\"Фильм 2\" role=\"Роль 6\"/>\r\n" +
                "    </films>\r\n" +
                "  </actor>\r\n" +
                "  <actor>\r\n" +
                "    <name>Актер 4</name>\r\n" +
                "    <age>70</age>\r\n" +
                "    <films>\r\n" +
                "      <film title=\"Фильм 2\" role=\"Роль 4\"/>\r\n" +
                "    </films>\r\n" +
                "  </actor>\r\n" +
                "</actors>\r\n";

        Assertions.assertEquals(expected, xml);

        System.out.println(xml);
    }
}
