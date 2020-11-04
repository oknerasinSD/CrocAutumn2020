package ru.croc.java.school.lesson3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.croc.java.school.lesson3.Data;
import ru.croc.java.school.lesson3.Executor;
import ru.croc.java.school.lesson3.Main;
import ru.croc.java.school.lesson3.Task;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/*
В качестве имени файла всегда используется поле id объекта.
 */
public class DataTest {

    Data data = new Data();

    Executor ivanIvanov = new Executor("Ivan", "Ivanov", "Junior Developer");
    Executor petrPetrov = new Executor("Petr", "Petrov", "Middle Developer");
    Executor andreyAndreev = new Executor("Andrey", "Andreev", "Senior Developer");

    Task task1 = new Task(
            "Система ведения задач",
            "Разработка системы ведения задач для ОАО \"Рога и копыта\"",
            ivanIvanov
    );
    Task task2 = new Task(
            "Продвинутая система ведения задач",
            "Разработка продвинутой системы ведения задач для ОАО \"Рога и копыта\"",
            petrPetrov
    );
    Task task3 = new Task(
            "Самая продвинутая система ведения задач",
            "Разработка самой продвинутой системы ведения задач для ОАО \"Рога и копыта\"",
            andreyAndreev
    );

    @Test
    @DisplayName("Сериализация и десериализация объектов")
    public void serializeAndDeserializeObjectsTest() {

        data.serializeObject(ivanIvanov, ivanIvanov.getId());
        List<String> fileNames =
                new ArrayList<>(Arrays.asList(Objects.requireNonNull(data.getExecutorsDirectory().list())));
        Assertions.assertTrue(fileNames.contains(ivanIvanov.getId() + ".bin"));

        Executor checkExecutor =
                (Executor) data.deserializeObject(data.getExecutorsDirectory(), ivanIvanov.getId() + ".bin");
        Assertions.assertEquals(ivanIvanov.toString(), checkExecutor.toString());

        data.serializeObject(task1, task1.getId());
        fileNames = new ArrayList<>(Arrays.asList(Objects.requireNonNull(data.getTasksDirectory().list())));
        Assertions.assertTrue(fileNames.contains(task1.getId() + ".bin"));

        data.deserializeObject(data.getTasksDirectory(), task1.getId() + ".bin");
        Task checkTask =
                (Task) data.deserializeObject(data.getTasksDirectory(), task1.getId() + ".bin");
        Assertions.assertEquals(task1.toString(), checkTask.toString());
    }

    @Test
    @DisplayName("Добавление объекта пользователем")
    public void addObjectTest() {

        String input = "4\nИмя\nФамилия\nДолжность\n3\nНаименование\nОписание\n1\n0";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        int executorsQuantity = Objects.requireNonNull(data.getExecutorsDirectory().list()).length;
        int tasksQuantity = Objects.requireNonNull(data.getTasksDirectory().list()).length;
        new Main().run(inputStream);
        Assertions.assertEquals(++executorsQuantity, Objects.requireNonNull(data.getExecutorsDirectory().list()).length);
        Assertions.assertEquals(++tasksQuantity, Objects.requireNonNull(data.getTasksDirectory().list()).length);
    }

    @Test
    @DisplayName("Изменение статуса задачи пользователем")
    public void updateStatusTest() {

        String input = "5\n1\n1\n0";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        data.serializeObject(task2, task2.getId());
        Task checkedTask = (Task) data.deserializeObject(
                data.getTasksDirectory(),
                Objects.requireNonNull(data.getTasksDirectory().list())[0]
        );
        boolean initialStatus = checkedTask.isActive();
        new Main().run(inputStream);
        checkedTask = (Task) data.deserializeObject(
                data.getTasksDirectory(),
                Objects.requireNonNull(data.getTasksDirectory().list())[0]
        );
        Assertions.assertNotEquals(initialStatus, checkedTask.isActive());
    }

    @Test
    @DisplayName("Изменение исполнителя задачи пользователем")
    public void updateExecutorTest() {

        String input = "6\n1\n1\n1\n1\n0";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        data.serializeObject(task3, task3.getId());
        data.serializeObject(petrPetrov, petrPetrov.getId());
        new Main().run(inputStream);
        Executor chosenExecutor = (Executor) data.deserializeObject(
                data.getExecutorsDirectory(),
                Objects.requireNonNull(data.getExecutorsDirectory().list())[0]
        );
        Task checkedTask = (Task) data.deserializeObject(
                data.getTasksDirectory(),
                Objects.requireNonNull(data.getTasksDirectory().list())[0]
        );
        Assertions.assertEquals(chosenExecutor.getId(), checkedTask.getExecutor().getId());
    }
}
