package ru.croc.javaschool.final_task.database.repository;

import org.junit.jupiter.api.*;
import ru.croc.javaschool.final_task.database.datasource.DataSourceProvider;
import ru.croc.javaschool.final_task.database.model.Organization;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class OrganizationRepositoryTest {

    private OrganizationRepository repository;

    /** Работает круглосуточно - должна всегда находится при поиске */
    private Organization roundTheClockOrg;

    /** Работает с 8 до 20 - игнорируется при проверке другого времени */
    private Organization standardWithoutBreakOrg;

    /** Работает с 20 до 6 - должна попадать в итоговый список при проверке для 3 часов ночи */
    private Organization nonStandardWithoutBreakOrg;

    /** Работает с 8 утра до полуночи, перерыв с 15:30 до 16:30 - должна игнорироваться в перерыв */
    private Organization withStandardBreakOrg;

    /** Работает с 20 до 6, перерыв с 23:00 до 01:00 - должна игнорироваться в 00:30 */
    private Organization withNonStandardBreakOrg;

    @BeforeEach
    public void init() throws IOException {
        DataSourceProvider dataSourceProvider = new DataSourceProvider();
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
        standardWithoutBreakOrg = new Organization(
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
        nonStandardWithoutBreakOrg = new Organization(
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
        withStandardBreakOrg = new Organization(
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
        withNonStandardBreakOrg = new Organization(
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
    @DisplayName("Добавление и извлечение данных в/из БД")
    public void testDb() throws IOException {
        repository.add(roundTheClockOrg);
        Organization organization = repository.find(roundTheClockOrg.getId());
        Assertions.assertEquals(roundTheClockOrg, organization);
    }

    @Test
    @DisplayName("Игнор: стандартный случай")
    public void testSkipOpenCloseSameDay() {
        repository.add(roundTheClockOrg);
        repository.add(standardWithoutBreakOrg);
        List<Organization> workingOrganizations = repository.findWorking(LocalTime.of(7, 0));
        Assertions.assertEquals(1, workingOrganizations.size());
        Assertions.assertEquals(roundTheClockOrg, workingOrganizations.get(0));
    }

    @Test()
    @DisplayName("Учет: конец работы на следующий день")
    public void testNotSkipCloseNextDay() {
        repository.add(roundTheClockOrg);
        repository.add(standardWithoutBreakOrg);
        repository.add(nonStandardWithoutBreakOrg);
        List<Organization> workingOrgs = repository.findWorking(LocalTime.of(3, 0));
        Assertions.assertEquals(2, workingOrgs.size());
        Assertions.assertTrue(workingOrgs.containsAll(Arrays.asList(roundTheClockOrg, nonStandardWithoutBreakOrg)));
        Assertions.assertFalse(workingOrgs.contains(standardWithoutBreakOrg));
    }

    @Test
    @DisplayName("Игнор в перерыв: стандартный случай")
    public void testSkipBreakSameDay() {
        repository.add(roundTheClockOrg);
        repository.add(standardWithoutBreakOrg);
        repository.add(withStandardBreakOrg);
        List<Organization> workingOrgs = repository.findWorking(LocalTime.of(16, 0));
        Assertions.assertEquals(2, workingOrgs.size());
        Assertions.assertFalse(workingOrgs.contains(withStandardBreakOrg));
        Assertions.assertTrue(workingOrgs.containsAll(Arrays.asList(roundTheClockOrg, standardWithoutBreakOrg)));
    }

    @Test
    @DisplayName("Игнор в перерыв: конец перерыва в другой день")
    public void testSkipBreakNewDay() {
        repository.add(roundTheClockOrg);
        repository.add(nonStandardWithoutBreakOrg);
        repository.add(withNonStandardBreakOrg);
        List<Organization> workingOrgs = repository.findWorking(LocalTime.of(0, 30));
        Assertions.assertEquals(2, workingOrgs.size());
        Assertions.assertFalse(workingOrgs.contains(withNonStandardBreakOrg));
        Assertions.assertTrue(workingOrgs.containsAll(Arrays.asList(roundTheClockOrg, nonStandardWithoutBreakOrg)));
    }
}
