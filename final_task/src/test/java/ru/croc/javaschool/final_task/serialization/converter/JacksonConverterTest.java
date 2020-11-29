package ru.croc.javaschool.final_task.serialization.converter;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import ru.croc.javaschool.final_task.database.model.Organization;
import ru.croc.javaschool.final_task.serialization.xml_lists.Organizations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

public class JacksonConverterTest {

    private static final String XML_DIRECTORY = "src/main/resources/xml";

    private Organization testOrganization1;
    private Organization testOrganization2;
    private Organization testOrganization3;

    @BeforeEach
    public void init() {
        testOrganization1 = new Organization(
                1,
                "Title 1",
                "Address 1",
                "Phone Number 1",
                "Type 1",
                LocalTime.of(8, 0),
                LocalTime.of(20, 0),
                LocalTime.of(14,0),
                LocalTime.of(15,0),
                LocalDate.of(2020, 1, 1)
        );
        testOrganization2 = new Organization(
                2,
                "Title 2",
                "Address 2",
                "Phone Number 2",
                "Type 2",
                LocalTime.of(10, 0),
                LocalTime.of(18, 0),
                LocalTime.of(14, 0),
                LocalTime.of(14, 30),
                LocalDate.of(2019, 2, 5)
        );
        testOrganization3 = new Organization(
                3,
                "Title 3",
                "Address 3",
                "Phone Number 3",
                "Type 1",
                null,
                null,
                null,
                null,
                LocalDate.of(2018, 6, 1)
        );
    }

    @Test
    @DisplayName("Сериализация простого объекта")
    public void testToSimpleXml() throws IOException, SAXException {

        JacksonConverter converter = new JacksonConverter();
        String xml = converter.toXml(testOrganization1);

        Path path = Paths.get(XML_DIRECTORY, "SimpleTest.xml");
        String expected = Files.readString(path);

        XMLUnit.setIgnoreWhitespace(true);
        Assertions.assertTrue(XMLUnit.compareXML(expected, xml).similar());
    }

    @Test
    @DisplayName("Сериализация списка объектов")
    public void testListToXml() throws IOException, SAXException {

        JacksonConverter converter = new JacksonConverter();
        String xml = converter.toXml(new Organizations(Arrays.asList(testOrganization1, testOrganization2)));

        Path path = Paths.get(XML_DIRECTORY, "ListSerializationTest.xml");
        String expected = Files.readString(path);

        XMLUnit.setIgnoreWhitespace(true);
        Assertions.assertTrue(XMLUnit.compareXML(expected, xml).similar());
    }

    @Test
    @DisplayName("Сериализация объекта с null полями")
    public void testNullFieldObject() throws IOException, SAXException {

        JacksonConverter converter = new JacksonConverter();
        String xml = converter.toXml(testOrganization3);

        Path path = Paths.get(XML_DIRECTORY, "NullObjectTest.xml");
        String expected = Files.readString(path);

        System.out.println(xml);

        XMLUnit.setIgnoreWhitespace(true);
        Assertions.assertTrue(XMLUnit.compareXML(expected, xml).similar());
    }
}
