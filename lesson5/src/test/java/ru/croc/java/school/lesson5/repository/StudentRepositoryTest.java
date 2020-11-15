package ru.croc.java.school.lesson5.repository;

import org.junit.jupiter.api.*;
import ru.croc.java.school.lesson5.db.DataSourceProvider;
import ru.croc.java.school.lesson5.model.Student;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class StudentRepositoryTest {

    private DataSourceProvider dataSourceProvider;
    private StudentRepository studentRepository;
    private Student testStudent1;
    private Student testStudent2;

    /**
     * Инициализация объектов для тестов.
     */
    @BeforeEach
    public void init() {
        try {
            dataSourceProvider = new DataSourceProvider();
        } catch (IOException e) {
            System.out.println("Data source creation error");
        }
        studentRepository = new StudentRepository(dataSourceProvider.getDataSource());
        testStudent1 = new Student(
                1,
                "Ivan",
                "Ivanov",
                LocalDate.now().minusYears(20),
                3,
                false
        );
        testStudent2 = new Student(
                2,
                "Petr",
                "Petrov",
                LocalDate.now().minusYears(21),
                4,
                false
        );
    }

    /**
     * Удаление тестовой таблицы после тестирования каждого отдельного метода.
     */
    @AfterEach
    public void clear() {
        studentRepository.dropTable();
    }

    /**
     * Добавление записи в таблицу.
     */
    @Test
    @DisplayName("Create")
    public void testAddStudent() {
        studentRepository.addStudent(testStudent1);
        List<Student> students = studentRepository.findAll();
        Assertions.assertEquals(1, students.size());
        Assertions.assertEquals(testStudent1, students.get(0));
    }

    /**
     * Получения студента из таблицы по имени и фамилии.
     */
    @Test
    @DisplayName("Read")
    public void testFindStudent() {

        studentRepository.addStudent(testStudent1);
        List<Student> students = studentRepository.findStudent("Ivan", "Ivanov");
        Assertions.assertEquals(1, students.size());
        Assertions.assertEquals(testStudent1, students.get(0));
    }

    /**
     * Обновление информации в таблице.
     */
    @Test
    @DisplayName("Update")
    public void testUpdateInfo() {

        studentRepository.addStudent(testStudent1);
        Boolean initialScholarship = testStudent1.hasScholarship();
        testStudent1.setScholarship(!testStudent1.hasScholarship());
        studentRepository.updateInfo(testStudent1);
        List<Student> students = studentRepository.findAll();
        Assertions.assertEquals(1, students.size());
        Assertions.assertNotEquals(initialScholarship, students.get(0).hasScholarship());

        /*
        Пытаемся обновить информацию о студенте, которого нет в таблице.
         */
        Assertions.assertDoesNotThrow(() -> {studentRepository.updateInfo(testStudent2);});
        Assertions.assertEquals(1, studentRepository.findAll().size());
    }

    /**
     * Удаление записи из таблицы.
     */
    @Test
    @DisplayName("Delete")
    public void testDeleteStudent() {
        studentRepository.addStudent(testStudent1);
        Assertions.assertEquals(1, studentRepository.findAll().size());
        studentRepository.deleteStudent(testStudent1.getId());
        Assertions.assertEquals(0, studentRepository.findAll().size());
    }
}
