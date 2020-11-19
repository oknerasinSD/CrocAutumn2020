package ru.croc.java.school.lesson6_1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class CleanDirectoryTest {

    /** Тестовая директория, которую очищает поток */
    private File testDirectory = new File("testDirectory");

    @Test
    public void testCleanDirectory() throws IOException, InterruptedException {

        testDirectory.mkdir();
        addFiles(testDirectory, 10);
        Assertions.assertEquals(10, Objects.requireNonNull(testDirectory.list()).length);
        CleanDirectory cleanDirectory = new CleanDirectory(1000, testDirectory.getPath());
        cleanDirectory.startThread();
        Thread.sleep(500);
        Assertions.assertEquals(0, Objects.requireNonNull(testDirectory.list()).length);

        addFiles(testDirectory, 100);
        Assertions.assertEquals(100, Objects.requireNonNull(testDirectory.list()).length);
        Thread.sleep(1000);
        Assertions.assertEquals(0, Objects.requireNonNull(testDirectory.list()).length);

        cleanDirectory.stopThread();
        Assertions.assertFalse(cleanDirectory.getThread().isAlive());
    }

    /**
     * Добавление файлов в директорию.
     * @param directory - путь к директории.
     * @param quantity - количество файлов.
     * @throws IOException - ошибка создания файла.
     */
    private synchronized void addFiles(File directory, int quantity) throws IOException {
        for (int i = 1; i <= quantity; ++i) {
            new File(directory + File.separator + i).createNewFile();
        }
    }
}
