package ru.croc.java.school.lesson3;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private Scanner scanner;

    /**
     * Запуск программы в консольном режиме.
     * @param inputStream - поток с входными данными.
     */
    public void run(InputStream inputStream) {
        scanner = new Scanner(inputStream);
        int action = -1;
        while (action != 0) {
            System.out.println(
                    "Выберете дальнейшее действие: " + "\n"
                            + "1. Посмотреть список задач." + "\n"
                            + "2. Посмотреть список исполнителей." + "\n"
                            + "3. Добавить новую задачу." + "\n"
                            + "4. Добавить нового исполнителя." + "\n"
                            + "5. Изменить статус задачи." + "\n"
                            + "6. Изменить исполнителя задачи." + "\n"
                            + "0. Выход."
            );
            action = Integer.parseInt(scanner.nextLine());
            System.out.println();
            System.out.println(action);
            System.out.println();
            Data data = new Data();
            Map<Integer, Executor> executorsCache;
            Map<Integer, Task> tasksCache;
            int choice;
            switch (action) {
                case 1:
                    tasksCache = new HashMap<>();
                    printAndSaveEntities("Список задач", data.getTasksDirectory(), tasksCache);
                    System.out.println();
                    action = askForAdditionalInfo("задачи", tasksCache);
                    if (action > 0 && action <= tasksCache.size()) {
                        System.out.println("Введите число от 1 до 9 для возвращения в главное меню или 0 для выхода.");
                        action = Integer.parseInt(scanner.nextLine());
                    }
                    System.out.println();
                    tasksCache.clear();
                    break;
                case 2:
                    executorsCache = new HashMap<>();
                    printAndSaveEntities("Список исполнителей", data.getExecutorsDirectory(), executorsCache);
                    System.out.println();
                    action = askForAdditionalInfo("исполнителя", executorsCache);
                    if (action > 0 && action <= executorsCache.size()) {
                        System.out.println("Введите число от 1 до 9 для возвращения в главное меню или 0 для выхода.");
                        action = Integer.parseInt(scanner.nextLine());
                    }
                    System.out.println();
                    executorsCache.clear();
                    break;
                case 3:
                    addNewTask();
                    System.out.println();
                    break;
                case 4:
                    addNewExecutor();
                    System.out.println();
                    break;
                case 5:
                    System.out.println("Для обновления статуса задачи откройте" +
                            " подробную информацию о задаче и подтвердите действие");
                    tasksCache = new HashMap<>();
                    printAndSaveEntities("Список задач", data.getTasksDirectory(), tasksCache);
                    System.out.println();
                    action = askForAdditionalInfo("задачи", tasksCache);
                    choice = action;
                    if (action != 0) {
                        System.out.println("Нажмите 1 для изменения статуса задачи, " +
                                "0 для завершения программы и любое другое число для возвращения в главное меню"
                        );
                        action = Integer.parseInt(scanner.nextLine());
                    }
                    if (action == 1) {
                        tasksCache.get(choice).updateStatus(!tasksCache.get(choice).isActive());
                        System.out.println("Статус задачи обновлен.");
                    } else {
                        System.out.println("Статус задачи не обновлен.");
                    }
                    System.out.println();
                    break;
                case 6:
                    System.out.println("Для обновления исполнителя задачи откройте подробную информацию о задаче.");
                    tasksCache = new HashMap<>();
                    printAndSaveEntities("Список задач", data.getTasksDirectory(), tasksCache);
                    System.out.println();
                    action = askForAdditionalInfo("задачи", tasksCache);
                    choice = action;
                    if (action != 0) {
                        System.out.println("Нажмите 1 для перехода к выбору нового исполнителя, " +
                                "0 для завршения программы и любое другое число для возвращения в главное меню"
                        );
                        action = Integer.parseInt(scanner.nextLine());
                    }
                    if (action == 1) {
                        executorsCache = new HashMap<>();
                        printAndSaveEntities("Список исполнителей", data.getExecutorsDirectory(), executorsCache);
                        System.out.println();
                        action = askForAdditionalInfo("исполнителя", executorsCache);
                        int choice2 = action;
                        if (action != 0) {
                            System.out.println();
                            System.out.println("Нажмите 1 для выбора данного исполнителя для задачи, " +
                                    "0 для завершения программы и любое другое число для возвращения в главное меню"
                            );
                            action = Integer.parseInt(scanner.nextLine());
                        }
                        if (action == 1) {
                            tasksCache.get(choice).updateExecutor(executorsCache.get(choice2));
                            System.out.println("Назначен новый исполнитель задачи.");
                        } else {
                            System.out.println("Исполнитель задачи не обновлен.");
                        }
                        System.out.println();
                    }
            }
        }
    }

    /**
     * Добавление нового исполнителя пользователем.
     */
    private void addNewExecutor() {
        Data data = new Data();
        System.out.println("Введите имя:");
        String firstName = scanner.nextLine();
        System.out.println("Введите фамилию:");
        String lastName = scanner.nextLine();
        System.out.println("Введите должность:");
        String position = scanner.nextLine();
        Executor createdExecutor = new Executor(firstName, lastName, position);
        data.serializeObject(
                createdExecutor,
                createdExecutor.getId()
        );
        System.out.println();
        System.out.println("Исполнитель сохранен.");
    }

    /**
     * Добавление новой задачи пользователем.
     */
    private void addNewTask() {
        Data data = new Data();
        System.out.println("Введите наименование задания: ");
        String name = scanner.nextLine();
        System.out.println("Введите описание задания: ");
        String description = scanner.nextLine();
        System.out.println("Выберете исполнителя задания:");
        Map<Integer, Executor> cache = new HashMap<>();
        printAndSaveEntities(
                "Список исполнителей",
                data.getExecutorsDirectory(),
                cache
        );
        int action = Integer.parseInt(scanner.nextLine());
        Task createdTask = new Task(name, description, cache.get(action));
        data.serializeObject(
                createdTask,
                createdTask.getId()
        );
        System.out.println();
        System.out.println("Задача создана.");
    }

    /**
     * Обработка случая, когда пользователь запрашивает подробную информацию о сущности.
     * @param subject - тип сущности с логической точки зрения (например Задача или Исполнитель).
     * @param cache - кэш, в котором хранятся сущности, краткую информацию о которых видит пользователь.
     * @param <T> - тип сущности с точки зрения компилятора (например ru.croc.java.school.lesson3.Task или ru.croc.java.school.lesson3.Executor).
     * @return
     */
    private <T extends Entity> int askForAdditionalInfo(String subject, Map<Integer, T> cache) {
        System.out.println(
                "Введите номер "
                + subject
                + " для вывода подробной информации, "
                + "0 для выхода или любое другое число для возвращения в главное меню."
        );
        System.out.println();
        int newAction = Integer.parseInt(scanner.nextLine());
        if (newAction > 0 && newAction <= cache.keySet().size()) {
            System.out.println(cache.get(newAction).toString());
        }
        return newAction;
    }

    /**
     * Вывод на экран краткой информации о сущностях, хранящихся в директории, и сохранение этих сущностей в кэш
     * на случай запроса от пользователся подробной информации.
     * @param header - верхняя строка при выводе на экран.
     * @param directory - директория, файлы из которой будут десериализовываться.
     * @param cache - кэш.
     * @param <T> - тип сущности (например ru.croc.java.school.lesson3.Task или ru.croc.java.school.lesson3.Executor).
     */
    private <T extends Entity> void printAndSaveEntities(String header, File directory, Map<Integer, T> cache) {
        Data data = new Data();
        int count = 0;
        System.out.println(header + ":");
        for (String fileName : Objects.requireNonNull(directory.list())) {
            T taskOrExecutor = (T) data.deserializeObject(directory, fileName);
            cache.put(++count, taskOrExecutor);
            System.out.println(count + ". " + taskOrExecutor.getShortName());
        }
    }

    /*
    Можно потестить вручную.
     */
    public static void main(String[] args) {

        Data data = new Data();

        Executor executor1 = new Executor("Ivan", "Ivanov", "Junior Developer");
        Task task1 = new Task(
                "Система ведения задач",
                "Разработка системы ведения задач для ОАО \"Рога и копыта\"",
                executor1
        );
        data.serializeObject(executor1, executor1.getId());
        data.serializeObject(task1, task1.getId());

        /*task1.updateStatus(false);*/

        Executor executor2 = new Executor("Petr", "Petrov", "Middle Developer");
        Task task2 = new Task(
                "Продвинутая система ведения задач",
                "Разработка продвинутой системы ведения задач для ОАО \"Рога и копыта\"",
                executor2
        );
        data.serializeObject(executor2, executor2.getId()
        );
        data.serializeObject(task2, task2.getId());

        new Main().run(System.in);
    }
}
