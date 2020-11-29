package ru.croc.javaschool.final_task.database.datasource;

import org.apache.derby.jdbc.EmbeddedDataSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Класс-провайдер для доступа к БД.
 */
public class DataSourceProvider {

    /** DataSource */
    private EmbeddedDataSource dataSource;

    /** Параметры конфигурации */
    private Map<String, String> properties = new HashMap<>();

    public DataSourceProvider() throws IOException {
        loadProperties();
    }

    /**
     * Загрузка параметров конфигурации
     * @throws IOException - ошибка загрузки настроек
     */
    private void loadProperties() throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties")
            );
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                this.properties.put((String) entry.getKey(), (String) entry.getValue());
            }
        } catch (Exception e) {
            System.out.println("Error occurred during loading application properties");
            throw e;
        }
    }

    /**
     * Получение экземпляра DataSource.
     * @return - Объект типа DataSource.
     */
    public EmbeddedDataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new EmbeddedDataSource();
            dataSource.setUser("");
            dataSource.setPassword("");
            dataSource.setDatabaseName(properties.get("dbname"));
            dataSource.setCreateDatabase("create");
        }
        return dataSource;
    }
}
