package ru.croc.java.school.lesson3;

import java.io.Serializable;
import java.util.UUID;

public class Task implements Serializable, Entity {

    /** Версия объекта */
    private static final long serialVersionUID = 1L;

    /** Код задачи */
    private String id = UUID.randomUUID().toString();

    /** Наименование задачи */
    private String title;

    /** Описание задачи */
    private String description;

    /** Исполнитель задачи */
    private Executor executor;

    /** Статус задачи */
    private boolean active;

    public Task(String name, String description, Executor executor) {
        this.title = name;
        this.description = description;
        this.executor = executor;
        this.active = true;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getShortName() {
        return title;
    }

    @Override
    public String toString() {
        return "Задача:" + "\n" +
                "ID: " + id + '\n' +
                "Наименование: " + title + '\n' +
                "Описание: " + description + '\n' +
                "Исполнитель: " + executor.getShortName() + "\n" +
                "ID исполнителя: " + executor.getId() + "\n" +
                "Статус: " + (active ? "Активна" : "Неактивна");
    }

    /**
     * Обновление статуса задачи.
     * @param active - новый статус задачи.
     */
    public void updateStatus(boolean active) {
        this.active = active;
        Data data = new Data();
        data.serializeObject(this, id);
    }

    /**
     * Обновление исполнителя задачи.
     * @param executor - новый исполнитель.
     */
    public void updateExecutor(Executor executor) {
        this.executor = executor;
        Data data = new Data();
        data.serializeObject(this, id);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Executor getExecutor() {
        return executor;
    }

    public boolean isActive() {
        return active;
    }
}
