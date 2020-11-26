package ru.croc.javaschool.final_task.repository;

import org.apache.derby.jdbc.EmbeddedDataSource;
import ru.croc.javaschool.final_task.converter.DateConverter;
import ru.croc.javaschool.final_task.converter.TimeConverter;
import ru.croc.javaschool.final_task.model.Organization;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public List<Organization> findOpened(LocalTime time) {
        Time sqlTime = TimeConverter.toDatabase(time);
        String sqlQuery =
                "";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            return createListFromResultSet(preparedStatement.executeQuery());
        } catch (SQLException e) {
            System.out.println("Error while searching for opened organizations: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private void prepareSearchingStatement() {

    }

    private List<Organization> createListFromResultSet(ResultSet resultSet) throws SQLException {
        List<Organization> result = new ArrayList<>();
        while (resultSet.next()) {
            LocalTime openTime = TimeConverter.fromDatabase(resultSet.getTime("open_time"));
            LocalTime closeTime = TimeConverter.fromDatabase(resultSet.getTime("close_time"));
            LocalTime breakStart = TimeConverter.fromDatabase(resultSet.getTime("break_start"));
            LocalTime breakEnd = TimeConverter.fromDatabase(resultSet.getTime("break_end"));
            LocalDate foundationDate = DateConverter.fromDatabase(resultSet.getDate("foundation_date"));
            LocalDate closeDate = DateConverter.fromDatabase(resultSet.getDate("close_date"));
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

    private void prepareAddStatement(
            Organization organization,
            PreparedStatement preparedStatement
    ) throws SQLException {
        int counter = 1;
        Time openTime = TimeConverter.toDatabase(organization.getOpenTime());
        Time closeTime = TimeConverter.toDatabase(organization.getCloseTime());
        Time breakStart = TimeConverter.toDatabase(organization.getBreakStart());
        Time breakEnd = TimeConverter.toDatabase(organization.getBreakEnd());
        Date foundationDate = DateConverter.toDatabase(organization.getFoundationDate());
        Date closeDate = DateConverter.toDatabase(organization.getCloseDate());
        preparedStatement.setInt(counter++, organization.getId());
        preparedStatement.setString(counter++, String.valueOf(organization));
        preparedStatement.setString(counter++, String.valueOf(organization));
        preparedStatement.setString(counter++, String.valueOf(organization));
        preparedStatement.setString(counter++, String.valueOf(organization));
        preparedStatement.setTime(counter++, openTime);
        preparedStatement.setTime(counter++, closeTime);
        preparedStatement.setTime(counter++, breakStart);
        preparedStatement.setTime(counter++, breakEnd);
        preparedStatement.setDate(counter++, foundationDate);
        preparedStatement.setDate(counter, closeDate);
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
