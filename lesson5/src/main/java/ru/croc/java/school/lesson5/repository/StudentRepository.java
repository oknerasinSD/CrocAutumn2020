package ru.croc.java.school.lesson5.repository;

import org.apache.derby.jdbc.EmbeddedDataSource;
import ru.croc.java.school.lesson5.converter.LocalDateConverter;
import ru.croc.java.school.lesson5.model.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для доступа к таблице с данными о студентах.
 */
public class StudentRepository {

    /** Имя таблицы */
    private static final String TABLE_NAME = "student";

    /** DataSource */
    private EmbeddedDataSource dataSource;

    public StudentRepository(EmbeddedDataSource dataSource) {
        this.dataSource = dataSource;
        initTable();
    }

    /** Инициализация таблицы */
    private void initTable() {
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(
                    null,
                    null,
                    TABLE_NAME.toUpperCase(),
                    new String[]{"TABLE"}
            );
            if (!resultSet.next()) {    // Создаем таблицу только в том случае, если такой еще нет
                statement.executeUpdate(
                        "CREATE TABLE "
                        + TABLE_NAME
                        + "("
                        + "id INTEGER PRIMARY KEY, "
                        + "name VARCHAR(255), "
                        + "surname VARCHAR(255), "
                        + "date_of_birth DATE, "
                        + "university_year INTEGER, "
                        + "scholarship BOOLEAN)"
                );
            }
        } catch (SQLException e) {
            System.out.println("Error during table initializing: " + e.getMessage());
        }
    }

    /**
     * Получение объектов из всех строк таблицы.
     * @return - список объектов типа Student.
     */
    public List<Student> findAll() {
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            return createListFromResultSet(statement.executeQuery("SELECT * FROM " + TABLE_NAME));
        } catch (SQLException e) {
            System.out.println("SQL query runtime error: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Поиск студента в таблице по имени и фамилии.
     * @param name - имя.
     * @param surname - фамилия.
     * @return - список студентов с таким именем и фамилией.
     */
    public List<Student> findStudent(String name, String surname) {
        String sqlQuery =
                "SELECT * FROM " + TABLE_NAME + " WHERE name = '" + name + "' AND surname = '" + surname + "'";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            return createListFromResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            System.out.println("SQL query runtime error: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Формирование списка студентов из объекта ResultSet.
     * @param resultSet - объект типа ResultSet, из которого необходимо получить список объектов типа Student.
     * @throws SQLException - ошибка исполнения SQL-запроса; должна обрабатываться вызывающим методом.
     */
    private List<Student> createListFromResultSet(ResultSet resultSet) throws SQLException {
        List<Student> students = new ArrayList<>();
        while (resultSet.next()) {
            LocalDate dateOfBirth = new LocalDateConverter()
                    .convertFromEntityAttribute(resultSet.getDate("date_of_birth"));
            students.add(new Student(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    dateOfBirth,
                    resultSet.getInt("university_year"),
                    resultSet.getBoolean("scholarship")
            ));
        }
        return students;
    }

    /**
     * Добавление записи в таблицу.
     * @param student - студент, информация о котором добавляется в таблицу.
     */
    public void addStudent(Student student) {
        String sqlQuery = "INSERT INTO " + TABLE_NAME + " VALUES(?, ?, ?, ?, ?, ?)";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            preparedStatement.setString(1, student.getId().toString());
            prepareStudentStatement(student, preparedStatement, 2);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("SQL query runtime error: " + e.getMessage());
        }
    }

    /**
     * Обновление данных о конкретном студенте.
     * @param student - студент.
     */
    public void updateInfo(Student student) {
        String sqlQuery =
                "UPDATE " + TABLE_NAME
                + " SET name = ?, surname = ?, date_of_birth = ?, university_year = ?, scholarship = ?"
                + "WHERE id = " + student.getId().toString();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            prepareStudentStatement(student, preparedStatement, 1);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("SQL query runtime error: " + e.getMessage());
        }
    }

    /**
     * Удаление студента из таблицы.
     * @param id - ID студента.
     */
    public void deleteStudent(Integer id) {
        String sqlQuery = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            List<Student> students = findAll();
            for (Student student : students) {
                if (student.getId().equals(id)) {
                    preparedStatement.setString(1, id.toString());
                    break;
                }
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("SQL query runtime error: " + e.getMessage());
        }
    }

    /**
     * Подстановка параметров в запрос.
     * @param Student - студент.
     * @param preparedStatement - экземпляр класса PreparedStatement.
     * @param counter - счетчик параметров (параметр, с которого начинается подстановка, опционален).
     * @throws SQLException - ошибка исполнения SQL-запроса.
     */
    private void prepareStudentStatement(
            Student Student,
            PreparedStatement preparedStatement,
            int counter
    ) throws SQLException {
        preparedStatement.setString(counter++, Student.getName());
        preparedStatement.setString(counter++, Student.getSurname());
        String date =
                new LocalDateConverter().convertToDatabaseColumn(Student.getDateOfBirth()).toString();
        preparedStatement.setString(counter++, date);
        preparedStatement.setString(counter++, Student.getYear().toString());
        preparedStatement.setString(counter, Student.hasScholarship().toString());
    }

    /**
     * Удаление таблицы из базы.
     */
    public void dropTable() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE " + TABLE_NAME);
        } catch (SQLException e) {
            System.out.println("Error during dropping table: " + e.getMessage());
        }
    }
}
