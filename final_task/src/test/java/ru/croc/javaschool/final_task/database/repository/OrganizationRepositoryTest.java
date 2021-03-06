package ru.croc.javaschool.final_task.database.repository;

import org.junit.jupiter.api.*;
import ru.croc.javaschool.final_task.database.datasource.DataSourceProvider;
import ru.croc.javaschool.final_task.database.datasource.PropertyType;
import ru.croc.javaschool.final_task.database.model.Organization;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * Тестирование работы репозитория, отвечающего за взаимодействие с сущностью "organization".
 */
public class OrganizationRepositoryTest {

    /** Репозиторий */
    private OrganizationRepository repository;

    /** Провайдер для доступа к БД */
    private DataSourceProvider dataSourceProvider;

    /*
    Тестовые организации для различных случаев.
     */

    /** Работает круглосуточно - должна всегда находиться при поиске */
    private Organization roundTheClockOrg;

    /** Работает с 8 до 20 - игнорируется при проверке другого времени */
    private Organization org8to20;

    /** Работает с 20 до 6 - должна попадать в итоговый список при проверке для 3 часов ночи */
    private Organization org20to6;

    /** Работает с 8 утра до полуночи, перерыв с 15:30 до 16:30 - должна игнорироваться в перерыв */
    private Organization org8to00WithBreak;

    /** Работает с 20 до 6, перерыв с 23:00 до 01:00 - должна игнорироваться в 00:30 */
    private Organization org20to6withBreak;

    @BeforeEach
    public void init() throws IOException, SQLException {
        dataSourceProvider = new DataSourceProvider(PropertyType.TEST);
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
                "Муниципальная",
                LocalTime.of(20, 0),
                LocalTime.of(6, 0),
                null,
                null,
                LocalDate.of(2015, 5, 5)
        );
        org8to00WithBreak = new Organization(
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
    public void clear() throws SQLException {
        repository.dropTable();
    }

    @Test
    @DisplayName("Проверка подключения к тестовой БД")
    public void testConnection() {
        Assertions.assertEquals("db_test", dataSourceProvider.getDbName());
    }

    @Test
    @DisplayName("Добавление и извлечение данных в/из БД")
    public void testDb() throws IOException, SQLException {
        repository.add(roundTheClockOrg);
        Organization organization = repository.find(roundTheClockOrg.getId());
        Assertions.assertEquals(roundTheClockOrg, organization);
    }

    @Test
    @DisplayName("Игнор: стандартный случай")
    public void testSkipOpenCloseSameDay() throws SQLException {
        repository.add(roundTheClockOrg);
        repository.add(org8to20);
        List<Organization> workingOrganizations = repository.findWorking(LocalTime.of(7, 0));
        Assertions.assertEquals(1, workingOrganizations.size());
        Assertions.assertEquals(roundTheClockOrg, workingOrganizations.get(0));
    }

    @Test()
    @DisplayName("Учет: конец работы на следующий день")
    public void testNotSkipCloseNextDay() throws SQLException {
        repository.add(roundTheClockOrg);
        repository.add(org8to20);
        repository.add(org20to6);
        List<Organization> workingOrgs = repository.findWorking(LocalTime.of(3, 0));
        Assertions.assertEquals(2, workingOrgs.size());
        Assertions.assertTrue(workingOrgs.containsAll(Arrays.asList(roundTheClockOrg, org20to6)));
        Assertions.assertFalse(workingOrgs.contains(org8to20));
    }

    @Test
    @DisplayName("Игнор в перерыв: стандартный случай")
    public void testSkipBreakSameDay() throws SQLException {
        repository.add(roundTheClockOrg);
        repository.add(org8to20);
        repository.add(org8to00WithBreak);
        List<Organization> workingOrgs = repository.findWorking(LocalTime.of(16, 0));
        Assertions.assertEquals(2, workingOrgs.size());
        Assertions.assertFalse(workingOrgs.contains(org8to00WithBreak));
        Assertions.assertTrue(workingOrgs.containsAll(Arrays.asList(roundTheClockOrg, org8to20)));
    }

    @Test
    @DisplayName("Игнор в перерыв: конец перерыва в другой день")
    public void testSkipBreakNewDay() throws SQLException {
        repository.add(roundTheClockOrg);
        repository.add(org20to6);
        repository.add(org20to6withBreak);
        List<Organization> workingOrgs = repository.findWorking(LocalTime.of(0, 30));
        Assertions.assertEquals(2, workingOrgs.size());
        Assertions.assertFalse(workingOrgs.contains(org20to6withBreak));
        Assertions.assertTrue(workingOrgs.containsAll(Arrays.asList(roundTheClockOrg, org20to6)));
    }
}
