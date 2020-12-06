package ru.croc.javaschool.final_task;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import ru.croc.javaschool.final_task.database.datasource.DataSourceProvider;
import ru.croc.javaschool.final_task.database.datasource.PropertyType;
import ru.croc.javaschool.final_task.database.model.Organization;
import ru.croc.javaschool.final_task.database.repository.OrganizationRepository;
import ru.croc.javaschool.final_task.serialization.converter.JacksonConverter;
import ru.croc.javaschool.final_task.serialization.xml_lists.Organizations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Тестирование полного процесса работы программы.
 */

public class FullTest {

    /** Пусть до директории с XML-файлами для проверки корректености сериализации */
    private static final String XML_DIRECTORY = "src/test/resources/xml";

    /** Репозиторий */
    private OrganizationRepository repository;

    /** Работает круглосуточно - должна всегда находится при поиске */
    private Organization roundTheClockOrg;

    /** Работает с 8 до 20 - игнорируется при проверке другого времени */
    private Organization org8to20;

    /** Работает с 20 до 6 - должна попадать в итоговый список при проверке для 3 часов ночи */
    private Organization org20to6;

    /** Работает с 8 утра до полуночи, перерыв с 15:30 до 16:30 - должна игнорироваться в перерыв */
    private Organization org8to00withBreak;

    /** Работает с 20 до 6, перерыв с 23:00 до 01:00 - должна игнорироваться в 00:30 */
    private Organization org20to6withBreak;

    @BeforeEach
    public void init() throws IOException {
        DataSourceProvider dataSourceProvider = new DataSourceProvider(PropertyType.TEST);
        repository = new OrganizationRepository(dataSourceProvider.getDataSource());
        roundTheClockOrg = new Organization(
                1,
                "Наименование 1",
                "Адрес 1",
                "Телефон 1",
                "Муниципальная",
                null,
                null,
                null,
                null,
                LocalDate.of(2020, 1, 1)
        );
        org8to20 = new Organization(
                2,
                "Наименование 2",
                "Адрес 2",
                "Телефон 2",
                "Досуговая",
                LocalTime.of(8, 0),
                LocalTime.of(20, 0),
                null,
                null,
                LocalDate.of(2019, 6, 1)
        );
        org20to6 = new Organization(
                3,
                "Наименование 3",
                "Адрес 3",
                "Телефон 3",
                "Досуговая",
                LocalTime.of(20, 0),
                LocalTime.of(6, 0),
                null,
                null,
                LocalDate.of(2015, 5, 5)
        );
        org8to00withBreak = new Organization(
                4,
                "Наименование 4",
                "Адрес 4",
                "Телефон 4",
                "Досуговая",
                LocalTime.of(8, 0),
                LocalTime.of(0, 0),
                LocalTime.of(15, 30),
                LocalTime.of(16, 30),
                LocalDate.of(2018, 2, 2)
        );
        org20to6withBreak = new Organization(
                5,
                "Наименование 5",
                "Адрес 5",
                "Телефон 5",
                "Муниципальная",
                LocalTime.of(20, 0),
                LocalTime.of(6, 0),
                LocalTime.of(23, 0),
                LocalTime.of(1, 0),
                LocalDate.of(2012, 10, 10)
        );
    }

    @AfterEach
    public void clear() {
        repository.dropTable();
    }

    @Test
    public void testFullProcess() throws IOException, SAXException {

        JacksonConverter converter = new JacksonConverter();
        XMLUnit.setIgnoreWhitespace(true);

        repository.add(roundTheClockOrg);
        repository.add(org8to20);
        repository.add(org20to6);
        repository.add(org8to00withBreak);
        repository.add(org20to6withBreak);

        Organizations organizations = new Organizations(repository.findWorking(LocalTime.of(0, 30)));
        String xml = converter.toXml(organizations);
        Path path = Paths.get(XML_DIRECTORY, "FullTest1.xml");
        String expected = Files.readString(path);
        Assertions.assertTrue(XMLUnit.compareXML(expected, xml).similar());

        organizations = new Organizations(repository.findWorking(LocalTime.of(16, 0)));
        xml = converter.toXml(organizations);
        path = Paths.get(XML_DIRECTORY, "FullTest2.xml");
        expected = Files.readString(path);
        Assertions.assertTrue(XMLUnit.compareXML(expected, xml).similar());
    }
}
