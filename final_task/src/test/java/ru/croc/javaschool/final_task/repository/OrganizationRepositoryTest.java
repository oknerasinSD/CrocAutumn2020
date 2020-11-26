package ru.croc.javaschool.final_task.repository;

import org.junit.jupiter.api.*;
import ru.croc.javaschool.final_task.db.DataSourceProvider;
import ru.croc.javaschool.final_task.model.Organization;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class OrganizationRepositoryTest {

    private DataSourceProvider dataSourceProvider;
    private OrganizationRepository organizationRepository;
    private Organization testOrganization1;
    private Organization testOrganization2;

    @BeforeEach
    public void init() throws IOException {
        dataSourceProvider = new DataSourceProvider();
        organizationRepository = new OrganizationRepository(dataSourceProvider.getDataSource());
        testOrganization1 = new Organization(
                1,
                "Наименование 1",
                "Адрес 1",
                "Телефон 1",
                "Муниципальная",
                LocalTime.of(9, 0),
                LocalTime.of(21, 0),
                LocalTime.of(14, 30),
                LocalTime.of(15, 30),
                LocalDate.of(2020, 1, 1)
        );
        testOrganization2 = new Organization(
                2,
                "Наименование 2",
                "Адрес 2",
                "Телефон 2",
                "Досуговая",
                LocalTime.of(0, 0),
                LocalTime.of(0, 0),
                null,
                null,
                LocalDate.of(2019, 6, 1)
        );
    }

    @AfterEach
    public void clear() {
        organizationRepository.dropTable();
    }

    @Test
    @DisplayName("Добавление данных в базу")
    public void testAdd() throws IOException {
        organizationRepository.add(testOrganization1);
        Organization organization = organizationRepository.find(testOrganization1.getId());
        Assertions.assertEquals(testOrganization1, organization);
    }

    @Test
    public void testFindOpened() {
        organizationRepository.add(testOrganization1);
        organizationRepository.add(testOrganization2);
        List<Organization> workingOrganizations = organizationRepository.findOpened(LocalTime.of(15, 0));
        Assertions.assertEquals(1, workingOrganizations.size());
        Assertions.assertEquals(testOrganization2, workingOrganizations.get(0));
    }
}
