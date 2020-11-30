package ru.croc.javaschool.final_task.database.repository;

import org.apache.derby.jdbc.EmbeddedDataSource;
import ru.croc.javaschool.final_task.database.converter.DateConverter;
import ru.croc.javaschool.final_task.database.converter.TimeConverter;
import ru.croc.javaschool.final_task.database.model.Organization;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Репозиторий для доступа к таблице с данными о предприятиях.
 */
public class OrganizationRepository {

    /** Имя таблицы */
    private static final String TABLE_NAME = "organization";

    /** DataSource */
    private EmbeddedDataSource dataSource;

    /** SQL запрос для создания таблицы */
    private static final String CREATE_QUERY =
            "CREATE TABLE " + TABLE_NAME + "("
            + "id INTEGER PRIMARY KEY, "
            + "title VARCHAR(255), "
            + "address VARCHAR(255), "
            + "phone_number VARCHAR(255), "
            + "type VARCHAR(255), "
            + "open_time TIME, "
            + "close_time TIME, "
            + "break_start TIME, "
            + "break_end TIME, "
            + "foundation_date DATE, "
            + "close_date DATE)";

    public OrganizationRepository(EmbeddedDataSource dataSource) {
        this.dataSource = dataSource;
        initTable();
    }

    /**
     * Инициализация таблицы.
     */
    private void initTable() {
        try(
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(
                    null,
                    null,
                    TABLE_NAME.toUpperCase(),
                    new String[] {"TABLE"}
            );
            if (!resultSet.next()) {
                statement.executeUpdate(CREATE_QUERY);
            }
        } catch (SQLException e) {
            System.out.println("Error during initializing table: " + e.getMessage());
        }
    }

    /**
     * Поиск предприятий, работающих на момент утсановленного времени.
     * @param time - проверяемое время.
     * @return - список подходящих организаций.
     */
    public List<Organization> findWorking(LocalTime time) {
        return findAll().stream()
                .filter(organization -> organization.isWorking(time))
                .collect(Collectors.toList());
    }

    /**
     * Извлечение данных обо всех организациях из базы.
     * @return - список всех доступных организаций.
     */
    private List<Organization> findAll() {
        String sqlQuery = "SELECT * FROM " + TABLE_NAME;
        try (
                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement()
        ) {
            return createListFromResultSet(statement.executeQuery(sqlQuery));
        } catch (SQLException e) {
            System.out.println("Error while extracting all rows from the table " + TABLE_NAME);
        }
        return Collections.emptyList();
    }

    /**
     * Создание списка организаций из объекта типа ResultSet.
     * @param resultSet - анализируемый объект типа ResultSet.
     * @return - список объектов типа Organization.
     * @throws SQLException - ошибка при взаимодействии с resultSet.
     */
    private List<Organization> createListFromResultSet(ResultSet resultSet) throws SQLException {
        List<Organization> result = new ArrayList<>();
        TimeConverter timeConverter = new TimeConverter();
        DateConverter dateConverter = new DateConverter();
        while (resultSet.next()) {
            LocalTime openTime = timeConverter.fromDatabase(resultSet.getTime("open_time"));
            LocalTime closeTime = timeConverter.fromDatabase(resultSet.getTime("close_time"));
            LocalTime breakStart = timeConverter.fromDatabase(resultSet.getTime("break_start"));
            LocalTime breakEnd = timeConverter.fromDatabase(resultSet.getTime("break_end"));
            LocalDate foundationDate = dateConverter.fromDatabase(resultSet.getDate("foundation_date"));
            LocalDate closeDate = dateConverter.fromDatabase(resultSet.getDate("close_date"));
            result.add(new Organization(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("address"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("type"),
                    openTime,
                    closeTime,
                    breakStart,
                    breakEnd,
                    foundationDate,
                    closeDate
            ));
        }
        return result;
    }

    /**
     * Добавление записи в базу.
     * @param organization - добавляемая организация.
     */
    public void add(Organization organization) {
        String sqlQuery = "INSERT INTO " + TABLE_NAME + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            prepareAddStatement(organization, preparedStatement);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Error during adding organization into database: " + e.getMessage());
        }
    }

    /**
     * Подстановка параметров в SQL-запрос для добавления данных в таблицу.
     * @param organization - добавляемая организация.
     * @param statement - объект, приводящий SQL-запрос в исполнение.
     * @throws SQLException - ошибка подстановки данных.
     */
    private void prepareAddStatement(Organization organization, PreparedStatement statement) throws SQLException {
        int counter = 1;
        TimeConverter timeConverter = new TimeConverter();
        DateConverter dateConverter = new DateConverter();
        Time openTime = timeConverter.toDatabase(organization.getOpenTime());
        Time closeTime = timeConverter.toDatabase(organization.getCloseTime());
        Time breakStart = timeConverter.toDatabase(organization.getBreakStart());
        Time breakEnd = timeConverter.toDatabase(organization.getBreakEnd());
        Date foundationDate = dateConverter.toDatabase(organization.getFoundationDate());
        Date closeDate = dateConverter.toDatabase(organization.getCloseDate());
        statement.setInt(counter++, organization.getId());
        statement.setString(counter++, String.valueOf(organization.getTitle()));
        statement.setString(counter++, String.valueOf(organization.getAddress()));
        statement.setString(counter++, String.valueOf(organization.getPhoneNumber()));
        statement.setString(counter++, String.valueOf(organization.getType()));
        statement.setTime(counter++, openTime);
        statement.setTime(counter++, closeTime);
        statement.setTime(counter++, breakStart);
        statement.setTime(counter++, breakEnd);
        statement.setDate(counter++, foundationDate);
        statement.setDate(counter, closeDate);
    }

    /**
     * Поиск организации в БД по id.
     * @param id - искомый идентификатор.
     * @return - объект типа Organization.
     */
    public Organization find(int id) {
        String sqlQuery = "SELECT * FROM " + TABLE_NAME + " WHERE id = " + id;
        List<Organization> listFromResultSet = Collections.emptyList();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            listFromResultSet = createListFromResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            System.out.println("Error during searching for organization by id: " + e.getMessage());
        }
        if (listFromResultSet.size() > 1) {
            throw new IllegalStateException("Multiple entries with same ID.");
        }
        return listFromResultSet.isEmpty() ? null : listFromResultSet.get(0);
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
