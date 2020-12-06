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

    /** Мапа, устанавливающее соответствия между типми из перечисления PropertyType
     * и путями к нужным конфигурационным файлам */
    private Map<PropertyType, String> propertyTypes = Map.ofEntries(
            Map.entry(PropertyType.MAIN, "application.properties"),
            Map.entry(PropertyType.TEST, "test.properties")
    );

    public DataSourceProvider(PropertyType propertyType) throws IOException {
        String propertyPath = propertyTypes.get(propertyType);
        loadProperties(propertyPath);
    }

    /**
     * Загрузка параметров конфигурации
     * @throws IOException - ошибка загрузки настроек
     */
    private void loadProperties(String propertyPath) throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyPath));
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
     * @return - объект типа DataSource.
     */
    public EmbeddedDataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new EmbeddedDataSource();
            dataSource.setDatabaseName(properties.get("dbname"));
            dataSource.setUser(properties.get("username"));
            dataSource.setPassword(properties.get("password"));
            dataSource.setCreateDatabase("create");
        }
        return dataSource;
    }

    public String getDbName() {
        return properties.get("dbname");
    }
}
